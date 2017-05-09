import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.Timer;

public class SocketThread extends Thread {
	protected Socket socket;	
	public int id=-1;
	Player p=null;
	public boolean currGame=false;
	InputStream inp = null;
	BufferedReader brinp = null;
	DataOutputStream out = null;
	public SocketThread(Socket clientSocket) {
		this.socket = clientSocket;
	}
	public void update(){
		try{
			if(p!=null && p.getGame()!=null){
				send("UPD|"+p.getGame().toString(p));
//				System.out.println("UPD|"+p.getGame().toString(p)+"\n");
			} 
		}catch(Exception e){}
	}
	public void quit(){
		try{
			socket.close();
			System.out.println(id+" has disconnected");
			if(p!=null){
				Server.mgr.online.remove(p);
				Server.mgr.playersearch.remove(p);
			}
		}catch(Exception e){}
	}
	@Override
	public void run() {
		try {
			inp = socket.getInputStream();
			brinp = new BufferedReader(new InputStreamReader(inp));
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			return;
		}
		String line;
		while (true) {
	

				if(!currGame && p!=null && p.getGame()!=null){
					currGame = true;
					send("START|"+p.getGame().getLayout(p));
				} else if(currGame && p!=null && p.getGame() == null) {
					currGame = false;
					send("QUIT");
				}
				else if(currGame){
					update();
				}


			line = sendWResp("GET");
			if(line==null||line.equals("")){
				continue;
			}
			else if ((line == null) || line.equalsIgnoreCase("END")) {
				quit();
				return;
			} 
			else {
				if(line.trim().equals("PLAY")){
					if(p!=null)
						Manager.playersearch.add(p);
				}
				else{
					String[] temp = line.split("\\|");
					for(String m: temp)
					{
						String[] parts = m.split(" ");
						actions(parts);
					}
//					System.out.println(id+": "+line);
				}
			}
		}
	}
	public void actions(String[] parts){
		if(parts[0].equals("GTID")){
			System.out.println("num id"+Player.numPlayers);
			send(Integer.toString(Player.numPlayers));
			Player.addPlayer();
		} else if(parts[0].equals("ID")){
			// "ID 7 billy"
			id = Integer.parseInt(parts[1].trim());
			System.out.println("Client "+id+" connected");
			p=new Player(id,parts[2]);
			Manager.online.add(p);
		} else if(parts[0].equals("XY")){
			// "XY 400 100
			int x=Integer.parseInt(parts[1].trim());
			int y=Integer.parseInt(parts[2].trim());
			p.getGame().setXY(p,x,y);
		} else if(parts[0].equals("HLTH")){
			// "HLTH 300"
			int health=Integer.parseInt(parts[1].trim());
		} else if(parts[0].equals("BULT")){
			// "BULT 1 15 1352"
			// if x or y is negative, delete object
			int id = Integer.parseInt(parts[1].trim());
			int x1 = Integer.parseInt(parts[2].trim());
			int y1 = Integer.parseInt(parts[3].trim());
		}
	}
	public void send(String in){
		try{
			out.writeBytes(in+"\n");
			System.out.println(id+" sent:"+in);
			out.flush();
		}catch (Exception e){}
	}
	public String sendWResp(String in){
		try{
			out.writeBytes(in+"\n");
			out.flush();
			String rec = brinp.readLine();
			System.out.println(id+" sent:"+in+"\n"+id+" rec:"+rec);
			return rec;
		}catch (Exception e){}
		return null;
	}
}
