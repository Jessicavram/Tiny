package ast;

public class NodoCallFuncion extends NodoBase {
	private NodoBase args;
	private String nombre;
	
	public NodoCallFuncion (){
		super();
		this.args =null;
		this.nombre="";
	}
	
	public NodoCallFuncion (String nombre,NodoBase args){
		super();
		this.nombre=nombre;
		this.args= args;		
	}
	public NodoCallFuncion (String nombre){
		super();
		this.nombre=nombre;
		this.args= null;		
	}
	
	public NodoBase getArgs(){
		return args;
	}
	public String getNombre(){		
		return nombre;
	}	
}

