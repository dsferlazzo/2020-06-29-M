package it.polito.tdp.imdb.model;

public class Corrispondenza {

	private int idDirector1;
	private int idDirector2;
	private int peso;
	public Corrispondenza(int idDirector1, int idDirector2, int peso) {
		super();
		this.idDirector1 = idDirector1;
		this.idDirector2 = idDirector2;
		this.peso = peso;
	}
	public int getIdDirector1() {
		return idDirector1;
	}
	public int getIdDirector2() {
		return idDirector2;
	}
	public int getPeso() {
		return peso;
	}
	
	
}
