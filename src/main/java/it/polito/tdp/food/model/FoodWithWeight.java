package it.polito.tdp.food.model;

public class FoodWithWeight implements Comparable<FoodWithWeight>{
	
	private Food f;
	private double peso;
	
	public FoodWithWeight(Food f, double peso) {
		super();
		this.f = f;
		this.peso = peso;
	}

	public Food getF() {
		return f;
	}

	public void setF(Food f) {
		this.f = f;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(FoodWithWeight other) {
		if(this.peso>other.getPeso())
			return -1;
		if(this.peso<other.getPeso())
			return 1;
		return 0;
	}

	@Override
	public String toString() {
		return f.toString();
	}
	
	
	
	

}
