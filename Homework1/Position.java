package Homework1;
/**
 * Interface for position. Position describes the holding. Position describes what instrument the user holds. The position object has two data members: Symbol and quantity. Symbol represents the ticker for the company and the quantity represents the number of shares present in the portfolio.
 * @author Aniket
 *
 */
public interface Position {
	/**
	 * Abstract method. Returns the number of shares of the corresponding company.
	 * @return number of shares.
	 */
	public int getQuantity();
	/**
	 * Abstract Method. Returns the Ticker Symbol for the company present in the user's portfolio.
	 * @return ticker for the stock.
	 */
	public String getSymbol();
	
}
