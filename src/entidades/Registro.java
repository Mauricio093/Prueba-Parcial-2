package entidades;

import java.time.Duration;
import java.time.LocalDateTime;

public class Registro {
    private final Vehiculo vehiculo;
    private final LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;
    private int montoCobrado;

    public Registro(Vehiculo vehiculo, LocalDateTime horaEntrada) {
        this(vehiculo, horaEntrada, null, 0);
    }

    public Registro(Vehiculo vehiculo, LocalDateTime horaEntrada, LocalDateTime horaSalida, int montoCobrado) {
        this.vehiculo = vehiculo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.montoCobrado = montoCobrado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public int getMontoCobrado() {
        return montoCobrado;
    }

    public void registrarSalida(LocalDateTime horaSalida, int montoCobrado) {
        this.horaSalida = horaSalida;
        this.montoCobrado = montoCobrado;
    }

    public boolean estaActivo() {
        return horaSalida == null;
    }

    public long getMinutosParqueados() {
        LocalDateTime fin = horaSalida != null ? horaSalida : LocalDateTime.now();
        return Math.max(0, Duration.between(horaEntrada, fin).toMinutes());
    }
}
