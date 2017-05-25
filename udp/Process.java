import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.DatagramSocket;;
public class Process {
	public static void process(DatagramPacket dp)
	{
		String line = new String(dp.getData());
		//ID 4|XY 500 100|HLTH 100|OBJ 1 15 1352
		//  --> GET
		//GTID
		//	--> 5
		if(line==null||line.equals("")){
//			Communicate.send(server,"",dp.getAddress(),dp.getPort());
		} else if(line.trim().equals("GTID")){
			String out = "GTID "+Integer.toString(ServerUDP.newID());
			ServerUDP.c.send(out,dp.getAddress(),dp.getPort());
		} else {
			String[] temp = line.split("\\|");
			int id;
			if(temp[0].contains("ID"))
			{
				String[] parts = temp[0].split(" ");
				id = Integer.parseInt(parts[1]);
				for(int c=1;c<parts.length;c++)
				{
					String[] temp2 = temp[c].split(" ");
					String resp = actions(temp2,id,dp.getAddress(),dp.getPort());
					if(resp!=null){
						ServerUDP.c.send(resp,dp.getAddress(),dp.getPort());
					}
				}
			}
		}
	}
	public static String actions(String[] parts, int id,InetAddress ad, int port){
		Player p = ServerUDP.m.getPlayer(id);
		if(parts[0].equals("XY")){
			// "XY 400 100
			int x=Integer.parseInt(parts[1].trim());
			int y=Integer.parseInt(parts[2].trim());
			double a = Double.parseDouble(parts[3].trim());
			p.getGame().setXYA(p,x,y,a);
		} else if(parts[0].equals("HLTH")){
			// "HLTH 300"
			int health=Integer.parseInt(parts[1].trim());
		}else if(parts[0].trim().equals("LOGIN")){
			// "LOGIN suryar --> ID 6|LOGIN suryar"
//			ServerUDP.m.online.add(new Player(id,parts[1],ad,port));
			ServerUDP.m.playerLogOn(id,parts[1],ad,port);
			System.out.println("New Player added: "+parts[1]+" id: "+id);
		} else if(parts[0].trim().equals("PLAY")){
			// "LOGIN suryar --> ID 6|PLAY"
			ServerUDP.m.playerPlay(id);
//			ServerUDP.m.playersearch.add(ServerUDP.m.getPlayer(id));
		} else if(parts[0].trim().equals("END")){
			// "END --> ID 4|END"
//			System.out.println(ServerUDP.m.getPlayer(id).username+" has logged out");
			ServerUDP.m.playerLogOff(id);
		} else if(parts[0].trim().equals("CANCEL")){
			ServerUDP.m.removePlayerFromSearch(id);
		}else if(parts[0].trim().equals("KILL")){
			System.out.println("killed");
			p.getGame().kill(p);
			ServerUDP.m.endGame(p);
		} else if(parts[0].equals("OBJ")){
			// "BULT 1 15 1352"
			// if x or y is negative, delete object
			int idObj = Integer.parseInt(parts[1].trim());
			int x1 = Integer.parseInt(parts[2].trim());
			int y1 = Integer.parseInt(parts[3].trim());
			double a1 = Double.parseDouble(parts[4].trim());
			if(p.getGame()!=null)
				p.getGame().addGO(p,idObj,x1,y1,a1); 
		}
		return null;
	}
}
