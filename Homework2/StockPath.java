package Homework2;

import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.joda.time.DateTime;

/**
 * Interface for creating StockPath. 
 * @author Aniket
 *
 */
public interface StockPath{
	/**
	 * Function for obtaining the stock path 
	 * @return : Returns the list of the prices for a stock path. The returned list is ordered by date
	 */
public List<Pair<DateTime,Double>> getPrices();
}