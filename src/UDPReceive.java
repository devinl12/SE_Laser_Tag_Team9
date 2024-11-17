import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;

public class UDPReceive {
    public static void listenForHits(Consumer<String> eventHandler) {
        try {
            DatagramSocket socket = new DatagramSocket(7501); // Listening on port 7501
            byte[] buffer = new byte[256];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message: " + receivedMessage); // Debug line
                eventHandler.accept(receivedMessage); // Pass the event to the handler
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
