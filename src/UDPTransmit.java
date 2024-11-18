import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTransmit {

    public static void transmitEquipmentCode(int equipmentId) {

        try {

            DatagramSocket socket = new DatagramSocket(); 
            InetAddress castAddress = InetAddress.getByName("127.0.0.1");
            
            String message = String.valueOf(equipmentId);
            byte[] packetBytes = message.getBytes();
            
            // creating packet containing the equipment ID and sending over the UDP socket to the castaddress
            DatagramPacket packet = new DatagramPacket(packetBytes, packetBytes.length, castAddress, 7500);
            socket.send(packet);

            
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

