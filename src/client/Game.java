package client;

import javax.swing.*;


public class Game {
	
    public static void main(String args[]) {

        JFrame gameFrame = new JFrame("Minesweeper Multiplayer");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setSize(1280/2, 720/2);
        gameFrame.setLocationRelativeTo(null);

        new MenuGUI(gameFrame);
    }

}
