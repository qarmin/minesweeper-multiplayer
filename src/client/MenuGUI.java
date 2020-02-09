package client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class MenuGUI implements ActionListener {

    private JButton startGame = new JButton("Rozpocznij grę");

    private JButton toplist = new JButton("Najlepsze wyniki");
    
    private JButton toplistJDBC = new JButton("Najlepsze wyniki(JDBC)");

    private JTextField login = new JTextField(10);
    
    private JLabel loginText = new JLabel("Login: ", SwingConstants.CENTER);

    private String toplistMenu = ""; // Lista wyników
    
    private String toplistMenuJDBC = ""; // Lista wyników

    public MenuGUI(JFrame menuFrame) {
    	
        JLabel backgroundLabel = new JLabel();

        login.setPreferredSize(new Dimension(100, 40));
        loginText.setPreferredSize(new Dimension(100, 40));
        startGame.setPreferredSize(new Dimension(200, 40));
        toplist.setPreferredSize(new Dimension(200, 40));

        startGame.addActionListener(this);
        toplist.addActionListener(this);

        backgroundLabel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 50, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundLabel.add(loginText, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        backgroundLabel.add(login, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        backgroundLabel.add(startGame, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        backgroundLabel.add(toplist, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        backgroundLabel.add(toplistJDBC, gbc);

        menuFrame.setContentPane(backgroundLabel);
        menuFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGame) {
        	if(!login.getText().trim().equals(""))
        	{
	            try {
					new GameGUI(login.getText().trim());
				} catch (IOException e1) { // Nie można połączyć się z serwerem
					e1.printStackTrace();
				}
            }
        	else
        		System.err.println("Login must be set");
        } else if (e.getSource() == toplist) { // Wyświetl tabelę z wynikami
        	downloadTableFromServer("");
            createTable("");
        } else if (e.getSource() == toplistJDBC) { // Wyświetl tabelę z wynikami
        	downloadTableFromServer("JDBC");
            createTable("JDBC");
        } 

    }
    
    public void downloadTableFromServer(String driver) {
    	String output = "";
    	
    	Socket openedSocket = null;
    	String host = "127.0.0.1";
    	int port = 9999;
    	PrintWriter out = null;
    	BufferedReader in = null;

		System.out.println("Łączenie z serwerem " + host + " na porcie " + port + ".");

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
    	
		if(driver.equals("JDBC"))
			out.println("TopListJDBC");
		else
			out.println("TopList");
		while(true) {
			try {
				output = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(output != null)
			{
				if(driver.equals("JDBC"))
					toplistMenuJDBC = output;
				else
					toplistMenu = output;
				break;
			}
				
		}
		
    	if(out != null) {
    		out.close();
    	}
    	if(in != null) {
			try {
				in.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	if(openedSocket != null) {
			try {
				openedSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

    public void createTable(String driver) { // Stwórz tabelę wyników
        JFrame tableFrame = new JFrame();
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setSize(500, 300);
        tableFrame.setTitle("Ranking");
        tableFrame.setLocationRelativeTo(null);

        String[] header = {"Numer", "Czas", "Zwycięzca", "Przegrany", "Typ"}; // TODO dodać tryb zwycięstwa 
        DefaultTableModel tableModel = new DefaultTableModel(header, 0);
        JTable table = new JTable(tableModel);
        if(driver != "JDBC") {
	        if (table.getRowCount() == 0) {
	        	String[] split = toplistMenuJDBC.split("=");
	            for (int i = 0; i < split.length; i++) {
	
	                Object[] data = {i, split[0], split[1], split[2], split[3]};
	                tableModel.addRow(data);
	            }
	        }
        }
        else {
	        if (table.getRowCount() == 0) {
	        	String[] split = toplistMenuJDBC.split("=");
	            for (int i = 0; i < split.length; i++) {
	                Object[] data = {i, split[0], split[1], split[2], split[3]};
	                tableModel.addRow(data);
	            }
	        }
        	
        }
        tableFrame.add(new JScrollPane(table));
        tableFrame.setVisible(true);
    }


}