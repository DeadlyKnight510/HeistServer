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
    public static Game containsPlayer(Player p){
        for(Game g : ongoingGames){
            if(g.containsPlayer(p)){
                return g;
            }
        }
        return null;
    }
    public static boolean removePlayerFromGame(Player p){
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
    public static Player getPlayer(Player not){
        for(Player g:playersearch){
            if(!g.equals(not))
                return g;
        }
        return null;
    }
    //Get player at ID
    public static Player getPlayer(int i){
        for(Player g:playersearch){
            if(g.id==i)
                return g;
        }
        return null;
    }
    public static Player getPlayer(){
        int r = (int)Math.random()*getNumSearching();
        return playersearch.get(r);
    }
}
