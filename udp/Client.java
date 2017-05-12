import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class Client {

public static void main(String args[]) throws IOException{

	InetAddress address = InetAddress.getByName("heistserver.ddns.net");
	URL whatismyip = new URL("http://checkip.amazonaws.com");
	BufferedReader in = new BufferedReader(new InputStreamReader(
	                whatismyip.openStream()));

	String ip = in.readLine(); //you get the IP as a String
    Socket s1=null;
    String line=null;
    BufferedReader br=null;
    BufferedReader is=null;
    PrintWriter os=null;

    try {
   // 	if(address.getHostAddress().equals(ip)){
    		s1 = new Socket("192.168.1.87",8080);
   // 	}
   // 	else
    //		s1 = new Socket(address,5001); // You can use static final constant PORT_NUM
        br= new BufferedReader(new InputStreamReader(System.in));
        is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
        os= new PrintWriter(s1.getOutputStream());
    }
    catch (IOException e){
        e.printStackTrace();
        System.err.print("IO Exception");
    }

    System.out.println("Client Address : "+address);
    System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

    String response=null;
    try{
        line=br.readLine(); 
        while(line.compareTo("QUIT")!=0){
                os.println(line);
                os.flush();
                response=is.readLine();
                System.out.println("Server Response : "+response);
                line=br.readLine();

            }



    }
    catch(IOException e){
        e.printStackTrace();
    System.out.println("Socket read Error");
    }
    finally{

        is.close();os.close();br.close();s1.close();
                System.out.println("Connection Closed");

    }

}
private static Socket createClientSocket(InetAddress clientName, int port){

    boolean scanning = true;
    Socket socket = null;
    int numberOfTry = 0;

    while (scanning && numberOfTry < 10){
        numberOfTry++;
        try {
            socket = new Socket(clientName, port);
            scanning = false;
        } catch (IOException e) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

    }
    return socket;
}
}

