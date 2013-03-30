package Homework1;
import java.util.HashMap;

/**
 * Class implementation of the interface Portfolio.
 * @author Aniket
 *
 */
public class ImplPortfolio implements Portfolio{
	/**
	 * Implementation of the portfolio. HashMap with the symbol as the key and the position object as the value.
	 */
	HashMap <String,ImplPosition> data = new HashMap<String,ImplPosition>();

	/**Creates, updates and removes the position object for every trade.
	 * @param symbol : Symbol of the position object
	 * @param quantity : Number of shares. 
	 */
	public void newTrade(String symbol, int quantity)
	{
		//Test for border cases. If the quantity is 0 or the symbol is null or empty string.
		if(quantity==0 || symbol==null || symbol.trim().length()==0)	
			return;
			
		//Checking for the presense of the symbol in the portfolio.
		if(data.containsKey(symbol))
		{
			int v=data.get(symbol).getQuantity();
			quantity=v+quantity;					//If found, updating the quantity
		}
		if(quantity==0)								//Remove the symbol if the quantity becomes 0.
			data.remove(symbol);				
		else data.put(symbol,new ImplPosition(symbol,quantity));	//Insert a new symbol or update the existing symbol.
	}
	/**
	 * Returns the pointer to the first object in the portfolio bag.
	 * @return : First position object from the bag.
	 */
	public PositionIter getPositionIter()
	{
		PositionIter pt = new ImplPositionIter(this);
		return pt;
	}
	/**
	 * The function checks the number of entries in the portfolio bag. 
	 * @return : count of entries in the bag.
	 */
	int isEmpty()
	{
		return data.size();
	}
}
