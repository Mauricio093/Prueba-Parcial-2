package entidades;

public class Vehiculo {
    private final String placa;
    private final String tipo;

    public Vehiculo(String placa, String tipo) {
        this.placa = placa;
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public String getTipo() {
        return tipo;
    }
}
