package Homework2;

import java.util.List;

import org.apache.commons.math3.util.Pair;
import org.joda.time.DateTime;
/**
 * This class implements the PayOut interface for European call option. 
 * @author Aniket
 *
 */
public class PayOutEuropean implements PayOut {
	/**
	 * Computes the pay out for each simulation using European call option
	 * @return : The option price for one simulation.
	 */
	public double getPayout(StockPath path)
	{
		double strike_price = 165;
		List <Pair<DateTime,Double>> l = path.getPrices();
		double val = l.get(251).getValue();
		double po = val-strike_price;
		return po>0?po:0;
	}
	/**
	 * Computes the pay out for a stock using European call option over a number of simulations.
	 * @return : Profit obtained s=using european call option
	 */
	
	public double doIterations()
	{
		double avg_european,mean=0,sigma=0,sigma_cap=0,d=0;
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
				
			avg_european = getPayout(sp);
			mean=(mean*count+avg_european)/(count+1);
			sigma=((sigma*count)+avg_european*avg_european)/(count+1);
			count++;			
			sigma_cap=Math.sqrt(Math.abs(sigma-mean*mean));
			d=2.0537*sigma_cap/Math.sqrt(count);
			if(d<0.01 && count%10000==0) break;
		}
		
		mean=mean*Math.exp(-0.0001*252);
		System.out.println(count);
		return mean;
	}
}
