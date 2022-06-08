import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Scanner;

public class HttpServer {
    public static int   port = 80;

    public HttpServer() {
    }


    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }



        while (true) {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            boolean Sontour = false;
            boolean stop = false;
            String fichier = null;
            stop=false;
            while (!stop) {
                if (Sontour) {

                    try {
                        String reponse = "HTTP/1.1 200 OK\\r\\n";
                        System.out.println("[serveur] En attente d'un message clavier");

                        // ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                        DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
                        byte[]rep=reponse.getBytes();
                        InputStream fileInputStream = new FileInputStream(fichier);
                        byte[] data = fileInputStream.readAllBytes();
                        socket.getOutputStream().write(rep);
                        socket.getOutputStream().write(data);
                        socket.getOutputStream().flush();
                        Sontour = false;
                    } catch (FileNotFoundException f) {

                        System.out.println("non trouve");
                    }
                    stop=true;
                }
                else {
                    System.out.println("[server]En attente du client");
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String recu = bufferedReader.readLine();
                    System.out.println(recu);
                    if(recu!=null) {
                        fichier = "site web" + recu.substring(4, recu.indexOf("HTTP/1.1"));
                        System.out.println(fichier);
                    }
                    Sontour = true;
                    System.out.println("yes");

                }

            }
            serverSocket.close();
        }
    }
}


