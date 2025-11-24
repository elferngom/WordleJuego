import java.io.Serializable;
import java.util.*;

public class TablaRecords implements Serializable {
    private static final Map<String, List<EntradaRecord>> tablas = new HashMap<>();
    private static final int ENTRADAS = 3;
    public static synchronized void agregarEntrada(String palabra, String jugador, long tiempo) {
        palabra = palabra.toUpperCase();
        tablas.putIfAbsent(palabra, new ArrayList<>());

        List<EntradaRecord> ranking = tablas.get(palabra);
        ranking.add(new EntradaRecord(jugador,tiempo));

        ranking.sort(Comparator.comparingLong(e -> e.tiempo));
        if(ranking.size() > ENTRADAS) {
            ranking.remove(ranking.size()-1);
        }
    }
}
