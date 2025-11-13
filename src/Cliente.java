import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            //Escribir las normas por pantalla
            //Un bucle while con: recibir y guardar el mensaje del servidor,sacralo por pantalla,
            // preguntar con un scanner
            // la palabra siguiente y mandarla al servidor.
            //Que al acabar pregunte si quiere otra partida(??)
            //Creo, si no preguntale a una IA cual seria la mejor forma de acuerdo al codigo que hay del servidor

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
