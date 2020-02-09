package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {
	Socket openedSocket = null;
	String host = "127.0.0.1";
	int port = 9999;
	PrintWriter out = null;
	BufferedReader in = null;
	public static Client client = null;
	public boolean is_alive = false;
	public boolean waitingEnded = false;
	public WaitClass waitingThread;
	
	
	GameGUI gameGUI;

	public Client(GameGUI gameGUI) throws IOException{
			this.gameGUI = gameGUI;
			client = this;
			is_alive = true;
			String serverHostname = new String("127.0.0.1");
			System.out.println("Łączenie z serwerem " + serverHostname + " na porcie " + port + ".");

			try {
				openedSocket = new Socket(host, port);
				out = new PrintWriter(openedSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(openedSocket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Nieznany host " + host);
				return;
			} catch (IOException e) {
				System.err.println("Nie można pobrać danych od serwera");
				return;
			}
			System.out.println("Poprawnie połączono się z serwerem.");
			firstResponse();
	}
	
	public void firstResponse() throws IOException {
		out.println("UserConnected = " + gameGUI.login);
		while (true) { // Rozpoczęcie połączenia, Wymiar X = Wymiar Y = Czy gracz stworzył serwer(jeśli tak, to wykonuje ruch drugi) = Liczba bomb(opcjonalne)

			String readedOutput = in.readLine();
			if(readedOutput != null) {
				System.out.println("Coś dostałem, a mianowicie " + readedOutput);
				String[] roArray = readedOutput.split("=");
				for(int i = 0; i < roArray.length; i++)
					roArray[i] = roArray[i].trim();
				
				if (roArray[0].equals("MapData")){
					gameGUI.width = Integer.parseInt(roArray[1]);
					gameGUI.height = Integer.parseInt(roArray[2]);

					System.out.println("Ustawiono rozmiar mapy na - " + roArray[1] + "x" + roArray[2]);
				} 
				else{
					System.out.println("Błąd odpowiedzi serwera przy wysyłaniu danych o mapie - " + readedOutput);
					gameGUI.width = 10;
					gameGUI.height = 10;
				}
				gameGUI.setupGUI();
				System.out.println("RR " + roArray[3]);
				if(roArray[3].equals("0")) { // Pierwsza osoba, która tworzy serwer ustępuje drugiej osoby pierwszy ruch
					//gameGUI.disa
					if (waitingThread != null) {
						System.err.print("Wątek czekający nadal pracuje, dziwne.");
					}
					gameGUI.SetEnableButtons(false);
					out.println("Waiting");
					waitingThread = new WaitClass(this,in); // Czeka na przeciwnika
					waitingThread.start();
					//waitingForOpponent();
				}
				break;
			}
		}
	}
	public String clickedBlock(int x, int y){
		//BlockMovement();
		out.println("Clicked = " + y + " = " + x);
		while (true) { // Format odpowiedzi  Board = WIDTH = HEIGHT = Pole1,Pole2,Pole3 = Mina
			String readedOutput;
			try {
				readedOutput = in.readLine();
			} catch (IOException e) {
				readedOutput = null;
				e.printStackTrace();
			}
			if(readedOutput != null) {
				//System.out.println(readedOutput);
				String[] roArray = readedOutput.split("=");
				
				for(int i = 0; i < roArray.length; i++)
					roArray[i] = roArray[i].trim();

				gameGUI.UpdateButtons(roArray[3]);
				gameGUI.SetEnableButtons(false);
				if (roArray[4].equals("1")) {
					System.out.println("Wpadłeś na minę");
					gameGUI.SetEnableButtons(false);
					out.println("END = Mina");

					JOptionPane.showMessageDialog(null, "Przegrałeś, bo wpadłeś na minę");
					break;
				}
				out.println("Waiting");
				waitingThread = new WaitClass(this,in); // Czeka na przeciwnika
				waitingThread.start();			
				break;
			}
		}
		return "";
	}
		
	public void waitingEndedFunction(String readedOutput) {
		System.out.println("Updatuje pola");
		//System.out.println(readedOutput);
		String[] roArray = readedOutput.split("=");
		
		for(int i = 0; i < roArray.length; i++)
			roArray[i] = roArray[i].trim();
		
		if (roArray[0].equals("WIN")) {
			System.out.println("Wygrałeś, przeciwnik rozłączył się lub trafił na minę");
			JOptionPane.showMessageDialog(null, "Wygrałeś, przeciwnik rozłączył się lub trafił na minę\"");
			return;
		}

		gameGUI.UpdateButtons(roArray[3]);
		gameGUI.SetEnableButtons(true);
		
		if (roArray[4].equals("0")) {
			System.out.println("Wpadłeś na minę");
			gameGUI.SetEnableButtons(false);
			out.println("END = Mina");

			JOptionPane.showMessageDialog(null, "Przegrałeś, bo wpadłeś na minę");
		}
	}
	
	
	public void closeIt() throws IOException {
		if(is_alive)
		{
			is_alive = false;
			out.println("END = EXIT");
			System.out.println("Klient się rozłączył");
			out.close();
			in.close();
			openedSocket.close();
		}
	}
}