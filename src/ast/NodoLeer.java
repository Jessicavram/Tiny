package ast;

public class NodoLeer extends NodoBase {
	private String id;
	public NodoBase Vector;
	public NodoBase Posicion;
	
	public NodoLeer(String identificador) {
		super();
		this.id = identificador;
	}
	public NodoLeer(NodoBase vector){
		super();
		this.Vector=vector;
		this.id=((NodoIdentificador)vector).getNombre();
		this.Posicion=((NodoIdentificador)vector).getExpresion();
	}

	public NodoLeer() {
		super();
		id="";
	}
    public NodoBase getPosicion(){
    	return Posicion;
    }
	public String getIdentificador() {
		return id;
	}

	public void setExpresion(String identificador) {
		this.id = identificador;
	}

}
