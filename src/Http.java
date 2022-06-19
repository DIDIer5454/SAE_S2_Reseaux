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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Http {
    public static boolean indexB;
    public static ArrayList<String> rejette;
    public static ArrayList<String> accepte;
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
        Document document = documentBuilder.parse("../fxml/webconfig.xml");


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

        int port = Integer.parseInt(Sport);
        indexB = Boolean.parseBoolean(Sindex);
        String[] rejecttab = Sreject.split("\n");
        String[] accepttab = Saccept.split("\n");

        ServerSocket serverSocket = new ServerSocket(port);
        String fichier = null;
        while (true) {
            Socket socket = serverSocket.accept();
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String recu = bufferedReader.readLine();
            System.out.println("recu:" + recu);
            if (recu != null) {
                fichier = Sroot + recu.substring(4, recu.indexOf("HTTP/1.1"));
                try {
                    String reponse = "HTTP/1.1 200 OK\r\n";
                    String contenttype = "Content-Type: text/html\r\n\r\n";
                    byte[] rep = reponse.getBytes(StandardCharsets.UTF_8);
                    byte[] content = contenttype.getBytes(StandardCharsets.UTF_8);
                    InputStream fileInputStream = new FileInputStream(fichier);
                    byte[] data = fileInputStream.readAllBytes();
                    socket.getOutputStream().write(rep);
                    socket.getOutputStream().write(content);
                    socket.getOutputStream().write(data);
                    socket.getOutputStream().flush();
                    socket.close();
                } catch (FileNotFoundException f) {
                    System.out.println("non trouve");
                    System.out.println(fichier);
                    socket.close();
                }
            }
        }
    }
}
            
