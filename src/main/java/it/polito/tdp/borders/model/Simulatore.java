package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	
	/*
	 * Sempre pensare ai quattro tipi di dati coinvolti:
	 * 1-Coda degli eventi
	 * 2-Paraemtri di simulazione
	 * 3-Output della simulazione
	 * 4-Stato del mondo
	 */
	
	//Parametri
	private int nInizialeMigranti;
	private Country nazioneIniziale;
	
	//Stato del mondo simulato
	private Graph<Country, DefaultEdge> grafo;
	//e anche la mappa persone
	
	//Output
	private int nPassi;
	private Map<Country,Integer> persone;
	
	//Coda
	private PriorityQueue<Event> coda;

	public Simulatore(Graph<Country, DefaultEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void initialize(Country partenza, int migranti) {
		this.nazioneIniziale=partenza;
		this.nInizialeMigranti=migranti;
		
		coda = new PriorityQueue<Event>();
		persone = new HashMap<Country,Integer>();
	
		for(Country c : grafo.vertexSet()) {
			persone.put(c, 0);
		}
		
		coda.add(new Event(1,nazioneIniziale,migranti));
	}
	
	public void run() {
		while(!coda.isEmpty()) {
			Event e = coda.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		int stanziali = e.getPersone()/2; //approssima per difetto
		int migranti = e.getPersone()-stanziali;
		int confinanti = grafo.degreeOf(e.getNazione());
		int gruppiMigranti = migranti/confinanti;
		stanziali += migranti - gruppiMigranti*confinanti;
		
		persone.put(e.getNazione(), persone.get(e.getNazione())+stanziali);
		nPassi = e.getTime();
		
		if(gruppiMigranti!=0) {
			for(Country c : Graphs.neighborListOf(grafo, e.getNazione())){
			coda.add(new Event(e.getTime()+1, c, gruppiMigranti));
			}
		}
		
	}

	public int getnPassi() {
		return nPassi;
	}

	public Map<Country, Integer> getPersone() {
		return persone;
	}
	
	
}
