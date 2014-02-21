package orderGenerator;

import orderGenerator.DoubleLinkedList;
import java.util.*;
import java.util.Map.Entry;

/**
 * Class implementation of book. There is one book maintained for each of the Symbol/Ticker in the exchange. The book consists of two parts.
 * -bid book and the ask book. The bid book maintains the all the buy orders for the symbol and the ask book maintains the sell orders for that 
 * symbol. the bid and the ask books are arranged to keep different priority queues for each distinct price quoted in the order. The priority 
 * queues are implemented in the form a double linked list. This allows deletion of the node in O(1) time with the help of the logic written in 
 * Runner and silentRunner classes. Double Linked List is defined in the DoubleLinkedList class. It also provides functions to perform trade of 
 * orders associated with the book.
 * 
 * 
 * @author Aniket
 *
 */
public class Book {

	SortedMap <Double, DoubleLinkedList<Message>> bid = new TreeMap<Double,DoubleLinkedList<Message>>(java.util.Collections.reverseOrder());  //Maintaining the list of bid orders
	SortedMap <Double, DoubleLinkedList<Message>> ask = new TreeMap<Double,DoubleLinkedList<Message>>();		//Maintaining a list of ask orders
	
	/**InsertBook function is responsible for inserting the new order in the book. The order is classified as ask and bid depending on the size of the 
	 * order. If the size of the order is negative, then it is considered as Ask while if it is positive, it is considered as Bid. The orders are added
	 * in the appropriate queues. If the size is 0, then it is considered a cancel order and removed from the list. Function returns the node of newly
	 * inserted function to the caller
	 * 
	 * @param m : Represents the message/order that is to be inserted in the book.
	 * @return : Returns the node of the order that is added.
	 */
	
	DNode<Message> InsertBook(Message m)
	{
		NewOrder no=(NewOrder)m;
		int size=no.getSize();
		Double lp=new Double(no.getLimitPrice());
		String orderId=no.getOrderId();

		if(size==0)		//Size=0 represents a cancel order. 
		{
			return null;
		}

		if(size>0)		//Positive size represents a bid order
		{
			DoubleLinkedList<Message> q;
			if(bid.containsKey(lp))			//Check for the presence of queue at that price point
			{
				q=bid.get(lp);				//Obtain the specified queue
			}
			else							//Create a new list for the specified price
			{
				q=new DoubleLinkedList<Message>();	
			}
			DNode<Message> p=q.add(m);		//If present, insert the node at the end		
			
			bid.put(lp, q);					//Add the updated list to the book
			return p;
				
		}
		else					//Negative size represents a ask order
		{
			DoubleLinkedList<Message> q;		
			if(ask.containsKey(lp))		//Check for the presence of queue at that price point
			{
				q=ask.get(lp);			//Obtain the specified queue
			}
			else						//Create a new list for the specified price
			{
				q=new DoubleLinkedList<Message>();
			}
			DNode<Message> p=q.add(m);		//If present, insert the node at the end
			
			ask.put(lp, q);				//Add the updated list to the book

			return p;			//Returns the node from the list.
		}
		
	}
	
	/**
	 * Function prints the Top of the book - both bid and ask books. Only one entry that represents the top of book is printed. This entry represents the highest
	 * limit price order in case of buy and the least order in terms of ask. 
	 * the book is printed. 
	 */
	
	void printTopOfBook()
	{
		if(bid.size()!=0)					//Bid Book should not be empty
		{
			Double key=bid.firstKey();		//Consider the first key. Since it is arranged in descending order, first key will represent the maximum limit price
			DoubleLinkedList<Message> l=bid.get(key);	//Obtain the Corresponding list for that limit price
			DNode<Message> it=l.iterator();			//Iterator for the double linked list
	
			System.out.println("|-----BID BOOK-----|");
			if(it!=null)				//Till the end of the list.
			{	
				Message m=(Message)it.getNode();		//Obtaining the Message object stored in the node.
				NewOrder no=(NewOrder)m;
				
				System.out.println("| "+no.getOrderId()+" "+((no.getLimitPrice()==Double.MAX_VALUE)?Double.NaN:no.getLimitPrice())+" "+no.getSize()+" |");
			}
			
		
		}
		if(ask.size()!=0)			//Ask Book should not be empty
		{
			Double key=ask.firstKey();		//Consider the first entry. Since the list is arranged in the increasing order, forst key will be the minimum limit price
			DoubleLinkedList<Message> l=ask.get(key);	//Obtain the corresponding list for the limit price
			DNode<Message> it=l.iterator();		//Iterator for the double linked list.
		
			System.out.println("|-----ASK BOOK-----|");
			if(it!=null)		//Iterate till the end of the list.
			{
				Message m=(Message)it.getNode();
				NewOrder no=(NewOrder)m;
				
				System.out.println("| "+no.getOrderId()+" "+((no.getLimitPrice()==-1)?Double.NaN:no.getLimitPrice())+" "+no.getSize()+" |");
			}
				
		}
	}
	
