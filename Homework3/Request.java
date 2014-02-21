package Homework3;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Request implements Serializable  {
	
	double sigma;
	double r;
	double strike_price;
	double cur_price;
	int type_of_option;
	int duration;
	
	public Request(double sigma,double r, double strike_price, double cur_price,int type_of_option,	int duration)
	{
	
		this.sigma=sigma;
		this.r=r;
		this.strike_price=strike_price;
		this.cur_price=cur_price;
		this.type_of_option=type_of_option;
		this.duration=duration;
	}
}
