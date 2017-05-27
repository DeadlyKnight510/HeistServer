public class GameObject{
	int pid,obj,x,y;
	double a;
	public GameObject(int playerid, int objid, int x, int y, double a){
		this.pid = playerid;
		this.obj = objid;
		this.x = x;
		this.y = y;
		this.a = a;
	}
	@Override
	public String toString(){
		return ""+pid+" "+obj+" "+x+" "+y+" "+a;
	}
}