	/**
	 * Function used to clean up the books in case of remove. It is called after each iteration. After completing
	 * an order, if there is no other order in the book for that pricelimit, then that list is eliminated.
	 */
	
	void cleanUpBook()
	{
		Set bidSet=bid.entrySet();			//Obtain an iterator in the bid book
		Iterator bid_it = bidSet.iterator();
		DoubleLinkedList<Message> ask_list=null,bid_list=null;
		
		while(bid_it.hasNext())			//For each price limit
		{
			Map.Entry me=(Map.Entry) bid_it.next();		
			bid_list = (DoubleLinkedList)me.getValue();		//Obtain the double linked list for that price limit
			if(bid_list.size()==0)		//If the size of the list is 0, indicates that the list is empty and should be removed from the map.
			{
				bid_it.remove();
			}			
		}
		
		Set askSet=ask.entrySet();			//Obtain an iterator in the ask book
		Iterator ask_it = askSet.iterator();
		
		while(ask_it.hasNext())			//For each price limit
		{
			Map.Entry me=(Map.Entry) ask_it.next();
			ask_list = (DoubleLinkedList)me.getValue();		//Obtain the double linked list for that price limit

			if(ask_list.size()==0) 		//If the size of the list is 0, indicates that the list is empty and should be removed from the map.
			{
				ask_it.remove();
			}			
		}		
	}
	
	/**
	 * The function is responsible for performing the trade of the order if it is a bid order. It checks if the current order is a market order. If so, it 
	 * executes the order assuming that there is enough size of orders available to trade. If it is a limit order then, then it trades only if the limit price  
	 * of the arriving order is greater than or equal to the limit price of the order on the top of the book. If the newly arriving order is completely traded 
	 * then, the function returns null. But of the part or the whole of the order cannot be traded, then the function creates an entry in the corresponding
	 * list and returns node to the caller.   
	 * @param caller : Represents the source of the call. If caller =true indicates that the Runner has called the trade 
	 * 					method. If flag=false, then SilentRunner is the caller.
	 * @return : Node of the newly created entry else null.
	 */

	DNode<Message> tradeBid(Message m,boolean caller)
	{
		NewOrder no=(NewOrder)m;		
		int size=no.getSize();		//Obtain the size of the current order
		Double lp=no.getLimitPrice();	//Obtain the limit price of the order
		Set set=null;
		Iterator it=null;
		
		set=ask.entrySet();			//Since this a bid order, consider only the ask book.
		it = set.iterator();		
		int diff=no.getSize();		//Variable used to maintain the amount of more shares that can be traded from this order
		
Outer:	while(it.hasNext())		//Iterate through the entire ask book
		{
			Map.Entry me = (Entry) it.next();		
			double listprice=((Double) me.getKey()).doubleValue();		//Obtain the price limit for set of orders in the corresponding list
			DoubleLinkedList dl=(DoubleLinkedList) me.getValue();		//Obtain the linked list.
			
			DNode dn=dl.iterator();				//Obtain the iterator for the list.
			
			while(dn!=null)					//Iterating through all the orders of the same price level
			{
				Message cur=(Message) dn.data;		//Obtain the message object
				NewOrder current= (NewOrder) cur;	
							
				if(Double.isNaN(no.getLimitPrice()))		//In case of market order
				{
					diff=Math.abs(current.getSize())-Math.abs(diff);	//Obtaining the amount of order that will be traded in this transaction
					
					if(diff==0)		//Both, the first element of the ask list and the input order have exactly equal size.   
					{
						dl.removeTop();		//Delete the node at the head of the list.
						if(caller)
							System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());		//Display result
						
						return null;		
					}
					else if(diff>0)		//The order at the top of the list has greater size than the current order
					{
						dl.removeTop();		//Updating the top of list to represent the order currently traded
						Message temp=new OrdersIterator.OrderImpl(current.getSymbol(),-1*diff,current.getOrderId(),current.getLimitPrice());
						dl.addBeg(temp);
						if(caller)
							System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
						return null;
					}
					else if(diff<0)		//Current order has larger size than the order at the top of the list. The next order can also be traded partially/completely 
					{
						dl.removeTop();		//Updating the node at the top of the list
						if(dl.size()==0)
						{
							bid.remove(listprice);
							break;
						}
						if(caller)
							System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
						dn=dl.iterator();		//Repeating the same process
						
					}
				}
				else if(lp>=listprice)		//For Limit Order 
				{
					// limit order
					diff=Math.abs(current.getSize())-Math.abs(diff);	//Obtain the difference in the size of the current order and the order at the top of list
					{
						if(diff==0)			//Both, the first element of the ask list and the input order have exactly equal size.	
						{
							dl.removeTop();		//Delete the node at the head of the list.
							if(caller)
								System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
							return null;
						}
						else if(diff>0)		//The order at the top of the list has greater size than the current order
						{
							dl.removeTop();		//Updating the top of list to represent the order currently traded
							Message temp=new OrdersIterator.OrderImpl(current.getSymbol(),-1*diff,current.getOrderId(),current.getLimitPrice());
							dl.addBeg(temp);
							if(caller)
								System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
							return null;
						}
						else if(diff<0)		//Current order has larger size than the order at the top of the list. The next order can also be traded partially/completely
						{
							dl.removeTop();		//Updating the node at the top of the list
							if(dl.size()==0)
							{
								bid.remove(listprice);
								break;
							}
							if(caller)
								System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
							dn=dl.iterator();		//Repeating the same process
						}
					}
				}
				else break Outer; 	//Neither of the two possible orders
				
			}
			
		}

