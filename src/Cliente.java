import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            //Escribir las normas por pantalla
            //Que le pida y mande al servidor el nombre del jugador
            // 3. Cada partida funciona así:
//
//           a) Recibir del servidor la cadena de guiones, que indica la longitud
//                 de la palabra secreta. Mostrarla al usuario.
//
//            b) Repetir hasta 6 intentos:
//            - Pedir una palabra al usuario con Scanner.
//            - Enviarla al servidor.
//            - Recibir la respuesta del servidor (ej: "[0,1,2,0,1]").
//            - Mostrarla por pantalla.
//
//               * Nota: el servidor YA tiene su propio bucle de 6 intentos.
//              Es decir: el cliente SOLO manda intentos y espera respuesta.
//              El cliente NO debe hacer ningún for de 6 intentos por su cuenta,
//              simplemente pide palabra -> manda -> recibe.
            //Que al acabar pregunte si quiere otra partida(??)
            //Creo, si no preguntale a una IA cual seria la mejor forma de acuerdo al codigo que hay del servidor

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
