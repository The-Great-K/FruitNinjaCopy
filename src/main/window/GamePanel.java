package main.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JPanel;

import main.Main;
import main.handlers.KeyHandler;
import main.screens.UI;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = -5621533987836333041L;

	// SCREEN SETTINGS
	public int screenWidth, screenHeight;

	int TPS = 60; // TICKS PER SECOND

	public KeyHandler keyH = new KeyHandler(); // KEY CONTROL

	public Thread gameThread; // CONTROLS TIME

	// GAME STATES
	public int gameState;
	public final int titleState = 0;

	// SCREENS
	public UI ui = new UI(this);

	public GamePanel() {
		setFullScreen();
		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // SETS SCREEN SIZE
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // BETTER PERFORMANCE, LOWER FPS, TURN OFF IF YOU WANT TO FLEX FPS
		this.addKeyListener(keyH); // LISTENS FOR KEY INPUT
		this.setFocusable(true); // FOCUSES ON KEY INPUT
	}

	public void setupGame() {
		gameState = titleState;
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() { // GAME LOOP
		while (gameThread != null) {
			// DELTA GAME LOOP
			double drawInterval = 1000000000 / TPS;
			double delta = 0;
			long lastTime = System.nanoTime();
			long timer = System.currentTimeMillis();
			long fps = 0;

			while (gameThread != null) {
				long currentTime = System.nanoTime();
				delta += (currentTime - lastTime) / drawInterval;
				lastTime = currentTime;

				while (delta >= 1) {
					update(); // UPDATE
					delta--;
				}
				if (gameThread != null) {
					repaint(); // RENDER
				}

				fps++;
				if (System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					System.out.println("FPS: " + fps);
					fps = 0;
				}
			}
		}

	}

	public void update() { // UPDATES
	}

	@Override
	public void paintComponent(Graphics g) { // RENDERS
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		if (gameState == titleState) {
			ui.render(g2);
		}

		g2.dispose(); // DELETES OLD IMAGE
	}

	public void setFullScreen() {
		GraphicsEnvironment gEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gDevice = gEnvironment.getDefaultScreenDevice();

		gDevice.setFullScreenWindow(Main.window);

		screenWidth = Main.window.getWidth();
		screenHeight = Main.window.getHeight();
	}

}
