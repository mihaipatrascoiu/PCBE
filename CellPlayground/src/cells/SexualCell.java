package cells;

import java.awt.Color;
import java.util.List;

import playground.Playground;

public class SexualCell extends LiveCell {
	private boolean readyStatus;
	
	/**
	 * Initializes a new, hungry cell with random position and velocity.
	 * Also, sets the corresponding color for the sexual cell type.
	 */
	public SexualCell(Playground playground) {
		super(playground);
		color = Color.red;
		readyStatus = false;
	}
	
	/**
	 * Initializes a new, hungry cell at a given position with random velocity.
	 * Also, sets the corresponding color for the sexual cell type.
	 */
	public SexualCell(Playground playground, double rx, double ry) {
		super(playground);
		color = Color.red;
		readyStatus = false;
		this.rx = rx;
		this.ry = ry;
	}
	
	/**
	 * Moves the cell based on its velocity for the specified amount of time.<br>
	 * Decrease the <tt>starveTime</tt> or <tt>fullTime</tt> timers.<br>
	 * Checks for intersection with food cells.
	 * If the cell is ready to reproduce, 
	 * checks for intersection with other cells of the same kind.
	 * @param dt amount of time passed
	 */
	@Override
	public void move(double dt) {
		super.move(dt);
		
		if (getReadyStatus() == true) {
			checkLiveCellCollisions();
		}
	}
	
	/**
	 * When the cell is set to reproduce, it will change its color. Once it finds another 
	 * sexual cell ready to reproduce, they will both revert to their initial color.
	 */
	@Override
	protected void reproduce() {
		color = Color.magenta;
		readyStatus = true;
	}
	
	/**
	 * Returns whether the cell is ready to reproduce or not.
	 * A cell is considered ready when it has fed 10 times.
	 */
	protected boolean getReadyStatus() {
		return readyStatus;
	}
	
	/**
	 * Resets the ready status of the cell.
	 */
	protected void resetReadyStatus() {
		readyStatus = false;
		color = Color.red;
		fedCounter = 0;
	}
	
	
	/**
	 * Checks collisions with other sexual cells. If both are ready to reproduce,
	 * a new sexual cell will be spawned.
	 */
	private void checkLiveCellCollisions() {
		List<LiveCell> liveCells = playground.getLiveCells();
		double px, py;
		
		for (int i = 0; i < liveCells.size(); i++) {
			// avoid reproducing with itself
			if (liveCells.get(i).equals(this)) {
				continue;
			}
			
			if (intersects(liveCells.get(i)) && liveCells.get(i).getReadyStatus()) {
				// find position inside the board within this cell's radius
				do {
					px = (-1 + Math.random() * 2) * radius + rx;
					py = (-1 + Math.random() * 2) * radius + ry;
				} while ((px < 0 || px > 1) || (py < 0 || px > 1));
				
				// create the new live cell
				playground.addLiveCell(new SexualCell(playground, px, py));
				
				// reset ready status
				resetReadyStatus();
				liveCells.get(i).resetReadyStatus();
			}
		}
	}
}
