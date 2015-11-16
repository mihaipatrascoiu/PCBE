package cells;

import java.awt.Color;

import playground.Playground;

public class NonSexualCell extends LiveCell {
	
	/**
	 * Initializes a new, hungry cell with random position and velocity.
	 * Also, sets the corresponding color for the non-sexual cell type.
	 */
	public NonSexualCell(Playground playground) {
		super(playground);
		color = Color.green;
	}
	
	/**
	 * Initializes a new, hungry cell at a given position with random velocity and given radius.
	 * Also, sets the corresponding color for the non-sexual cell type.
	 */
	public NonSexualCell(Playground playground, double rx, double ry, double radius) {
		super(playground);
		color = Color.green;
		this.rx = rx;
		this.ry = ry;
		this.radius = radius;
	}
	
	/**
	 * The cell death scenario for a non sexual cell.
	 * The cell will be removed from the playground and its Thread stopped.
	 */
	@Override
	protected void cellDeath() {
		playground.removeCell(this);
		super.cellDeath();
	}
	
	/**
	 * Once the cell is ready to reproduce, it will split into two other cells, 
	 * with radius half of the original size.
	 */
	@Override
	protected void reproduce() {
		double px, py;
		
		// remove the cell
		playground.removeCell(this);
		
		for (int i = 0; i < 2; i++) {
			// search for a  position inside the board within this cell's radius
			do {
				px = (-1 + Math.random() * 2) * radius + rx;
				py = (-1 + Math.random() * 2) * radius + ry;
			} while ((px < 0 || px > 1) || (py < 0 || px > 1));
			
			// create the new live cell
			playground.addCell(new NonSexualCell(playground, px, py, radius / 2));
		}
		
		// stop the thread
		running = false;
	}
}
