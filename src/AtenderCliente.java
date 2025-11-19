import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;

public class AtenderCliente implements Runnable {
    private Socket socket;
    private static String[] PALABRAS= {"PERLA", "RIFLE", "SOLER", "MOLER", "FONTANERO","ESCAÑO"};
    private static Random random=new Random();
    public AtenderCliente(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        String respuestaServidor = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);) {
            boolean jugando = true;
            String jugador = br.readLine();
            while (jugando) { //Puede jugar más de una partida
                int numero = random.nextInt(PALABRAS.length); //Coge al azar una de las palabras de la lista
                String palabra = PALABRAS[numero];
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
                        pw.println("¡Has acertado la palabra!");
                        break;
                    }
                }
                long fin=System.currentTimeMillis();
                long tiempo=fin-inicio;
                pw.println("Has tardado "+tiempo+ " ms");
                TablaRecords.agregarEntrada(palabra,jugador, tiempo);

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
        return Arrays.toString(resultado);
    }
}
