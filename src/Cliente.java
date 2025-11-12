import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
