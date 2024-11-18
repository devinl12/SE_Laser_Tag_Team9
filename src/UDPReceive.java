import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.function.Consumer;
import java.net.InetAddress;

public class UDPReceive {
    public static void listenForHits(Consumer<String> eventHandler) {
        DatagramSocket socket = null;
        try {
            //System.out.println("Attempting to bind DatagramSocket to port 7501...");
            socket = new DatagramSocket(7501, InetAddress.getByName("127.0.0.1")); // Listening on port 7501
            //System.out.println("DatagramSocket successfully bound to port 7501.");
            //System.out.println("Socket bound to address: " + socket.getLocalAddress() + " on port: " + socket.getLocalPort());
            // Add this line to log the local address and port
            //System.out.println("Expecting packets at: " + socket.getLocalAddress() + ":" + socket.getLocalPort());

            byte[] buffer = new byte[256];

            while (true) {
                //System.out.println("Listening for UDP packets..."); // Debug line
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                // Log packet details
                //System.out.println("Packet received:");
                //System.out.println("  From: " + packet.getAddress() + ":" + packet.getPort());
                //System.out.println("  Length: " + packet.getLength());

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                //System.out.println("Received message: " + receivedMessage); // Debug line
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


