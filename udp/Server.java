import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static final int PORT = 8080;
	public static Manager mgr;
    public static void main(String args[]) {
        mgr = new Manager();
		mgr.start();
		ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(PORT);
			System.out.println("created server");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                socket = serverSocket.accept();
				socket.setTcpNoDelay(true);
//				System.out.println("new client");
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new SocketThread(socket).start();
        }
    }
}
