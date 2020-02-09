package server;

import java.util.Random;

public class Board {
	public static int numberOfBoards = 0;
	
	int currentBoardNumber;
	int numberOfPlayers = 0;
	int maxNumberOfPlayers = 2;//2;
	boolean gameAvailable = false;
	boolean gameStarted = false;
	boolean gameEnded = false; // Pokazywane gdy np. jeszcze jeden gracz czeka w kolejce
	boolean firstMove = true; // Upewnia się, że gracz w pierwszej turze nie trafi na minę
	
	int currentUser = 0;
	
	int winnerID = 0;
	
	int width;
	int height;
	int bombs;
	
	int [][] gameBoardCellValue;
	boolean[][] gameBoardBombsList;
	boolean[][] gameBoardVisibleCells;
	
	static Board[] boards = null;
		
	Timer timer = new Timer();
	
	ClientThread[] users;
	
	Random rand;

	public int getTimerTime() {
		return (int)(timer.GetTime());
	}
	public int getUserID(ClientThread client) {
		for(int i=0;i< users.length;i++) {
			if (users[i] == client)
				return i;
		}
		return -1;
	}
	public boolean getGameAvailable() {
		return gameAvailable;
	}
	public boolean getGameEnded() {
		return gameEnded;
	}
	public int getCurentBoardNumber() {
		return  currentBoardNumber;
	}
	public int getNumberOfUsers() {
		return  numberOfPlayers;
	}
	public synchronized int getCurentUser() {
		return  currentUser;
	}
	public synchronized boolean isCurentUser(ClientThread clientThread) {
		if(users[currentUser] == clientThread) {
			return true;
		}
		return false;
	}
	public synchronized void nextCurentUser() {
		currentUser  = (currentUser + 1) % numberOfPlayers;
		//System.out.println(users[currentUser].login);
	}
	
	Board(int width, int height,int bombs){
		
		users = new ClientThread[maxNumberOfPlayers];
		
		rand = new Random();
		
		this.width = width;
		this.height = height;
		this.bombs = bombs;
		this.currentBoardNumber = numberOfBoards;
		
		numberOfBoards++;
		
		gameAvailable = true;
		gameStarted = false;
		gameEnded = false;
		createBoard();
	}
	public void createBoard() {
		
		gameBoardBombsList = new boolean[width][height];
		gameBoardVisibleCells = new boolean[width][height];
		gameBoardCellValue = new int[width][height];
		
		for(int i = 0; i< width; i++) {
			for(int j = 0; j< height; j++) {
				gameBoardVisibleCells[i][j] = false;
				gameBoardBombsList[i][j] = false;
				gameBoardCellValue[i][j] = 0;
			}
		}
		
		//Dodaje bomby
		int bombsLeave = bombs;
		while(bombsLeave > 0) {
			int temp_x = rand.nextInt(width);
			int temp_y = rand.nextInt(height);
			if(gameBoardBombsList[temp_x][temp_y] == false) {
				gameBoardBombsList[temp_x][temp_y] = true;
				bombsLeave--;
			}
		}
		
		/// TEST BEGIN SHOW MAP
//		for(int i = 0; i< width; i++) {
//			for(int j = 0; j< height; j++) {
//				if(gameBoardBombsList[i][j] == false)
//					System.out.print("0 ");
//				else
//					System.out.print("1 ");
//			}
//			System.out.println("");
//		}
//		System.out.println("");
		/// TEST END

		for(int i = 0; i< width; i++) {
			for(int j = 0; j< height; j++) {
				int temp_number = 0;
				if (gameBoardBombsList[i][j] == true) {
					gameBoardCellValue[i][j] = -1;
				}
				else {
					if(( i - 1 ) >= 0 && 	( j - 1 ) >= 0 && 		gameBoardBombsList[i-1][j-1] == true)
						temp_number++;
					if(( i - 1 ) >= 0 && 							gameBoardBombsList[i-1][j] == true)
						temp_number++;
					if(( i - 1 ) >= 0 && 	( j + 1 ) < height && 	gameBoardBombsList[i-1][j+1] == true)
						temp_number++;
					if(						( j - 1 ) >= 0 && 		gameBoardBombsList[i][j-1] == true)
						temp_number++;
					if(						( j + 1 ) < height && 	gameBoardBombsList[i][j+1] == true)
						temp_number++;
					if(( i + 1 ) < width && ( j - 1 ) >= 0 && 		gameBoardBombsList[i+1][j-1] == true)
						temp_number++;
					if(( i + 1 ) < width && 						gameBoardBombsList[i+1][j] == true)
						temp_number++;
					if(( i + 1 ) < width && ( j + 1 ) < height && 	gameBoardBombsList[i+1][j+1] == true)
						temp_number++;

					gameBoardCellValue[i][j] = temp_number;
				}
			}
		}
//		/// TEST BEGIN SHOW MAP
//		for(int i = 0; i< width; i++) {
//			for(int j = 0; j< height; j++) {
//				System.out.print(gameBoardCellValue[i][j] + " ");
//			}
//			System.out.println("");
//		}
//		System.out.println("");
//		
//		
//		/// TEST END
//		for(int i = 0; i< width; i++) {
//			for(int j = 0; j< height; j++) {
//				if(gameBoardVisibleCells[i][j] == false)
//					System.out.print("0 ");
//				else
//					System.out.print("1 ");
//			}
//			System.out.println("");
//		}
//		System.out.println("");
//		
//
//		/// TEST BEGIN SHOW MAP
//		//System.out.println(ClickedAt(0,0));
//		for(int i = 0; i< width; i++) {
//			for(int j = 0; j< height; j++) {
//				if(gameBoardVisibleCells[i][j] == false)
//					System.out.print("0 ");
//				else
//					System.out.print("1 ");
//			}
//			System.out.println("");
//		}
//		System.out.println("RR");
//
//		/// TEST END
		
	}
	public boolean ClickedAt(int x, int y) {// True oznacza bombę czyli przegraną
		while(true) {
		if(x < 0 || x >= width || y < 0 || y >= height) { // Zdarza się na obrzeżach
			firstMove = false;
			return false;
			}
		if(gameBoardVisibleCells[x][y] == true) {
			firstMove = false;
			return false;
	}
		gameBoardVisibleCells[x][y] = true;
		if(gameBoardBombsList[x][y] == true) {
			if(firstMove) { // Jeśli to pierwszy ruch i gracz trafi na minę, to cała mapa się ponownie losuje/tworzy dopóki nie będzie satysfakcjonującego wyniku
				createBoard();
				System.out.println("Tworzę zapasową planszę.");
				continue;
			}
			gameEnded = true;
			return true;
		}
		if(gameBoardCellValue[x][y] == 0) {
			ClickedAtReq(x-1,y-1);
			ClickedAtReq(x-1,y);
			ClickedAtReq(x-1,y+1);
			ClickedAtReq(x,y-1);
			ClickedAtReq(x,y+1);
			ClickedAtReq(x+1,y-1);
			ClickedAtReq(x+1,y);
			ClickedAtReq(x+1,y+1);
		}

		firstMove = false;
		return false;
		}
	}
	public void ClickedAtReq(int x, int y) {// True oznacza bombę czyli przegraną
		if(x < 0 || x >= width || y < 0 || y >= height) // Zdarza się na obrzeżach
			return;
		if(gameBoardVisibleCells[x][y] == true)
			return;
		gameBoardVisibleCells[x][y] = true;
		if(gameBoardBombsList[x][y] == true) {
			return;
		}
		if(gameBoardCellValue[x][y] == 0) {
			ClickedAtReq(x-1,y-1);
			ClickedAtReq(x-1,y);
			ClickedAtReq(x-1,y+1);
			ClickedAtReq(x,y-1);
			ClickedAtReq(x,y+1);
			ClickedAtReq(x+1,y-1);
			ClickedAtReq(x+1,y);
			ClickedAtReq(x+1,y+1);
		}
		return;
	}
	
	
	public void StartGame() {
		
		timer.start();
		
		gameAvailable = false;
		gameStarted = true;
		gameEnded = false;
	}
	public void GameEnd(int winnerID, String type) {
		timer.stop();
		
		gameAvailable = false;
		gameStarted = false;
		gameEnded = true;
		
		if(users.length>1) {
			String what = "";
			what += getTimerTime() + ";";
			what += users[winnerID].login + ";";
			what += users[(winnerID +1)%2] + ";";
			what += type + ";";
			TopList.writeToFile(what);
			System.err.println("Nie");
		}
		else {
			System.err.println("co");
		}
	}
	
