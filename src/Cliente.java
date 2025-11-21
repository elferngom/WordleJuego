import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce tu nombre: ");
            String nombre = sc.nextLine();
            pw.println(nombre);

            String guiones = br.readLine();
            System.out.println("La palabra tiene " + guiones.length() + " letras");
            System.out.println(guiones);

            boolean win = false;
            while (!win) {
                System.out.println("Introduce tu intento: ");
                String intento = sc.nextLine().toUpperCase();
                pw.println(intento);
                String cod =  br.readLine();
                System.out.println("Resultado: " + cod);

                String s = br.readLine();
                if (s != null) {
                    win = Boolean.parseBoolean(s);
                    if (win) {
                        String tiempoServidor = br.readLine();
                        long tiempo = Long.parseLong(tiempoServidor);
                        System.out.println("¡Has ganado! Tiempo: " + tiempo);
                    } else {
                        String palabra = br.readLine();
                        System.out.println("Has perdido. La palabra era: " + palabra);
                    }
                }
            }

            System.out.println("Quieres jugar otra vez?");


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
