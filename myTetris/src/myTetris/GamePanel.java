package myTetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.text.html.HTMLDocument.Iterator;

import blocks.*;

public class GamePanel extends JPanel implements ActionListener{
	private int score = 0;
	private static int whenItGoesDown = 0;
	private static final int LEVEL = 3;
	private static final int SCREEN_WIDTH = 275;
	private static final int SCREEN_HEIGHT= 500;
	private static final int UNIT_SIZE = 25;
	private Random random;
	private Color comingColor;
	private boolean running;
	private static final int DELAY = 50;
	private Block block = null;
	private int[][] coordinatesOfBlock;
	private enum Directions{Left,Right,Fast,Rotate,Normal};
	private Directions direction;
	//private HashSet<ArrayList<ArrayList<Integer>>> blocks = new HashSet<ArrayList<ArrayList<Integer>>>();
	private HashSet<Coordinate> blocks = new HashSet<Coordinate>();
	Timer timer;
	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	private void startGame() {
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	private void destroyLine(int y) {
		ArrayList<Coordinate> blocksShouldBeRemoved = new ArrayList<Coordinate>();
		ArrayList<Coordinate> blocksWhosePositionShouldBeUpdated = new ArrayList<Coordinate>(); 
		for(Coordinate c : blocks) {
			if(c.getY() == y) {
				blocksShouldBeRemoved.add(c);
			} 
		}
		for(int i=0;i<blocksShouldBeRemoved.size();i++) {
			blocks.remove(blocksShouldBeRemoved.get(i));
		}
		/*
		for(Coordinate c: blocks) {
			System.out.println(c.getX() + " " + c.getY() + " Y:" + y);
			if(c.getY() < y) {
				//blocks.remove(c); 
				System.out.println("YES");
				blocksWhosePositionShouldBeUpdated.add(c);	      
				//c.increment(UNIT_SIZE);
				//blocks.add(c);
			}
		}
		for(Coordinate c:blocksWhosePositionShouldBeUpdated) {
			blocks.remove(c);// when we increment the y value , hashcode changes, so when we try to reach it we can not
			c.increment(UNIT_SIZE);
			blocks.add(c);// we should remove it and put it again to the hashset 
		}*/
		
		
		
	}
	private void deleteLine(int y) {
		ArrayList<Coordinate> deletedBlocks = new ArrayList<Coordinate>();
		for(Coordinate c:blocks) {
			if(c.getY() == y) {
				deletedBlocks.add(c);
			}
		}
		for(Coordinate c:deletedBlocks) {
			blocks.remove(c);
		}
	}
	private void makeLinesFall(int topLine ,int numberOfUnits) {
		ArrayList<Coordinate> fallingLines = new ArrayList<Coordinate>();
		for(Coordinate c:blocks) {
			if(c.getY() < topLine) {
				fallingLines.add(c);
			}
		}
		for(Coordinate c: fallingLines) {
			blocks.remove(c);
			c.increment(numberOfUnits * UNIT_SIZE);
		}
		
		for(Coordinate c: fallingLines) {
			blocks.add(c);
		}
	}
	private void destroyFilledLines() {
		//precondition : last block does not move no longer
		ArrayList<Integer> linesShouldBeDestroyed = new ArrayList<Integer>();
		ArrayList<Integer> readedY =  new ArrayList<Integer>();
		for(int i=0;i<coordinatesOfBlock.length;i++) {
			boolean filled = true;
			if(readedY.contains(coordinatesOfBlock[i][1])) {
				continue;
			}
			else {
				readedY.add(coordinatesOfBlock[i][1]);
			}
			for(int j=0;j<SCREEN_WIDTH/UNIT_SIZE;j++) {
				if(!blocks.contains(new Coordinate(j*UNIT_SIZE,coordinatesOfBlock[i][1],null))) {
					filled = false;
					break;
				}
			}
			if(filled == true) {
				//destroyLine(coordinatesOfBlock[i][1]);
				linesShouldBeDestroyed.add(coordinatesOfBlock[i][1]);
				//System.out.println(coordinatesOfBlock[i][1]);
				score+=10;
			}
			
			filled = true;
		}
		int topLine = SCREEN_HEIGHT + UNIT_SIZE; // y coordinate which is not in the screen
		for(int line:linesShouldBeDestroyed) {
			if(line<topLine) {
				topLine = line;
			}
			deleteLine(line);
			//destroyedLines.add(line);
			//showDestroyedLine(line);
		}
		makeLinesFall(topLine,linesShouldBeDestroyed.size());
	}
	//private ArrayList<Integer> destroyedLines = new ArrayList<Integer>();
	private void showDestroyedLine(int line,Graphics graphic) {
		for(int i=0;i<3;i++) {
			graphic.setColor(new Color(255,0,0));
			graphic.drawRect(0, line, SCREEN_WIDTH, UNIT_SIZE);
			try
			{
			    Thread.sleep(1000);
			}
			catch(InterruptedException ex)
			{
			    Thread.currentThread().interrupt();
			}
			graphic.setColor(new Color(0,255,0));
			graphic.drawRect(0, line, SCREEN_WIDTH, UNIT_SIZE);
			try
			{
			    Thread.sleep(1000);
			}
			catch(InterruptedException ex)
			{
			    Thread.currentThread().interrupt();
			}
			
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(coordinatesOfBlock == null)
			return;
		if(running) {
			
			move();
			if(collision()) {
				//ArrayList<Coordinate>arr = new ArrayList<Coordinate>();
				for(int i=0;i<coordinatesOfBlock.length;i++) {
					/*ArrayList<Integer> coordinate = new ArrayList<Integer>();
					coordinate.add(coordinatesOfBlock[i][0]);
					coordinate.add(coordinatesOfBlock[i][1]);
					arr.add(coordinate);*/
					blocks.add(new Coordinate(coordinatesOfBlock[i][0], coordinatesOfBlock[i][1],comingColor));
					if(coordinatesOfBlock[i][1] == 0) {
					//	System.out.println("Game Over");
						running = false;
						return;
					}
				}
				
				//blocks.add(arr);
				destroyFilledLines();
				newBlock();
				
			}
		}
		repaint();
	}
	
	private void move() {
	//	System.out.println("aaaa");
		whenItGoesDown++;
		if(whenItGoesDown == LEVEL) {
			boolean collision = false;
			for(int i=0;i<coordinatesOfBlock.length;i++) {
				
				coordinatesOfBlock[i][1] += UNIT_SIZE;
				if(blocks.contains(new Coordinate(coordinatesOfBlock[i][0],coordinatesOfBlock[i][1]))) {
					collision = true;
				}
			}
			
			whenItGoesDown = 0;
			if(collision == true) {
				for(int i=0;i<coordinatesOfBlock.length;i++) {
					coordinatesOfBlock[i][1] -= UNIT_SIZE;
				}
				return;
			}
		
			
		}
		if(direction == Directions.Rotate ) {
			// rotate will be handled
			int [][] temp = new int[4][2];
			for(int i=0;i<4;i++) {
				temp[i][0] = coordinatesOfBlock[i][0];
				temp[i][1] = coordinatesOfBlock[i][1];
			}
			coordinatesOfBlock= block.rotate(UNIT_SIZE,SCREEN_WIDTH,blocks,SCREEN_HEIGHT);
			boolean thereIsCollision = checkCollisions();
			if(thereIsCollision) {
				for(int i=0;i<4;i++) {
					coordinatesOfBlock[i][0] = temp[i][0];
					coordinatesOfBlock[i][1] = temp[i][1];
				}
			}
			for(int i=0;i<4;i++) {
				System.out.println(coordinatesOfBlock[i][0] + ","+ coordinatesOfBlock[i][1]);
			}
		} else {
			changeCoordinates(direction);
		}
		direction = Directions.Normal;
	}
	private boolean checkCollisions() {
		for(int i=0;i<4;i++) {
			int x = coordinatesOfBlock[i][0];
			int y = coordinatesOfBlock[i][1];
			if(blocks.contains(new Coordinate(x, y)) 
					||
				x>=SCREEN_WIDTH
					||
				x<0
					||
				y>SCREEN_HEIGHT	
					) {
				System.out.println("Abbb");
				return true;
			}
		}
		return false;
	}
	private void changeCoordinates(Directions direction) {
		if(direction == Directions.Left) {
			for(int i=0;i<coordinatesOfBlock.length;i++) {
				coordinatesOfBlock[i][0] -= UNIT_SIZE;
			}
		} else if(direction == Directions.Right) {
			for(int i=0;i<coordinatesOfBlock.length;i++) {
				coordinatesOfBlock[i][0]+=UNIT_SIZE;
			}
		} else if(direction == Directions.Fast) {
			
			boolean collision = false;
			for(int i=0;i<coordinatesOfBlock.length;i++) {
				
				coordinatesOfBlock[i][1] += UNIT_SIZE;
				if(coordinatesOfBlock[i][1] >= SCREEN_HEIGHT) {
					collision = true;
				}
				if(blocks.contains(new Coordinate(coordinatesOfBlock[i][0],coordinatesOfBlock[i][1],null))) {
					collision = true;
				}
			}
			if(collision == true) {
				for(int i=0;i<coordinatesOfBlock.length;i++) {
					coordinatesOfBlock[i][1] -= UNIT_SIZE;
				}
				
			}
		} else {
			
		}
	}
	//private Graphics graphic;
	private void draw(Graphics g) {
		if(!running) {
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+score, (SCREEN_WIDTH - metrics.stringWidth("Score: "+score))/2, g.getFont().getSize());
			return;
		}
		
		//graphic = g;
		//Iterator<ArrayList<ArrayList<Integer>>> iter = blocks.iterator();
		for(Coordinate coordinate: blocks) {
			g.setColor(coordinate.getColor());
			g.fillRect(coordinate.getX(), coordinate.getY(), UNIT_SIZE, UNIT_SIZE);
		}
		if(block == null)
			newBlock();
		for(int i=0;i<coordinatesOfBlock.length;i++) {
			/*int tempX =middleX + (coordinatesOfBlock[i][0] * UNIT_SIZE);
			int tempY =(coordinatesOfBlock[i][1] * UNIT_SIZE);
			
		*/
			g.setColor(comingColor);
			g.fillRect(coordinatesOfBlock[i][0], coordinatesOfBlock[i][1], UNIT_SIZE, UNIT_SIZE);
		}
		g.setColor(Color.black);
		
		for(int i=0;i<SCREEN_WIDTH/UNIT_SIZE;i++) {
			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
		}
		for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
			g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
		}
		/*g.setColor(Color.green);
		g.fillRect(UNIT_SIZE * 5, UNIT_SIZE*2, 50, 50);*/
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 20));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: "+score, (SCREEN_WIDTH - metrics.stringWidth("Score: "+score))/2, g.getFont().getSize());
	
		
	}
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			if(!timer.isRunning()) {
				timer.start();
			}
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT: {
					if(!leftAligned()) {
						//System.out.println("LEFT");
						direction = Directions.Left;
					} else {
						direction = Directions.Normal;
					}
					break;
				}
				case KeyEvent.VK_RIGHT :{
					if(!rightAligned()) {
						//System.out.println("Right");
						direction = Directions.Right;
					} else {
						direction = Directions.Normal;
					}
					break;
				}
				case KeyEvent.VK_DOWN :{
					if(!collision()) {
						direction = Directions.Fast;
					} else {
						direction = Directions.Normal;
					}
					break;
				} 
				case KeyEvent.VK_UP :{
					block.rotate(UNIT_SIZE,SCREEN_WIDTH,blocks,SCREEN_HEIGHT);
					//coordinatesOfBlock = block.getCoordinates();
					//setCoordinatesInTable();
					break;
				}
				case KeyEvent.VK_SPACE:{
				
					
					timer.stop();
					direction = Directions.Normal;
				}
				
		}
	}
	}
	private boolean collision() {
		//when it goes down u degıstır
		for(int i=0;i<coordinatesOfBlock.length;i++) { 
			if(coordinatesOfBlock[i][1] == SCREEN_HEIGHT - UNIT_SIZE || (blocks.contains(new Coordinate(coordinatesOfBlock[i][0], coordinatesOfBlock[i][1]+UNIT_SIZE)) && whenItGoesDown == LEVEL-1)) {
				return true;
			}
		}
		return false;
	}
	private boolean leftAligned() {
		for(int i=0;i<coordinatesOfBlock.length;i++) {
			if(coordinatesOfBlock[i][0]-UNIT_SIZE <0 
					|| blocks.contains(new Coordinate(coordinatesOfBlock[i][0]-UNIT_SIZE, coordinatesOfBlock[i][1]))
					|| (blocks.contains(new Coordinate(coordinatesOfBlock[i][0]-UNIT_SIZE, coordinatesOfBlock[i][1]+UNIT_SIZE)) && whenItGoesDown == LEVEL-1) ) {
				
				return true;
			}
		}
		return false;
	}
	private boolean rightAligned() {
		for(int i=0;i<coordinatesOfBlock.length;i++) {
			if(coordinatesOfBlock[i][0] == SCREEN_WIDTH-UNIT_SIZE 
					|| blocks.contains(new Coordinate(coordinatesOfBlock[i][0]+UNIT_SIZE, coordinatesOfBlock[i][1]))
					|| (blocks.contains(new Coordinate(coordinatesOfBlock[i][0]+UNIT_SIZE, coordinatesOfBlock[i][1]+UNIT_SIZE)) && whenItGoesDown == LEVEL-1) ) {
				return true;
			}
		}
		return false;
	}
	
	private void setCoordinatesInTable() {
		int middleX = (SCREEN_WIDTH / UNIT_SIZE) / 2 *(UNIT_SIZE) ;
		for(int i=0;i<coordinatesOfBlock.length;i++) {
			coordinatesOfBlock[i][0] =middleX + (coordinatesOfBlock[i][0] * UNIT_SIZE);
			coordinatesOfBlock[i][1] =(coordinatesOfBlock[i][1] * UNIT_SIZE);
		}
	}
	private void newBlock() {
		int colorChooser = random.nextInt(3);
		comingColor = new Color(random.nextInt(155)+((colorChooser % 3 == 0) ? 100 : 0),random.nextInt(155)+((colorChooser % 3 == 1) ? 100 : 0),random.nextInt(155)+((colorChooser % 3 == 2) ? 100 : 0));
		int i = random.nextInt(100);
		int numberOfShapes = 7;
		if(i % numberOfShapes == 0)
			block = new Block();
		else if(i%numberOfShapes == 1) {
			block = new SquareBlock();
		} else if(i%numberOfShapes == 2){
			block = new SBlock();
		} else if(i%numberOfShapes == 3){
			block = new SBlock2();
		}else if(i%numberOfShapes == 4){
			block = new LBlock();
		}
		else if(i%numberOfShapes == 5){
			block = new LBlock2();
		} else {
			block = new TBlock();
		}
		direction = Directions.Normal;
		coordinatesOfBlock = block.getCoordinates();
		setCoordinatesInTable();
		
	}
}
