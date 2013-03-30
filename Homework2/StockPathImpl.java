package Homework2;

import org.apache.commons.math3.util.Pair;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.joda.time.DateTime;
/**
 * Class implementation of the interface stockpath. 
 * Implements the getPrices method of the interface. 
 * @author Aniket
 *
 */
public class StockPathImpl implements StockPath {
	/**
	 * Data member for storing the random vector generated for each simulation
	 */
	double arr[]=new double[252];
	/**
	 * Parameterized Constructor. Stores the random vector generator for the simulation in the data member.
	 * @param tmp : Random vector generated for the simulation.
	 */
	StockPathImpl(double tmp[])
	{
		int i;
		for(i=0;i<252;i++)
		{
			arr[i]=tmp[i];
		}
	}
	
	/**
	 * Implementation of the getPrices. Orders the pairs based on the increasing order of the days.
	 * @return : List of pair of date and time of the stock price and the price, 
	 */
	
	public List<Pair<DateTime,Double>> getPrices()
	{
		int i;
		List <Pair<DateTime,Double>> S= new ArrayList<Pair<DateTime,Double>>();
		double prev, cur;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,2013);
		cal.set(Calendar.MONTH,4);
		cal.set(Calendar.DAY_OF_MONTH,10);
		cal.set(Calendar.HOUR_OF_DAY,12);
		cal.set(Calendar.MINUTE,0);
		
		
		DateTime d = new DateTime(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));	
		Pair <DateTime,Double> p = new Pair <DateTime,Double> (d,new Double(152.35));
		S.add(p);
		prev=152.65;
		
		for(i=1;i<252;i++)
		{
			cur=prev*Math.exp((0.0001-0.01*0.01/2)+0.01*arr[i-1]);
			
			if(cal.get(Calendar.DAY_OF_MONTH)==30)
			{
				cal.set(Calendar.DATE,1);
								
				if(cal.get(Calendar.MONTH)==11)
				{
					cal.set(Calendar.MONTH,0);
					cal.add(Calendar.YEAR,1);
				}
				else cal.add(Calendar.MONTH,1);
			}
			else cal.add(Calendar.DAY_OF_MONTH, 1);
			DateTime dl = new DateTime(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE),cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE));
			Pair <DateTime,Double> pl = new Pair <DateTime,Double> (dl,new Double(cur));
			S.add(pl);
			prev=cur;
//			System.out.println(cur+" "+cal.getTime());
		}
		return S;
	}
}
