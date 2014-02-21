package Homework3;
import javax.jms.*;
import Homework2.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Client extends Thread  {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "TESTQUEUE";
    private static String asian_subject = "ASIANQUEUE";
    private static String european_subject = "EUROPEANQUEUE";

    public static void main(String[] args) throws JMSException {
    
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
   
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
    
        Destination destination = session.createQueue(subject);
    
        MessageConsumer consumer = session.createConsumer(destination);
       
//        int i=0;
//        for(i=0;i<5;i++)
       Message message;
        while((message = consumer.receive())!=null)
    	{
//        	Message message = consumer.receive();
    	
	    	if (message instanceof ObjectMessage) {
		
	    		System.out.println("Object received");
	    		ObjectMessage objMessage = (ObjectMessage) message;
		        Request req = (Request) objMessage.getObject();
		        
		        RandomVectorGenerator rvg = new RandomVectorGeneratorImpl();
		        
		        double arr[]=rvg.getVector();
				StockPath sp = new StockPathImpl(arr,req.r,req.sigma,req.duration);
				Destination return_destination=null;
				double result=0;
				
				if(req.type_of_option==0)
				{
					PayOutAsian pa = new PayOutAsian(req.strike_price,req.duration);
					result = pa.getPayout(sp);
					return_destination = session.createQueue(asian_subject);
				}
				else
				{
					PayOutEuropean pe = new PayOutEuropean(req.strike_price,req.duration);
					result = pe.getPayout(sp);
					return_destination = session.createQueue(european_subject);
				}
		
				MessageProducer producer = session.createProducer(return_destination);
		   		message = session.createTextMessage(result+"");
		   		producer.send(message);
	    	}
	    }
        
    	connection.close();
    }
}



