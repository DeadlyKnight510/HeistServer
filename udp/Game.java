import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
public class Game {
	public ArrayList<Player> team1,team2;
	public ArrayList<GameObject> gos;
	public String name=null;
	public int gameid;
	public static int numgames=0;
	public boolean gameStart=false;

	public Game(String n){
		team1 = new ArrayList<Player>();
		team2 = new ArrayList<Player>();
		/*
		team1.add(new Player(8,"me"));
		team2.add(new Player(11,"why"));
		team1.add(new Player(17,"no"));
		team2.add(new Player(9,"you"));
		*/
		gos = new ArrayList<GameObject>();
		name=n;
		System.out.println("new game created");
		gameid=numgames;
		numgames++;
	}
	public void startGame(){
		gameStart=true;
	}
	public void addGO(Player p, int id, int x, int y, double a){
		GameObject temp = new GameObject(p.id,id,x,y,a);
		gos.add(temp);
	}
	public String getPlayers(){
		String out="GTPERS|"+gameid;
		for(Player p1:team2){
			out+="|"+p1.id+","+p1.username.substring(0,4);
		}
		for(Player p2:team1){
			out+="|"+p2.id+","+p2.username.substring(0,4);
		}
		System.out.println("generated "+out);
		return out;
	}
	public void addPlayer(Player p){
		if(team1.size()>team2.size()){
			team2.add(p);
		} else {
			team1.add(p);
		}
		p.setGame(this);
		sendToAll(getPlayers());
	}
	public boolean containsPlayerInOne(Player p){
		for(Player temp:team1){
			if(temp.equals(p))
				return true;
		}
		return false;
	}
	public boolean containsPlayerInTwo(Player p){
		for(Player temp:team2){
			if(temp.equals(p))
				return true;
		}
		return false;
	}
	public boolean containsPlayer(Player p){
		return (containsPlayerInTwo(p) || containsPlayerInOne(p));
	}
	public void removePlayer(Player p)
	{
		for(Player temp1:team1){
			if(temp1.equals(p)){
				team1.remove(p);
				p.setGame(null);
				sendToAll(getPlayers());
				return;
			}
		}
		for(Player temp2:team2){
			if(temp2.equals(p)){
				team2.remove(p);
				p.setGame(null);
				sendToAll(getPlayers());
				return;
			}
		}
	}
	public void setXYA(Player p, int x, int y, double a, int h)
	{
		p.setXYAH(x,y,a,h);
	}
	public void end(){
		sendToAll("DONE");
	}
	public void kill(Player p){
/*		sendToAll("UPD|);
		if(getPlayerNum(p)==1)
			ServerUDP.c.send(p2.getSend("UPD|KILL"));
		else
			ServerUDP.c.send(p1.getSend("UPD|KILL"));
*/
	}
	public boolean start(){
		try{
			setPlayers();
			sendToAll(toString("START"));
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public void setPlayers(){
		for(Player p1:team1){
			p1.setXYAH(1500,200,0.0,100);
		}
		for(Player p2:team2){
			p2.setXYAH(2000,200,0.0,100);
		}
	}
	public boolean update(){
		if(gameStart){
			if(isChanged()){
				sendToAll(toString("UPD"));
				gos.clear();
				for(Player p1:team1){
					p1.reset();
				}
				for(Player p2:team2){
					p2.reset();
				}
			}
		}
		return true;
	}
	public boolean isChanged(){
		for(Player p1:team1){
			if(p1.isChanged())
				return true;
		}
		for(Player p2:team2){
			if(p2.isChanged())
				return true;
		}
		return false;
	}
	public String toString(String beg)
	{
		//only gives value of other player
		String out=beg;
		for(Player p1:team1){
			out+="|T1 "+p1.toString();
		}
		for(Player p2:team2){
			out+="|T2 "+p2.toString();
		}
		for(GameObject go : gos){
			out+="|OBJ "+go.toString();
		}
		return out;
	}
	public void sendToAll(String in){
		for(Player p1:team1){
			ServerUDP.c.send(p1.getSend(in));
		}
		for(Player p2:team2){
			ServerUDP.c.send(p2.getSend(in));
		}
	}
}
