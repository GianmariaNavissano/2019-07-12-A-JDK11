package it.polito.tdp.food.model;

public class Evento implements Comparable<Evento>{
	
	private double time;
	private Food out;
	private Food in;
	
	
	Evento(double time, Food out, Food in) {
		super();
		this.time = time;
		this.out = out;
		this.in = in;
	}
	


	public double getTime() {
		return time;
	}



	public void setTime(double time) {
		this.time = time;
	}



	public Food getOut() {
		return out;
	}



	public void setOut(Food out) {
		this.out = out;
	}



	public Food getIn() {
		return in;
	}



	public void setIn(Food in) {
		this.in = in;
	}



	@Override
	public int compareTo(Evento other) {
		if(this.time<other.getTime())
			return -1;
		if(this.time>other.getTime())
			return 1;
		return 0;
	}
	
	

}