		//If the neither of the case satisfies, insert the order in the list
		Message temp=new OrdersIterator.OrderImpl(no.getSymbol(),Math.abs(diff),no.getOrderId(),no.getLimitPrice());
			 
		return InsertBook(temp);		//Insertion in the book and returning the node
	}	
	
	/**
	 * The function is responsible for performing the trade of the order if it is a ask order. It checks if the current order is a market order. If so, it 
	 * executes the order assuming that there is enough size of orders available to trade. If it is a limit order then, then it trades only if the limit price  
	 * of the arriving order is less than or equal to the limit price of the order on the top of the book. If the newly arriving order is completely traded 
	 * then, the function returns null. But of the part or the whole of the order cannot be traded, then the function creates an entry in the corresponding
	 * list and returns node to the caller.   
	 * @param caller : Represents the source of the call. If caller =true indicates that the Runner has called the trade 
	 * 					method. If flag=false, then SilentRunner is the caller.
	 * @return : Node of the newly created entry else null.
	 */
	
	DNode<Message> tradeAsk(Message m,boolean caller)
	{
		cleanUpBook();		//Clean up the book.
		NewOrder no=(NewOrder)m;
		
		int size=no.getSize();		//Obtain the size of the current order
		Double lp=no.getLimitPrice();		//Obtain the limit price of the order
		Set set=null;
		Iterator it=null;
		
		set=bid.entrySet();			//Since this a ask order, consider only the bid book.
		it = set.iterator();		

		int diff=no.getSize();		//Variable used to maintain the amount of more shares that can be traded from this order
		
Outer:	while(it.hasNext())			//Iterate through the entire ask book
		{
			Map.Entry me = (Entry) it.next();
			double listprice=((Double) me.getKey()).doubleValue();		//Obtain the price limit for set of orders in the corresponding list
			DoubleLinkedList dl=(DoubleLinkedList) me.getValue();		//Obtain the linked list.
			
			DNode dn=dl.iterator();				//Obtain the iterator for the list.
			while(dn!=null)			//Iterating through all the orders of the same price level
			{
				
				Message cur=(Message) dn.data;		//Obtain the message object
				NewOrder current= (NewOrder) cur;
					
				if(Double.isNaN(no.getLimitPrice()))		//In case of market order
				{
					diff=Math.abs(current.getSize())-Math.abs(diff);	//Obtaining the amount of order that will be traded in this transaction
			
					// market order
					if(diff==0)		//Both, the first element of the ask list and the input order have exactly equal size.   
					{
						dl.removeTop();		//Delete the node at the head of the list.
						if(caller)
							System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());		//Display result
						return null;
					}
					else if(diff>0)		//The order at the top of the list has greater size than the current order
					{
						dl.removeTop();		//Updating the top of list to represent the order currently traded
						Message temp=new OrdersIterator.OrderImpl(current.getSymbol(),diff,current.getOrderId(),current.getLimitPrice());
						if(caller)
							System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
						dl.addBeg(temp);
						return null;
					}
					else if(diff<0)		//Current order has larger size than the order at the top of the list. The next order can also be traded partially/completely 
					{
						dl.removeTop();
						if(dl.size()==0)
						{
							ask.remove(listprice);
							break;
						}
						if(caller)
							System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
						dn=dl.iterator();		//Repeating the same process
					}	
				}
				else if(lp<=listprice)		//For Limit Order
				{
					// limit order
					diff=Math.abs(current.getSize())-Math.abs(diff);
					{
						if(diff==0)
						{
							dl.removeTop();
							if(caller)
								System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
							return null;
						}
						else if(diff>0)
						{
							dl.removeTop();
							Message temp=new OrdersIterator.OrderImpl(current.getSymbol(),diff,current.getOrderId(),current.getLimitPrice());
							dl.addBeg(temp);
							if(caller)
								System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
							return null;
						}
						else if(diff<0)
						{
							dl.removeTop();
							if(dl.size()==0)
							{
								ask.remove(listprice);
								break;
							}
							if(caller)
								System.out.println("Order "+no.getOrderId()+" traded with "+current.getOrderId());
							dn=dl.iterator();
						}
					}
				}
				else break Outer; 
			}
			
		}

		//Insert into the list in case it is not fully traded.
		Message temp=new OrdersIterator.OrderImpl(no.getSymbol(),-1*Math.abs(diff),no.getOrderId(),no.getLimitPrice());
		return InsertBook(temp);	
	}
	
	
	/**
	 * Function that drives the functionality of the trade. It accepts the arriving order. If the size of the arriving order is 0, then it considers it
	 * as a cancel order and returns null. If the size is non zero, it calls the function above to perform the appropriate operation 
	 * @param m : Arriving Message
	 * @param caller : Represents the source of the call. If caller =true indicates that the Runner has called the trade 
	 * 					method. If flag=false, then SilentRunner is the caller.
	 * @return : Node of the newly created entry else null.
	 */
	
	DNode<Message> trade(Message m,boolean caller)
	{
		NewOrder no=(NewOrder)m;		//Current order
		int size=no.getSize();			//Size of the current order
		cleanUpBook();

		if(size==0)			//If the size is 0, consider it as a cancel order.
			return null;
		else if(size<0)		//If the size is less than 0, consider it as ask order
		{
			return tradeAsk(m,caller);
		}
		else		//If the size is greater than 0, consider it to be a bid order
		{
			return tradeBid(m, caller);
		}
	}
	/**
	 * Function prints the entire book in the format specified.price,ask/bid,size, where the price is rounded to
	 * two decimal places. Function is called in the end.
	 */
	
	void printEntireBook()
	{
		if(bid.size()!=0)		//Bid Book should not be empty
		{
			Set bidSet=bid.entrySet();			//Getting the iterator for the bid book
			Iterator bid_it = bidSet.iterator();
						
			while(bid_it.hasNext())				//Iterating for every limit price entry in the book
			{
				Map.Entry me = (Map.Entry)bid_it.next();	
				Double key=(Double)me.getKey();
				DoubleLinkedList<Message> l=bid.get(key);		//Obtaining the list associated with the price
				DNode<Message> it=l.iterator();
	
				while(it!=null)			//Iterating through the list for a current price
				{	
					Message m=(Message)it.getNode();
					NewOrder no=(NewOrder)m;
				
					System.out.println((double)Math.round(no.getLimitPrice()*100)/100+",bid,"+no.getSize());
					it=it.next;
				}
			}
		}
		if(ask.size()!=0)		//Ask Book should not be empty
		{
			Set askSet=ask.entrySet();			//Getting the iterator for the ask book
			Iterator ask_it = askSet.iterator();
						
			while(ask_it.hasNext())		//Iterating for every limit price entry in the book
			{
				Map.Entry me = (Map.Entry)ask_it.next();
				Double key=(Double)me.getKey();
				DoubleLinkedList<Message> l=ask.get(key);		//Obtaining the list associated with the price
				DNode<Message> it=l.iterator();
		
				while(it!=null)			//Iterating through the list for a current price
				{
					Message m=(Message)it.getNode();
					NewOrder no=(NewOrder)m;
				
					System.out.println((double)Math.round(no.getLimitPrice()*100)/100+",ask,"+no.getSize());
					it=it.next;
				}
			}
		}
	}
	
	/**
	 * Function is used to remove the first node from the list. It is used in case of a replace order, that requires 
	 * deleting the node at the head of the list. The list whose first node is to be deleted is identified by p, which 
	 * represents the price of the order that is to be deleted. Size, s is used to identify whether the entry belongs to 
	 * ask/bid list.   
	 * @param p : Price of the order to be deleted. 
	 * @param s : Size of the order to be deleted.
	 */
	
	void removeFirst(double p,int s)		
	{
		if(s>0)				//Deleting from the bid book
		{
			if(bid.containsKey(new Double(p)))		//Checking for the entry in the bid book
			{
				DoubleLinkedList<Message> dll = bid.get(p);		//Obtaining the list associated with the price
				dll.removeTop();				//Removing the top node in the linked list
			}
		}
		else		//Deleting from the ask book
		{
			if(ask.containsKey(new Double(p)))		//Checking for the entry in the ask book
			{
				DoubleLinkedList<Message> dll = ask.get(p);		//Obtaining the list associated with the price
				dll.removeTop();		//Removing the top node in the linked list
			}
		}
	}
}


