package logica;

import accesodatos.RegistroArchivoDAO;
import entidades.Registro;
import entidades.Vehiculo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParqueoService {
    private static final int TARIFA_POR_HORA = 500;
    private final RegistroArchivoDAO registroArchivoDAO;

    public ParqueoService() {
        this(new RegistroArchivoDAO());
    }

    public ParqueoService(RegistroArchivoDAO registroArchivoDAO) {
        this.registroArchivoDAO = registroArchivoDAO;
    }

    public ResultadoOperacion registrarIngreso(String placa, String tipo) {
        String placaLimpia = normalizarPlaca(placa);
        String tipoLimpio = normalizarTipo(tipo);

        if (placaLimpia.isEmpty()) {
            return new ResultadoOperacion(false, "La placa es obligatoria.");
        }
        if (tipoLimpio.isEmpty()) {
            return new ResultadoOperacion(false, "Debe seleccionar el tipo de vehiculo.");
        }
        if (!tipoLimpio.equals("Carro") && !tipoLimpio.equals("Moto")) {
            return new ResultadoOperacion(false, "El tipo de vehiculo no es valido.");
        }

        List<Registro> activos = registroArchivoDAO.listarActivos();
        for (Registro registro : activos) {
            if (registro.getVehiculo().getPlaca().equalsIgnoreCase(placaLimpia)) {
                return new ResultadoOperacion(false, "La placa ya se encuentra activa dentro del parqueo.");
            }
        }

        Registro nuevoRegistro = new Registro(new Vehiculo(placaLimpia, tipoLimpio), LocalDateTime.now());
        activos.add(nuevoRegistro);
        registroArchivoDAO.guardarActivos(activos);

        List<Registro> historial = registroArchivoDAO.listarHistorial();
        historial.add(nuevoRegistro);
        registroArchivoDAO.guardarHistorial(historial);

        return new ResultadoOperacion(true, "Ingreso registrado correctamente.");
    }

    public ResultadoOperacion registrarSalida(String placa) {
        String placaLimpia = normalizarPlaca(placa);
        if (placaLimpia.isEmpty()) {
            return new ResultadoOperacion(false, "Debe seleccionar un vehiculo activo.");
        }

        List<Registro> activos = registroArchivoDAO.listarActivos();
        Registro activo = null;
        for (Registro registro : activos) {
            if (registro.getVehiculo().getPlaca().equalsIgnoreCase(placaLimpia)) {
                activo = registro;
                break;
            }
        }

        if (activo == null) {
            return new ResultadoOperacion(false, "No se encontro un ingreso activo para la placa seleccionada.");
        }

        LocalDateTime salida = LocalDateTime.now();
        int monto = calcularMonto(activo.getHoraEntrada(), salida);
        activo.registrarSalida(salida, monto);
        activos.remove(activo);
        registroArchivoDAO.guardarActivos(activos);

        List<Registro> historial = registroArchivoDAO.listarHistorial();
        for (Registro registro : historial) {
            if (registro.getVehiculo().getPlaca().equalsIgnoreCase(placaLimpia) && registro.estaActivo()) {
                registro.registrarSalida(salida, monto);
                break;
            }
        }
        registroArchivoDAO.guardarHistorial(historial);

        return new ResultadoOperacion(true, "Salida registrada. Monto a pagar: " + formatearMonto(monto));
    }

    public ResultadoOperacion eliminarHistorial() {
        registroArchivoDAO.limpiarHistorial();
        return new ResultadoOperacion(true, "Historial eliminado correctamente.");
    }

    public List<Registro> obtenerRegistrosActivos() {
        return new ArrayList<>(registroArchivoDAO.listarActivos());
    }

    public List<Registro> obtenerHistorial() {
        return new ArrayList<>(registroArchivoDAO.listarHistorial());
    }

    public int calcularMonto(LocalDateTime entrada, LocalDateTime salida) {
        long minutos = Math.max(1, java.time.Duration.between(entrada, salida).toMinutes());
        long horasCobradas = (long) Math.ceil(minutos / 60.0);
        return (int) horasCobradas * TARIFA_POR_HORA;
    }

    public String formatearMonto(int monto) {
        return "CRC " + monto;
    }

    private String normalizarPlaca(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase();
    }

    private String normalizarTipo(String tipo) {
        return tipo == null ? "" : tipo.trim();
    }
}
