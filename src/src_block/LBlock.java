package blocks;

public class LBlock extends Block {
	public  LBlock() {
		setVertical();
	}
	@Override
	protected void setVertical() {
		coordinates[0][0] = 0;
		coordinates[0][1] = 1;
		

		coordinates[1][0] = 0;
		coordinates[1][1] = 0;

		coordinates[2][0] = 0;
		coordinates[2][1] = -1;

		coordinates[3][0] = 1;
		coordinates[3][1] = -1;
	}
}
