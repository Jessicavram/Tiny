package ast;

public class NodoFuncion extends NodoBase {
	private NodoBase args;
	private NodoBase sent;
	private String nombre;
	private NodoBase siguiente;
	
	public NodoFuncion (){
		super();
		this.args =null;
		this.sent = null;
		this.nombre="";
	}
	
	public NodoFuncion (String nombre,NodoBase args, NodoBase sent){
		super();
		this.nombre=nombre;
		this.args= args;
		this.sent= sent;
		this.siguiente=null;
	}
	public NodoFuncion (String nombre, NodoBase args, NodoBase sent, NodoBase siguiente){
		super();
		this.nombre=nombre;
		this.args= args;
		this.sent= sent;
		this.siguiente=siguiente;
	}
	
	public NodoFuncion (String nombre,NodoBase sent){
		super();
		this.nombre=nombre;
		this.args= null;
		this.sent= sent;
		this.siguiente=null;
	}

	public NodoFuncion (String nombre){
		super();
		this.nombre=nombre;
		this.args=null;
		this.sent=null;
		this.siguiente=null;
	}
	
	public NodoBase getArgs(){
		return args;
	}
	public NodoBase getSent(){		
		return sent;
	}
	public String getNombre(){		
		return nombre;
	}	
}
