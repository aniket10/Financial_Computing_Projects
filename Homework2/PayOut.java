package Homework2;
/**
 * Interface for calculating the payout function
 * @author Aniket
 *
 */
public interface PayOut {
	/**
	 * Function implemented by the inheriting classes.
	 * @param path : Stock Path for one simulation
	 * @return : Option price based on the type of call option used for one simulation
	 */
	public double getPayout(StockPath path);
}