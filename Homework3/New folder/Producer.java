import java.io.Serializable;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import Homework3.Request;


public class Producer {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "TESTQUEUE";
    private static String return_subject = "RETURNQUEUE";
    static Request req;

    public static void main(String[] args) throws JMSException {

    	double sigma=0.01;
    	double r=0.001;
    	double strike_price=165;
    	double cur_price=152.35;
    	int type_of_option=0;
    	int duration=252;    	
    	
    	req=new Request(sigma,r,strike_price,cur_price,type_of_option,duration);
    	
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(subject);
        MessageProducer producer = session.createProducer(destination);
        
        Destination return_destination = session.createQueue(return_subject);
        MessageConsumer consumer = session.createConsumer(return_destination);
        
        double avg_asian,mean=0,sigma_cap=0,i=0,count=0;
        sigma=0;
        
        while(count<10)
        {
	        for(i=0;i<5;i++)
	        {	
	        	ObjectMessage message = session.createObjectMessage((Serializable) req);
	            producer.send(message);
	        }
	        for(i=0;i<5;i++)
	        {
	        	Message ret_message = consumer.receive();
	        
		        if (ret_message instanceof TextMessage) {
		        	TextMessage textMessage = (TextMessage) ret_message;
	                       
		            avg_asian=new Double( textMessage.getText()).doubleValue();
		            mean=((mean*count)+avg_asian)/(count+1);
					sigma=((sigma*count)+avg_asian*avg_asian)/(count+1);
					count++;
					sigma_cap=Math.sqrt(Math.abs(sigma-mean*mean));
					double d=2.0537*sigma_cap/Math.sqrt(count);
					//if(d<0.01 && count%10000==0) break; 
		        }
	        }
        }
		mean=mean*Math.exp(-0.0001*252);
		System.out.println("Final Mean is "+mean);
        connection.close();
    }
}

