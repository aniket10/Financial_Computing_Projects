package Homework2;
/**
 * Decorator pattern implementation for the RandomVectorGenerator Class.
 * @author Aniket
 *
 */
public class RandomVectorGeneratorDecImpl implements RandomVectorGenerator {
	/**
	 * Data member used for storing the random vector generated
	 */
	double ranvec [] = new double[252];
	
	/**
	 * Parameterized constructor for storing the random vector generated. Also negates the random vector generated for the simulation 
	 * @param : Random vector generated for the previous simulation.
	 */
	RandomVectorGeneratorDecImpl(RandomVectorGenerator rvg)
	{
		
		int i;
		ranvec=rvg.getVector();
		for(i=0;i<252;i++)
		{
			ranvec[i]*=-1;
		}
	}
	/**
	 * Returns the random vector 
	 * @return : array of negated random values.
	 */
	public double[] getVector()
	{
		return ranvec;
	}
}
