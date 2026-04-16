package accesodatos;

import entidades.Registro;
import entidades.Vehiculo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroArchivoDAO {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String SEPARADOR = ";";
    private final Path directorioDatos;
    private final Path archivoActivos;
    private final Path archivoHistorial;

    public RegistroArchivoDAO() {
        this(Paths.get("datos"));
    }

    public RegistroArchivoDAO(Path directorioDatos) {
        this.directorioDatos = directorioDatos;
        this.archivoActivos = directorioDatos.resolve("registros_activos.txt");
        this.archivoHistorial = directorioDatos.resolve("historial_registros.txt");
        inicializarArchivos();
    }

    public List<Registro> listarActivos() {
        return leerRegistros(archivoActivos);
    }

    public List<Registro> listarHistorial() {
        return leerRegistros(archivoHistorial);
    }

    public void guardarActivos(List<Registro> registros) {
        escribirRegistros(archivoActivos, registros);
    }

    public void guardarHistorial(List<Registro> registros) {
        escribirRegistros(archivoHistorial, registros);
    }

    public void limpiarHistorial() {
        escribirRegistros(archivoHistorial, Collections.emptyList());
    }

    private void inicializarArchivos() {
        try {
            Files.createDirectories(directorioDatos);
            if (!Files.exists(archivoActivos)) {
                Files.write(archivoActivos, new ArrayList<>(), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            }
            if (!Files.exists(archivoHistorial)) {
                Files.write(archivoHistorial, new ArrayList<>(), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("No fue posible inicializar los archivos de datos.", ex);
        }
    }

    private List<Registro> leerRegistros(Path archivo) {
        try {
            if (!Files.exists(archivo)) {
                return new ArrayList<>();
            }
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            List<Registro> registros = new ArrayList<>();
            for (String linea : lineas) {
                if (linea == null || linea.isBlank()) {
                    continue;
                }
                String[] partes = linea.split(SEPARADOR, -1);
                if (partes.length < 5) {
                    continue;
                }
                Vehiculo vehiculo = new Vehiculo(partes[0], partes[1]);
                LocalDateTime horaEntrada = LocalDateTime.parse(partes[2], FORMATO_FECHA);
                LocalDateTime horaSalida = partes[3].isBlank() ? null : LocalDateTime.parse(partes[3], FORMATO_FECHA);
                int monto = partes[4].isBlank() ? 0 : Integer.parseInt(partes[4]);
                registros.add(new Registro(vehiculo, horaEntrada, horaSalida, monto));
            }
            return registros;
        } catch (IOException ex) {
            throw new IllegalStateException("No fue posible leer los registros.", ex);
        }
    }

    private void escribirRegistros(Path archivo, List<Registro> registros) {
        List<String> lineas = new ArrayList<>();
        for (Registro registro : registros) {
            lineas.add(formatearRegistro(registro));
        }
        try {
            Files.write(archivo, lineas, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            throw new IllegalStateException("No fue posible guardar los registros.", ex);
        }
    }

    private String formatearRegistro(Registro registro) {
        String salida = registro.getHoraSalida() == null ? "" : registro.getHoraSalida().format(FORMATO_FECHA);
        return registro.getVehiculo().getPlaca()
                + SEPARADOR + registro.getVehiculo().getTipo()
                + SEPARADOR + registro.getHoraEntrada().format(FORMATO_FECHA)
                + SEPARADOR + salida
                + SEPARADOR + registro.getMontoCobrado();
    }
}
