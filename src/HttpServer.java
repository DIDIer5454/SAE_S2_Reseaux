import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.print.attribute.Attribute;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {


    public static int port = 80;

    public HttpServer() {
    }

    public static String toString(NodeList Zut) {
        String S = "";
        for (int i = 0; i < Zut.getLength(); i++) {
            S +=  Zut.item(i).getTextContent();
        }
        return S;
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse("fxml/webconfig.xml");


        NodeList Nport = document.getElementsByTagName("port");

        NodeList root = document.getElementsByTagName("root");
        NodeList index = document.getElementsByTagName("index");
        NodeList accept = document.getElementsByTagName("accept");
        NodeList reject = document.getElementsByTagName("reject");

        String Sport = toString(Nport);
        String Sroot = toString(root);
        String Sindex = toString(index);
        String Saccept = toString(accept);
        String Sreject = toString(reject);


        port = Integer.parseInt(Sport);



        while (true) {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            boolean Sontour = false;
            boolean stop = false;
            String fichier = null;
            stop = false;
            while (!stop) {
                if (Sontour) {

                    try {
                        String reponse = "HTTP/1.1 200 OK\\r\\n";
                        System.out.println("[serveur] En attente d'un message clavier");

                        byte[] rep = reponse.getBytes();
                        InputStream fileInputStream = new FileInputStream(fichier);
                        byte[] data = fileInputStream.readAllBytes();
                        socket.getOutputStream().write(rep);
                        socket.getOutputStream().write(data);
                        socket.getOutputStream().flush();
                        Sontour = false;
                    } catch (FileNotFoundException f) {

                        System.out.println("non trouve");
                    }
                    stop = true;

                } else {
                    System.out.println("[server]En attente du client");
                    InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String recu = bufferedReader.readLine();
                    System.out.println(recu);
                    if (recu != null) {
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


