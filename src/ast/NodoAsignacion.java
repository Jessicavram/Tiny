package ast;

public class NodoAsignacion extends NodoBase {
	private String identificador;
	private NodoBase expresion;
	private NodoBase posicion;
	
	public NodoAsignacion(String identificador) {
		super();
		this.identificador = identificador;
		this.expresion = null;
	}
	
	public NodoAsignacion(String identificador, NodoBase expresion) {
		super();
		this.identificador = identificador;
		this.expresion = expresion;
	}
	public NodoAsignacion(NodoBase vector, NodoBase expresion){
		super();
		this.identificador=((NodoIdentificador)vector).getNombre();
		this.expresion=expresion;
		this.posicion=((NodoIdentificador)vector).getExpresion();
	}
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public NodoBase getExpresion() {
		return expresion;
	}

	public void setExpresion(NodoBase expresion) {
		this.expresion = expresion;
	}
	public NodoBase getPosicion(){
		return posicion;
	}
	
	
}
