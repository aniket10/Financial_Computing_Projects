package Homework2;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
/**
 * Class Implementation of the RandomVectorGenerator interface 
 * @author Aniket
 *
 */
public class RandomVectorGeneratorImpl implements RandomVectorGenerator {
	
	double arr[]=new double[252];
	/**
	 * Default Constructor for the class. It also creates a random vector.
	 */
	RandomVectorGeneratorImpl()
	{
		RandomGenerator rg = new JDKRandomGenerator();
		GaussianRandomGenerator grg = new GaussianRandomGenerator(rg);
		int i;
		
		for(i=0;i<252;i++)
		{
			arr[i]=grg.nextNormalizedDouble();
		}
		
	}
	/**
	 * Implementation of the getVector. 
	 * @return : Vector of Random numbers
	 */
	public double[] getVector()
	{
		return arr;
	}
}
