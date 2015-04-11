package ast;

public class NodoFuncion extends NodoBase {
	private NodoBase args;
	private NodoBase sent;
	
	public NodoFuncion (){
		super();
		this.args =null;
		this.sent = null;
	}
	
	public NodoFuncion (NodoBase args, NodoBase sent){
		super();
		this.args= args;
		this.sent= sent;		
	}
	
	public NodoFuncion (NodoBase sent){
		super();
		this.args= null;
		this.sent= sent;		
	}
	
	public NodoBase getArgs(){
		return args;
	}
	public NodoBase getSent(){		
		return sent;
	}
	
	
	
}
