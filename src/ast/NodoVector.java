package ast;

public class NodoVector extends NodoBase {
	private String nombre;
	private Integer tamano;
	private NodoBase siguiente;
	private NodoBase expresion;
	
	public NodoVector(String nombre, Integer tamano){
		super();
		this.nombre=nombre;
		this.tamano=tamano;
		this.siguiente=null;
		this.expresion=null;
	}
	public NodoVector(String nombre, NodoBase expresion){
		super();
		this.nombre=nombre;
		this.expresion=expresion;
		this.tamano=null;
		this.siguiente=null;
	}
	public NodoVector(NodoBase actual, NodoBase siguiente) {
		super();
		this.nombre 	= ((NodoVector)actual).nombre;
		this.tamano 	= null;
		this.siguiente 	= siguiente;		
	}	
	public NodoVector(){
		super();
	}
	public String getNombre(){
		return nombre;
	}
	public int getTamano(){
		return tamano;
	}
	public NodoBase getSiguiente(){
		return siguiente;
	}
	public NodoBase getExpresion(){
		return expresion;
	}
}
