package ast;

public class NodoProgram extends NodoBase{
	private NodoBase functions;
	private NodoBase main;
	
	public NodoProgram(){
		super();
		this.functions = null;
		this.main = null;
	}
	
	public NodoProgram(NodoBase main){
		super();
		this.functions = null;
		this.main = main;
	}
	
	public NodoProgram(NodoBase functions,NodoBase main){
		super();
		this.functions = functions;
		this.main = main;
	}
	
	public NodoBase getFunctions(){
		return this.functions;
	}
	
	public NodoBase getMain (){
		return this.main;
	}
	
	
}
