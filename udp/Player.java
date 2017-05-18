import java.net.DatagramPacket;
import java.net.InetAddress;

public class Player{
	public int id;
	public String username;
	public Game current=null;
	public InetAddress address;
	public int port;

	public Player(int id,String user,InetAddress address, int po){
		this.id = id;
		username = user;
		this.address = address;
		this.port = po;
	}
	public Player(int id){
		this.id = id;
	}
	public void setGame(Game g){
		current = g;
	}
	public Game getGame(){
		return current;
	}
	public DatagramPacket getSend(String in){
		byte[] send = new byte[1024];
		send = in.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(send, send.length, address, port);
		return sendPacket;
	}
	public boolean equals(Player p) {
		if(p.id==this.id){
			return true;
		}
		else{
			return false;
		}
	}
}
