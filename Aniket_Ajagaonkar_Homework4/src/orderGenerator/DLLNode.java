package orderGenerator;

/**
 * Class implementation of the node of the generic double linked list. Each node contains three elements:data and two
 * pointers, one to the next node and the other to the previous node. Class also includes constructors for initialising 
 * the node, functions to check for the next node, deleting the next node etc. 
 * @author Aniket
 *
 * @param <E> : Represents the data type (Message in this program)
 */

public class DLLNode<E> {
	
    E data;						//	Represents the node.
    DLLNode<E> next,prev;		// 	Pointers to the next and the previous node in the list
    
    /**
     * Parameterized Constructor. Initialized the node with the value of the order.
     * @param d : Order
     */
    
    public DLLNode(E d)			
    {
    	this.data=d;
        next=prev=null;    	
    }
    
    /**
     * Function to test if the current node has a next node
     * @return true if there exists a next node. False otherwise.
     */
    boolean hasNext()
    {
    	if(this.next!=null)
    		return true;
    	else return false;
    }
    
    /**
     * Returns the next node in the list. Returns null if it has reached the end of the list
     * @return : Next node
     */
    
    DLLNode<E> next()
    {
    	if(this!=null)
    		return this.next;
    	else return null;
    }
    
    /**
     * Returns the data part of the current node
     * @return : Object of the type of data that is stored.
     */
    
    E getNode()
    {
    	if(this!=null)
    		return this.data;
    	else return null;
    }
    
    /**
     * Removes the current element of the list. It returns true if successful. Function doesnot remove any element if it is the head of the queue.
     * Function returns false if the current node is the first node in the list.
     * @return Status whether delete was successful/not
     */
    
    boolean remove()
    {
    	if(this.prev!=null)
    	{
    		this.prev.next=this.next;
    		return true;
    	}  		
    	return false;
    }
    
}
