package presentacion;

import entidades.Registro;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RegistroTableModel extends AbstractTableModel {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final String[] columnas;
    private final boolean mostrarSalida;
    private List<Registro> registros;

    public RegistroTableModel(boolean mostrarSalida) {
        this.mostrarSalida = mostrarSalida;
        this.columnas = mostrarSalida
                ? new String[]{"Placa", "Tipo", "Entrada", "Salida", "Monto"}
                : new String[]{"Placa", "Tipo", "Entrada"};
        this.registros = new ArrayList<>();
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = new ArrayList<>(registros);
        fireTableDataChanged();
    }

    public Registro getRegistroEn(int fila) {
        if (fila < 0 || fila >= registros.size()) {
            return null;
        }
        return registros.get(fila);
    }

    @Override
    public int getRowCount() {
        return registros.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Registro registro = registros.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return registro.getVehiculo().getPlaca();
            case 1:
                return registro.getVehiculo().getTipo();
            case 2:
                return registro.getHoraEntrada().format(FORMATO);
            case 3:
                return mostrarSalida
                        ? (registro.getHoraSalida() == null ? "Activo" : registro.getHoraSalida().format(FORMATO))
                        : "";
            case 4:
                return mostrarSalida ? ("CRC " + registro.getMontoCobrado()) : "";
            default:
                return "";
        }
    }
}
