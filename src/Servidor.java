import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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
                        int numero=random.nextInt(PALABRAS.length); //Coge al azar una de las palabras de la lista
                        String palabra=PALABRAS[numero];
                        StringBuilder guiones=new StringBuilder();
                        for(int i=0;i<palabra.length();i++){
                            guiones.append("_");
                        }
                        pw.println(guiones); //Envia al cliente el número de letras que tiene la palabra
                        int intentos=0; //número de intentos por partida
                        while(intentos<6){
                            String respuesta=br.readLine(); //Leo y guardo la respuesta del cliente
                            String respuestaServidor=verificarIntento(palabra,respuesta);
                            pw.println(respuestaServidor);
                            intentos++;
                        }
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();

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
            if(resultado[i]!=2){
                char letra=cliente.charAt(i);
                int indiceSecreta=servidorModificable.indexOf(String.valueOf(letra));
                if(resultado[indiceSecreta]!=-1){
                    resultado[i]=1;
                    servidorModificable.setCharAt(i, '#');
                } else{
                    resultado[i]=0;
                }
            }
        }
        return Arrays.toString(resultado);
    }
}
