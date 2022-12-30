package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; //how big the objects in the game 
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; //how many objects can be fitted to the screen
	static final int DELAY =75;
	//hold all the cordinations of the snake by 2 arrys
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;//initial amount of body parts
	int applesEaten;
	int appleX; //x coordinate where the apple is located
	int appleY;
	char direction = 'R'; //when initializing the game snake will move to right
	boolean running = false;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.gray);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this); //DELAY will dictate how fast the game is running, this->bcz we use action listener interface
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		
		if(running) {
			//in order to see things easier we can turn this into grid -optional
			/*for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); //starting coordinated and ending coordinates
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}*/
			g.setColor(Color.green);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0;i<bodyParts;i++) {  //Drawing the snake
				if(i==0) {
					g.setColor(Color.black);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					if(i%2 == 0) {
						g.setColor(new Color(129,86,20));
						g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
					if(i%2 == 1) {
						g.setColor(new Color(159,109,33));
						g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
					}
					
					
				}
			}
			
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD,40));
			FontMetrics metrics = getFontMetrics(g.getFont()); //to center the text
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; //randomly display apple ->x coordinate in range of screen width in display on one of the unit
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		for(int i = bodyParts; i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U': 
			y[0] = y[0] - UNIT_SIZE; //go to next position
			break;
		case 'D': 
			y[0] = y[0] + UNIT_SIZE; //go to next position
			break;
		case 'R': 
			x[0] = x[0] + UNIT_SIZE; //go to next position
			break;
		case 'L': 
			x[0] = x[0] - UNIT_SIZE; //go to next position
			break;
		
		}
	}

	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkCollissions() {
		
		//checks if head  collides with body
		for(int i = bodyParts;i > 0 ;i--) {
			if((x[0] == x[i]) && (y[0]== y[i])) {
				running = false;
			}
		}
		
		//check if head touches left borders
		if(x[0] < 0) {
			running = false;
		}
		
		//check if head touches right borders
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		//check if head touches lower borders
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		//check if head touches upper borders
		if(y[0] < 0) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
		
	}

	public void gameOver(Graphics g) {
		//Game over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics = getFontMetrics(g.getFont()); //to center the text
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
		//score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics2 = getFontMetrics(g.getFont()); //to center the text
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollissions();
		}
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) { //FOR ARROW KEYS
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;

			}
		}
	}

}
