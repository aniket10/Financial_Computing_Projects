package Homework3;
import java.io.Serializable;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * Implementation of the server. Performs the task of computing the mean and standard deviation at each iteration. It is the responsibility of the server to determine when to stop the iterations. 
 * @author Aniket
 *
 */
public class Server {
		/**
		 * Connection URL for ActiveMQ
		 */
	    private static String url = "tcp://localhost:61616"; //ActiveMQConnection.DEFAULT_BROKER_URL;
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
	    /**
	     *  Object for sending the request.
	     */
	    static Request req;

	    public static void main(String[] args) throws JMSException {

	    	BrokerService broker = new BrokerService();
	    	broker.setPersistent(false);
	    	broker.setUseJmx(false);
	    	try {
				broker.addConnector(url);
				broker.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	/**
	    	 * Parameters for computing the pay out.
	    	 */
	    	double sigma=0.01;
	    	double r=0.001;
	    	double strike_price=165;
	    	double cur_price=152.35;
	    	int type_of_option=0; 			//0 = Asian and 1 = European
	    	int duration=252;    			// in days
	    	
	    	//Creating a new request.
	    	
	    	req=new Request(sigma,r,strike_price,cur_price,type_of_option,duration);
	    	
	    	//Setting up the connection
	        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
	        Connection connection = connectionFactory.createConnection();
	        connection.start();

	        //Setting up a session.
	        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
	        Destination destination = session.createQueue(subject);				// Destination queue. Client will listens to this queue.
	        MessageProducer producer = session.createProducer(destination);
	        Destination return_destination=null;
	        // Server listens to this queue. Client enqueues it with the result for each iteration.
	        if(type_of_option==0)  //For asian call option.
	        {
	        	return_destination = session.createQueue(asian_subject);
	        }
	        else	// for european call option
	        {
	        	return_destination = session.createQueue(european_subject);
	        }
	        //Creation of a consumer.
	        MessageConsumer consumer = session.createConsumer(return_destination);
	        
	        double avg_asian,mean=0,sigma_cap=0,i=0,count=0;
	        sigma=0;
	        
	        loop:while(true)
	        {
	        	//Server adds a set of 100 requests at a time.
		        for(i=0;i<1000;i++)
		        {	
		        	//Create a message object
		        	ObjectMessage message = session.createObjectMessage((Serializable) req);
		        	//Put the request object in the queue
		            producer.send(message);
		        }
		        for(i=0;i<1000;i++)
		        {
		        	// Read the result put by the Client in the queue.
		        	Message ret_message = consumer.receive();
		        	
		        	//For valid returns by the client
			        if (ret_message instanceof TextMessage) {
			        	TextMessage textMessage = (TextMessage) ret_message;
		                 
			        	//Computing the mean, standard deviation.
			            avg_asian=new Double( textMessage.getText()).doubleValue();
			            mean=((mean*count)+avg_asian)/(count+1);
						sigma=((sigma*count)+avg_asian*avg_asian)/(count+1);
						count++;
						sigma_cap=Math.sqrt(Math.abs(sigma-mean*mean));
						double d=2.0537*sigma_cap/Math.sqrt(count);
						System.out.println(d);
						if(d<0.01 && count%1000==0) break loop; 
			        }
		        }
		        System.out.println(count);
	        }
	        //Discounting the payout
			mean=mean*Math.exp(-0.0001*252);
			System.out.println("Final Mean is "+mean);
	        connection.close();
	    }
}