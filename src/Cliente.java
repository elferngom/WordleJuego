import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce tu nombre: ");
            String nombre = sc.nextLine();
            pw.println(nombre);

            boolean jugando = true;
            boolean partida = true;
            boolean win = false;
            while (jugando) {
                String guiones = br.readLine();
                while (partida) {
                    System.out.println("La palabra tiene " + guiones.length() + " letras");
                    System.out.println(guiones);
                    boolean entradaValida = false;
                    String intento = "";
                    while (!entradaValida) {
                        System.out.println("Introduce tu intento: ");
                        intento = sc.nextLine().toUpperCase();
                        if (intento.length() != guiones.length()) {
                            System.out.println("La palabra tiene " + guiones.length() + " letras");
                        } else if (!intento.matches("[A-Z]+")) {
                            System.out.println("La palabra solo contiene letras");
                        } else {
                            entradaValida = true;
                        }
                    }
                    pw.println(intento);
                    String cod = br.readLine();
                    System.out.println("Resultado: " + cod);
                    if (cod.equals("2".repeat(guiones.length()))) break;
                }
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

                String seguir = "";
                do {
                    System.out.print("Quieres jugar otra vez? (S/N): ");
                    seguir = sc.nextLine().trim().toUpperCase();
                } while (!seguir.equals("S") && !seguir.equals("N"));
                if (seguir.equals("S")) {
                    System.out.println("Iniciando nueva partida...");
                } else {
                    System.out.println("Finalizando partida...");
                    jugando = false;
                }
                pw.println(seguir);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
