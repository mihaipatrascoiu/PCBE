/****************************************************
 *  Project created as first assignment for the 
 *  Concurrent and Event-Based Programming class.
 *  
 *  Name of the team: NullProjectException
 *  Members
 *  -------
 *    -  Mihai Patrascoiu
 *    -  Sandru Adrian
 *    -  Rus Alexandru
 *    -  Zamfirache Iuliu
 ****************************************************/
 
package playground;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cells.Cell;
import cells.FoodCell;
import cells.NonSexualCell;
import cells.SexualCell;
import edu.princeton.cs.algs4.StdDraw;

public class Playground {
	public static final double dt = (double) 1000 / 60;			// refresh rate of 60 Hz
	public static final double unit = (double) 1 / 60 / 10;		// fundamental unit to traverse canvas in 1 second
	
	public static final int CANVAS_SIZE_X = 800;
	public static final int CANVAS_SIZE_Y = 800;
	
	Lock drawableCellsLock = new ReentrantLock();
	Lock foodCellsLock = new ReentrantLock();
	Lock sexualCellsLock = new ReentrantLock();
	Lock nonSexualCellsLock = new ReentrantLock();

	private long startTime = 0;
	private List<Cell> drawableCells;
	private List<FoodCell> foodCells;
	private List<SexualCell> sexualCells;
	private List<NonSexualCell> nonSexualCells;
	
	/**
	 * Creates a new playground.
	 * @param numberFoodCells number of food cells to initialize
	 * @param numberCells number of alive cells to initialize
	 */
	public Playground(int numberFoodCells, int numberCells) {
		Random random = new Random();
		drawableCells = new LinkedList<Cell>();
		
		// create the food cells
		foodCells = new LinkedList<FoodCell>();
		for (int i = 0; i < numberFoodCells; i++) {
			this.addCell(new FoodCell());
		}

		// create the live cells
		sexualCells = new LinkedList<SexualCell>();
		nonSexualCells = new LinkedList<NonSexualCell>();
		for (int i = 0; i < numberCells; i++) {
			if (random.nextBoolean() == true) {
				this.addCell(new SexualCell(this));				
			} else {
				this.addCell(new NonSexualCell(this));
			}
		}
	}
	
	/**
	 * Draws all the cells on the canvas.
	 */
	private void redraw() {
		// set up to avoid concurrency problems
		StdDraw.clear();
		drawableCellsLock.lock();
		drawableCells.clear();
		
		foodCellsLock.lock();
		drawableCells.addAll(foodCells);
		foodCellsLock.unlock();
		
		sexualCellsLock.lock();
		drawableCells.addAll(sexualCells);
		sexualCellsLock.unlock();
		
		nonSexualCellsLock.lock();
		drawableCells.addAll(nonSexualCells);
		nonSexualCellsLock.unlock();
		
		// draw all cells
		for (Cell cell: drawableCells) {
			cell.draw();
		}
		drawableCellsLock.unlock();
		
		// draw time elapsed
		double timeElapsed = System.currentTimeMillis() - startTime;
		StdDraw.setPenColor(Color.black);
		StdDraw.text(0.95, 0.95, String.format("%ss", (timeElapsed / 1000)));
		
		StdDraw.show(20);
	}
	
	/**
	 * Starts the thread execution of each cell inside the playground.
	 * Eve <tt>dt</tt> units of time, the playground is redrawn. 
	 */
	public void run() {
		startTime = System.currentTimeMillis();
		
		// start thread execution of cells
		for (SexualCell sexualCell: sexualCells) {
			new Thread(sexualCell).start();
		}
		
		for (NonSexualCell nonSexualCell: nonSexualCells) {
			new Thread(nonSexualCell).start();
		}
		
		// draw the cells on the canvas
		while (true) {
			redraw();
		}
	}
	
	/* Methods for adding cells to the playground. */
	
	public synchronized void addCell(FoodCell foodCell) {
		foodCellsLock.lock();
		foodCells.add(foodCell);
		foodCellsLock.unlock();
		}
	
	public synchronized void addCell(SexualCell sexualCell){
		sexualCellsLock.lock();
		sexualCells.add(sexualCell);
		new Thread(sexualCell).start();
		sexualCellsLock.unlock();
	}
	
	public synchronized void addCell(NonSexualCell nonSexualCell){
		nonSexualCellsLock.lock();
		nonSexualCells.add(nonSexualCell);
		new Thread(nonSexualCell).start();
		nonSexualCellsLock.unlock();
	}
	
	/* Methods for removing cells from the playground. */
	
	public synchronized void removeCell(FoodCell cell) {
		foodCellsLock.lock();
		foodCells.remove(cell);
		foodCellsLock.unlock();
	}
	
	public synchronized void removeCell(SexualCell cell) {
		sexualCellsLock.lock();
		sexualCells.remove(cell);
		sexualCellsLock.unlock();
	}
	
	public synchronized void removeCell(NonSexualCell cell) {
		nonSexualCellsLock.lock();
		nonSexualCells.remove(cell);
		nonSexualCellsLock.unlock();
	}
	
	/**
	 * Returns the list of food cells.
	 * @return food cells list
	 */
	public synchronized List<FoodCell> getFoodCells() {
		return new LinkedList<FoodCell>(foodCells);
	}
	
	/**
	 * Returns the list of sexual cells.
	 * @return sexual cells list
	 */
	public synchronized List<SexualCell> getSexualCells(){
		return new LinkedList<SexualCell>(sexualCells);
	}
	
	/**
	 * Returns the list of non sexual cells.
	 * @return non sexual cells list
	 */
	public synchronized List<NonSexualCell> getNonSexualCells(){
		return new LinkedList<NonSexualCell>(nonSexualCells);
	}
	
	public static void main(String[] args) {
		try {
			int foodCells = 200;		// default food cells value
			int liveCells = 25;			// default live cells value
			
			// check input
			if (args.length == 2) {
				foodCells = Integer.parseInt(args[0]);
				liveCells = Integer.parseInt(args[1]);
			}
			
			// set up the canvas
			StdDraw.setCanvasSize(CANVAS_SIZE_X, CANVAS_SIZE_Y);
			// turn on animation mode
			StdDraw.show(0);
			
			// create the playground and start the simulation
			Playground playground = new Playground(foodCells, liveCells);
			playground.run();
		} catch (Throwable ex) {
			System.err.println("Uncaught exception - " + ex.getMessage());
	        ex.printStackTrace();
		}
	}
}
