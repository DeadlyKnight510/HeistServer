import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
public class Manager extends Thread{
    public static List<Player> online;
    public static List<Game> ongoingGames;
    public Manager(){
        online = Collections.synchronizedList(new ArrayList<Player>());
        ongoingGames = Collections.synchronizedList(new ArrayList<Game>());
    }
    public void run(){
        while(true){
			synchronized(ongoingGames) {
       			Iterator<Game> iterator = ongoingGames.iterator(); 
				while (iterator.hasNext()){
					Game g = iterator.next();
					if(g.gameOver){
						g.update();	
						removeGame(g.gameid);
					}
				}
			}
        }
    }
	public void startGame(int id){
		Game g = getGame(id);
		if(g==null) return;
		g.start();
		g.gameStart=true;
	}
	public Game createGame(String in){
		Game g = new Game(in);
		ongoingGames.add(g);
		updateGames();
		return g;
	}
	public void removeGame(int gid){
		synchronized(ongoingGames){
			for(int x=0;x<ongoingGames.size();x++){
				if(ongoingGames.get(x).gameid==gid){
					ongoingGames.remove(x);
					updateGames();
					return;
				}
			}
		}
		updateGames();
	}
	public String toString(){
		String out = "ONLINE|"+online.size();
		synchronized(ongoingGames){
			Iterator<Game> iterator = ongoingGames.iterator(); 
			while (iterator.hasNext()){
				Game temp = iterator.next();
				int t = (temp.gameStart)?1:0;
				out+="|"+temp.name+","+temp.gameid+","+t;	
			}
		}
		return out;
	}
    public int getNumOnline(){
        return online.size();
    }
	public boolean updateGames(){
		try{
			for(Player p : online){
				ServerUDP.c.send(p.getSend(toString()));	
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
    public Game containsPlayer(int i){
		Player p = getPlayer(i);
        for(Game g : ongoingGames){
            if(g.containsPlayer(p)){
                return g;
            }
        }
        return null;
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
    public Player getPlayer(int i){
        for(Player g:online){
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
	public void playerLogOff(int id){
		if(getPlayer(id)!=null){
            removePlayerFromGame(getPlayer(id));
            deletePlayerOnline(id);
			updateGames();
            return;
		}
        System.out.println("ID "+id+" is not a player");
	}
	public String getPlayersFromGame(int gid){
		if(getGame(gid)==null)
			return null;
		return getGame(gid).getPlayers();
	}
	public Game getGame(int gid){
		for(Game g:ongoingGames){
			if(g.gameid==gid)
				return g;
		}
		return null;
	}
	public void playerPlay(int id,int gid){
		if(getGame(gid)==null)
			return;
		getGame(gid).addPlayer(getPlayer(id));
		System.out.println("added");
		ServerUDP.c.send(getPlayer(id).getSend(getPlayersFromGame(gid)));
	}
	public void playerCancel(int id,int gid){
		if(getGame(gid)==null)
			return;
		getGame(gid).removePlayer(getPlayer(id));
		ServerUDP.c.send(getPlayer(id).getSend(getPlayersFromGame(gid)));
	}
	public boolean playerLogOn(int id, String name, InetAddress ad, int port){
        if(id<0 || name==null || ad==null || port<0)
            return false;
		online.add(new Player(id,name,ad,port));
		updateGames();
        return true;
	}
}
