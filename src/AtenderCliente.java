import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;

public class AtenderCliente implements Runnable {
    private Socket socket;
    private static Map<Integer,List<String>> PALABRAS;
    private static Random random=new Random();
    private static List<String> PALABRASMULTIJUGADOR;
    private PartidaMultijugador partidaMultijugador;
    public AtenderCliente(Socket socket, Map<Integer,List<String>> PALABRAS) {
        this.socket = socket;
        this.PALABRAS = PALABRAS;
    }
    public boolean win = false;
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);) {
            boolean jugando = true;
            String jugandoStr = "";
            String jugador = br.readLine();
            List<String> palabrasTamanio = new ArrayList<>();
            String opcionStr = "";
            String tamanioStr = "";
            int opcion;
            int tamanio;

            while (jugando) {//Puede jugar más de una partida
                opcionStr = br.readLine();
                opcion = Integer.parseInt(opcionStr);

                switch (opcion) {
                    case 1:
                        tamanioStr = br.readLine();
                        tamanio = Integer.parseInt(tamanioStr);
                        jugarPartida(br, pw, jugador, tamanio, palabrasTamanio, false);
                        break;
                    case 2:
                        tamanio = random.nextInt(4,8);
                        jugarPartida(br, pw, jugador, tamanio, palabrasTamanio, true);
                        break;
                    case 3:
                        partidaMultijugador(br,pw,jugador,random);
                        break;
                }
                jugandoStr = br.readLine();
                jugando = Boolean.parseBoolean(jugandoStr);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void jugarPartida(BufferedReader br, PrintWriter pw, String jugador, int tamanio,List<String> palabrasTamanio, boolean contrarreloj)throws IOException {
        win = false;
        String respuestaServidor = null;
        String palabra;
        palabrasTamanio = PALABRAS.get(tamanio);
        palabra = palabrasTamanio.get(random.nextInt(palabrasTamanio.size()));
        pw.println(palabra.length());
        int intentos = 1;
        long inicio = 0;
        if (!contrarreloj) {
            intentos = 6;
            pw.println(intentos);
        } else {
            inicio = System.currentTimeMillis();
        }


        while (intentos > 0) {
            String respuesta = br.readLine(); //Leo y guardo la respuesta del cliente
            respuestaServidor = verificarIntento(palabra, respuesta);
            pw.println(respuestaServidor); //ej: 01201
            if (!contrarreloj) intentos--;
            if (respuestaServidor.equals("2".repeat(palabra.length()))) {
                win = true;
                break;
            }
        }

        pw.println(win);
        if (win) {
            if (contrarreloj) {
                long fin = System.currentTimeMillis();
                long tiempo = fin - inicio;
                pw.println(tiempo);
                TablaRecords.agregarEntrada(palabra, jugador, tiempo);
                guardar(Servidor.records);
            }
        } else {
            pw.println(palabra);
        }
    }

    public void partidaMultijugador(BufferedReader br, PrintWriter pw, String jugador,Random rd){
        int misPuntos;
        int puntosRival;
        boolean primero=false;
        List<String> palabras = new ArrayList<>();
        palabras.add(PALABRAS.get(5).get(rd.nextInt(PALABRAS.get(5).size())));
        palabras.add(PALABRAS.get(5).get(rd.nextInt(PALABRAS.get(5).size())));
        palabras.add(PALABRAS.get(5).get(rd.nextInt(PALABRAS.get(5).size())));
        synchronized(Servidor.class) {
            if (Servidor.partidaMultijugadorEnEspera == null) { //Si no hay partida soy el primero la creo y le paso ya las palabras al azar
                partidaMultijugador = new PartidaMultijugador(palabras);
                //Dejo la partida visible y accesible en el servidor para otro jugador
                Servidor.partidaMultijugadorEnEspera = partidaMultijugador;
                primero = true;
            } else {
                primero = false;
                partidaMultijugador = Servidor.partidaMultijugadorEnEspera;
                //Ya somos dos entonces vacio la partida en espera
                Servidor.partidaMultijugadorEnEspera = null;
            }
        }
        try{
            this.partidaMultijugador.getBarrera().await();
            pw.println("EMPIEZA LA PARTIDA");
            for(int i=0;i<3;i++){
                pw.println("RONDA "+i+1);
                int intentos=jugarPartidaMulti(br,pw,jugador,palabras.get(i));
                long tiempo=1000;
                pw.println("Esperando al otro jugador...");
                this.partidaMultijugador.registrarResultados(intentos, tiempo, primero);
                this.partidaMultijugador.getBarrera().await();
                if (primero) {
                    this.partidaMultijugador.apuntarPuntos();
                }
                this.partidaMultijugador.getBarrera().await();
            }
        if(primero){
            misPuntos=this.partidaMultijugador.puntosJ1;
            puntosRival=this.partidaMultijugador.puntosJ2;
        }else{
            misPuntos=this.partidaMultijugador.puntosJ2;
            puntosRival=this.partidaMultijugador.puntosJ1;
        }
        if(misPuntos>puntosRival){
            pw.println("HAS GANADO CON "+ misPuntos+"!");
        } else{
            pw.println("HAS PERDIDO CON "+ misPuntos+"!");
        }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    //Este metodo es booleano para que cuando un cliente acierte la palabra vctorias sume un punto
    public int jugarPartidaMulti(BufferedReader br, PrintWriter pw, String jugador, String palabra)throws IOException {
        win = false;
        String respuestaServidor = null;
        StringBuilder guiones = new StringBuilder();
        for (int i = 0; i < palabra.length(); i++) {
            guiones.append("_");
        }
        pw.println(guiones); //Envia al cliente el número de letras que tiene la palabra
        int intentos = 0; //número de intentos por partida
        long inicio = System.currentTimeMillis();
        while (!win) {
            String respuesta = br.readLine(); //Leo y guardo la respuesta del cliente
            respuestaServidor = verificarIntento(palabra, respuesta);
            pw.println(respuestaServidor); //ej: 01201
            intentos++;
            if (respuestaServidor.equals("2".repeat(palabra.length()))) {
                win = true;
                break;
            }
        }
        return intentos;
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
            if(resultado[i] != 2){
                char letraIntento = cliente.charAt(i);
                int indiceEnSecreta = servidorModificable.indexOf(String.valueOf(letraIntento));
                if (indiceEnSecreta != -1) {
                    resultado[i] = 1;
                    servidorModificable.setCharAt(indiceEnSecreta, '#');
                } else {
                    resultado[i] = 0;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i : resultado) {
            sb.append(i);
        }
        return sb.toString();
    }
    public static void guardar(TablaRecords tabla) {
        synchronized (tabla) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("records.ser"))) {
                oos.writeObject(tabla);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
