package ast;

public class NodoFor extends NodoBase 
{
	private NodoBase asignacion;
	private NodoBase prueba;
	private NodoBase paso;
	private NodoBase cuerpo;
	
	public NodoFor(NodoBase cuerpo, NodoBase prueba, NodoBase asignacion, NodoBase paso) {
		super();
		this.cuerpo = cuerpo;
		this.prueba = prueba;
		this.paso = paso;
		this.asignacion = asignacion;
	}
	
	public NodoFor() {
		super();
		this.cuerpo = null;
		this.prueba = null;	
		this.paso = null;
		this.asignacion = null;
	}

	public NodoBase getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(NodoBase cuerpo) {
		this.cuerpo = cuerpo;
	}

	public NodoBase getPrueba() {
		return prueba;
	}

	public void setPrueba(NodoBase prueba) {
		this.prueba = prueba;
	}
	
	public NodoBase getPaso() {
		return paso;
	}

	public void setPaso(NodoBase paso) {
		this.paso = paso;
	}
	
	public NodoBase getAsignacion() {
		return asignacion;
	}

	public void setAsignacion(NodoBase asignacion) {
		this.asignacion = asignacion;
	}
	
}
