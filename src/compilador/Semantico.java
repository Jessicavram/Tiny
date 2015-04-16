package compilador;
import ast.*;
public class Semantico {
	
	private TablaSimbolos tablaSimbolos;
	private String ultimoAmbito;

	
	public Semantico(TablaSimbolos tablaSimbolos) {
		super();
		this.tablaSimbolos = tablaSimbolos;

	}

	public int cargarTabla(NodoBase raiz){
		while (raiz != null) {
	    if (raiz instanceof NodoIdentificador){
	    	String identificador = ((NodoIdentificador)raiz).getNombre(); 
	    	
	    	// Compruebo que la variable ha sido declara en el ambito
	    	if(!tablaSimbolos.buscarTabla(ultimoAmbito, identificador)){
	    		System.err.println("Error Semantico: La variable " + identificador +" en la funcion "+ ultimoAmbito + " no ha sido declarado");
	    	}	    		    	
	    	
	    }

	    /* Hago el recorrido recursivo */
	    if (raiz instanceof  NodoIf){
	    	cargarTabla(((NodoIf)raiz).getPrueba());
	    	cargarTabla(((NodoIf)raiz).getParteThen());
	    	if(((NodoIf)raiz).getParteElse()!=null){
	    		cargarTabla(((NodoIf)raiz).getParteElse());
	    	}
	    }
	    else if (raiz instanceof  NodoRepeat){
	    	cargarTabla(((NodoRepeat)raiz).getCuerpo());
	    	cargarTabla(((NodoRepeat)raiz).getPrueba());
	    }
	    else if (raiz instanceof  NodoAsignacion){
	    	cargarTabla(((NodoAsignacion)raiz).getExpresion());
	    }
	    else if (raiz instanceof  NodoReturn){
	    	cargarTabla(((NodoReturn)raiz).getExpresion());
	    }
	    else if (raiz instanceof  NodoEscribir)
	    	cargarTabla(((NodoEscribir)raiz).getExpresion());
	    else if (raiz instanceof NodoOperacion){
	    	Integer a,b;
	    	a = cargarTabla(((NodoOperacion)raiz).getOpIzquierdo());
	    	b = cargarTabla(((NodoOperacion)raiz).getOpDerecho());

	    	
	    }
	    else if (raiz instanceof NodoDeclaracion) {
	    	cargarTabla(((NodoDeclaracion)raiz).getVariable());
	    } 	    
	    else if (raiz instanceof NodoFuncion) {
	    	ultimoAmbito = ((NodoFuncion)raiz).getNombre();	// Cambio el ambito cuando entro a una funcion    	    	
	    	cargarTabla(((NodoFuncion)raiz).getSent());
	    } 
	    else if (raiz instanceof NodoProgram) {
	    	if(((NodoProgram)raiz).getFunctions()!=null){
	    		cargarTabla(((NodoProgram)raiz).getFunctions());
	    	}	
	    	ultimoAmbito = "main";
	    	cargarTabla(((NodoProgram)raiz).getMain());
	    }  
	    	
	    raiz = raiz.getHermanoDerecha();
		
	  }
		return -1;
	}
	
	private void print(Object chain){
		System.out.println(chain);
	}
}
