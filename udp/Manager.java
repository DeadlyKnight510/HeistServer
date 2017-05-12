import java.util.ArrayList;
public class Manager extends Thread{
    public static ArrayList<Player> online;
    public static ArrayList<Player> playersearch;
    public static ArrayList<Game> ongoingGames;
	public Manager(){
		online = new ArrayList<Player>();
		playersearch = new ArrayList<Player>();
		ongoingGames = new ArrayList<Game>();
	}
	public void run(){
		while(true){
            if(getNumSearching()>=2)
            {
            	Player first = getPlayer();
            	Player second = getPlayer(first);
            	ongoingGames.add(new Game(first,second));
            	playersearch.remove(first);
            	playersearch.remove(second);
				System.out.println("New Game Started Between "+first.username+" and "+second.username);
            }
		}
	}
    public static int getNumOnline(){
    	return online.size();
    }
    public static int getNumSearching(){
    	return playersearch.size();
    }
    public static Player getPlayer(Player not){
    	for(Player g:playersearch){
    		if(!g.equals(not))
    			return g;
    	}
    	return null;
    }
    public static Player getPlayer(){
    	int r = (int)Math.random()*getNumSearching();
    	return playersearch.get(r);
    }
}
