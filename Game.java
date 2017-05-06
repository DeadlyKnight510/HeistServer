
public class Game {
	public int p1,p2;
	public int p1X,p2X,p1Y,p2Y;
	public Game(int player1, int player2){
		p1 = player1;
		p2 = player2;
	}
	public int getP1X(){
		return p1X;
	}
	public int getP2X(){
		return p2X;
	}
	public int getP2Y(){
		return p2Y;
	}
	public int getP1Y(){
		return p1Y;
	}
	public void setP1X(int x){
		p1X = x;
	}
	public void setP2X(int x){
		p2X = x;
	}
	public void setP1Y(int x){
		p1Y = x;
	}
	public void setP2Y(int x){
		p2Y = x;
	}
}
