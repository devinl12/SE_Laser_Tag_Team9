import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTransmit {

    public static void transmitEquipmentCode(int equipmentId) {

        try {

            DatagramSocket socket = new DatagramSocket(); 
            //using 255.255.255.255 broadcast address to send to all devices on the same local network
            InetAddress broadcastAddress = InetAddress.getByName("127.0.0.1");
            
            String message = String.valueOf(equipmentId);
            byte[] packetBytes = message.getBytes();
            
            // creating packet containing the equipment ID and sending over the UDP socket to the multicast address
            DatagramPacket packet = new DatagramPacket(packetBytes, packetBytes.length, broadcastAddress, 7500);
            socket.send(packet);

            //for debugging
            System.out.println("Equipment ID: " + equipmentId);
            
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

