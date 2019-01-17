package core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class IntermediateHost extends Thread{
	
	
	//CONSTANT VARIABLES
	private final int BUFFER_SIZE = 64;
	private final int PORT_CLIENT = 23;
	private final int PORT_SERVER = 69;
	
	//GLOBAL VARIABLES
	private DatagramSocket socket_client;
	private DatagramSocket socket_server;
	private InetAddress IPAddress;
	private Calendar cal; 
	private SimpleDateFormat time;
	private DatagramPacket packet_client;
	private DatagramPacket packet_server;
	
	//HOLDERS
	private InetAddress holdAddressClient = null;
	private int holdPortClient = 0;
	private InetAddress holdAddressServer = null;
	private int holdPortServer = 0;

	//Constructor for the Intermediate Host
	public IntermediateHost() throws Exception{
		
		socket_client = new DatagramSocket(PORT_CLIENT);
		socket_server = new DatagramSocket();
		time = new SimpleDateFormat("HH:mm:ss");
		IPAddress = InetAddress.getLocalHost();
		
		System.out.println("Launched Intermediate Host!");
		System.out.println("Waiting for a client message...");
		System.out.print("\n\n\n");
	}
	
	//Overrides the run method from parent Class Thread
	//Used to run a server in order to interact with Client and Server Classes
	@Override
	public void run() {
		

		
		while(true) {
			
			
			
			try {
				byte[] buffer_client = new byte[BUFFER_SIZE];
				byte[] buffer_server = new byte[BUFFER_SIZE];
				
				//RECEIVING CLIENT MESSAGE
				packet_client = new DatagramPacket(buffer_client, buffer_client.length);
				socket_client.receive(packet_client);
				
				holdAddressClient = packet_client.getAddress();
				holdPortClient = packet_client.getPort();
				
				byte [] client_data = packet_client.getData();
				
				System.out.print("PACKET RECEIVED AT: ");
				cal = Calendar.getInstance();
		        System.out.print(time.format(cal.getTime()) + "\n");
		        
		        System.out.print("LENGTH OF THE PACKET: " + packet_client.getLength() + "\n");
		        System.out.print("PORT OF THE PACKET: " + packet_client.getPort() + "\n");
		        System.out.print("HOST OF THE PORT: " + packet_client.getAddress() + "\n");
		        
		        if(Parser.parseByteArray(client_data) != null) {
					System.out.print("BYTE ARRAY: ");
					for(int i = 0; i < Parser.trim(client_data).length; i++ ) System.out.print(Parser.trim(client_data)[i] + " ");
		        }
		        else {
		        	System.out.print("BYTE ARRAY: ");
					for(int i = 0; i < client_data.length; i++ ) System.out.print(client_data[i] + " ");
		        	
		        }
				System.out.print("\n");
				System.out.print("RAW STRING VALUE: ");
				System.out.print(new String(client_data));
				System.out.print("\n");
				if(Parser.parseByteArray(client_data) != null) {
					System.out.print("PARSED STRING VALUE: ");
					System.out.print( Parser.parseByteArray(client_data)[0] + " " + Parser.parseByteArray(client_data)[1] + " " + Parser.parseByteArray(client_data)[2]);
				}
				else {
					System.out.print("INVALID DATA");
				}
				
				System.out.print("\n\n\n");
				
				

				//FORWARDING MESSAGE TO SERVER
				System.out.print("FORWARDING MESSAGE TO SERVER:");
				System.out.print("\n");
				System.out.print("BYTE ARRAY: ");
				for(int i = 0; i < Parser.trim(client_data).length; i++ ) System.out.print(Parser.trim(client_data)[i] + " ");
				System.out.print("\n");
				System.out.print("RAW STRING VALUE: ");
				System.out.print(new String(client_data));
				System.out.print("\n");
				if(Parser.parseByteArray(client_data) != null) {
					System.out.print("PARSED STRING VALUE: ");
					System.out.print(Parser.parseByteArray(client_data)[0] + " " + Parser.parseByteArray(client_data)[1] + " " + Parser.parseByteArray(client_data)[2]);
				}
				else {
					System.out.print("INVALID DATA");
				}
				System.out.print("\n");

		        if(holdAddressServer == null && holdPortServer == 0) {
					packet_server = new DatagramPacket(client_data, client_data.length, IPAddress, PORT_SERVER);
					socket_server = new DatagramSocket();
					socket_server.send(packet_server);	
		        }
		        else {
		        	packet_server = new DatagramPacket(client_data, client_data.length, holdAddressServer, holdPortServer);
					socket_server = new DatagramSocket();
					socket_server.send(packet_server);	
		        }
		        
		        System.out.print("LENGTH OF THE PACKET: " + packet_server.getLength() + "\n");
		        System.out.print("PORT OF THE PACKET: " + packet_server.getPort() + "\n");
		        System.out.print("HOST OF THE PORT: " + packet_server.getAddress() + "\n");
		        
		        
		        System.out.print("PACKET SENT AT: ");
				cal = Calendar.getInstance();
				System.out.print(time.format(cal.getTime()));
				System.out.print("\n\n\n");
				
				
		
				//RECEIVING RESPONSE FROM THE SERVER
				packet_server = new DatagramPacket(buffer_server, buffer_server.length);
				socket_server.receive(packet_server);
				holdAddressServer = packet_server.getAddress();
				holdPortServer = packet_server.getPort();
				
				byte [] response_data = packet_server.getData();
				
				System.out.print("RECEIVING A RESPONSE FROM THE SERVER:");
				System.out.print("\n");
				System.out.print("PACKET RECEIVED AT: ");
				cal = Calendar.getInstance();
		        System.out.print(time.format(cal.getTime()) + "\n");
		        System.out.print("LENGTH OF THE PACKET: " + packet_server.getLength() + "\n");
		        System.out.print("PORT OF THE PACKET: " + packet_server.getPort() + "\n");
		        System.out.print("HOST OF THE PORT: " + packet_server.getAddress() + "\n");
		        System.out.print("BYTE ARRAY: ");
				for(int i = 0; i < Parser.responseTrimmer(response_data).length; i++ ) System.out.print(Parser.responseTrimmer(response_data)[i] + " ");
				System.out.print("\n");
				System.out.print("RAW STRING VALUE: ");
				System.out.print(new String(Parser.responseTrimmer(response_data)));
				System.out.print("\n\n\n");
				
				
				//FORWARDING MESSAGE TO CLIENT
				System.out.print("FORWARDING MESSAGE TO CLIENT:");
				System.out.print("\n");
				System.out.print("BYTE ARRAY: ");
				for(int i = 0; i < Parser.responseTrimmer(response_data).length; i++ ) System.out.print(Parser.responseTrimmer(response_data)[i] + " ");
				System.out.print("\n");
				System.out.print("RAW STRING VALUE: ");
				System.out.print(new String(response_data));
				System.out.print("\n");
				
				DatagramPacket packet_client1 = new DatagramPacket(response_data, response_data.length, holdAddressClient, holdPortClient);
				socket_client = new DatagramSocket();
				
				System.out.print("LENGTH OF THE PACKET: " + packet_client1.getLength() + "\n");
		        System.out.print("PORT OF THE PACKET: " + packet_client1.getPort() + "\n");
		        System.out.print("HOST OF THE PORT: " + packet_client1.getAddress() + "\n");
				System.out.print("PACKET SENT AT: ");
				cal = Calendar.getInstance();
		        System.out.print(time.format(cal.getTime()));
				socket_client.send(packet_client1);	
				System.out.print("\n\n\n");
				
			}
			
			catch(Exception e) {
				System.out.println("ERROR: Can't Connect");
				e.printStackTrace();
				break;
				
			}
			
				
		}
		
	}
	
	
	
	//Main method - first method that the compiler executes
	public static void main(String[] args) throws Exception{
		IntermediateHost intermediateHost = new IntermediateHost();
		intermediateHost.run();
	}

}