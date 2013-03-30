package Homework2;

import org.joda.time.DateTime;
import org.apache.commons.math3.util.Pair;
import java.util.Iterator;
import java.util.List;

/**
 * Class implements the PayOut interface for Asian Call option.
 * @author Aniket
 *
 */
public class PayOutAsian implements PayOut {
	/**
	 * Computes the pay out for each simulation for Asian call option.
	 * @return : The option price for one simulation
	 */
	public double getPayout(StockPath path)
	{
		List <Pair<DateTime,Double>> l = path.getPrices();
		Iterator <Pair<DateTime,Double>> it = l.iterator();
		double avg=0,sum=0;
		double strike_price=165;
				
		while(it.hasNext())
		{
			Pair <DateTime,Double> p = it.next();
			sum=sum+p.getValue();
		}
		avg=sum/252-strike_price;
		avg=avg>0?avg:0;
		return avg; 
		
	}
	
	/**
	 * Computes the pay out for a stock using Asian call option over a number of simulations.
	 * @return : Profit obtained s=using Asian call option
	 */
	public double doIterations()
	{
		double avg_asian,mean=0,sigma=0,sigma_cap=0;
		int count=0;
		RandomVectorGenerator rvg=null;
		while(true)
		{			
			if(count%2==0)
			{
				rvg = new RandomVectorGeneratorImpl();
			}
			else
			{
				rvg = new RandomVectorGeneratorDecImpl(rvg);
				
			}
			double arr[]=rvg.getVector();
			StockPath sp = new StockPathImpl(arr);
				
			avg_asian = getPayout(sp);
			mean=((mean*count)+avg_asian)/(count+1);
			sigma=((sigma*count)+avg_asian*avg_asian)/(count+1);
			count++;
			sigma_cap=Math.sqrt(Math.abs(sigma-mean*mean));
			double d=2.0537*sigma_cap/Math.sqrt(count);
			if(d<0.01 && count%10000==0) break; 
			
		}
		mean=mean*Math.exp(-0.0001*252);
		System.out.println(count);
		return mean;
		

	}
}
