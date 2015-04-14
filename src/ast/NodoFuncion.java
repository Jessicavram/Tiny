package ast;

public class NodoFuncion extends NodoBase {
	private String tipo;
	private NodoBase args;
	private NodoBase sent;
	private String nombre;
	private NodoBase siguiente;
	private int num_arg;
	
	public NodoFuncion (){
		super();
		this.args =null;
		this.sent = null;
		this.nombre="";
		this.tipo=null;
		this.num_arg=0;
	}
	
	public NodoFuncion (String tipo,String nombre,NodoBase args, NodoBase sent){
		super();
		this.tipo = tipo;
		this.nombre=nombre;
		this.args= args;
		this.sent= sent;
		this.siguiente=null;
		this.num_arg=0;
	}
	public NodoFuncion (String tipo,String nombre, NodoBase args, NodoBase sent, NodoBase siguiente){
		super();
		this.tipo = tipo;
		this.nombre=nombre;
		this.args= args;
		this.sent= sent;
		this.siguiente=siguiente;
		this.num_arg=0;
	}
	
	public NodoFuncion (String tipo,String nombre,NodoBase sent){
		super();
		this.tipo = tipo;
		this.nombre=nombre;
		this.args= null;
		this.sent= sent;
		this.siguiente=null;
		this.num_arg=0;
	}

	public NodoFuncion (String tipo,String nombre){
		super();
		this.tipo=tipo;
		this.nombre=nombre;
		this.args=null;
		this.sent=null;
		this.siguiente=null;
		this.num_arg=0;
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
	public String getTipo(){		
		return tipo;
	}
	public int getNum(){		
		return num_arg;
	}
	
	public void setNum(int numero){		
		this.num_arg = numero;
	}
}
