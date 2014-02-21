package orderGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Class is an exact replica of the runner class, except that it doesnot print the intermediate results.
 * 
 * The class represents the continuous input accepting function of the exchange. It accepts the order provided 
 * by the order iterator and processes it. Function classifies the order as either NewOrder or Cancel and 
 * replace order depending upon the input order. It maintains the collection of all the distinct tickers that
 * are active in the exchange and maps them to their corresponding books. The class also maintains the 
 * mapping of the order id and the Order object for updating the node in case of cancel/replace order in O(1) 
 * time.
 * @author Aniket
 *
 */

public class SilentRunner 
{
	HashMap<String,Book> coll = new HashMap<String,Book>();		//Maintain a book for each ticker on the exchange
	HashMap<String,DNode<Message>>OrderMap = new HashMap<String,DNode<Message>>();	//Mapping for orderid and the Order
	
	/**
	 * Function iterates through the input to accept the input, build the collection and its corresponding books
	 * and then perform trade. The function makes use of the iterator provided by the OrdersIterator class to iterate
	 * through the input to build up the collection of tickers for the exchange. The function also handles the 
	 * NewOrder by creating a new entry in the corresponding book. Function handles the cancel and replace orders
	 * by deleting the existing entry and creating a new one. Function also prints the top of the book by
	 * calling the printAllBooks function. Trade is executed by calling the executeBook function. 
	 *  
	 */
	
	public void iterate()
	{
		Iterator<Message> it=OrdersIterator.getIterator();		//Iterator for the input stream of elements
		int count=0;
		Message  m;

		while(it.hasNext())		//Obtaining each order
		{
			m=it.next();
			if(	m instanceof NewOrder)		//Check to determine new order
			{
				NewOrder no=(NewOrder)m;		
				String sym=no.getSymbol();		//Obtaining the symbol for that order
				String orderId=no.getOrderId();		//Obtaining the order id
				Book b=null;
				if(coll.containsKey(sym))		//Checking if the symbol exists in the exchange
				{
					b=coll.get(sym);			//Obtaining the book for that symbol		
				}
				else				//Creating a new entry in the exchange for that symbol 
				{
					b=new Book();
					coll.put(sym, b);
				}
				DNode<Message>p=b.trade(m, false);		//Checking to see if trade is possible for the current entry
				if(p!=null)						//if Current order is not completely traded and added to the list
					OrderMap.put(orderId, p);		//Adding an order entry in the map
			}
			else if(m instanceof OrderCxR)		//Check to see if the current order is a cancel and replace order
			{
				OrderCxR cxr=(OrderCxR)m;
				String orderId=cxr.getOrderId();		//Obtaining the order id
				double lp=cxr.getLimitPrice();			//Obtaining the limit price
				
				if(OrderMap.containsKey(orderId))		//Checking for a valid and an untraded order 
				{
					DNode<Message> p =OrderMap.get(orderId);  //Getting the message object	
					NewOrder no=(NewOrder)p.data;			//Extracting the new order object
					String sym = no.getSymbol();		//Obtaining the symbol of the order
					Book b=coll.get(sym);			//Obtaining the book for that symbol
					
					boolean c=p.remove();		//Deleting the node from the list
					OrderMap.remove(orderId);	//Deleting its entry in the map
					Message temp=new OrdersIterator.OrderImpl(sym,cxr.getSize(),orderId,lp); 	//Creating a new order for the updated order 
					
					if(!c)		//If old order at the head of the list
					{
						double remPrice=no.getLimitPrice();		//Extracting the price
						int size=no.getSize();		//Obtain the size
						b.removeFirst(remPrice, size);		//Remove from the head of the list
						p=b.trade(temp, false);		//Trade the new order if possible					
					}
					else
					{	
						if(Double.isNaN(cxr.getLimitPrice()))		//If the updated order is a market order, then trade the order
						{
							p=b.trade(temp, false);
						}						
						else							//Otherwise insert in the book
							p=b.InsertBook(temp);
					}
					if(p!=null)			//Inserting in the order map, if the input is not completely traded
					{
						OrderMap.put(orderId, p);
					}
				
				}
			}
			else
			{
				System.out.println("Invalid Operation");
				System.exit(0);
			}
		}
	}
	
	/**
	 * Function used to print the final state of the book at the end of all the iterations. All the orders that
	 * have remained unexecuted will be displayed by the function. The function iterates across all the 
	 * tickers in the exchange. 
	 */
	
	void printStateOfTheBook()
	{
		Set eb = coll.entrySet();		//Obtaining the iterator
		Iterator it=eb.iterator();
		
		while(it.hasNext())			//Iterating through all the tickers
		{
			Map.Entry me=(Map.Entry)it.next();
			Book b=(Book)me.getValue();
			System.out.println("|----STATE OF THE BOOK FOR "+me.getKey()+"----|");
			b.printEntireBook();			
		}	
	}
	
	/**
	 * Main class that drives the functionality
	 * @param args
	 */

	public static void main (String[] args)
	{
		SilentRunner r=new SilentRunner();
		r.iterate();
		System.out.println("\n\n");
		r.printStateOfTheBook();
	}

}
