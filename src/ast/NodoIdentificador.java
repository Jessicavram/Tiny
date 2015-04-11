package ast;

public class NodoIdentificador extends NodoBase {
	private String nombre;
	private Integer tamano;
	private NodoBase siguiente;
	
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
	
	public NodoIdentificador(String nombre) {
		super();
		this.nombre 	= nombre;
		this.tamano 	= null;		
		this.siguiente 	= null;
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
	
}
