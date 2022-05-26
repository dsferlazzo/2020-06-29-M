package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Director, DefaultWeightedEdge> grafo;
	private List<Director> directors;
	private Map<Integer, Director> mapDirectorId;
	
	public List<Director> getDirectors(){
		return directors;
	}
	
	public List<Director> getDirectorsByYear(int anno) {		//DA IMPLEMENTARE EVENTUALE IDMAP
		dao = new ImdbDAO();
		mapDirectorId = new HashMap<Integer, Director>();
		directors = dao.getDirectorsByYear(anno);
		for(Director d : directors)
			mapDirectorId.put(d.getId(), d);
		return directors;
	}
	
	public List<Corrispondenza> getCorrispondenzeByYear(int anno){
		dao = new ImdbDAO();
		return this.dao.getCorrispondenzeByYear(anno);
	}
	
	public String creaGrafo(int anno) {
		dao = new ImdbDAO();
		grafo = new SimpleWeightedGraph<Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//AGGIUNGO I VERTICI
		
		Graphs.addAllVertices(grafo, this.getDirectorsByYear(anno));
		
		//AGGIUNGO GLI ARCHI
		
		List<Corrispondenza> corrispondenze = this.getCorrispondenzeByYear(anno);
		
		for(Corrispondenza c : corrispondenze) {
			Graphs.addEdge(grafo, mapDirectorId.get(c.getIdDirector1()),
					mapDirectorId.get(c.getIdDirector2()), c.getPeso());
		}
		
	
		String result = "Grafo creato\n# VERTICI: " + grafo.vertexSet().size() +
				"\n# ARCHI: " + grafo.edgeSet().size() + "\n";
		return result;
		
	}
	
	public String getRegistiAdiacenti(Director regista){
		String resultS = "REGISTI ADIACENTI A : " + regista +"\n";
		List<Director> adiacenti = Graphs.successorListOf(grafo, regista);
		List<Director> result = new ArrayList<Director>();
		
		while(!adiacenti.isEmpty()) {
			int maxWeight=0;
			Director mwDirector=null;
			
			for(Director d : adiacenti) {
				int peso = (int) grafo.getEdgeWeight(grafo.getEdge(d, regista));
				if(peso>maxWeight) {
					maxWeight = peso;
					mwDirector = d;
				}
			}
			result.add(mwDirector);
			adiacenti.remove(mwDirector);
			resultS += mwDirector + " - #attori condivisi: " + maxWeight + "\n";
		}
		return resultS;
	}
	
	public String doRicorsione(int maxActors, Director regista) {
		Ricorsione r = new Ricorsione();
		List<Director> registi = r.iniziaRicorsione(maxActors, regista, grafo);
		
		String result="RISULTATO RICORSIONE:\n";
		
		for(Director d : registi) {
			int peso = (int) grafo.getEdgeWeight(grafo.getEdge(d, regista));
			result += d + " - attori condivisi: " + peso + "\n";
		}
		return result;
		
	}

}
