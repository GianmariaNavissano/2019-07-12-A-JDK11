package it.polito.tdp.food.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {
	
	//modello
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> inPreparazione;
	private Queue<Evento> coda;
	private Map<Integer, Food> toDo;
	
	//input
	private int K;
	private Food primo;
	
	
	//output
	private Map<Integer, Food> preparati;
	private double totTime;
	
	public void init(int K, Graph<Food, DefaultWeightedEdge> grafo, List<Food> primi, Food primo) {
		
		
		
		this.coda = new PriorityQueue<>();
		this.grafo = grafo;
		this.K = K;
		this.primo = primo;
		
		this.inPreparazione = new HashMap<>();
		this.preparati = new HashMap<>();
		this.toDo = new HashMap<>();
		this.totTime = 0;
		
		
		for(Food f : primi) {
			//Aggiungo i primi eventi, ovvero al tempo 0 inizierà la lavorazione
			//dei cibi adiacenti quello di partenza, già calcolati e passati dal model
			coda.add(new Evento(0, null, f));
		}
		
		
		
	}
	
	public void run() {
		
		
		
		while(!coda.isEmpty()) {
			
			
			Evento e = coda.poll();
			double time = e.getTime();
			
			//out è il cibo che ha appena finito la preparazione
			Food out = e.getOut();
			
			//in è il cibo che subentra
			Food in = e.getIn();
			
			
			System.out.println("In: "+in+" out: "+out+" t: "+time);
			

			//***************GESTISCO L'USCITA*******************
			if(out!=null) {
				
				
				
				//tolgo da quelli in preparazione il cibo in uscita
				this.inPreparazione.remove(out.getFood_code());
				
				//aggiorno gli output
				this.preparati.put(out.getFood_code(), out);
				this.totTime = time;
				
				
				
			}else {//nel caso sia uno dei primi
				out = this.primo;
			}
			
			
			
			//******************GESTISCO L'ENTRATA********************
			
			if(in!=null) {//verifico che non si tratti degli ultimi. In tal caso non vi è alcuna entrata da gestire
				
				
				
				//verifico che ci siano postazioni libere. Se non ci sono non faccio nulla per
				//quanto riguarda l'entrata
				if(this.inPreparazione.size()<K) {
					
					//tolgo il cibo dalla lista di quelli in programma (toDo)
					this.toDo.remove(in.getFood_code());
					
					
					//metto il cibo IN dentro inPreparazione
					this.inPreparazione.put(in.getFood_code(), in);
				
				
				
					
					
					
					//*******************GESTISCO CREAZIONE NUOVO EVENTO************************
					
					//creo nuovo evento che segnerà l'uscita di questo cibo dopo un tempo
					//calcolato in base alle calorie congiunte tra questo e quello precedente
					//devo determinare chi entrerà al suo posto
					List<FoodWithWeight> nextList = this.getNext(in);
				
				
					//verifico che vi siano successori possibili
					if(!nextList.isEmpty()) {
						
						
					
						//prendo il primo della nextList che non sia già in lavorazione e non sia stato lavorato
						//(e non sia uguale a quello iniziale)
						FoodWithWeight next = null;
						for(FoodWithWeight fww : nextList) {
							if(!this.inPreparazione.containsKey(fww.getF().getFood_code()) //non deve essere tra quelli in preparazione
									&& !this.preparati.containsKey(fww.getF().getFood_code()) //non deve essere tra quelli preparati
									&& !fww.getF().getDisplay_name().equals(primo.getDisplay_name())//non deve essere il primo
									&& !this.toDo.containsKey(fww.getF().getFood_code())) { //Non deve essere nella lista di quelli da fare
								next = fww;
								break;
								//inserito = true;
							}
						}
						
						
						
						
						
						
						if(next==null) {
							
							//se non ci sono successori adeguati (PT1)
							
							
							System.out.println("Non ci sono cibi disponibili dopo "+in);
							
							
							//Genero evento di SOLA USCITA
							
							//in sarà il valore di OUT
							//il valore di IN sarà NULL
							//calcolo il delta, dato dal peso dell'arco tra in ed out:
							double delta = 0.0;
							
							if(grafo.getEdge(in, out)!=null)
								delta = grafo.getEdgeWeight(grafo.getEdge(in, out));
							else if(grafo.getEdge(out, in)!=null) delta = grafo.getEdgeWeight(grafo.getEdge(out, in));
							
							if(delta == 0.0)
								System.out.println("Errore: delta nullo");
							else
								coda.add(new Evento(time+delta, in, null));
							
							
							
							
							
							
						}else {
							//Una volta individuato il valore del successore posso creare il nuovo evento
							
							
							
							//Inserisco il successore nella lista di quelli in programma (toDo)
							this.toDo.put(next.getF().getFood_code(), next.getF());
							
							
							//next sarà il valore di IN dell'evento
							//in sarà il valore di OUT dell'evento
							//il valore del TIME che ci impiego a far uscire in sarà dato dal peso dell'arco che unisce in e out
							double delta = 0.0;
							if(grafo.getEdge(in, out)!=null)
								delta = grafo.getEdgeWeight(grafo.getEdge(in, out));
							else if(grafo.getEdge(out, in)!=null) delta = grafo.getEdgeWeight(grafo.getEdge(out, in));
							
							if(delta == 0.0)
								System.out.println("Errore: delta nullo");
							else
								coda.add(new Evento(time+delta, in, next.getF()));
							
							
								
							
						}
						
						
						
						
						
						
						
						
						
					}else {
						
						
						
						//se non ci sono successori adeguati (PT2)
						
						
						//Genero evento di SOLA USCITA
						
						System.out.println("No vicini disponibili per "+in);
				
					
						//in sarà il valore di OUT
						//il valore di IN sarà NULL
						//calcolo il delta, dato dal peso dell'arco tra in ed out:
						double delta = 0.0;
						
						if(grafo.getEdge(in, out)!=null)
							delta = grafo.getEdgeWeight(grafo.getEdge(in, out));
						else if(grafo.getEdge(out, in)!=null) delta = grafo.getEdgeWeight(grafo.getEdge(out, in));
						
						if(delta == 0.0)
							System.out.println("Errore: delta nullo");
						else
							coda.add(new Evento(time+delta, in, null));
					
					
					
					}
				
				
				
				
					
				}
			}
				
				
					
								
					
					
		}		
				
	}
	
		
	
	public List<FoodWithWeight> getNext(Food f) {
		//ottengo una lista contenente i vicini ORDINATI del cibo che sta entrando 
		//nella stazione. Mi serve per scegliere il prossimo cibo da inserire nella coda
		List<FoodWithWeight> vicini = new LinkedList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(grafo.getEdgeSource(e).equals(f)) 
				vicini.add(new FoodWithWeight(grafo.getEdgeTarget(e), grafo.getEdgeWeight(e)));
			if(grafo.getEdgeTarget(e).equals(f))
				vicini.add(new FoodWithWeight(grafo.getEdgeSource(e), grafo.getEdgeWeight(e)));
			
		}
		//ordino i vicini in base alle calorie congiunte
		Collections.sort(vicini);
		return vicini;
	}

	public double getTotTime() {
		return totTime;
	}
	
	public int getCibiPreparati() {
		return this.preparati.size();
	}
	

}
