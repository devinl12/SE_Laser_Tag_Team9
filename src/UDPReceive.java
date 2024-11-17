import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;

public class UDPReceive {
    public static void listenForHits(Consumer<String> eventHandler) {
        DatagramSocket socket = null;
        try {
            System.out.println("Attempting to bind DatagramSocket to port 7501...");
            socket = new DatagramSocket(7501); // Listening on port 7501
            System.out.println("DatagramSocket successfully bound to port 7501.");

            byte[] buffer = new byte[256];

            while (true) {
                System.out.println("Listening for UDP packets..."); // Debug line
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message: " + receivedMessage); // Debug line
                eventHandler.accept(receivedMessage); // Pass the event to the handler
            }
        } catch (Exception e) {
            System.out.println("Failed to bind DatagramSocket to port 7501.");
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("DatagramSocket closed."); // Debug line
            }
        }
    }
}

