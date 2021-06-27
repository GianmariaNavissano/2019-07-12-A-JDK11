package it.polito.tdp.food.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<Food,DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	private Simulator s;
	
	public Model() {
		dao = new FoodDao();
		s = new Simulator();
	}
	
	public void creaGrafo(int nPorzioni) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		this.idMap = this.dao.getVertexes(nPorzioni);
		Graphs.addAllVertices(grafo, this.idMap.values());
		
		//archi
		//ricordo di controllare che la lista non sia vuota
		List<Adiacenza> edges = this.dao.getEdges(idMap);
		if(!edges.isEmpty()) {
			for(Adiacenza a : edges) {
				Graphs.addEdgeWithVertices(grafo, a.getF1(), a.getF2(), a.getPeso());
			}
		}
		
	}
	
	public List<Food> getFoods(int nPorzioni){
		List<Food> foods = new LinkedList<>(this.idMap.values());
		Collections.sort(foods);
		return foods;
	}
	
	public int getNumVertex() {
		return this.grafo.vertexSet().size();
	}
	public int getNumEdges() {
		return this.grafo.edgeSet().size();
	}
	
	public String doCalorie(Food f) {
		List<FoodWithWeight> cibiCalMax = new LinkedList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(grafo.getEdgeSource(e).equals(f)) 
				cibiCalMax.add(new FoodWithWeight(grafo.getEdgeTarget(e), grafo.getEdgeWeight(e)));
			if(grafo.getEdgeTarget(e).equals(f))
				cibiCalMax.add(new FoodWithWeight(grafo.getEdgeSource(e), grafo.getEdgeWeight(e)));
			
		}
		if(cibiCalMax.isEmpty()) {
			return "Non vi sono cibi adiacenti a "+f+"\n";
		}
		String result = "Il cibo "+f+" ha "+cibiCalMax.size()+" vertici adiacenti\nEccone la lista:\n";
		Collections.sort(cibiCalMax);
		for(FoodWithWeight fww : cibiCalMax) {
			result += fww.getF()+" "+fww.getPeso()+"\n";
		}
		result += "\n\n";
		
		int N = 5;
		if(cibiCalMax.size()<5)
			N = cibiCalMax.size();
		
		for(int i=0; i<N; i++) {
			result += cibiCalMax.get(i).getF()+" "+cibiCalMax.get(i).getPeso()+"\n";
		}
		
		return result;
	}
	
	
	public String doSimulazione(Food f, int K) {
		
		
		//Prendo i migliori K vicini
		List<FoodWithWeight> vicini = new LinkedList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(grafo.getEdgeSource(e).equals(f)) 
				vicini.add(new FoodWithWeight(grafo.getEdgeTarget(e), grafo.getEdgeWeight(e)));
			if(grafo.getEdgeTarget(e).equals(f))
				vicini.add(new FoodWithWeight(grafo.getEdgeSource(e), grafo.getEdgeWeight(e)));
			
		}
		if(vicini.isEmpty())
			return "Non vi sono cibi adiacenti a "+f+"\n";
		Collections.sort(vicini);
		if(K>vicini.size())
			K = vicini.size();
		List<Food> primi = new LinkedList<>();
		for(int i=0; i<K; i++)
			primi.add(vicini.get(i).getF());
		
		s.init(K, grafo, primi, f);
		s.run();
		
		String result = "La simulazione è terminata dopo un tempo totale di "+s.getTotTime()+" e sono stati preparati "+s.getCibiPreparati()+" cibi\n";
		
		return result;
	}

}
