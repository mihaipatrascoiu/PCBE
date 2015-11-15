package cells;

public interface ReadyStatusInterface {
	
	/**
	 * Returns whether the cell is ready to reproduce or not.
	 */
	abstract boolean getReadyStatus();
	
	/**
	 * Resets the ready status of the cell.
	 */
	abstract void resetReadyStatus();
	
}
