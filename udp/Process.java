import java.net.DatagramPacket;

public class Process {
	public static int numClient=0;
	public static String process(DatagramPacket dp)
	{
		String line = new String(dp.getData());
		//ID 4|XY 500 100|HLTH 100|OBJ 1 15 1352
		//GTID
		//	--> 5
		if(line.toLowerCase().trim().equals("GTID")){
			return Integer.toString(numClient);
		}
		if(line==null||line.equals("")){
			return "";
		}
		else if ((line == null) || line.equalsIgnoreCase("END")) {
			return "";
		} 
		else {
			String[] temp = line.split("\\|");
			int id;
			if(temp[0].contains("ID"))
			{
				String[] parts = temp[0].split(" ");
				id = Integer.parseInt(parts[1]);
				Player p=new Player(id,parts[2]);
				Manager.online.add(p);
				for(int c=1;c<parts.length;c++)
				{
					String[] temp2 = temp[c].split(" ");
					actions(temp2,id);
				}
			}
		}
		return "";
	}
	public static void actions(String[] parts, int id){
		Player p = Manager.getPlayer(id);
		if(parts[0].equals("XY")){
			// "XY 400 100
			int x=Integer.parseInt(parts[1].trim());
			int y=Integer.parseInt(parts[2].trim());
			p.getGame().setXY(p,x,y);
		} else if(parts[0].equals("HLTH")){
			// "HLTH 300"
			int health=Integer.parseInt(parts[1].trim());
		} else if(parts[0].equals("OBJ")){
			// "BULT 1 15 1352"
			// if x or y is negative, delete object
			int idObj = Integer.parseInt(parts[1].trim());
			int x1 = Integer.parseInt(parts[2].trim());
			int y1 = Integer.parseInt(parts[3].trim());
		}
	}
}