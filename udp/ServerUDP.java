import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
public class ServerUDP {

    static final int PORT = 8080;
	
    public static void main(String args[]) {
		DatagramSocket server = null;
			
		try{
			server = new DatagramSocket(PORT);
			System.out.println("server created");
		}catch(Exception e){
			System.out.println("server creation failed");
			return;
		}
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while(true){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try{
				server.receive(receivePacket);
			}catch(Exception e){continue;}
			String temp = new String(receivePacket.getData());
			System.out.println(temp);
			String out = "OK";
			InetAddress ipadr = receivePacket.getAddress();
			int port = receivePacket.getPort();
			sendData = out.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipadr, port);
			try{
				server.send(sendPacket);
			} catch (Exception e) { continue; };
		}

    }
}
