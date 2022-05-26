package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Ricorsione {

	private List<Director> soluzione;
	private int maxActors;
	private Director regista;
	private Graph<Director, DefaultWeightedEdge> grafo;
	
	public List<Director> iniziaRicorsione(int maxActors, Director regista, Graph<Director, DefaultWeightedEdge> grafo){
		this.maxActors = maxActors;
		this.regista = regista;
		this.grafo = grafo;
		
		List<Director> rimanenti = Graphs.successorListOf(grafo, regista);
		List<Director> parziale = new ArrayList<Director>();
		int remainingActors = maxActors;
		
		this.Ricorsione(parziale, rimanenti, remainingActors, 0);	//NELL'INIZIALIZZAZIONE NON HO ANCORA SVOLTO IL PASSO 0 DELLA RICORSIONE
		
		return soluzione;
	}
	
	public void Ricorsione(List<Director> parziale, List<Director> rimanenti, int remainingActors, int livello) {
		
		if(rimanenti.isEmpty()) {		//PER QUANDO MINACTORS E MAGGIORI DEGLI "ATTORI" TOTALI
			soluzione = parziale;
			return;
		}
		
		
		int minActors = 0;
		Director maRegista = null;
		for(Director d : rimanenti) {
			int peso = (int) grafo.getEdgeWeight(grafo.getEdge(d, regista)); 
			if(peso<minActors || minActors==0) {
				minActors = peso;
				maRegista = d;
			}
		}
		
		if(minActors<=remainingActors) {
			remainingActors -= minActors;
			parziale.add(maRegista);
			rimanenti.remove(maRegista);
			this.Ricorsione(parziale, rimanenti, remainingActors, livello+1);
			
		}
		else {
			soluzione=parziale;
			return;
		}
	
		
	}
}
