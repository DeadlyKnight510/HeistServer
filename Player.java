public class Player{
	public static int numPlayers=0;
	public int id;
	public String username;
	public Game current=null;
	public Player(int id,String user){
		this.id = id;
		username = user;
		numPlayers++;
	}
	public void setGame(Game g){
		current = g;
	}
	public Game getGame(){
		return current;
	}
	public boolean equals(Player p) {
		if(p.id==this.id){
			return true;
		}
		else{
			return false;
		}
	}
	public static void addPlayer(){
		numPlayers++;
	}
}
