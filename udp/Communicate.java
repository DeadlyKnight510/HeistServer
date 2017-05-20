import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Communicate {
	public static DatagramSocket server;
	static final int PORT = 8080;
	public Communicate(){
		try{
			server = new DatagramSocket(PORT);
			System.out.println("server successfully created");
		}catch(Exception e){
			System.out.println("server failed creation");
		}
	}
	public boolean send(DatagramPacket dp){
		try{
			server.send(dp);
			System.out.println("send: ip "+dp.getAddress()+" port "+dp.getPort()+" : "+new String(dp.getData()));
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public boolean send(String in, InetAddress ip, int port)
	{
		byte[] sendData = new byte[1024];
		sendData = in.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
		try{
			server.send(sendPacket);
		} catch (Exception e) {
			return false;
		}
		System.out.println("send: ip "+ip.getAddress()+" port "+port+" : "+in);
		return true;
	}
	public DatagramPacket receive(){
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try{
			server.receive(receivePacket);
		}catch(Exception e){}
		return receivePacket;
	}
}
