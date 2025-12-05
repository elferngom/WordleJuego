import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static final String cursiva = "\u001B[3m";
    public static final String negrita = "\u001B[1m";
    public static final String subrayado = "\u001B[4m";
    public static final String verde = "\u001B[32m";
    public static final String amarillo = "\u001B[33m";
    public static final String rojo = "\u001B[31m";
    public static final String reset = "\u001B[0m";

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce tu nombre: ");
            String nombre = sc.nextLine();
            pw.println(nombre);

            boolean jugando = true;
            int opcion;
            while (jugando) {
                System.out.println(negrita + subrayado + "--MENÚ PRINCIPAL--\n" + reset);
                System.out.println(negrita + " - Modo individual - " + reset);
                System.out.println("    1) Palabra de 4 letras");
                System.out.println("    2) Palabra de 5 letras");
                System.out.println("    3) Palabra de 6 letras");
                System.out.println("    4) Palabra de 7 letras");
                System.out.println("    5) Contrarreloj");
                System.out.println(negrita + " - Modo multijugador - " + reset);
                System.out.println("    6) Partida al mejor de 3 aciertos");
                System.out.print(cursiva + "\nElige un modo de juego: " + reset);

                while (true) {
                    if (sc.hasNextInt()) {
                        opcion = sc.nextInt();
                        sc.nextLine();
                        if (opcion >= 1 && opcion <= 6) break;
                    } else {
                        sc.next();
                    }
                    System.out.println(cursiva + "Por favor introduce una opción válida" + reset);
                }

                switch (opcion) {
                    case 1:
                        pw.println(1);
                        pw.println(4);
                        JugarPartidaSolo(br,pw,sc);
                        break;
                    case 2:
                        pw.println(1);
                        pw.println(5);
                        JugarPartidaSolo(br,pw,sc);
                        break;
                    case 3:
                        pw.println(1);
                        pw.println(6);
                        JugarPartidaSolo(br,pw,sc);
                        break;
                    case 4:
                        pw.println(1);
                        pw.println(7);
                        JugarPartidaSolo(br,pw,sc);
                        break;
                    case 5:
                        pw.println(2);
                        JugarPartidaSolo(br,pw,sc);
                        break;
                    case 6:
                        pw.println(3);
                        break;
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
                pw.println(jugando);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void JugarPartidaSolo(BufferedReader br, PrintWriter pw, Scanner sc) throws IOException{
        String tamanioStr = br.readLine();
        int tamanio = Integer.parseInt(tamanioStr);
        StringBuilder huecos = new StringBuilder("[][][][]");
        String intento = "";
        boolean win = false;
        System.out.println("La palabra tiene " + tamanio + " letras");
        for (int i = 4; i < tamanio; i++) {
            huecos.append("[]");
        }
        while (true) {
            System.out.println(huecos);
            boolean entradaValida = false;
            while (!entradaValida) {
                intento = sc.nextLine().toUpperCase();
                if (intento.length() != tamanio) {
                    System.out.println("La palabra tiene " + tamanio + " letras");
                    System.out.println(huecos);
                } else if (!intento.matches("[A-Z]+")) {
                    System.out.println("La palabra solo contiene letras");
                    System.out.println(huecos);
                } else {
                    entradaValida = true;
                }
            }
            pw.println(intento);
            String cod = br.readLine();
            for (int i = 0; i < tamanio; i++) {
                System.out.print("[");
                switch (cod.charAt(i)) {
                    case '0':
                        System.out.print(rojo + intento.charAt(i) + reset);
                        break;
                    case '1':
                        System.out.print(amarillo + intento.charAt(i) + reset);
                        break;
                    case '2':
                        System.out.print(verde + intento.charAt(i) + reset);
                }
                System.out.print("]");
            }
            System.out.println();
            if (cod.equals("2".repeat(tamanio))) break;
        }
        String s = br.readLine();
        if (s != null) {
            win = Boolean.parseBoolean(s);
            if (win) {
                String tiempoServidor = br.readLine();
                long tiempo = Long.parseLong(tiempoServidor);
                System.out.println("¡Has ganado! Tiempo: " + tiempo * 0.001 + " segundos.");
            } else {
                String palabra = br.readLine();
                System.out.println("Has perdido. La palabra era: " + palabra);
            }
        }

    }


    public static void PartidaMultijugador(){

    }
}
