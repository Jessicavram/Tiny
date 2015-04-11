package ast;

public class NodoIdentificador extends NodoBase {
	private String nombre;
	private NodoBase arg;
	private NodoBase siguiente;
	
	public NodoIdentificador(String nombre, NodoBase arg) {
		super();
		this.nombre = nombre;
		this.arg = arg;
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
