package blocks;

import java.util.HashSet;

import myTetris.Coordinate;

public class SquareBlock extends Block{
	public SquareBlock() {
		//for(int i=0;i<2;i++) {
		coordinates[0][0] = 0;
		coordinates[0][1] = 0;

		coordinates[1][0] = 1;
		coordinates[1][1] = 0;
		

		coordinates[2][0] = 0;
		coordinates[2][1] = 1;
		

		coordinates[3][0] = 1;
		coordinates[3][1] = 1;
		
		//}
	}
	@Override
	public int[][] rotate(int unit_size,int screen_width,HashSet<Coordinate> set,int screen_height){
		return coordinates;
	}
}
