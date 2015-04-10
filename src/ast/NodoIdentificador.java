package ast;

public class NodoIdentificador extends NodoBase {
	private String nombre;
	private NodoBase siguiente;
	
	public NodoIdentificador(String nombre, NodoBase siguiente) {
		super();
		this.nombre = nombre;
		this.siguiente = siguiente;
	}
	public NodoIdentificador(String nombre) {
		super();
		this.nombre = nombre;
		this.siguiente = null;
	}
	
	public NodoIdentificador() {
		super();
	}

	public String getNombre() {
		return nombre;
	}
	public NodoBase getSiguiente() {
		return siguiente;
	}
	
}
