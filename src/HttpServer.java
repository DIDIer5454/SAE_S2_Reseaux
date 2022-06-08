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
import java.io.IOException;

public class HttpServer {


    public static int port = 80;

    public HttpServer() {
    }

    public static void toString(NodeList Zut) {
        for (int i = 0; i < Zut.getLength(); i++) {
            System.out.println(Zut.item(i).getNodeName()+": "+Zut.item(i).getTextContent());
        }
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse("fxml/webconfig.xml");

        NodeList ligne;//=document.getDocumentElement().getTextContent();
        ligne = document.getDocumentElement().getElementsByTagName("port");
        //String [] tab=ligne.split("\t");
        System.out.println(ligne.item(0).getTextContent());

        Node port = document.getDocumentElement();

            NodeList root = document.getElementsByTagName("root");
            NodeList index = document.getElementsByTagName("index");
            NodeList accept = document.getElementsByTagName("accept");
            NodeList reject = document.getElementsByTagName("reject");

        System.out.println("port :" + port.getTextContent());
        toString(root);
        toString(index);
        toString(accept);
        toString(reject);



       /* if (args.length == 1) {
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
        }*/
    }
}


