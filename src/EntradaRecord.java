import java.io.Serializable;

public class EntradaRecord implements Serializable {
    public final String jugador;
    public final long tiempo;

    public EntradaRecord(String jugador, long tiempo) {
        this.jugador = jugador;
        this.tiempo = tiempo;
    }
    public String toString() {
        return jugador + " → " + tiempo + " ms";
    }
}
