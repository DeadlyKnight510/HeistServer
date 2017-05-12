
public class Game {
	public Player p1,p2;
	public int p1X=0,p2X=500,p1Y=0,p2Y=500;
	public int hlth1=100,hlth2=100;
	public Game(Player player1, Player player2){
		p1 = player1;
		p2 = player2;
		p1.setGame(this);
		p2.setGame(this);
	}
	public int getP1X(){ return p1X; }
	public int getP2X(){ return p2X; }
	public int getP2Y(){ return p2Y; }
	public int getP1Y(){ return p1Y; }
	public void setP1X(int x){ p1X = x; }
	public void setP2X(int x){ p2X = x; }
	public void setP1Y(int x){ p1Y = x; }
	public void setP2Y(int x){ p2Y = x; }
	public void setXY(Player p, int x, int y)
	{
		if(getPlayerNum(p)==1){
			setP1X(x); setP1Y(y);
		}
		else {	
			setP2X(x); setP2Y(y);
		}
	}
	public int getPlayerNum(Player p){
		if(p.equals(p1))
			return 1;
		else
			return 2;
	}
	public synchronized String getLayout(Player p){
		String out = "";
		if(getPlayerNum(p)==1){
			out+="XY 0 "+p1X+" "+p1Y;
			out+="|XY 1 "+p2X+" "+p2Y;
		} else {
			out+="XY 0 "+p2X+" "+p2Y;
			out+="|XY 1 "+p1X+" "+p1Y;
		}
		return out;
	}
	public synchronized String toString(Player p)
	{
		//only gives value of other player
		String out="";
		if(getPlayerNum(p)==1){
			out+="XY "+p2X+" "+p2Y+"|";
			out+="HLTH "+hlth2;
		} else {
			out+="XY "+p1X+" "+p1Y+"|";
			out+="HLTH "+hlth1;
		}
		return out;
	}
}
