import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class HttpServer {

    public static boolean indexB;
    public static int port = 80;
    public static ArrayList<String> rejette;
    public static ArrayList<String> accepte;

    public HttpServer() {
    }

    public static String toString(NodeList Zut) {
        String S = "";
        for (int i = 0; i < Zut.getLength(); i++) {
            S += Zut.item(i).getTextContent();
            if (Zut.getLength() > 1) {
                S += "\n";
            }
        }
        return S;
    }

    /**
     * methode convertissant un entier en tableau de 8 entier representant les 8 bit dun octet
     *
     * @param i entier
     * @return
     */
    public static int[] conversionbit(int i) {
        int[] binaries = {128, 64, 32, 16, 8, 4, 2, 1};
        int[] reseau = {0, 0, 0, 0, 0, 0, 0, 0};
        for (int p = 0; p < reseau.length; p++) {
            if (i - binaries[p] >= 0) {
                i = i - binaries[p];
                reseau[p] = 1;
            }
        }
        return reseau;
    }

    /**
     * methode convertissant un tableau de 8 entier representant des bit doctet
     * en entier
     *
     * @param i
     * @return
     */
    public static int convertionOctet(int[] i) {
        int octet = 0;
        int[] binaries = {128, 64, 32, 16, 8, 4, 2, 1};
        for (int k = 0; k < i.length; k++) {
            if (i[k] == 1) {
                octet += binaries[k];
            }
        }
        return octet;
    }

    /**
     * methode retournant un tableau a double entree de bit
     * representant les bit du masque
     *
     * @param mask
     * @return
     */
    public static int[][] faireBitMask(int mask) {
        int nb1 = 0;
        int k1 = 0;
        int k2 = 0;
        int[][] reseaumaskBit = new int[4][8];
        while (nb1 < mask) {
            reseaumaskBit[k1][k2] = 1;
            nb1++;
            k2++;
            if (k2 > 7) {
                k2 = 0;
                k1++;
            }
        }
        return reseaumaskBit;
    }

    /**
     * methode fesant une addition bit a bit
     *
     * @param Ip
     * @param mask
     * @return
     */
    public static int[] addition(int[] Ip, int mask) {
        int[][] reseauBit = new int[4][8];
        int[][] addressBit = new int[4][8];

        int[] addressReseau = new int[4];

        int[][] reseaumaskBit = faireBitMask(mask);

        for (int k = 0; k < Ip.length; k++) {
            for (int l = 0; l < reseauBit[k].length; l++) {
                reseauBit[k][l] = conversionbit(Ip[k])[l];

                if (reseauBit[k][l] == 1 && reseaumaskBit[k][l] == 1) {
                    addressBit[k][l] = 1;
                } else {
                    addressBit[k][l] = 0;
                }
            }
            addressReseau[k] = convertionOctet(addressBit[k]);
        }
        return addressReseau;
    }

    /**
     * methode verfiant si l'addresse ip coorespond a une addresse de l'adresse reseau en parametre
     *
     * @param tab
     * @param Ip
     * @return
     */
    public static boolean estDans(String[] tab, String Ip) {
        boolean m = true;

        /*
-currentAddressMask=tableau de String de la forme {"Adrresse Reseau" ;"Mask"}
-addressReseauString est l'AdrressReseau sous forme de chaine de caractère
-mask cest le masque reseau sous forme d'entier
-currentIP est laddresse IP client sous forme de tableau dentier de taille 4 (ce programme ne traite que les addresse en IPv4
            ,sauf si lhote du client  est aussi lordinateur hebergeant le serveur).

 **la logique de cette methode est de rechercher une addresse reseau a partir de laddresse Ip du client
 et du masque du reseau cette addresse calculé sera comparé a laddresse reseau fourni par le tableau en
 parametre si cest identique alors la methode renvoi true.

 */
        for (int j = 0; j < tab.length; j++) {

            String[] currentAddressMask = tab[j].split("/");
            String[] addressReseauString = currentAddressMask[0].split("\\.");
            int mask = Integer.parseInt(currentAddressMask[1]);
            String[] currentIP = Ip.split("\\.");

            int[] addressIP = new int[4];
            int[] addressReseau = new int[4];
            /*
            remplissage des tableau addressIP et addressReseau
             */
            for (int i = 0; i < currentIP.length; i++) {
                addressIP[i] = Integer.parseInt(currentIP[i]);
                addressReseau[i] = Integer.parseInt(addressReseauString[i]);
            }
            /*
            addition de ladresse Ip et du mask
             */
            int[] reseauIP = addition(addressIP, mask);
            /*
             verfication avec un parcours partiel
             */
            int k = 0;
            while (m == true && k < 4) {
                m = reseauIP[k] == addressReseau[k];
                k++;
            }
        }

        return m;
    }

    /**
     * methode retournant un texte html
     * repertoriant tout les fichier et dossier présent
     * dans le repertoire root du site
     * avec les liens correspondant
     *
     * @return
     */
    public String genererPremier() {
        String texte = "";

        return texte;
    }


    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        System.clearProperty("java.net.preferIPv6Addresses");
        System.clearProperty("preferIPv4Stack");

        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");


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
        indexB = Boolean.parseBoolean(Sindex);
        String[] rejecttab = Sreject.split("\n");
        String[] accepttab = Saccept.split("\n");


        while (true) {
            ServerSocket serverSocket = new ServerSocket(port);

            Socket socket = serverSocket.accept();
            String address = "" + socket.getInetAddress();
            /*
            traitement de laddresse ip pour etre exploitable dans la methode estDAns()
             */
            address = address.substring(1);
            if (address.contains(":")) {
                address = address.replace(':', '.');

                address = address.substring(8);
            }
            System.out.println(address);
            System.out.println(serverSocket.getLocalSocketAddress());
            if (estDans(rejecttab, address)) {
                socket.close();
                serverSocket.close();
            }
           /*if (accepttab.length > 0) {
                if (!estDans(accepttab, address)) {
                    socket.close();
                    serverSocket.close();
                }
            }*/

            boolean Sontour = false;
            boolean stop = false;
            String fichier = null;
            stop = false;
            while (!stop) {
                if (Sontour) {

                    try {
                        String reponse = "HTTP/1.1 200 OK\\r\\n";
                        System.out.println("[serveur] En attente d'un message clavier");

                        // ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
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
                        fichier = Sroot + recu.substring(4, recu.indexOf("HTTP/1.1"));
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


