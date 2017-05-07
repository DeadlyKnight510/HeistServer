import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketThread extends Thread {
    protected Socket socket;	
    public int id=-1;
	Player p=null;
	public boolean currGame=false;
	public SocketThread(Socket clientSocket) {
        this.socket = clientSocket;
    }
    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
					System.out.println(id+" has disconnected");
//                    Server.online.remove(id);
                    return;
                } 
				else {
/*					if(p!=null){
						System.out.println("p is not null");
						if(p.getGame()==null)
							System.out.println("p game is also null");
					}
					else
						System.out.println("p is null");
*/					
					if(line.equals("GET")){
						if(currGame==false && p!=null && p.getGame()!=null)
						{
							currGame=true;
							out.writeBytes("START|"+p.getGame().getLayout(p)+"\n");
						}
						if(p!=null && p.getGame()!=null){
							out.writeBytes("UPD|"+p.getGame().toString(p)+"\n");
							out.flush();
							System.out.println("UPD|"+p.getGame().toString(p)+"\n");
							continue;
						} 
						else
						{
							out.writeBytes("OK\n");
							out.flush();
							continue;
						}
					}
					else if(line.trim().equals("PLAY")){
						if(p!=null){
							Manager.playersearch.add(p);
						}
					}
					else{
						String[] temp = line.split("\\|");
						for(String m: temp)
						{
							String[] parts = m.split(" ");
							if(parts[0].equals("GTID")){
								System.out.println("num id"+Player.numPlayers);
								out.writeBytes(Integer.toString(Player.numPlayers)+"\n");
								out.flush();
								Player.addPlayer();
							}
							else if(parts[0].equals("ID")){
								// "ID 7 billy"
								id = Integer.parseInt(parts[1].trim());
								System.out.println("Client "+id+" connected");
								p=new Player(id,parts[2]);
								Manager.online.add(p);
								break;
							}
							else if(parts[0].equals("XY"))
							{
								// "XY 400 100
								int x=Integer.parseInt(parts[1].trim());
								int y=Integer.parseInt(parts[2].trim());
								p.getGame().setXY(p,x,y);
							}
							else if(parts[0].equals("HLTH"))
							{
								// "HLTH 300"
								int health=Integer.parseInt(parts[1].trim());
							}
							else if(parts[0].equals("BULT"))
							{
								// "BULT 1 15 1352"
								int id = Integer.parseInt(parts[1].trim());
								int x1 = Integer.parseInt(parts[2].trim());
								int y1 = Integer.parseInt(parts[3].trim());
							}
						}
						System.out.println(id+": "+line);
        	            out.writeBytes("OK\n");
                	    out.flush();
                	}
				}
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

    }
}
