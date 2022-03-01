package blocks;



public class SBlock extends Block{
	public SBlock () {
		setVertical();
	}
	@Override
	protected void setVertical() {
		coordinates[0][0] = 0;
		coordinates[0][1] = 1;
		

		coordinates[1][0] = 0;
		coordinates[1][1] = 0;

		coordinates[2][0] = 1;
		coordinates[2][1] = 0;

		coordinates[3][0] = 1;
		coordinates[3][1] = -1;
	}
	/*@Override
	public int[][] rotate(int unit_size,int screen_width,HashSet<Coordinate> set){
		return coordinates;
	}*/
}
