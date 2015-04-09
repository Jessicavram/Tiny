package ast;

public class NodoTipo extends NodoBase {
	private NodoBase izquierdo;

	public NodoTipo(NodoBase izquierdo) {
		super();
		this.izquierdo = izquierdo;
	}

	public NodoTipo() {
		super();
	}
	
	public NodoBase getTipo() {
		return izquierdo;
	}
}
