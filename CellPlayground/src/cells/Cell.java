package cells;

import java.awt.Color;

import edu.princeton.cs.algs4.StdDraw;

public abstract class Cell {
	
	protected double rx, ry;		// position
	protected double radius;		// radius
	protected Color color;			// color
	
	/**
	 * Draws the cell on the canvas.
	 */
	public void draw() {
		StdDraw.setPenColor(color);
		StdDraw.filledCircle(rx, ry, radius);
	}
	
	/**
	 * Determines whether this cell intersects the given one. It does this by
	 * calculating the euclidean distance between the two cells.
	 * @param cell the given cell
	 * @return true if they intersect, false otherwise
	 */
	public boolean intersects(Cell cell) {
		// euclidean distance between two points: sqrt( (x1 - x2)^2 + (y1 - y2)^2 )
		double distance = Math.sqrt(Math.pow(rx - cell.getRX(), 2) + Math.pow(ry - cell.getRY(), 2));
		
		return distance <= (radius + cell.getRadius());
	}
	
	public double getRX() {
		return rx;
	}
	
	public double getRY() {
		return ry;
	}
	
	public double getRadius() {
		return radius;
	}
}
