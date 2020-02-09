package client;

import java.io.BufferedReader;
import java.io.IOException;

public class WaitClass extends Thread {
	Client  client;
	BufferedReader in;
	
	public WaitClass(Client client, BufferedReader in) {
		this.client = client;
		this.in = in;
	}
	
	public void run() {
		
			while (in != null) { // Po rozłączeniu użytkownika, ten wątek ciągle może pracować	
				String readedOutput = null;
				try {
					readedOutput = in.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				if(readedOutput != null) {
					client.waitingEndedFunction(readedOutput);
					break;
				}
				
			}
		
	}
	
}
