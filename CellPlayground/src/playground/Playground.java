package playground;

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
			if(random.nextBoolean() == true){
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
		drawableCells.clear();
		drawableCells.addAll(foodCells);
		drawableCells.addAll(sexualCells);
		drawableCells.addAll(nonSexualCells);
		
		// draw all cells
		for (Cell cell: drawableCells) {
			cell.draw();
		}
		
		StdDraw.show(20);
	}
	
	/**
	 * Starts the thread execution of each cell inside the playground.
	 * Eve <tt>dt</tt> units of time, the playground is redrawn. 
	 */
	public void run() {
	
		for(int i = 0; i < sexualCells.size(); i++) {
			new Thread(sexualCells.get(i)).start();
		}
		
		for(int i = 0; i < nonSexualCells.size(); i++) {
			new Thread(nonSexualCells.get(i)).start();
		}
		
		try {
			while (true) {
				// draw the cells on the canvas
				redraw();
				// sleep for dt units of time
				Playground.sleep((long) dt);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/* Methods for adding cells to the playground. */
	
	public synchronized void addCell(FoodCell foodCell) {
		foodCells.add(foodCell);
	}
	
	public synchronized void addCell(SexualCell sexualCell){
		sexualCells.add(sexualCell);
		new Thread(sexualCell).start();
	}
	
	public synchronized void addCell(NonSexualCell nonSexualCell){
		nonSexualCells.add(nonSexualCell);
		new Thread(nonSexualCell).start();
	}
	
	/* Methods for removing cells from the playground. */
	
	public synchronized void removeCell(FoodCell cell) {
		foodCells.remove(cell);
	}
	
	public synchronized void removeCell(LiveCell cell) {
		if (cell instanceof SexualCell) {
			sexualCells.remove(cell);
		} else if (cell instanceof NonSexualCell) {
			nonSexualCells.remove(cell);
		}
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
		int foodCells = 100;		// default food cells value
		int liveCells = 10;			// default live cells value
		
		// check input
		if (args.length == 2) {
			foodCells = Integer.parseInt(args[0]);
			liveCells = Integer.parseInt(args[1]);
		}
		
		// set up the canvas
		StdDraw.setCanvasSize(CANVAS_SIZE_X, CANVAS_SIZE_Y);
		// turn on animation mode
		StdDraw.show(0);
		
		// create the playground and start simulation
		Playground playground = new Playground(foodCells, liveCells);
		playground.run();
	}
}
