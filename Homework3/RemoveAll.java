package Homework3;
import javax.jms.JMSException;

import Homework3.Client;
public class RemoveAll {
	public static void main(String arg[])
	{
		while(true)
			try {
				Client.main(null);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
