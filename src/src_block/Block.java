package blocks;

import java.util.HashSet;
import java.util.Iterator;

import myTetris.Coordinate;
import myTetris.GameFrame;
import myTetris.GamePanel;

public class Block {
	protected int[][] coordinates = new int[4][2];
	public Block() {
		setVertical();
	}
	protected int pivotIndex = 2;
	protected  void setVertical() {
		for(int i=0;i<coordinates.length;i++) {
			coordinates[i][0] = 0;
			coordinates[i][1] = i;
		}
	}
	public int[][] getCoordinates(){
		return coordinates;
	}
	public int[][] rotate(int unit_size,int screen_width,HashSet<Coordinate> set,int screen_height) {
		//broken
		int [][] temp = new int[4][2];
		for(int i=0;i<4;i++) {
			temp[i][0] = coordinates[i][0];
			temp[i][1] = coordinates[i][1];
		}
		
		changeBy90(unit_size);
		for(int i=0;i<2;i++) {
			if(lessThanZero()) {
				increaseX(unit_size);
			} else if(greaterThanScreenWidth(screen_width)) {
				increaseX(-unit_size);
			}
		}

		if(collision(set) || !(inBorders(screen_width,screen_height))) {
			
			if(makeCollisionDisappear(set,screen_width,unit_size,screen_height)) {
				//do not forget to checkBorders
				boolean flag = false;
				for(int i=0;i<4;i++) {
					if(!inBorders(screen_width, screen_height)) {
						flag = true;
						break;
					}
				}
				if(!flag)
					return coordinates;
			}
			for(int i=0;i<4;i++) {
				coordinates[i][0] = temp[i][0];
				coordinates[i][1] = temp[i][1];
			}
			//coordinates = temp;
		}
		
		return coordinates;
	}
	private boolean greaterThanScreenWidth(int screen_width) {
		for(int i=0;i<4;i++) {
			if(coordinates[i][0] >= screen_width) {
				return true;
			}
		}
		return false;
	}
	private boolean lessThanZero() {
		for(int i=0;i<4;i++) {
			if(coordinates[i][0] < 0) {
				return true;
			}
		}
		return false;
	}
	protected  boolean makeCollisionDisappear(HashSet<Coordinate> set,int screen_width,int unit_size,int screen_height) {
		// 2 birim sola 2 birim saða kaydirip hepsini deniyor oldu mu diye
		
		for(int i=0;i<2;i++) {
			increaseX(-unit_size);
		}
		// += (-2*unitSize) sonra 4 birim saga goturup deniyoruz eger herhangi biri olursa bu olmustur
		for(int i=0;i<4;i++) {
			
			if(!collision(set) && (inBorders(screen_width,screen_height))) {
				return true;
			}
			increaseX(unit_size);
		}
		return false;
	}
	protected  void increaseX(int unit_size) {
		for(int i=0;i<4;i++) {
			coordinates[i][0] += unit_size;
		}
	}
	protected  void changeBy90(int unit_size) {
		int [][] temp = new int[4][2];
		for(int i=0;i<4;i++) {
			temp[i][0] = coordinates[i][0] - coordinates[pivotIndex][0];
			temp[i][1] = coordinates[i][1] - coordinates[pivotIndex][1];
		}
		for(int i=0;i<4;i++) {
			coordinates[i][0] = coordinates[pivotIndex][0]+ temp[i][1];
			coordinates[i][1] = coordinates[pivotIndex][1]+(-temp[i][0]);
		}
	}
	protected  boolean inBorders(int screen_width,int screen_height) {
		for(int i=0;i<coordinates.length;i++) {
			if(coordinates[i][0] >= screen_width || coordinates[i][0] <0 || coordinates[i][1] > screen_height) {
				return false;
			}
		}
		return true;
	}
	protected  boolean collision(HashSet<Coordinate> set) {
		for(int i=0;i<4;i++) {
			if(set.contains(new Coordinate(coordinates[i][0],coordinates[i][1]))) {
				return true;
			}
		}
		return false;
	}
	protected  boolean vertical() {
		return coordinates[0][0] == coordinates[1][0];
	}
	protected  void setHorizantal(int unit_size) {
		//int x = -1;
		//int y = 2;
		for(int i=0;i<coordinates.length;i++) {
			coordinates[i][0] = coordinates[i][0] + (2-i)*unit_size;
			coordinates[i][1] = coordinates[2][1];
			/*
			 from
			 -***
			 -***
			 -***
			 -***
			 
			 to
			 
			 ****
			 ****
			 ----
			 ****
			 */
		}
	}
}
