package compilador;

import java.util.ArrayList;

public class Funciones {
	private String tipo;
	private Integer iMem;
	private ArrayList<String> arrayArgumentos;
	
	Funciones(String tipo, Integer iMem, ArrayList<String> arrayArgumentos){
		this.tipo = tipo;
		this.iMem = iMem;
		this.arrayArgumentos = arrayArgumentos;
	}
	
	public String getTipo(){
		return tipo;
	}
	
	public Integer getiMem(){
		return iMem;
	}
	
	public ArrayList<String> getArrayArgumentos(){
		return arrayArgumentos;
	}
}
