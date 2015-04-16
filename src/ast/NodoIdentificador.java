package ast;

public class NodoIdentificador extends NodoBase {
	private String nombre;
	private Integer tamano;
	private NodoBase siguiente;
	private NodoBase expresion;
	
	public NodoIdentificador(String nombre, Integer tamano) {
		super();
		this.nombre 	= nombre;
		this.tamano 	= tamano;
		this.siguiente 	= null;
	}	
	
	public NodoIdentificador(String nombre, NodoBase siguiente) {
		super();
		this.nombre 	= nombre;
		this.tamano 	= null;
		this.siguiente 	= siguiente;		
	}	
	public NodoIdentificador(String nombre,NodoBase expresion, NodoBase siguiente){
		this.nombre= nombre;
		this.expresion=expresion;
		this.siguiente=null;
	}
	
	public NodoIdentificador(String nombre) {
		super();
		this.nombre 	= nombre;
		this.tamano 	= null;		
		this.siguiente 	= null;
	}
	
	public NodoIdentificador(NodoBase actual, NodoBase siguiente) {
		super();
		this.nombre 	= ((NodoIdentificador)actual).nombre;
		this.tamano 	= ((NodoIdentificador)actual).tamano;
		this.siguiente 	= siguiente;		
	}	
	public NodoIdentificador() {
		super();
	}

	public String getNombre() {
		return nombre;
	}
	public Integer getTamano() {
		return tamano;
	}	
	public NodoBase getSiguiente() {
		return siguiente;
	}
	public NodoBase getExpresion(){
		return expresion;
	}
	
}
