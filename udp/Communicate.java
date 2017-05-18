import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Communicate {
	public static boolean send(DatagramSocket server, String in, InetAddress ip, int port)
	{
		byte[] sendData = new byte[1024];
		sendData = in.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
		try{
			server.send(sendPacket);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static DatagramPacket receive(DatagramSocket server){
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try{
			server.receive(receivePacket);
		}catch(Exception e){}
		return receivePacket;
	}
}