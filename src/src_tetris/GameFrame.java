package myTetris;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	public GameFrame() {
		this.add(new GamePanel());
		this.setTitle("Tetris");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null); // ekranin ortasinda
	}
}
