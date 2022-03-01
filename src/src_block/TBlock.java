package blocks;

import java.util.HashSet;

import myTetris.Coordinate;

public class TBlock extends Block{
	public TBlock() {
		coordinates[0][0] = 0;
		coordinates[0][1] = -1;
		
		coordinates[1][0] = 1;
		coordinates[1][1] = 0;
		
		coordinates[2][0] = 0;
		coordinates[2][1] = 0;

		

		coordinates[3][0] = -1;
		coordinates[3][1] = 0;
	}
	@Override
	protected  boolean makeCollisionDisappear(HashSet<Coordinate> set,int screen_width,int unit_size,int screen_height) {
		// 2 birim sola 2 birim saga kaydirip hepsini deniyor oldu mu diye
		
		for(int i=0;i<1;i++) {
			increaseX(-unit_size);
		}
		// += (-2*unitSize) sonra 4 birim saga goturup deniyoruz eger herhangi biri olursa bu olmustur
		for(int i=0;i<3;i++) {
			
			if(!collision(set) && (inBorders(screen_width,screen_height))) {
				return true;
			}
			increaseX(unit_size);
		}
		return false;
	}
}
