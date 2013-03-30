package Homework1;
/**
 * Class implementation of the interface Position. 
 * @author Aniket
 *
 */
public class ImplPosition implements Position{
	/**
	 * Symbol of the Share trade
	 */
	String name;
	/**
	 * Number of shares the symbol
	 */
	int quantity;
	/**
	 * Default Constructor. 
	 */
	ImplPosition() {}
	/**
	 * Parameterized Constructor for initializing the position object.
	 * @param n : name of the symbol	
	 * @param q : Number of shares
	 */
	ImplPosition(String n, int q)
	{
		name=n;
		quantity=q;
	}
	/**
	 * Returns the quantity of the Company shares from the portfolio.
	 */
	public int getQuantity()
	{
		return quantity;
	}
	/**
	 * Returns the symbol the company from the portfolio.
	 */
	public String getSymbol()
	{
		return name;
	}
}
