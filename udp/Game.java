import java.util.ArrayList;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Collections;
public class Game {
	public List<Player> team1,team2;
	public List<GameObject> gos;
	public String name=null;
	public int gameid;
	public static int numgames=0;
	public boolean gameStart=false;
	public boolean gameOver=false;

	public int[] progress={0,-1}; //how long player in vault
	public int maxprog=300;
	public int currPersProg=-1;
/*	public void setProgress(int pid, int pg){
		if(currPersProg != pid){
			if(pg > progress[0]){
				currPersProg = pid;
				progress[0] = pg;
				System.out.println("1 "+progress[0]+" "+pg);
			}
		}
		else{
			progress[0] = pg;
			System.out.println("2 "+progress[0]+" "+pg);
		}
	}
*/
	public Game(String n){
		team1 = Collections.synchronizedList(new ArrayList<Player>()); //	robbers
		team2 = Collections.synchronizedList(new ArrayList<Player>()); // guards
		gos = Collections.synchronizedList(new ArrayList<GameObject>());
		name=n;
		gameid=numgames;
		numgames++;
	}
	public void startGame(){
		gameStart=true;
		ServerUDP.m.updateGames();
		start();
	}
	public void addGO(Player p, int id, int x, int y, double a){
		GameObject temp = new GameObject(p.id,id,x,y,a);
		gos.add(temp);
	}
	public String getPlayers(){
		String out="GTPERS|"+gameid;
		synchronized(team2){
			Iterator<Player> iterator = team2.iterator(); 
			while (iterator.hasNext()){
				Player p = iterator.next();
				out+="|"+p.id+","+p.username.substring(0,6);
			}
		}
		synchronized(team1){
			Iterator<Player> iterator = team1.iterator(); 
			while (iterator.hasNext()){
				Player p = iterator.next();
				out+="|"+p.id+","+p.username.substring(0,6);
			}
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
		for(Player temp1:team1){
			if(temp1.equals(p)){
				team1.remove(p);
				p.setGame(null);
//				Player l = new Player(p.id,p.username);
//				setXYA(l,p.getX(),p.getY(),p.getA(),-100);
//				team1.add(l);
				return;
			}
		}
		for(Player temp2:team2){
			if(temp2.equals(p)){
				team2.remove(p);
				p.setGame(null);
//				Player l = new Player(p.id,p.username);
//				setXYA(l,p.getX(),p.getY(),p.getA(),-100);
//				team1.add(l);
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
		sendToAll("DONE|3");
	}
	public int getMaxProg(){
		int pr = 0;
		for(Player p:team1){
			if(p.getP()>pr)
				pr = p.getP();
		}
		return pr;
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
			int size = team1.size();
			if(size==1)
				maxprog = 600;
			else if(size < 3)
				maxprog = 400;
			else if(size < 5)
				maxprog = 300;
			else if(size >=5)
				maxprog = 200;
			sendToAll(toString("START"));
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public void setPlayers(){
		for(Player p1:team1){
			p1.setXYAH(1000,1900,0.0,100);
		}
		for(Player p2:team2){
			p2.setXYAH(1900,474,0.0,100);
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
				progress[1]=progress[0];
			}
			if(team1.size()==0){
				sendToAll("DONE|2");
				gameStart=false;
				gameOver=true;
			}
			if(team2.size()==0){
				sendToAll("DONE|1");
				gameStart=false;
				gameOver=true;
			}
			progress[0] = getMaxProg();
			if(teamhealth()){
				// DONE [guards won]
				sendToAll("DONE|2");
				gameStart=false;
				gameOver=true;
			} else if(progress[0]==300){
				System.out.println("300");
				// DONE [robbers won]
				sendToAll("DONE|1");
				gameStart=false;
				gameOver=true;
			}
		}
		return true;
	}
	public boolean teamhealth(){
		for(Player p:team1){
			if(p.getHealth()>0)
				return false;
		}
		return true;
	}
	public boolean isChanged(){
		for(Player p1:team1){
			if(p1!=null){
				if(p1.isChanged())
					return true;
			} else
				System.out.println("null");
		}
		for(Player p2:team2){
			if(p2.isChanged())
				return true;
		}
		if(gos.size()>0)
			return true;
		if(progress[0]!=progress[1])
			return true;
		return false;
	}
	public String toString(String beg)
	{
		//only gives value of other player
		String out=beg;
		synchronized(team1){
			Iterator<Player> iterator = team1.iterator(); 
			while (iterator.hasNext()){
				out+="|T1 "+iterator.next().toString();
			}
		}
		for(Player p1:team1){
			out+="|T1 "+p1.toString();
		}
		for(Player p2:team2){
			out+="|T2 "+p2.toString();
		}
		if(beg.trim().equals("START")){
			out+="|MAXPROG "+maxprog;
		}
		out+="|PROG "+progress[0];
		for(GameObject go : gos){
			out+="|OBJ "+go.toString();
		}
		return out;
	}
	public void sendToAll(String in){
		for(Player p1:team1){
			if(p1.cansend())
				ServerUDP.c.send(p1.getSend(in));
		}
		for(Player p2:team2){
			if(p2.cansend())
				ServerUDP.c.send(p2.getSend(in));
		}
	}
}
