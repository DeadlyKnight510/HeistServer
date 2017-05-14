public class Player{
	public int id;
	public String username;
	public Game current=null;
	public Player(int id,String user){
		this.id = id;
		username = user;
	}
	public Player(int id){
		this.id = id;
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
}
