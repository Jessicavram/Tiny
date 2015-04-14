package compilador;
import ast.*;
public class Semantico {
	
	private TablaSimbolos tablaSimbolos;

	public Semantico(TablaSimbolos tablaSimbolos) {
		super();
		this.tablaSimbolos = tablaSimbolos;

	}

	public int cargarTabla(NodoBase raiz){
		while (raiz != null) {
	    if (raiz instanceof NodoIdentificador){
	    	String identificador = ((NodoIdentificador)raiz).getNombre(); 
	    	
	    	// Buscar en la tabla de simbolos
	    	
	    	RegistroSimbolo simbolo =(RegistroSimbolo) tablaSimbolos.getTipo("main", identificador);
	    	print("hola");
	    	
	    	
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
	    	cargarTabla(((NodoFuncion)raiz).getSent());
	    } 
	    	
	    raiz = raiz.getHermanoDerecha();
		
	  }
		return -1;
	}
	
	private void print(Object chain){
		System.out.println(chain);
	}
}
