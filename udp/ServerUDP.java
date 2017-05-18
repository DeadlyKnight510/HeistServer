import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

public class ServerUDP {
	static Properties prop;
	static final int PORT = 8080;
	public static int clientNum=0;

	public static void main(String args[]) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run(){
				try{
						OutputStream output = new FileOutputStream("config.properties");
						prop.store(output, null);
				}catch (Exception e){}
			}
		}, "Shutdown-thread"));
		
		if(!loadProps()){
			System.out.println("failed to load properties");
			try{
				OutputStream output = new FileOutputStream("config.properties");
				prop.setProperty("id", "0");
				prop.store(output, null);
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}catch(Exception e){}
		}
		DatagramSocket server = null;
		try{
			server = new DatagramSocket(PORT);
			System.out.println("server created");
		}catch(Exception e){
			System.out.println("server creation failed");
			return;
		}
		while(true){
			DatagramPacket receivePacket = Communicate.receive(server);
			String temp = new String(receivePacket.getData());
			System.out.println(temp);
			Process.process(server, receivePacket);
//			Communicate.send(server, out, receivePacket.getAddress(), receivePacket.getPort());
		}
	}
	public static int newID(){ 
		int currID = Integer.parseInt(prop.getProperty("id"));
		prop.setProperty("id", Integer.toString(currID+1));
		return currID;
	}
	public static boolean loadProps(){
		prop = new Properties();
		OutputStream output=null;
		InputStream input=null;
		try {
			File varTmpDir = new File("config.properties");
			boolean exists = varTmpDir.exists();
			if(exists)
			{
				System.out.println("properties exists");
				input = new FileInputStream("config.properties");
				// load a properties file
				prop.load(input);
				clientNum = Integer.parseInt(prop.getProperty("id"));
			}
			else{
				output = new FileOutputStream("config.properties");
				prop.setProperty("id", "0");
				prop.store(output, null);
			}
		} catch (Exception io) {
			io.printStackTrace();
			return false;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
