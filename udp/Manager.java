import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
public class Manager extends Thread{
    public static List<Player> online;
    public static List<Player> playersearch;
    public static List<Game> ongoingGames;
    public Manager(){
        online = Collections.synchronizedList(new ArrayList<Player>());
        playersearch = Collections.synchronizedList(new ArrayList<Player>());
        ongoingGames = Collections.synchronizedList(new ArrayList<Game>());
    }
    public void run(){
        while(true){
            if(getNumSearching()>=2)
            {
                Player first = getPlayer();
                Player second = getPlayerNot(first);
				Game tempGame = new Game(first,second);
				tempGame.start();
                ongoingGames.add(tempGame);
                playersearch.remove(first);
                playersearch.remove(second);
                System.out.println("New Game Started Between "+first.username+" and "+second.username);
            }
			synchronized(ongoingGames) {
       			Iterator<Game> iterator = ongoingGames.iterator(); 
				while (iterator.hasNext()){
					iterator.next().update();	
				}
			}
//			for(Game g: ongoingGames){
//				g.update();	
//			}
        }
    }
	public String toString(Player p){
		String out = "ONLINEPLAYERS";
		synchronized(online) {
			Iterator<Player> iterator = online.iterator(); 
			while (iterator.hasNext()){
				Player temp = iterator.next();
				if(temp.id!=p.id)
					out+="|"+temp.username;	
			}
		}
		return out;
	}
    public int getNumOnline(){
        return online.size();
    }
    public int getNumSearching(){
        return playersearch.size();
    }
	public boolean updatePlayers(){
		try{
			for(Player p : online){
				ServerUDP.c.send(p.getSend(toString(p)));	
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}
	public boolean sendToAll(String in){
		try{
			for(Player p : online){
				ServerUDP.c.send(p.getSend(in));	
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}
    public Game containsPlayer(Player p){
        for(Game g : ongoingGames){
            if(g.containsPlayer(p)){
                return g;
            }
        }
        return null;
    }
    public boolean removePlayerFromGame(Player p){
        Game g = containsPlayer(p);
        if(g==null)
            return false;
        else{
            g.end();
            ongoingGames.remove(g);
            return true;
        }
    }
    public boolean endGame(Player p){
        Game g = containsPlayer(p);
        if(g==null)
            return false;
        else{
            g.end();
            ongoingGames.remove(g);
            return true;
        }
    }
    //Get player that is not the one inputted
    public Player getPlayerNot(Player not){
        for(Player g:playersearch){
            if(!g.equals(not))
                return g;
        }
        return null;
    }
    public Player getPlayer(int i){
        for(Player g:online){
            if(g.id==i)
                return g;
        }
        return null;
    }
    //Get player at ID
    public Player getPlayerSearching(int i){
        for(Player g:playersearch){
            if(g.id==i)
                return g;
        }
        return null;
    }
    public boolean deletePlayerOnline(int id){
        for(int x=0;x<online.size();x++){
            if(online.get(x).id==id){
                online.remove(x);
                return true;
            }
        }
        return false;
    }
    public boolean deletePlayerSeach(int id){
        for(int x=0;x<playersearch.size();x++){
            if(playersearch.get(x).id==id){
                playersearch.remove(x);
                return true;
            }
        }
        return false;
    }
    public Player getPlayer(){
        int r = (int)Math.random()*getNumSearching();
        return playersearch.get(r);
    }
	public void playerLogOff(int id){
		if(getPlayer(id)!=null){
			deletePlayerSeach(id);
            removePlayerFromGame(getPlayer(id));
            deletePlayerOnline(id);
			updatePlayers();
            return;
		}
        System.out.println("ID "+id+" is not a player");
	}
	public void playerPlay(int id){
		if(getPlayerSearching(id)==null)
			playersearch.add(getPlayer(id));
	}
	public boolean playerLogOn(int id, String name, InetAddress ad, int port){
        if(id<0 || name==null || ad==null || port<0)
            return false;
		online.add(new Player(id,name,ad,port));
		updatePlayers();
        return true;
	}
}
