package logica;

public class ResultadoOperacion {
    private final boolean exitoso;
    private final String mensaje;

    public ResultadoOperacion(boolean exitoso, String mensaje) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }
}
