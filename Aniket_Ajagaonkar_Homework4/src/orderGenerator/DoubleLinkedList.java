package orderGenerator;

/**
 * Class implementation of the double linked list. The structure of the node used in the double linked list is 
 * same as the one defined in the DLLNode class. It builds upon that node to form a list by maintaining a first
 * and a last pointer that points to the first and the last node in the list.  
 * @author Aniket
 *
 * @param <E>
 */

public class DoubleLinkedList<E> 
{
	private DNode<E> first,last;		// Pointers to the first and the last node in the list.
    
    public DoubleLinkedList()
    {
    	first=last=null;
    }
    
    /**
     * Function to add the node in the beginning of the list. Function is used in case of modifying 
     * the node at the top of the list. Function updates the first node in the list.
     * @param ele : Element to be inserted
     * @return Node that was inserted.
     */
    
    public DNode<E> addBeg(E ele)
    {

    	DNode<E> p=new DNode<E>(ele);
        if(first==null)			//No elements in the list
            first=last=p;
        else			//Updating the first pointer
        {
            p.next=first;
            first.prev=p;
            first=p;
        }
        return first;
    }
    
    /**
     * Function to add the node to the end of the list. Function updates the last node to accomodate the new node.
     * Lasy now points to the newly inserted node.
     * @param ele : Element to be inserted
     * @return 
     */
    
    public DNode<E> add(E ele)
    {
    	DNode<E> p=new DNode<E>(ele);
        if(last==null)			//Empty list
           	first=last=p;
        else			//Updating the last pointer
        {
            last.next=p;
            p.prev=last;
            last=p;
        }
        return p;
    }
    
    /**
     * Function used to describe the iterator for the list. Function returns the first element of the list 
     * indicating the start of the list. 
     * @return : The first element of the list
     */
    
    public DNode<E> iterator()
    {
    	return first;		
    }
    
    /**
     * Function represents the size of the list. It represents the number of elements present in the list. The
     * count is obtained by iterating through the nodes from start to last.
     * @return : Count of the nodes in the list.
     */
    
    public int size()
    {
    	DNode<E> temp=first;
    	int count=0;
    	while(temp!=null)		//Iterating till the end of the list
    	{
    		count++;
    		temp=temp.next;
    	}
    	return count;
    }
    
    /**
     * Function removes the first node from the list. first pointer is pointed to the next element in the list.
     */
    
    void removeTop()
    {
    	if(first!=null)			
    	{
    		first=first.next;
    		if(first!=null)
    			first.prev=null;
    	}
    }
}
