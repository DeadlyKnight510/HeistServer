import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
public class Game {
	public Player p1,p2;
	// public int p1X=0,p2X=500,p1Y=0,p2Y=500;
	// public int[] p1X=new int[]{0,0},p2X=500,p1Y=0,p2Y=500;
	// public int old_p1X=0,old_p2X=500,old_p1Y=0,old_p2Y=500;
	// public int hlth1=100,hlth2=100;
	// public int old_hlth1=100,old_hlth2=100;

	public int[] hlth1=new int[]{100,-1},hlth2=new int[]{100,-1};
	//first value is current, second is the last value to be sent
	public int[] p1X=new int[]{1000,-1},p2X=new int[]{1500,-1},p1Y=new int[]{500,-1},p2Y=new int[]{500,-1};
	public double[] p1A=new double[]{0.0,-1.0},p2A=new double[]{0.0,-1.0};
	public ArrayList<GameObject> p1Q,p2Q;
//	public ArrayList<String> objects; //for now - eventually will be gameobjects
	public Game(Player player1, Player player2){
		p1 = player1;
		p2 = player2;
//		objects = new ArrayList<String>();  //for now - eventually will be gameobjects
		p1Q= new ArrayList<GameObject>();
		p2Q= new ArrayList<GameObject>();
		p1.setGame(this);
		p2.setGame(this);
	}
	public void addGO(Player p, int id, int x, int y, double a){
		GameObject temp = new GameObject(id,x,y,a);
		if(getPlayerNum(p)==1){
			p2Q.add(temp);
		}
		else {	
			p1Q.add(temp);
		}
	}
	public int getP1X(){ return p1X[0]; }
	public int getP2X(){ return p2X[0]; }
	public int getP2Y(){ return p2Y[0]; }
	public int getP1Y(){ return p1Y[0]; }
	public void setP1X(int x){ p1X[0] = x; }
	public void setP2X(int x){ p2X[0] = x; }
	public void setP1Y(int x){ p1Y[0] = x; }
	public void setP2Y(int x){ p2Y[0] = x; }
	public void setP2A(double x){ p2A[0] = x; }
	public void setP1A(double x){ p1A[0] = x; }
	public boolean containsPlayer(Player p){
		if(p1.equals(p)){
			return true;
		}
		else if(p2.equals(p))
			return true;
		else
			return false;
	}
	public void removePlayer(Player p)
	{
		
	}
	public void setXYA(Player p, int x, int y, double a)
	{
		System.out.println("called change xy : "+x);
		if(getPlayerNum(p)==1){
			setP1X(x); setP1Y(y); setP1A(a);
		}
		else {	
			setP2X(x); setP2Y(y); setP2A(a);
		}
	}
	public int getPlayerNum(Player p){
		if(p.equals(p1))
			return 1;
		else
			return 2;
	}
	public String getLayout(Player p){
		String out = "START|";
		if(getPlayerNum(p)==1){
			out+="XY 0 "+p1X[0]+" "+p1Y[0]+" "+p1A[0];
			out+="|XY 1 "+p2X[0]+" "+p2Y[0]+" "+p2A[0];
		} else {
			out+="XY 0 "+p2X[0]+" "+p2Y[0]+" "+p2A[0];
			out+="|XY 1 "+p1X[0]+" "+p1Y[0]+" "+p1A[0];
		}
		return out;
	}
	public boolean equals(Game g) {
		if(this.p1.equals(g.p1)&&this.p2.equals(g.p2)){
			return true;
		}
		else{
			return false;
		}
	}
	public void end(){
		ServerUDP.c.send(p1.getSend("DONE"));
		ServerUDP.c.send(p2.getSend("DONE"));
	}
	public void kill(Player p){
		if(getPlayerNum(p)==1)
			ServerUDP.c.send(p2.getSend("UPD|KILL"));
		else
			ServerUDP.c.send(p1.getSend("UPD|KILL"));
	}
	public boolean start(){
		try{
			ServerUDP.c.send(p1.getSend(getLayout(p1)));
			ServerUDP.c.send(p2.getSend(getLayout(p2)));
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public boolean update(){
		try{
			if(isChanged(p1)){
				ServerUDP.c.send(p1.getSend(toString(p1)));
				p2X[1]=p2X[0];
				p2Y[1]=p2Y[0];
				p2A[1]=p2A[0];
				hlth2[1]=hlth2[0];
			}
			if(isChanged(p2)){
				ServerUDP.c.send(p2.getSend(toString(p2)));
				p1X[1]=p1X[0];
				p1Y[1]=p1Y[0];
				p1A[1]=p1A[0];
				hlth1[1]=hlth1[0];
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public String toString(Player p)
	{
		//only gives value of other player
		String out="UPD|";
		if(getPlayerNum(p)==1){
			out+="XY "+p2X[0]+" "+p2Y[0]+" "+p2A[0]+"|";
			out+="HLTH "+hlth2[0];
			for(GameObject go : p1Q){
				out+="|OBJ "+go.toString();
			}
			p1Q.clear();
		} else {
			out+="XY "+p1X[0]+" "+p1Y[0]+" "+p1A[0]+"|";
			out+="HLTH "+hlth1[0];
			for(GameObject go : p2Q){
				out+="|OBJ "+go.toString();
			}
			p2Q.clear();
		}
		return out;
	}
	public boolean isChanged(Player p){
		if(getPlayerNum(p)==1){
			if(p2X[0]!=p2X[1])
				return true;
			if(p2Y[0]!=p2Y[1])
				return true;
			if(hlth2[0]!=hlth2[1])
				return true;
			if(p2A[0]!=p2A[1])
				return true;
			if(p1Q.size()>0)
				return true;
		} else {
			if(p1X[0]!=p1X[1])
				return true;
			if(p1Y[0]!=p1Y[1])
				return true;
			if(p1A[0]!=p1A[1])
				return true;
			if(hlth1[0]!=hlth1[1])
				return true;
			if(p2Q.size()>0)
				return true;
		}
		return false;
	}
}
