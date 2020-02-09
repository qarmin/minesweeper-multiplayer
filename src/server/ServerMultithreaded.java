package server;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class ServerMultithreaded implements Runnable{
    protected int          serverPort   = 9999;
    protected ServerSocket serverSocket = null;
    protected boolean      hasStopped   = false;
    protected int		   returnValue  = 0;

    public static void main(String [] args) throws InterruptedException {
    	ServerMultithreaded mineServer = new ServerMultithreaded(9999);
    	Thread main = new Thread(mineServer);
    	main.run();
    	main.join();
    	//main.call();
    }
    
    public ServerMultithreaded(int port){
        this.serverPort = port;
    }

    public int call() {
    	return returnValue;
    }
    public void run(){
    	/// Zrób testowy zapis
    	String toFile = "10;20,Qarmin;Phid;Mina" + "=";
    	toFile += "42;25,Qrak;Roman;Mina" + "=";
    	toFile += "25;26,Multum;Jacek;Mina" + "=";
    	toFile += "62;22,Zoraw;Krotos;Mina" + "=";
    	TopList.writeToFile(toFile);
    	
    	
    	
    	
    	///
    	
    	
    	
    	
    	
    	
    	
        System.out.println("Serwer został otwarty na porcie " + serverPort) ;
        openServerSocket();
        while(!hasStopped()){
            Socket clntSocket = null;
            try {
                clntSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(hasStopped()) {
                    System.out.println("Serwer został zatrzymany") ;
                    return;
                }
                System.out.println("Klient nie może zostać połączony");
                returnValue = 1;
                return;
            }
            new Thread(
                new ClientThread(
                    clntSocket, "Rozpoczęcie działania serwera")
            ).start();
        }
        System.out.println("Serwer został zatrzymany") ;
    }
    private synchronized boolean hasStopped() {
        return this.hasStopped;
    }
    public synchronized void stop(){
        this.hasStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Wystąpił błąd przy wyłączaniu servera", e);
        }
    }
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Nie można otworzyć połączenia na porcie 9999", e);
        }
    }

}