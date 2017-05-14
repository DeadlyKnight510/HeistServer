import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws IOException{
        int PORT = 8080;
        BufferedReader inFromUser =  new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = null;
        try{
            clientSocket = new DatagramSocket();
            System.out.println("connection worked");
        }catch(Exception e){
            System.out.println("connection failed");
            return;
        }
        InetAddress address = InetAddress.getByName("heistserver.ddns.net");
//      InetAddress address = InetAddress.getByName("192.168.1.87");
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));
        String ip = in.readLine(); //you get the IP as a String
        //      InetAddress IPAddress = InetAddress.getByName("localhost");
        if(address.getHostAddress().equals(ip)){
            System.out.println("Connecting to Local IP");
            PORT = 8080;
            address = InetAddress.getByName("192.168.1.87");
        }
        else{
            System.out.println("Connecting to Public IP");
            PORT = 5000;
        }
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        while(true){
            Arrays.fill( sendData, (byte)0 );

            String sentence = inFromUser.readLine();
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, PORT);
            try{
                clientSocket.send(sendPacket);
            }catch(Exception e){System.out.println("no");}
            
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);
        }
//      clientSocket.close();
    }
}
