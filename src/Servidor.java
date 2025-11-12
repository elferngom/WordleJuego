import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Servidor {
    private static String[] PALABRAS= {"PERLA", "RIFLE", "SOLER", "MOLER", "FONTANERO"};
    private static Random random=new Random();
    public static void main(String[] args){
        try(ServerSocket server=new ServerSocket(5000);){
            while(true){
                Socket cliente=server.accept();
                try(BufferedReader br=new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    PrintWriter pw=new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()))){
                    boolean jugando=true;
                    while(jugando){ //Puede jugar más de una partida
                        int numero=random.nextInt(4); //Coge al azar una de las palabras de la lista
                        String palabra=PALABRAS[numero];
                        StringBuilder guiones=new StringBuilder();
                        char[] partesServidor=palabra.toCharArray();
                        for(int i=0;i<palabra.length();i++){
                            guiones.append("_");
                        }
                        pw.println(guiones); //Envia al cliente el número de letras que tiene la palabra
                        int intentos=0; //número de intentos por partida
                        while(intentos<6){
                            String respuesta=br.readLine();
                            char[] partesCliente=respuesta.toCharArray();

                            intentos++;
                        }
                    }

                }
            }
        }catch(IOException e){
            e.printStackTrace();

        }

    }
}
