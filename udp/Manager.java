import java.util.ArrayList;
import java.net.InetAddress;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
public class Manager extends Thread{
    public static List<Player> online;
    public static List<Player> onlineNotInGame;
    public static List<Game> ongoingGames;
	public double currTime=0.0;
    public Manager(){
		currTime = System.currentTimeMillis();
        online = Collections.synchronizedList(new ArrayList<Player>());
        ongoingGames = Collections.synchronizedList(new ArrayList<Game>());
		onlineNotInGame = Collections.synchronizedList(new ArrayList<Player>());

    }
    public void run(){
        while(true){
			synchronized(ongoingGames) {
       			Iterator<Game> iterator = ongoingGames.iterator(); 
				ArrayList<Integer> nums = new ArrayList<Integer>();
				while (iterator.hasNext()){
					Game g = iterator.next();
					if(g.gameOver){
						//g.update();	
						System.out.println("g is over");
						nums.add(g.gameid);
					}
					else
						g.update();
				}
				for(Integer i : nums)
					removeGame(i.intValue());
			}
			synchronized(onlineNotInGame){
				if(System.currentTimeMillis()-currTime>1000){
					Iterator<Player> iterator = onlineNotInGame.iterator(); 
					while (iterator.hasNext()){
						ServerUDP.c.send(iterator.next().getSend(""));
					}
					currTime = System.currentTimeMillis();
				}
			}
        }
    }
	public void startGame(int id){
		Game g = getGame(id);
		if(g==null){ 
			System.out.println("null");
			return;
		}
		g.startGame();
	}
	public Game createGame(int id, String in){
		Game g = new Game(in);
		removePlayerFromGame(getPlayer(id));
		g.addPlayer(getPlayer(id));
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
		synchronized(online){
			Iterator<Player> iterator = online.iterator(); 
			while (iterator.hasNext()){
				try{
					ServerUDP.c.send(iterator.next().getSend(in));
				}catch(Exception e){
					return false;
				}
			}
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
    public boolean endGame(Player p){
        Game g = containsPlayer(p);
        if(g==null)
            return false;
        else{
            g.end();
            ongoingGames.remove(g);
			deletePlayerNotInGame(p.id);
            return true;
        }
    }
    public boolean removePlayerFromGame(Player p){
        Game g = containsPlayer(p);
        if(g==null)
            return false;
        else{
			System.out.println("going in removed");
            g.removePlayer(p);
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
			deletePlayerNotInGame(id);
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
    public boolean deletePlayerNotInGame(int id){
        for(int x=0;x<onlineNotInGame.size();x++){
            if(onlineNotInGame.get(x).id==id){
                onlineNotInGame.remove(x);
                return true;
            }
        }
        return false;
    }
	public void playerPlay(int id,int gid){
		if(getGame(gid)==null)
			return;
		if(getGame(gid).containsPlayer(getPlayer(id)))
			return;
		getGame(gid).addPlayer(getPlayer(id));
		System.out.println("added");
		deletePlayerNotInGame(id);
		ServerUDP.c.send(getPlayer(id).getSend(getPlayersFromGame(gid)));
	}
	public void playerCancel(int id,int gid){
		if(getGame(gid)==null)
			return;
		getGame(gid).removePlayer(getPlayer(id));
		ServerUDP.c.send(getPlayer(id).getSend(getPlayersFromGame(gid)));
		onlineNotInGame.add(getPlayer(id));
	}
	public boolean playerLogOn(int id, String name, InetAddress ad, int port){
        if(id<0 || name==null || ad==null || port<0)
            return false;
		online.add(new Player(id,name,ad,port));
		onlineNotInGame.add(getPlayer(id));
		updateGames();
        return true;
	}
}
