import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class PartidaMultijugador {
    public List<String> palabras;
    private CyclicBarrier barrera;
    public int puntosJ1 = 0;
    public int puntosJ2 = 0;

    //Guardamos los intentos de cada uno, -1 es que no ha acabado
    public int intentoJ1=0;
    public int intentoJ2 =0;

    //Guardamos el tiempo por si terminan en los mismos intentos
    public long tiempoJ1=0;
    public long tiempoJ2=0;

    public PartidaMultijugador(List<String> palabras) {
        this.palabras = palabras;
        this.barrera=new CyclicBarrier(2);
    }
    public List<String> getPalabras() {
        return this.palabras;
    }
    public CyclicBarrier getBarrera() {
        return this.barrera;
    }
    public synchronized void registrarResultados(int intentos, long timepo, boolean J1) {
        if(J1){
            this.intentoJ1=intentos;
            this.tiempoJ1=timepo;
        }else{
            this.intentoJ2=intentos;
            this.tiempoJ2=timepo;
        }
    }
    //Primero comprobamos los intentos para saber quien ha ganado, si han acabado en los mismos intentos miramos el tiempo
    public synchronized void apuntarPuntos() {
        boolean ganaJ1=false;
        boolean ganaJ2=false;
        if(intentoJ1<intentoJ2){
            ganaJ1=true;
        } else if(intentoJ2<intentoJ1){
            ganaJ2=true;
        } else if(intentoJ1==intentoJ2){
            if(tiempoJ1<tiempoJ2){
                ganaJ1=true;
            }else{
                ganaJ2=true;
            }
        }
        if(ganaJ1){
            puntosJ1++;
        } else{puntosJ2++;}
        intentoJ1=0;
        intentoJ2=0;
        tiempoJ1=0;
        tiempoJ2=0;
    }
}
