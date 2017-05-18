import java.net.DatagramPacket;

public class Process {
	public static void process(DatagramSocket server, DatagramPacket dp)
	{
		String line = new String(dp.getData());
		//ID 4|XY 500 100|HLTH 100|OBJ 1 15 1352
		//  --> GET
		//GTID
		//	--> 5
		if(line==null||line.equals("")){
//			Communicate.send(server,"",dp.getAddress(),dp.getPort());
		} else if(line.trim().equals("GTID")){
			String out = Integer.toString(ServerUDP.newID());
			Communicate.send(server,out,dp.getAddress(),dp.getPort());
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
					actions(temp2,id);
				}
			}
		}
	}
	public static String actions(String[] parts, int id){
		Player p = Manager.getPlayer(id);
		if(parts[0].equals("XY")){
			// "XY 400 100
			int x=Integer.parseInt(parts[1].trim());
			int y=Integer.parseInt(parts[2].trim());
			p.getGame().setXY(p,x,y);
		} else if(parts[0].equals("HLTH")){
			// "HLTH 300"
			int health=Integer.parseInt(parts[1].trim());
		}else if(parts[0].equals("LOGIN")){
			// "LOGIN suryar --> ID 6|LOGIN suryar"
			Manager.online.add(new Player(id,parts[1]));
		} else if(parts[0].equals("PLAY")){
			// "LOGIN suryar --> ID 6|LOGIN suryar|PLAY"
			Manager.playersearch.add(Manager.getPlayer(id));
		} else if(parts[0].equals("END")){
			// "END --> ID 4|END"
			if(Manager.playersearch.contains(new Player(id))){
				Manager.playersearch.remove(new Player(id));
			}
			Manager.removePlayerFromGame(Manager.getPlayer(id));
			Manager.online.remove(new Player(id));

		} else if(parts[0].equals("OBJ")){
			// "BULT 1 15 1352"
			// if x or y is negative, delete object
			int idObj = Integer.parseInt(parts[1].trim());
			int x1 = Integer.parseInt(parts[2].trim());
			int y1 = Integer.parseInt(parts[3].trim());
		}
		return "";
	}
}
