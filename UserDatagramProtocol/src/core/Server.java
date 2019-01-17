package core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Server extends Thread{
	
	//CONSTANT VARIABLES
	private final int BUFFER_SIZE = 64;
	private final int PORT = 69;
	
	//GLOBAL VARIABLES
	private DatagramSocket socket;
	private Calendar cal; 
	private SimpleDateFormat time;
	
	//Constructor for the Server Class
	public Server() throws Exception{
		
		socket = new DatagramSocket(PORT);
		time = new SimpleDateFormat("HH:mm:ss");
		
		System.out.println("Launched Server!");
		System.out.println("Waiting for an Intermediate Host message...");
		System.out.print("\n\n\n");
	}
	
	//Overrides the run method from parent Class Thread
	//Used to run a server in order to interact with Intermediate Class
	@Override
	public void run() {
		
		while(true) {
			try {
				
				byte[] buffer = new byte[BUFFER_SIZE];
				
				//RECEIVING INTERMEDIATE HOST MESSAGE
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				socket.receive(packet);
				byte [] data = packet.getData();				
				byte [] response = getResponse(data);
				
				if(response != null) {
					System.out.print("PACKET RECEIVED AT: ");
					cal = Calendar.getInstance();
			        System.out.print(time.format(cal.getTime()) + "\n");
			        System.out.print("LENGTH OF THE PACKET: " + packet.getLength() + "\n");
			        System.out.print("PORT OF THE PACKET: " + packet.getPort() + "\n");
			        System.out.print("HOST OF THE PORT: " + packet.getAddress() + "\n");			        
					System.out.print("BYTE ARRAY: ");
					for(int i = 0; i < Parser.trim(data).length; i++ ) System.out.print(Parser.trim(data)[i] + " ");
					System.out.print("\n");
					System.out.print("RAW STRING VALUE: ");
					System.out.print(new String(data));
					System.out.print("\n");
					System.out.print("PARSED STRING VALUE: ");
					System.out.print(Parser.parseByteArray(data)[0] + " " + Parser.parseByteArray(data)[1] + " " + Parser.parseByteArray(data)[2]);
					System.out.print("\n\n\n");
				}	
				else {
					throw new NullPointerException("INVALID DATA: terminating server program"); 
				}
				
				//RESPONDING TO THE INTERMEDIATE HOST
				System.out.print("RESPONDING TO INTERMEDIATE HOST:");
				System.out.print("\n");
				System.out.print("BYTE ARRAY: ");
				for(int i = 0; i < response.length; i++ ) System.out.print(response[i] + " ");
				System.out.print("\n");
				System.out.print("STRING BYTE EQUIVALENT: ");
				System.out.print(new String(response));
				System.out.print("\n");
				System.out.print("RESPONDING TO REQUEST: " + Parser.parseByteArray(data)[0]);
				System.out.print("\n");
				
				DatagramPacket packet_respond = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
				
				System.out.print("PACKET SENT AT: ");
				cal = Calendar.getInstance();
		        System.out.print(time.format(cal.getTime()) + "\n");
		        System.out.print("LENGTH OF THE PACKET: " + packet_respond.getLength() + "\n");
		        System.out.print("PORT OF THE PACKET: " + packet_respond.getPort() + "\n");
		        System.out.print("HOST OF THE PORT: " + packet_respond.getAddress() + "\n");
		        
		        socket = new DatagramSocket();
		        socket.send(packet_respond);	
				System.out.print("\n\n\n");
			}
			
			catch(Exception e) {
				System.out.println("INVALID DATA: Terminating server");
				e.printStackTrace();
				break;
				
			}
			
				
		}
		
	}
	
	//returns the an array byte {0,3,0,1} if the data is a read request
	//returns the an array byte {0,4,0,0} if the data is a read request
	//returns null if the data is INVALID
	private byte [] getResponse(byte [] data) {
		byte [] response = new byte [4];
		
		if(data[1] == (byte) 1) {
			response[0] = 0;
			response[1] = 3;
			response[2] = 0;
			response[3] = 1;
		}
		else if(data[1] == (byte) 2) {
			response[0] = 0;
			response[1] = 4;
			response[2] = 0;
			response[3] = 0;
		}
		else {
			return null;
		}
		return response;
		
	}
	
	//Main method - first method that the compiler executes
	public static void main(String[] args) throws Exception{
		Server server = new Server();
		server.run();
	}

}