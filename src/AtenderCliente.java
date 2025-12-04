import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class AtenderCliente implements Runnable {
    private Socket socket;
    private static List<String> PALABRAS;
    private static Random random=new Random();
    public AtenderCliente(Socket socket, List<String> PALABRAS) {
        this.socket = socket;
        this.PALABRAS = PALABRAS;
    }
    public boolean win = false;
    @Override
    public void run() {
        String respuestaServidor = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);) {
            boolean jugando = true;
            String jugador = br.readLine();
            while (jugando) {//Puede jugar más de una partida
                win = false;
                int numero = random.nextInt(PALABRAS.size()); //Coge al azar una de las palabras de la lista
                String palabra = PALABRAS.get(numero);
                StringBuilder guiones = new StringBuilder();
                for (int i = 0; i < palabra.length(); i++) {
                    guiones.append("_");
                }
                pw.println(guiones); //Envia al cliente el número de letras que tiene la palabra
                int intentos = 0; //número de intentos por partida
                long inicio=System.currentTimeMillis();
                while (intentos < 6) {
                    String respuesta = br.readLine(); //Leo y guardo la respuesta del cliente
                    respuestaServidor = verificarIntento(palabra, respuesta);
                    pw.println(respuestaServidor); //ej: 01201
                    intentos++;
                    if(respuestaServidor.equals("2".repeat(palabra.length()))){
                        win = true;
                        break;
                    }
                }

                pw.println(win);
                if (win) {
                    long fin=System.currentTimeMillis();
                    long tiempo=fin-inicio;
                    pw.println(tiempo);
                    TablaRecords.agregarEntrada(palabra,jugador, tiempo);
                    guardar(Servidor.records);
                } else {
                    pw.println(palabra);
                }

                String seguir = br.readLine();
                if (seguir.equals("N")) {
                    jugando = false;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String verificarIntento(String servidor, String cliente){
        servidor= servidor.toUpperCase();
        cliente = cliente.toUpperCase();
        int[] resultado=new int[servidor.length()];
        StringBuilder servidorModificable=new StringBuilder(servidor);
        for (int i = 0; i < servidor.length(); i++) {
            if (servidor.charAt(i) == cliente.charAt(i)) {
                resultado[i] = 2; //"Marcamos como verde de momento"
                servidorModificable.setCharAt(i, '#'); //Para que al volver a pasar no se marquen de amarillo, las quitamos
            }
        }
        for(int i = 0; i < servidor.length(); i++){
            if(resultado[i] != 2){
                char letraIntento = cliente.charAt(i);
                int indiceEnSecreta = servidorModificable.indexOf(String.valueOf(letraIntento));
                if (indiceEnSecreta != -1) {
                    resultado[i] = 1;
                    servidorModificable.setCharAt(indiceEnSecreta, '#');
                } else {
                    resultado[i] = 0;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i : resultado) {
            sb.append(i);
        }
        return sb.toString();
    }
    public static void guardar(TablaRecords tabla) {
        synchronized (tabla) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("records.ser"))) {
                oos.writeObject(tabla);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
