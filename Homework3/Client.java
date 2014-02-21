package Homework3;
import javax.jms.*;
import Homework2.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Client extends Thread  {
	/**
	 * Connection URL for ActiveMQ
	 */
    private static String url = "tcp://localhost:61616";//ActiveMQConnection.DEFAULT_BROKER_URL;
    /**
     * Queue used by the producer to put the requests. Client listens to this queue for requests. 
     */
    private static String subject = "TESTQUEUE";
    /**
     * Queue used for Asian call option. The client puts the output on this queue and the server listens to this queue. 
     */
    private static String asian_subject = "ASIANQUEUE";
    /**
     *  Queue used for European call option. The client puts the output on this queue and the server listens to this queue.
     */
    private static String european_subject = "EUROPEANQUEUE";

    public static void main(String[] args) throws JMSException {
    
    	//Connection to the ActiveMQ
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        
        //Setting up a session.
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //Queue for listening the requests sent by the server.
        Destination destination = session.createQueue(subject);
        //Consumer 
        MessageConsumer consumer = session.createConsumer(destination);
       
//        int i=0;
//        for(i=0;i<5;i++)
       Message message;
       //Client will read until it is blocked on the queue because the queue is empty.
        while((message = consumer.receive())!=null)
    	{
//        	Message message = consumer.receive();
//        	System.out.println("Object received");
	    	if (message instanceof ObjectMessage) {
//	    		System.out.println("inside if");
//	    		System.out.println("Object received");
	    		//Create a message object to extract information from the input request.
	    		ObjectMessage objMessage = (ObjectMessage) message;
		        Request req = (Request) objMessage.getObject();
		        
		        //Generate a random vector.
		        RandomVectorGenerator rvg = new RandomVectorGeneratorImpl();
		        
		        double arr[]=rvg.getVector();
		        //Generate a stock path.
				StockPath sp = new StockPathImpl(arr,req.r,req.sigma,req.duration);
				Destination return_destination=null;
				double result=0;
				
				if(req.type_of_option==0)
				{
					//Compute Asian Payout and put it in the queue
					PayOutAsian pa = new PayOutAsian(req.strike_price,req.duration);
					result = pa.getPayout(sp);
					return_destination = session.createQueue(asian_subject);
				}
				else
				{
					//Compute the european payout and put it in the queue
					PayOutEuropean pe = new PayOutEuropean(req.strike_price,req.duration);
					result = pe.getPayout(sp);
					return_destination = session.createQueue(european_subject);
				}
		
				MessageProducer producer = session.createProducer(return_destination);
		   		message = session.createTextMessage(result+"");
		   		//Send the result back to the server.
		   		producer.send(message);
	    	}
	    }
        
    	connection.close();
    }
}