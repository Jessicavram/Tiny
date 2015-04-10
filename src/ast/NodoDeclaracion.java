package ast;

public class NodoDeclaracion extends NodoBase {
	private String tipo;
	private NodoBase variable;
	
	public NodoDeclaracion(String tipo, NodoBase variable) {
		super();
		this.tipo = tipo;
		this.variable = variable;
	}

	public NodoDeclaracion() {
		super();
	}

	public String getTipo() {
		return tipo;
	}
	public NodoBase getVariable(){
		return variable;
	}
}
