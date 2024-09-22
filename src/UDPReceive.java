import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceive {
    public static void listenForHits() {
        try {
            DatagramSocket socket = new DatagramSocket(7500);
            byte[] buffer = new byte[256]; 
            
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received hit information: " + receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
