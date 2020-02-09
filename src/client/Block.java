package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Block extends JButton implements MouseListener {

	public static String imageResolution = "32";
	
	private static final long serialVersionUID = 210760075974214695L;

	private int fX;

	private int fY;

	private int currentContent;
	private boolean haveFlag;

	public GameGUI gameGUI;

	public final int HIDDEN = -100;

	public final int MINE = -1;

	public boolean clicksEnabled = true; // Stosowany, gdy inny gracz ma turę/ruch

	public Block(int x, int y, GameGUI gameGUI) {
		fX = x;
		fY = y;
		this.gameGUI = gameGUI;

		haveFlag = false;
		currentContent = -100;
		setBackground(Color.decode("#cccccc"));// Color.DARK_GRAY);
		addMouseListener(this);
	}

	public void UpdateButtons(int value) {
		// System.out.print(value + " ");
		if (currentContent != value) {
			currentContent = value;
			if (currentContent >= 1 && currentContent <= 8) {
				setBackground(Color.decode("#777777"));
				setIcon(new ImageIcon("img/" + imageResolution + "/" + currentContent + ".png"));
			} else if (currentContent == MINE) {
				setBackground(Color.decode("#777777"));
				setIcon(new ImageIcon("img/" + imageResolution + "/Mina.png"));
			} else if (currentContent == 0) {
				setBackground(Color.decode("#777777"));
			} else {
				System.out.println(value);
			}
		}
	}

	boolean isDisabledField() {
		return currentContent != HIDDEN;
	}

	public void setClicksEnabled(boolean clicksEnabled) {
		this.clicksEnabled = clicksEnabled;
		setEnabled(clicksEnabled);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (clicksEnabled) {

			if (!isDisabledField()) {
				if (e.getButton() == MouseEvent.BUTTON1) { // left click
					if (getIcon() == null) {
						gameGUI.client.clickedBlock(fX, fY);
					}
				}
				if (e.getButton() == MouseEvent.BUTTON3) { // right click
					if (haveFlag)
						setIcon(null);
					else
						setIcon(new ImageIcon("img/" + imageResolution + "/Flaga.png"));
					haveFlag = !haveFlag;
				}
			}
		}
	}

}
