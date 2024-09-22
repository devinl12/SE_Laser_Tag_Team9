import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceive {
    public static void listenForHits() {
        try {
            //port 7500 for now for debugging purposes to see if equipment id is being transmitted
            DatagramSocket socket = new DatagramSocket(7500);
            byte[] buffer = new byte[256]; 
            
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("got hit information: " + receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
