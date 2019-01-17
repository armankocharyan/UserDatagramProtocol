package core;


import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client {
	
	//CONSTANT VARIABLES:
	private final byte [] READ_WRITE_ARR = {(byte)1, (byte)2};
	private final String [] MODE_ARR = {"netascii", "octet","neTasCiI", "OcTet","neTAScii", "ocTEt","nEtASCii", "oCteT","neTAScii", "OCTEt"};
	private final String [] FILE_NAME_ARR = {"candice.txt", "sara.txt", "adriana.txt", "alessandra.txt", "lily.txt", "jasmine.txt", "taylor.txt", "elsa.txt","lais.txt", "josephine.txt"};
	private final int BUFFER_SIZE = 64;
	private final int PORT = 23;
	private final int NUM_MESSAGES = 11;

	//GLOBAL VARIABLES
	private DatagramSocket socket;
	private InetAddress IPAddress;
	private Calendar cal; 
	private SimpleDateFormat time;
	private DatagramPacket packet_send;
	
	//HOLDERS
	private InetAddress holdAddress = null;
	private int holdPort = 0;
	
	
	//Constructor for Client
	public Client() throws Exception {
		socket = new DatagramSocket();
		IPAddress = InetAddress.getLocalHost();
		time = new SimpleDateFormat("HH:mm:ss");
		
		System.out.println("Launched Client Program!");
		System.out.println("Sending messages to the intermediate host...");
		System.out.print("\n\n\n");
	}

	
	//This method sends and receives messages with the Intermediate Host
	private void communicate(int iter) throws Exception {
		
		//First ten messages are proper READ or WRITE
		if(iter<10) {
			//SENDING A MESSAGE TO THE INTERMEDIATE HOST
			byte[] buffer_send = getRequest(READ_WRITE_ARR[iter % 2], FILE_NAME_ARR[iter], MODE_ARR[iter]);
			
			System.out.println("SENDING MESSAGE[" + iter + "] PACKET TO INTERMEDIATE HOST");
			System.out.print("BYTE ARRAY: ");
			for(int i = 0; i < buffer_send.length; i++ ) System.out.print(buffer_send[i] + " ");
			System.out.print("\n");
			System.out.print("RAW STRING VALUE: ");
			System.out.print(new String(buffer_send));
			System.out.print("\n");
			System.out.print("PARSED STRING VALUE: ");
			System.out.print(Parser.parseByteArray(buffer_send)[0] + " " + Parser.parseByteArray(buffer_send)[1] + " " + Parser.parseByteArray(buffer_send)[2]);
			System.out.print("\n");			
	        
	        if(holdAddress == null && holdPort == 0) {
	        	try {
	        		packet_send = new DatagramPacket(buffer_send, buffer_send.length, IPAddress, PORT);
					socket = new DatagramSocket();
					socket.send(packet_send);
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        		System.exit(1);
	        	}
				
				}
	        else {
	        	try {
		        	packet_send = new DatagramPacket(buffer_send, buffer_send.length, holdAddress, holdPort);
		    		socket = new DatagramSocket();
		    		socket.send(packet_send);
	        	} catch(Exception e) {
	        		e.printStackTrace();
	        		System.exit(1);
	        	}
	        	
	        }
	        System.out.print("LENGTH OF THE PACKET: " + packet_send.getLength() + "\n");
	        System.out.print("PORT OF THE PACKET: " + packet_send.getPort() + "\n");
	        System.out.print("HOST OF THE PORT: " + packet_send.getAddress() + "\n");
	        System.out.print("PACKET SENT AT: ");
			cal = Calendar.getInstance();
	        System.out.print(time.format(cal.getTime()));
			System.out.print("\n\n\n");
			
			//RESPONSE FROM INTERMEDIATE_HOST FOR MESSAGE_0
			
			byte[] buffer_receive = new byte [BUFFER_SIZE];
			DatagramPacket packet_receive = new DatagramPacket(buffer_receive, buffer_receive.length);
			try {
				socket.receive(packet_receive);
			}catch(Exception e) {
				System.out.print("IO Exception: likely:");
		        System.out.println("Receive Socket Timed Out.\n" + e);
		        e.printStackTrace();
		        System.exit(1);
			}
			byte [] data0 = packet_receive.getData();
			System.out.print("RECEIVING A RESPONSE FROM THE INTERMEDIATE HOST:");
			System.out.print("\n");
			System.out.print("PACKET RECEIVED AT: ");
			cal = Calendar.getInstance();
	        System.out.print(time.format(cal.getTime()) + "\n");
	        System.out.print("LENGTH OF THE PACKET: " + packet_receive.getLength() + "\n");
	        System.out.print("PORT OF THE PACKET: " + packet_receive.getPort() + "\n");
	        System.out.print("HOST OF THE PORT: " + packet_receive.getAddress() + "\n");
	        System.out.print("BYTE ARRAY: ");
			for(int i = 0; i < Parser.responseTrimmer(data0).length; i++ ) System.out.print(Parser.responseTrimmer(data0)[i] + " ");
			System.out.print("\n");
			System.out.print("RAW STRING VALUE: ");
			System.out.print(new String(Parser.responseTrimmer(data0)));
			System.out.print("\n\n\n");
			
			
			holdAddress = packet_receive.getAddress();
			holdPort = packet_receive.getPort();
			socket.close();
		}
		else { //any message after the tenth message is invalid
			
			String msg = "Incorrect Message";
			byte[] buffer_send = msg.getBytes();
			
			System.out.println("SENDING INCORRECT MESSAGE PACKET TO INTERMEDIATE HOST");
			System.out.print("BYTE ARRAY: ");
			for(int i = 0; i < buffer_send.length; i++ ) System.out.print(buffer_send[i] + " ");
			System.out.print("\n");
			System.out.print("RAW STRING VALUE: ");
			System.out.print(new String(buffer_send));
			System.out.print("\n");
			
	        
	        if(holdAddress == null && holdPort == 0) {
				packet_send = new DatagramPacket(buffer_send, buffer_send.length, IPAddress, PORT);
				socket = new DatagramSocket();
				socket.send(packet_send);
				}
	        else {
	        	packet_send = new DatagramPacket(buffer_send, buffer_send.length, holdAddress, holdPort);
	    		socket = new DatagramSocket();
	    		socket.send(packet_send);
	        }
	        System.out.print("LENGTH OF THE PACKET: " + packet_send.getLength() + "\n");
	        System.out.print("PORT OF THE PACKET: " + packet_send.getPort() + "\n");
	        System.out.print("HOST OF THE PORT: " + packet_send.getAddress() + "\n");
	        System.out.print("PACKET SENT AT: ");
			cal = Calendar.getInstance();
	        System.out.print(time.format(cal.getTime()));
		}
	}
	
	
	//Creates and returns a request byte array with format {0,1,a0,a1,a2,a3....an,0,b0,b1,b2,b3....bn,0} or {0,2,a0,a1,a2,a3....an,0,b0,b1,b2,b3....bn,0}
	private byte[] getRequest (byte read_write, String file_name, String mode) {
		
		byte [] read_write_bytes = new byte [2];
		byte [] zero_byte = new byte [1];
		
		zero_byte[0] = 0;
		
		read_write_bytes[0] = 0;
		read_write_bytes[1] = read_write;		
		
		byte [] file_name_bytes = file_name.getBytes();
		byte [] mode_bytes = mode.getBytes();
		
		byte [] request_bytes = combineArrays(read_write_bytes, file_name_bytes, zero_byte, mode_bytes, zero_byte);
		
		return request_bytes;
		
	}
	
	//Combines and returns collections of byte arrays 
	private byte[] combineArrays(byte[]... collections) {
        int length = 0;
        
        //finding the length of all arrays combined
        for (byte[] arr : collections) {
            length += arr.length;
        }

        //allocating space
        byte[] concacenatedArray = (byte[]) Array.newInstance(collections[0].getClass().getComponentType(), length);

        //copying all the arrays into the collection
        int temp = 0;
        for (byte[] arr : collections) {
            System.arraycopy(arr, 0, concacenatedArray, temp, arr.length);
            temp += arr.length;
        }

        return concacenatedArray;
    }
	

	
	//Main method - first method that the compiler executes
	public static void main(String[] args) throws Exception {
		
		Client client = new Client();
		int count = 0;
		
		while(count < client.NUM_MESSAGES) {
			client.communicate(count);
			count++;
		}
	}

}
