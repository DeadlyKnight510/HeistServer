public class UpdateGame extends Thread {
	Player p;
    DataOutputStream out;
	public UpdateGame(DataOutputStream dos, Player p){
		this.p = p;
		DataOutputStream out = null;
	}
	public void run(){
		while(true){
			if(p.getGame!=null){
				out.writeBytes("UPD|"+p.getGame().toString()+"\n");
				out.flush();
				System.out.println("UPD|"+p.getGame().toString()+"\n");
				Thread.sleep(1000);
			}
			else
				return
		}
	}
}
