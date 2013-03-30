package Homework2;
/**
 * Main class that drives the functionality. 
 * @author Aniket
 *
 */
public class MainClass {
	public static void main(String args[])
	{
		PayOutAsian pa = new PayOutAsian();
		double mean=pa.doIterations();
		System.out.println("Payout using Asian Call Option is "+mean);
		
		PayOutEuropean pe = new PayOutEuropean();
		mean=pe.doIterations();
		System.out.println("Payout using European Call Option is "+mean);
	}
}
