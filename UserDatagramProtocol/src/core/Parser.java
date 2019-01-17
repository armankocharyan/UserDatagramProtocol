package core;

public class Parser {
	
	//This class has static methods defined in the core package
	//All three classes call methods from this class in order to parse byte arrays
	
	//Receives a databyte byte array in the following format {0,1,a0,a1,a2,a3....an,0,b0,b1,b2,b3....bn,0}
	//Parses and returns a string array in the following format {request, file_name, mode}
	//returns null if the databyte byte array is in wrong format
	public static String [] parseByteArray(byte [] dataByte) {
		
		String [] retArr = new String [3];
		
		String read_write = "";
		String file_name;
		String mode;
		
		//check to see if it's read OR write OR invalid		
		if(dataByte[1] == (byte) 1 ) read_write = "read";
		else if(dataByte[1] == (byte) 2 ) read_write = "write";
		else  return null;
		
		//parsing the file name between first two bytes and the first zero byte
		
		//finding the index of first zero
		int index_first_zero = 0;
		
		for (int i = 2; i < dataByte.length; i++) {
			if(dataByte[i] == (byte) 0 ) 
				{
					index_first_zero = i;
					break;
				}
		}
		
		//finding the bytes that make the file_name
		byte [] file_name_bytes = new byte[index_first_zero - 2];
		
		for(int i = 0; i < file_name_bytes.length; i++) {
			file_name_bytes[i] = dataByte[i+2];
		}
		//finding the file name String
		file_name = new String (file_name_bytes);
		
		//finding the bytes that make the mode
		byte [] mode_bytes = new byte[dataByte.length - index_first_zero - 2];
		
		index_first_zero ++;
		
		for (int i = 0; i < mode_bytes.length; i++) {
			mode_bytes[i] = dataByte[i + index_first_zero];
		}
		
		//find the mode String
		mode = new String(mode_bytes);
		
		retArr[0] = read_write;
		retArr[1] = file_name;
		retArr[2] = mode;
		
		
		return retArr;
	}

	//This methods trims zero bytes
	public static byte[] trim (byte [] data) {
		
		int count = 0;
		int length  = 0;
		
		for (int i = 0; i < data.length; i++) {
			if(data[i] == (byte)0) {
				count++;
			}
			
			length++;
			
			if(count >= 3) {
				break;
			}
		}
		
		byte [] returnArray = new byte[length];
		
		for(int i = 0; i < returnArray.length; i++) returnArray[i] = data[i];
		
		return returnArray;
	}
	
	//This methods removes the zero bytes from the response data array (keeps the first 4 elements)
	public static byte[] responseTrimmer(byte [] data) {
		byte [] response = new byte [4];
		
		for (int i = 0; i < response.length; i++) response[i] = data[i];
		
		
		return response;
	}
	

}
