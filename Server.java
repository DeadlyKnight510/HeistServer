import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static final int PORT = 4445;

    public static ArrayList<Integer> online;
    public static ArrayList<Integer> playersearch;
    public static ArrayList<Game> ongoingGames;
    public static void main(String args[]) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new SocketThread(socket).start();
            if(getNumSearching()>2)
            {
            	int first = getId();
            	int second = getId(first);
            	ongoingGames.add(new Game(first,second));
            	playersearch.remove(first);
            	playersearch.remove(second);
            }
        }
    }
    public static int getNumOnline(){
    	return online.size();
    }
    public static int getNumSearching(){
    	return playersearch.size();
    }
    public static int getId(int not){
    	for(Integer g:playersearch){
    		if(!g.equals(not))
    			return g.intValue();
    	}
    	return -1;
    }
    public static int getId(){
    	int r = (int)Math.random()*getNumSearching();
    	return playersearch.get(r);
    }
}