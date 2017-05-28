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
					actions(temp[c],id,dp.getAddress(),dp.getPort());
				}
			}
		}
	}
	public static void actions(String temp, int id,InetAddress ad, int port){
		String[] parts = temp.split(" ");
		Player p = ServerUDP.m.getPlayer(id);
		if(parts[0].equals("XY")){
			// "XY 400 100
			int x=Integer.parseInt(parts[1].trim());
			int y=Integer.parseInt(parts[2].trim());
			double a = Double.parseDouble(parts[3].trim());
			int h=Integer.parseInt(parts[4].trim());
			p.setXYAH(x,y,a,h);
		} else if(parts[0].trim().equals("ADD")){
			String[] temp2 = temp.split(",");
			// "HLTH 300"
			Game g = ServerUDP.m.createGame(temp2[1].trim());
			ServerUDP.c.send(p.getSend("NEWGAMEID|"+g.gameid));
		} else if(parts[0].trim().equals("REMOVE")){
			// "HLTH 300"
			ServerUDP.m.removeGame(Integer.parseInt(parts[1].trim()));
		} else if(parts[0].trim().equals("GTPERS")){
			// "HLTH 300"
			String send = ServerUDP.m.getPlayersFromGame(Integer.parseInt(parts[1].trim()));
			if(send!=null)
				ServerUDP.c.send(p.getSend(send));
		} else if(parts[0].trim().equals("STARTGM")){
			// "HLTH 300"
			ServerUDP.m.startGame(Integer.parseInt(parts[1].trim()));
		} else if(parts[0].trim().equals("HLTH")){
			// "HLTH 300"
			int health=Integer.parseInt(parts[1].trim());
		}else if(parts[0].trim().equals("HIT")){
			// "HLTH 300"
			int getid=Integer.parseInt(parts[1].trim());
			ServerUDP.m.getPlayer(getid).decreaseHLT();
		}else if(parts[0].trim().equals("LOGIN")){
			// "LOGIN suryar --> ID 6|LOGIN suryar"
//			ServerUDP.m.online.add(new Player(id,parts[1],ad,port));
			ServerUDP.m.playerLogOn(id,parts[1],ad,port);
			System.out.println("New Player added: "+parts[1]+" id: "+id);
		} else if(parts[0].trim().contains("PLAY")){
			String[] temp2 = temp.split(",");
			// "ID 6|PLAY,[game name],[game id]"
			// "ID 6|PLAY,bobs burgers,7"
			ServerUDP.m.playerPlay(id,Integer.parseInt(temp2[2].trim()));
		} else if(parts[0].trim().equals("END")){
			// "END --> ID 4|END"
			ServerUDP.m.playerLogOff(id);
		} else if(parts[0].trim().contains("CANCEL")){
			String[] temp2 = temp.split(",");
			ServerUDP.m.playerCancel(id,Integer.parseInt(temp2[2].trim()));
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
	}
}
