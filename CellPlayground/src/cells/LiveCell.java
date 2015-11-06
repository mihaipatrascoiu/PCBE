package cells;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import edu.princeton.cs.algs4.StdDraw;
import playground.Playground;

public abstract class LiveCell extends Cell {
	public static final double TIME_FULL = 1000;
	public static final double TIME_TO_STARVE = 5000;
	protected Playground playground;
	
	protected double vx, vy;			// velocity
	protected double fullTime;			// time duration of cell not being hungry
	protected double starveTime;		// time duration from starving until cell dies
	protected boolean hungry;			// shows if cell is hungry or not
	protected int fedCounter;			// number of times the cell has fed
	
	/**
	 * Initializes a new, hungry cell with random position and velocity.
	 */
	public LiveCell(Playground playground) {
		this.playground = playground;
		
		// position, velocity and radius
		rx = Math.random();		// position between [0, 1)
		ry = Math.random();
		vx = velocity();
		vy = velocity();
		radius = 0.015;
		
		// hungry flag, time to starve and fed counter
		hungry = true;
		starveTime = LiveCell.TIME_TO_STARVE;
		fedCounter = 0;
	}
	
	/**
	 * Moves the cell based on its velocity for the specified amount of time.<br>
	 * Decrease the <tt>starveTime</tt> or <tt>fullTime</tt> timers.<br>
	 * Checks for intersection with food cells.
	 * @param dt amount of time passed
	 */
	public void move(double dt) {
		// update position
		rx += vx * dt;
		ry += vy * dt;
		
		// checks collisions with exterior walls
		checkExteriorWallCollisions();
		
		// check collisions with food cells if the cell is hungry
		if (hungry == true) {
			checkFoodCellCollisions();
		}
		
		// decrease the timers
		handleTimers(dt);
	}
	
	/**
	 * Draws the cell on the canvas.
	 * If the cell is hungry, display an outline of the cell.
	 */
	@Override
	public void draw() {
		// draw outline if cell is hungry
		if (hungry == true) {
			StdDraw.setPenColor(Color.lightGray);
			StdDraw.filledCircle(rx, ry, radius + 0.005);
		}
		
		super.draw();
	}
	
	/**
	 * Handles the reproduction scenario of the live cell.
	 */
	protected abstract void reproduce();
	
	/**
	 * Returns whether the cell is ready to reproduce or not.
	 */
	protected abstract boolean getReadyStatus();
	
	/**
	 * Resets the ready status of the cell.
	 */
	protected abstract void resetReadyStatus();
	
	/**
	 * Handles cell collisions with exterior walls.
	 */
	private void checkExteriorWallCollisions() {
		if (rx - radius < 0) {		// exceeded left wall
			rx = radius;
			vx = -vx;
		} else if (rx >= 1) {		// exceeded right wall
			rx = 1 - radius;
			vx = -vx;
		}
		
		if (ry - radius < 0) {		// exceeded lower wall
			ry = radius;
			vy = -vy;
		} else if (ry >= 1) {		// exceeded upper wall
			ry = 1 - radius;
			vy = -vy;
		}
	}
	
	/**
	 * Checks for collisions between the alive cell and food cells.<br> 
	 * If one or more collisions are found, those food cells will be eaten.<br>
	 * With each cell eaten, the live cell grows in size and becomes fed.
	 * This function should be called only when the alive cell is hungry.
	 */
	private void checkFoodCellCollisions() {
		List<Cell> foodCells = playground.getFoodCells();
		
		for (int i = foodCells.size() - 1; i >= 0; i--) {
			if (intersects(foodCells.get(i))) {
				foodCells.remove(i);		// remove the eaten food cell
				
				radius = Math.min(0.065, radius + 0.002);			// increase the cell's size
				fedCounter++;										// increase the cell's fed counter
				if (fedCounter == 10) {
					reproduce();
				}
				
				hungry = false;					// the cell becomes fed
				fullTime = TIME_FULL;			// reset timers
				starveTime = TIME_TO_STARVE;
			}
		}
	}
	
	/**
	 * Handles the decrease of timers and triggers the corresponding actions, if necessary.
	 * @param dt amount of time passed
	 */
	private void handleTimers(double dt) {
		if (hungry == true) {
			starveTime -= dt;
			
			if (starveTime <= 0) {
				cellDeath();
			}
		} else {
			fullTime -= dt;
			
			if (fullTime <= 0) {
				hungry = true;
			}
		}
	}
	
	/**
	 * Handle the death of a cell in case its starve timer reached zero.
	 * @param cell the cell whose starve time expired
	 */
	private void cellDeath() {
		double px, py;
		Random random = new Random();
		
		// remove the cell
		playground.removeLiveCell(this);
		
		// create between 1 - 5 new food cells in a 5*radius distance
		int n = random.nextInt(5) + 1;
		for (int i = 0; i < n; i++) {
			// search a new position inside the board
			do {
				px = (-5 + random.nextDouble() * 10) * radius + rx;
				py = (-5 + random.nextDouble() * 10) * radius + ry;
			} while ((px < 0 || px > 1) || (py < 0 || px > 1));
			
			// create the new food cell
			playground.addFoodCell(new FoodCell(px, py));
		}
	}
	
	/**
	 * Random velocity of the cell between [-0.3, 0.3) times the fundamental unit.
	 * @return velocity of the cell
	 */
	private double velocity() {
		// formula for value in [a, b): a + random() * (b - a)
		return (-0.3 + Math.random() * 0.6) * Playground.unit;
	}
}
