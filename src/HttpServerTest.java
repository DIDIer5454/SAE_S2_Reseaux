import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {
    @Test
    public void Testconversionbit() {
        int[] IP = {192, 168, 12, 1};
        int[][] Bit = {{1, 1, 0, 0, 0, 0, 0, 0}, {1, 0, 1, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1}};
        int[] reseau;
        for (int i = 0; i < Bit.length; i++) {
            reseau = HttpServer.conversionbit(IP[i]);
            assertArrayEquals(Bit[i], reseau);
        }
    }

    @Test
    public void TestconversionOctet() {
        int[] IP = {192, 168, 12, 1};
        int[][] Bit = {{1, 1, 0, 0, 0, 0, 0, 0}, {1, 0, 1, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 1}};
        for (int i = 0; i < Bit.length; i++) {
            for (int j = 0; j < Bit[i].length; j++) {
                int octet = HttpServer.convertionOctet(Bit[i]);
                assertEquals(octet,IP[i]);
            }
        }
    }
    @Test
    public void TestAddition(){
        int[] address={192,168,12,0};
        int[] IP = {192, 168, 12, 1};
        int [] resultat=HttpServer.addition(IP,24);
        assertArrayEquals(address,resultat);
    }

    @Test
    public void TestEstDans(){
        boolean m;
        String[]tab={"192.168.12.0/24"};
        assertTrue(HttpServer.estDans(tab,"192.168.12.1"));
        assertFalse(HttpServer.estDans(tab,"192.168.13.2"));
        assertFalse(HttpServer.estDans(tab,"180.123.12.1"));
    }
}