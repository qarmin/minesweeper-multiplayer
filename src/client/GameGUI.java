package client;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GameGUI implements WindowListener {
	
	static GameGUI gameGUI;
	
	public int width = 445;
	
	public int height = 410;

    private JFrame gameFrame = new JFrame("Saper - Klient");

    JPanel gamePanel = new JPanel();

    //private JLabel info = new JLabel();
    
    private Panel minePanel = new Panel();
    
    private JLabel myTimerLabel = new JLabel();
    
    private JLabel enemyTimerLabel = new JLabel();
   
    private JButton saper = new JButton();
    
    Socket clientSocket;
        
    Block[][] mineButtons;
    
    Client client;
    
    String login;

    public GameGUI(String login) throws UnknownHostException, IOException {
    	
    	this.login = login;
    	
    	gameGUI = this;
    
		client = new Client(this);
    }
    
    public void setupGUI() {
    	mineButtons = new Block[width][height];
    	
        gameFrame.setSize(600,600);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.addWindowListener(this);
        
        Border pandingBorder = BorderFactory.createEmptyBorder(5,5,5,5);//留下空白
        

        Icon saperIcon = new ImageIcon("img/" + Block.imageResolution + "/Saper.png");
        saper.setIcon(saperIcon);
        saper.setPreferredSize(new Dimension(40,40));
        saper.setMaximumSize(new Dimension(40,40));
        saper.setMinimumSize(new Dimension(40,40));
        saper.setHorizontalAlignment(JButton.CENTER);

        GridLayout gridLayout=new GridLayout(1,3);
        gamePanel.setLayout(gridLayout);

        Dimension controlPanelSize = new Dimension(0,50);
        gamePanel.setPreferredSize(controlPanelSize);
        gamePanel.setMaximumSize(new Dimension(1000,50));
        
        gamePanel.add(myTimerLabel);
        gamePanel.add(saper);
        gamePanel.add(enemyTimerLabel);

        //info.setText("Waiting for connection");
        //info.setBackground(Color.blue);
        
        minePanel.setLayout(new GridLayout(width,height));
        
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
            	mineButtons[i][j] = new Block(i,j,gameGUI);
            	//mineButtons[i][j].setClicksEnabled(false);
            	
            	minePanel.add(mineButtons[i][j]);
				//mineButtons[i][j].addMouseListener(this);
            }
        }
        
        gameFrame.setLayout(new BoxLayout(gameFrame.getContentPane(),BoxLayout.Y_AXIS));
        
        gameFrame.add(gamePanel);
        //gameFrame.add(info);
        gameFrame.add(minePanel);
        
        gameFrame.setVisible(true);
    }

    public void UpdateButtons(String Buttons){
    	//System.out.println("Jestem");
    	String[] ButtonValues = Buttons.split(",");
        for(int i=0;i<ButtonValues.length;i++){
        	ButtonValues[i] = ButtonValues[i].trim();
        }
        //System.out.println(Buttons);
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
            	mineButtons[j][i].UpdateButtons(Integer.parseInt(ButtonValues[i*height + j]));
            }
        }
    }
    public void SetEnableButtons(boolean enable){
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
            	mineButtons[j][i].setClicksEnabled(enable);
            }
        }
    }

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		try {
			client.closeIt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
