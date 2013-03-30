package Homework1;
import java.util.Iterator;
import java.util.Map;


/**
 * Implementation of the interface PositionIter. The class inherits one function, getNextPosition from the interface. The class also defines two constructors. The default constructor does not perform any operation. The parameterized constructor takes one parameter of type ImplPortfolio.It also has a data member of type iterator specialized for ImplPosition.       
 * @author Aniket
 *
 */
public class ImplPositionIter extends ImplPortfolio implements PositionIter  {
	/**
	 * Iterator for the Portfolio class. 
	 */
	Iterator <Map.Entry<String,ImplPosition>> it;
	
	/**
	 * Default Constructor 
	 */
	ImplPositionIter() {}
	/**
	 * Parameterized Constructor.
	 * @param pf : Object of type ImplPortfolio
	 */
	ImplPositionIter(ImplPortfolio pf)
	{
		it= pf.data.entrySet().iterator();		//Initializing the iterator.
	}
	/**
	 * Returns the next position object from the portfolio and returns null if none exists or is the end of the list.
	 * @return : Next position object from the bag.  
	 */
	
	public Position getNextPosition()
	{
		if(it.hasNext())
		{	
			Position p = new ImplPosition ();
			p=it.next().getValue();
			return p;
		}
		return null;
	}
}
