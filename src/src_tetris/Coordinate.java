package myTetris;

import java.awt.Color;

public class Coordinate{
	private Color color;
	private int x;
	private int y;
	void increment(int unit_size) {
		y+=unit_size;
	}
	public Coordinate(int _x,int _y,Color c){
		x = _x;
		y = _y;
		color = c;
	}
	public Coordinate(int _x,int _y){
		this(_x,_y,null);
	}
	public Color getColor() {
		return color;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	@Override
	public boolean equals(Object o) {
		Coordinate other = (Coordinate) o;
		if(other.getX() == x && other.getY() == y) {
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return (x*1000) + y;
	}
}