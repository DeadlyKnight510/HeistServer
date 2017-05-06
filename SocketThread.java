import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketThread extends Thread {
    protected Socket socket;	
    public int id;
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
            String line = brinp.readLine();
            while(!line.substring(0, 2).equals("id"))
            {
            	line = brinp.readLine();
            }
            id = Integer.parseInt(line.substring(2, 10));
            Server.online.add(id);
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    Server.online.remove(id);
                    return;
                } else {
                	
                	if(line.toLowerCase().trim().equals("PLAY")){
                		Server.playersearch.add(id);
                	}
                	
                	String cmd =line.toLowerCase().substring(0,4);
                	switch(cmd)
                	{
                	case "XY  ":
                		//each num is 5 numbers long - use 0 as placeholder when smaller
                		//IE: "XY  00015 01352"
                		int x = Integer.parseInt(line.substring(4,9));
                		int y = Integer.parseInt(line.substring(10,15));
                		break;
                	case "HLTH":
                		//IE: "HLTH700"
                		int h = Integer.parseInt(line.substring(4));
                		break;
                	case "BULT":
                		//IE: "BULT id1 00015 01352"
                		int id = line.indexOf("id");
                		int space = line.indexOf(" ",id);
                		int bltid = Integer.parseInt(line.substring(id+2,space));
                		String p = line.substring(space).trim();
                		int space2 = p.indexOf(" ");
                		int x1 = Integer.parseInt(p.substring(0,space2));
                		
                		break;
                	}
                    out.writeBytes(line + "\n\r");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

    }
}