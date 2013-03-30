package Homework1;
/**
 * Interface for Portfolio. A portfolio is a collection of position objects. It contains two functions newTrade and getNextIterator. New Trade function is used to update the potfolio collection for new trade. A new symbol is added if one doesnot exists or is updates if one exists. If the quantity becomes 0, then the symbol is removed from the portfolio collection. The function getPositionIter returns the iterator pointing to the first objects in the bag. 
 * @author Aniket
 *
 */
public interface Portfolio {
	/**
	 * Creates a new trade for a particular position object. It also updates the quantity of the symbol with the new quantity and removes the symbol from the portfolio if the quantity becomes 0. 
	 * @param symbol : Represents the symbol of the Position object
	 * @param quantity : Represents the number of shares of the corresponding symbol.
	 */
	public void newTrade(String symbol, int quantity);
	/**
	 * Used to return the pointer to the first object in the portfolio bag
	 * @return : First Position object in the portfolio. 
	 */
	public PositionIter getPositionIter();
}
