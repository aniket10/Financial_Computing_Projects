	package orderGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class OrdersIterator
{
  public static Iterator<Message> getIterator()
  {
    List<Message> msgs = new ArrayList<Message>();
/*    msgs.add(new OrderImpl("IBM", 1000, "IBM1", 100.0D));
    msgs.add(new OrderImpl("IBM", -300, "IBM2", Double.NaN));
    msgs.add(new OrderImpl("IBM", -200, "IBM3", 103.0D));
    
    msgs.add(new OrderCxRImpl("IBM1",300,105.0D));
    msgs.add(new OrderImpl("IBM", -500, "IBM4", 100.0D));
    msgs.add(new OrderImpl("IBM", 1000, "IBM5", 95.0D));
    msgs.add(new OrderImpl("IBM", -700, "IBM6",(0.0D / 0.0D)));
    
    for (int i = 0; i < 5; i++) 
    {
    	msgs.add(new OrderImpl("MSFT", 300, "" + i, 22.559999999999999D));
    }
    msgs.add(new OrderCxRImpl("3",0,(0.0D / 0.0D)));
    msgs.add(new OrderCxRImpl("1",200,22.559999999999999D));
    msgs.add(new OrderCxRImpl("4",200,22.559999999999999D));

    msgs.add(new OrderCxRImpl("3",0,(0.0D / 0.0D)));
    
    
    msgs.add(new OrderImpl("MSFT", -1000, "MSFT1", 20.01D));
    //msgs.add(new OrderImpl("IBM", 1000, "IBM2", 21.03D));*/

    Random randNum = new Random();

    msgs.add(new OrderImpl("IBM", 1000, "IBM1", 100D));
    msgs.add(new OrderImpl("IBM", -1000, "IBM2", 101D));
    msgs.add(new OrderImpl("IBM", 100, "IBM3", (0.0D / 0.0D)));
    ArrayList<String> orderIds = new ArrayList<String>();
    int num = 100;
    for(int i = 0; i < num; i++)
    {
    msgs.add(new OrderImpl("MSFT", 300, (new StringBuilder()).append("").append(i).toString(), 22.559999999999999D));
    orderIds.add((new StringBuilder()).append("").append(i).toString());
    }
 //   msgs.add(new OrderImpl("MSFT", -3000, "MSFT2", 25.010000000000002D));
    randNum.setSeed(123456);
    Collections.shuffle(orderIds,randNum);
    String orderId;
    double rand=0.0;
    boolean tmp,tmp2;
    int j=orderIds.size();
    for(Iterator<String> i$ = orderIds.iterator(); i$.hasNext(); )
    {
    orderId = (String)i$.next();
    rand = randNum.nextDouble();
    tmp = randNum.nextDouble()<0.5D;
    tmp2 = randNum.nextDouble()<0.5D;
    if(rand<0.33){
    msgs.add(new OrderCxRImpl(orderId, tmp ? 0 : 200, tmp2 ? (0.0D / 0.0D) : 22.539999999999999D));
    }
    else if(rand>0.33 && rand<0.66){
    msgs.add(new OrderCxRImpl(orderId, tmp ? 0 : 200, tmp2 ? (0.0D / 0.0D) : 22.539999999999999D));
    }
    else {
    double tmprand = randNum.nextDouble();
    double price = 0;
    if(tmprand<(1.0D/3.0D)) price = 20;
    else if(tmprand>(1.0D/3.0D) && tmprand<(2.0D/3.0D)) price = 22.559999999999999D;
    else price = 50.0;
    msgs.add(new OrderImpl("MSFT",-200,""+j++,price));
    }
    }
    msgs.add(new OrderImpl("MSFT", -1000, "MSFT1", 25.010000000000002D));  
    return msgs.iterator();
  }

  public static void main(String[] args)
  {
    for (Iterator msg = getIterator(); msg.hasNext(); )
      System.out.println(msg.next());
  }

  public static class OrderCxRImpl
    implements OrderCxR
  {
    private String orderId;
    private int size;
    private double limitPrice;

    public OrderCxRImpl(String orderId, int size, double limitPrice)
    {
      this.orderId = orderId;
      this.size = size;
      this.limitPrice = limitPrice;
    }

    public int getSize()
    {
      return this.size;
    }

    public String getOrderId()
    {
      return this.orderId;
    }

    public double getLimitPrice()
    {
      return this.limitPrice;
    }

    public String toString() {
      return getOrderId();
    }
  }

  public static class OrderImpl
    implements NewOrder
  {
    private String symbol;
    private int size;
    private String orderId;
    private double limitPrice;

    public OrderImpl(String symbol, int size, String orderId, double limitPrice)
    {
      this.symbol = symbol;
      this.size = size;
      this.orderId = orderId;
      this.limitPrice = limitPrice;
    }

    public String getSymbol()
    {
      return this.symbol;
    }

    public int getSize()
    {
      return this.size;
    }

    public String getOrderId()
    {
      return this.orderId;
    }

    public double getLimitPrice()
    {
      return this.limitPrice;
    }

    public String toString() {
      return getOrderId();
    }
  }
}