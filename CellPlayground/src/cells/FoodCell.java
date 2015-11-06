package cells;

import java.awt.Color;

public class FoodCell extends Cell {
	
	/**
	 * Initializes a new food cell at a random position.
	 */
	public FoodCell() {
		rx = Math.random();
		ry = Math.random();
		color = Color.orange;
		radius = 0.01;
	}
	
	/**
	 * Initializes a new food cell at a given position.
	 * @param rx given x position
	 * @param ry given y position
	 */
	public FoodCell(double rx, double ry) {
		this.rx = rx;
		this.ry = ry;
		color = Color.orange;
		radius = 0.01;
	}
}
