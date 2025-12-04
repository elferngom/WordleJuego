import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static TablaRecords records;
    public static List<String> diccionario=new ArrayList<>();
    public static void main(String[] args) {
        records = cargar();
        cargarPalabras();

        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket server = new ServerSocket(5000);) {
            while (true) {
                Socket cliente = server.accept();
                AtenderCliente tarea = new AtenderCliente(cliente, diccionario);
                pool.execute(tarea);
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            pool.shutdown();
        }
    }

    public static TablaRecords cargar() {
        File f = new File("records.ser");
        if (!f.exists() || f.length() == 0) {
            return new TablaRecords();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (TablaRecords) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new TablaRecords();
        }
    }

    public static void cargarPalabras() {
        File archivo = new File("palabras.txt");
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("[SERVIDOR]: No se encuentan palabras");
        }
        try(BufferedReader br=new BufferedReader(new FileReader(archivo))){
            String linea;
            while ((linea = br.readLine()) != null) {
                String palabra = linea.trim();
                if(!palabra.isEmpty()) {
                    diccionario.add(palabra);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
