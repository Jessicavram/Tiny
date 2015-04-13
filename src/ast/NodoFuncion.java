package ast;

public class NodoFuncion extends NodoBase {
	private NodoBase args;
	private NodoBase sent;
	private String nombre;
	
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
	}
	
	public NodoFuncion (String nombre,NodoBase algo, boolean x){
		super();
		this.nombre=nombre;
		if (x){
			this.args= null;
			this.sent= algo;
		}else{
			this.args= algo;
			this.sent= null;			
		}
		
				
	}

	public NodoFuncion (String nombre){
		super();
		this.nombre=nombre;
		this.args=null;
		this.sent=null;
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