	public void AddUser(ClientThread clientThread) {
		users[numberOfPlayers] = clientThread;
		currentUser = numberOfPlayers;
		numberOfPlayers++;
		if (numberOfPlayers >= maxNumberOfPlayers)
		{
			StartGame();
		}
	}
	synchronized public static Board findEmptyBoard() {
		if(boards != null) {
			for (int i = 0; i<boards.length;i++) {
				if (boards[i].getGameAvailable()) {
					return boards[i];
				}
			}
		}
		return null;
	}
	
	synchronized static public Board AddBoard(int width, int height, int bombs) {
		
		if (boards == null)
		{
			boards = new Board[1];
			boards[0] = new Board(width,height,bombs);
		}
		else
		{
			Board[] tempBoard = new Board[boards.length + 1];
			for (int i = 0; i<boards.length;i++) {
				tempBoard[i] = boards[i];
			}
			tempBoard[boards.length] = new Board(width,height,bombs);
			boards = tempBoard;
 		}
		return boards[boards.length - 1];
	}
	public String toStringAll()
	{
		String temp = "";
		temp += "Board = ";
		temp += width + " = ";
		temp += height + " = ";
		for(int i = 0; i< width; i++) {
			for(int j = 0; j< height; j++) {
				if (i != width || j != height)
					temp += gameBoardCellValue[i][j] + ", ";
				else
					temp += "" + gameBoardCellValue[i][j];
			}
		}
		return temp;
	}
	public String toString()
	{
		String temp = "";
		temp += "Board = ";
		temp += width + " = ";
		temp += height + " = ";
		for(int i = 0; i< width; i++) {
			for(int j = 0; j< height; j++) {
				if(gameBoardVisibleCells[i][j]) {
					temp += "" + gameBoardCellValue[i][j];
				}
				else {
					temp += "-100"; // Oznacza, że pole nie zostało jeszcze odkryte
				}
				if (i != width || j != height)
					temp += ", ";	
			}
		}
		return temp;
	}
}
