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
		team1.add(new Player(8,"me"));
		team2.add(new Player(9,"you"));
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
		for(Player p1:team1){
			out+="|"+p1.id+","+p1.username;
		}
		for(Player p2:team2){
			out+="|"+p2.id+","+p2.username;
		}
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
		if(!containsPlayer(p))
			return;
		if(containsPlayerInTwo(p)){
			team1.remove(p);
		} else {
			team2.remove(p);
		}
		p.setGame(null);
		sendToAll(getPlayers());
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
			sendToAll(toString("START"));
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public boolean update(){
		if(gameStart){
			if(isChanged()){
				sendToAll(toString("UPD"));
				gos.clear();
			}
		}
		return true;
	}
	public boolean isChanged(){
		for(Player p1:team1){
			if(p1.isChanged())
				return true;
		}
		for(Player p2:team1){
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
		for(Player p2:team1){
			ServerUDP.c.send(p2.getSend(in));
		}
	}
}
