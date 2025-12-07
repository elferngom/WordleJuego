import java.io.Serializable;
import java.util.*;

public class TablaRecords implements Serializable {
    private final Map<String, List<EntradaRecord>> tablas = new HashMap<>();
    private static final int ENTRADAS = 3;
    public synchronized void agregarEntrada(String palabra, String jugador, long tiempo) {
        palabra = palabra.toUpperCase();
        tablas.putIfAbsent(palabra, new ArrayList<>());

        List<EntradaRecord> ranking = tablas.get(palabra);
        ranking.add(new EntradaRecord(jugador,tiempo));

        ranking.sort(Comparator.comparingLong(e -> e.tiempo));
        if(ranking.size() > ENTRADAS) {
            ranking.remove(ranking.size()-1);
        }
    }
    public synchronized List<EntradaRecord> getRanking(String palabra){
        return tablas.getOrDefault(palabra,new ArrayList<>());
    }
}
