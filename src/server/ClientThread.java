package server;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable {
	protected Socket clientSocket = null;
	protected String textFromServer = null;
	protected Board mainBoard = null;
	protected int WIDTH=4;
	protected int HEIGHT=4;
//	protected int TIME=4;
	protected int MINES=4;
	protected String login;
	protected boolean userWaitForOpponent = false;
	

	public ClientThread(Socket clientSocket, String textFromServer) {
		this.clientSocket = clientSocket;
		this.textFromServer = textFromServer;
	}

	public void run() {
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while (true) {

				if(userWaitForOpponent) {
					if(mainBoard != null) {
						if(mainBoard.isCurentUser(this)) {
							if(mainBoard.getNumberOfUsers() >= 2) {
								System.out.println("Tura gracza " + login);
								userWaitForOpponent = false;
								String ret = mainBoard.toString() + "=" + (mainBoard.getGameEnded() == true ? 0 : 1);
								out.println(ret);
							}
						}
					}
				}
				
				/*if(mainBoard != null && mainBoard.gameEnded == true) {
					out.println("WIN");
					break;
				}*/
				
				if(!userWaitForOpponent) {
					String messageSystem = in.readLine();
					if (messageSystem != null) {
						System.err.println(messageSystem);
						String[] message = messageSystem.split("=");
						for (int i = 0; i < message.length; i++)
							message[i] = message[i].trim();
	
						if (message[0].equals("UserConnected")) {
							login = message[1];
							System.out.println("Połączył się użytkownik: " +  login );
							Board tempBoard = Board.findEmptyBoard();
							if (tempBoard != null) {
								mainBoard = tempBoard;
								System.out.println("Znaleziono grę, jej numer to: " + mainBoard.getCurentBoardNumber());
							} else {
								mainBoard = Board.AddBoard(WIDTH, HEIGHT, MINES);
								System.out.println("Nie znaleziono uruchomionej gry, rozpoczynanie własne o numerze: " + mainBoard.getCurentBoardNumber());
							}
							mainBoard.AddUser(this);
							out.println("MapData = " + WIDTH + " = " + HEIGHT + " = " + (mainBoard.gameStarted ? 1 : 0) + " = " + MINES); // Wielkość Mapy
						} else if (message[0].equals("TopList")) {
							System.out.println("Wysłano Toplist");
							out.println(TopList.readFile());
							break;
						}else if (message[0].equals("TopListJDBC")) {
							System.out.println("Wysłano JDBC");
							//out.println(TopListJDBC.readFile());
							break;
						} else if (message[0].equals("Clicked")) {
							System.out.println("Użytkownik "+ login + " kliknął na pole: " + Integer.parseInt(message[1]) + "x" + Integer.parseInt(message[2]));
							boolean result = mainBoard.ClickedAt(Integer.parseInt(message[1]),Integer.parseInt(message[2]));
							String ret = mainBoard.toString() + "=" + (result == false ? 0 : 1);
	
							out.println(ret);
						} else if (message[0].equals("END")) {
							System.err.println("Kill");
							if(message[1].equals("Mina")) {
								System.out.println("Przeciwnik trafił minę");
								mainBoard.GameEnd((mainBoard.getUserID(this) + 1 ) %2, "Wygrana");
							}
							else if(message[1].equals("Pole")) {
								System.out.println("Użytkownik "+ login +  " jako ostatni odkrył pole");
								mainBoard.GameEnd((mainBoard.getUserID(this) + 1 ) %2, "Wszystko");
							}
							else {
								System.out.println("Użytkownik "+ login +  " wyszedł z gry.");
								mainBoard.GameEnd((mainBoard.getUserID(this) + 1 ) %2, "Wyjście");
							}
							break;
						} else if (message[0].equals("Waiting")) {
							System.out.println("Użytkownik " + login + " rozpoczął czekanie na swój ruch.");
							userWaitForOpponent = true;
							mainBoard.nextCurentUser();
						} else {
							System.err.println("Błędna wiadomość - " + messageSystem);
						}
					}
				}
			}
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}