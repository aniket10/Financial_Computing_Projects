package Homework1;
/**
 * Interface for the iterator for the portfolio. It is used to iterate through all the items from the portfolio collection. The interface contains a method called getNextPosition. It returns the next position object in the portfolio.
 * @author Aniket
 *
 */
public interface PositionIter {
	/**
	 * Return the the next position object in the portfolio
	 * @return : Returns the next position 
	 */
	public Position getNextPosition();
}
