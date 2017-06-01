
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Player{
	public int id;
	public String username;
	public Game current=null;
	public InetAddress address=null;
	public int port;
	public int[] health=new int[]{100,-1};
	public int[] x=new int[]{100,-1};
	public int[] y=new int[]{100,-1};
	public double[] a=new double[]{0.0,-1.0};
	public int[] progress = new int[]{0,-1};

	public Player(int id,String user,InetAddress address, int po){
		this.id = id;
		username = user;
		this.address = address;
		this.port = po;
	}
	public Player(int id,String user){
		this.id = id;
		username = user;
	}
	public Player(int id,String user,int x, int y, double a , int h){
		this.id = id;
		username = user;
		setXYAH(x,y,a,h);
	}
	public Player(int id){
		this.id = id;
	}
	public void reset(){
		x[1]=x[0];
		y[1]=y[0];
		a[1]=a[0];
		progress[1]=progress[0];
		health[1]=health[0];
	}
	public void setGame(Game g){
		current = g;
	}
	public Game getGame(){
		return current;
	}
	public boolean isChanged(){
		if(x[0]!=x[1])
			return true;
		if(y[0]!=y[1])
			return true;
		if(a[0]!=a[1])
			return true;
		if(health[0]!=health[1])
			return true;
		return false;
	}
	@Override
	public String toString(){
		return ""+id+" "+x[0]+" "+y[0]+" "+a[0]+" "+health[0];
	}
	public boolean cansend(){
		if(address==null)
			return false;
		return true;
	}
	public DatagramPacket getSend(String in){
		byte[] send = new byte[1024];
		if(in!=null)
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
	public void decreaseHLT(int h){
		setHealth(getHealth()-h);
	}
	public int getHealth(){ return health[0]; }
	public int getX(){ return x[0]; }
	public int getY(){ return y[0]; }
	public int getP(){ return progress[0]; }
	public double getA(){ return a[0]; }
	public void setHealth(int x){ health[0]=x; }
	public void setX(int z){ x[0]=z; }
	public void setY(int z){ y[0]=z; }
	public void setP(int z){ progress[0]=z; }
	public void setA(double z){ a[0]=z; }
	public void setXYAH(int x, int y, double a, int h){
		setX(x); setY(y); setA(a); setHealth(h);
	}
}
