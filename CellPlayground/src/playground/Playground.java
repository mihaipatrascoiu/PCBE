package playground;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cells.Cell;
import cells.FoodCell;
import cells.LiveCell;
import cells.NonSexualCell;
import cells.SexualCell;
import edu.princeton.cs.algs4.StdDraw;

public class Playground extends Thread {
	public static final double dt = (double) 1000 / 60;			// refresh rate of 60 Hz
	public static final double unit = (double) 1 / 60 / 10;		// fundamental unit to traverse canvas in 1 second
	
	public static final int CANVAS_SIZE_X = 800;
	public static final int CANVAS_SIZE_Y = 800;
	
	private List<Cell> foodCells;
	private List<LiveCell> liveCells;
	
	/**
	 * Creates a new playground.
	 * @param numberFoodCells number of food cells to initialize
	 * @param numberCells number of alive cells to initialize
	 */
	public Playground(int numberFoodCells, int numberCells) {
		Random random = new Random();
		
		// create the food cells
		List<Cell> _foodCells = new LinkedList<Cell>();
		for (int i = 0; i < numberFoodCells; i++) {
			_foodCells.add(new FoodCell());
		}
		foodCells = Collections.synchronizedList(_foodCells);

		// create the live cells
		List<LiveCell> _liveCells = new LinkedList<LiveCell>();
		for (int i = 0; i < numberCells; i++) {
			_liveCells.add((random.nextBoolean() == true)	?	new SexualCell(this)	:	new NonSexualCell(this));
		}
		liveCells = Collections.synchronizedList(_liveCells);
	}
	
	/**cd
	 * Draws all the food and alive cells on the canvas.
	 */
	private void redraw() {
		StdDraw.clear();
		
		// draw food cells
		for (int i = 0; i < foodCells.size(); i++) {
			foodCells.get(i).draw();
		}
		
		// draw live cells
		for (int i = 0; i < liveCells.size(); i++) {
			liveCells.get(i).draw();
		}
		
		StdDraw.show(20);
	}
	
	/**
	 * Method that simulates the movement of cells.
	 * Each <tt>dt</tt> units of time, a movement is made. 
	 */
	public void run() {
	
		try {
			while (true) {
				// move all live cells
				for (int i = liveCells.size() - 1; i >= 0; i--) { 
					liveCells.get(i).move(dt);
				}
				
				// draw the cells on the canvas
				redraw();
				// sleep for dt units of time
				Playground.sleep((long) dt);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes a given live cell from the playground.
	 * @param cell the given live cell
	 */
	public void removeLiveCell(LiveCell liveCell) {
		liveCells.remove(liveCell);
	}
	
	/**
	 * Adds a given live cell to the playground.
	 * @param liveCell the given live cell
	 */
	public void addLiveCell(LiveCell liveCell) {
		liveCells.add(liveCell);
	}
	
	/**
	 * Adds a given food cell to the playground.
	 * @param foodCell the given food cell
	 */
	public void addFoodCell(FoodCell foodCell) {
		foodCells.add(foodCell);
	}
	
	public List<LiveCell> getLiveCells() {
		return liveCells;
	}
	
	public List<Cell> getFoodCells() {
		return foodCells;
	}
	
	public static void main(String[] args) {
		// set up the canvas
		StdDraw.setCanvasSize(CANVAS_SIZE_X, CANVAS_SIZE_Y);
		// turn on animation mode
		StdDraw.show(0);
		
		// create the playground and start simulation
		Playground playground = new Playground(100, 5);
		playground.run();
	}
}
