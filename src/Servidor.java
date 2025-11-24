import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static TablaRecords records;
    public static void main(String[] args){
        records = cargar();
        ExecutorService pool=Executors.newCachedThreadPool();
        try(ServerSocket server=new ServerSocket(5000);){
            while(true){
                Socket cliente=server.accept();
                AtenderCliente tarea=new AtenderCliente(cliente);
                pool.execute(tarea);
            }
        }catch(IOException e){
            e.printStackTrace();

        }finally {
            pool.shutdown();
        }
    }
    public static TablaRecords cargar(){
        File f=new File("records.ser");
        if (!f.exists() || f.length() == 0) {
            return new TablaRecords();
        }
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f))){
            return (TablaRecords) ois.readObject();
        }
        catch(IOException |ClassNotFoundException e) {
            e.printStackTrace();
            return new TablaRecords();
        }
    }
}
