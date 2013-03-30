package Homework1;
/**
 * This is the main class that drives the functionality.
 * @author Aniket
 *
 */
public class MainClass {
	public static void main(String args[])
	{
		/**
		 * Object of the type Position. 
		 */
		Position pos = new ImplPosition();
		/**
		 * Object of type Portfolio. 
		 */
		ImplPortfolio pf = new ImplPortfolio();
		/**
		 * Inserting values in the portfolio.
		 */
		pf.newTrade("IBM", 100);
		pf.newTrade("YHOO", 100);
		pf.newTrade("MSFT", -100);
		

		/**
		 * Iterating through the portfolio of objects.
		 */
		
		PositionIter pt=pf.getPositionIter();
		System.out.println("My Portfolio");
		while((pos=pt.getNextPosition())!=null)
		{
			System.out.println(pos.getSymbol()+ "  "+ pos.getQuantity());
		}
		
	}
}
