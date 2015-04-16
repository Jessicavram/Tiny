package compilador;
import ast.*;
public class Semantico {
	
	private TablaSimbolos tablaSimbolos;
	private String ultimoAmbito;
	
	public boolean debug = true; 
	

	
	public Semantico(TablaSimbolos tablaSimbolos) {
		super();
		this.tablaSimbolos = tablaSimbolos;

	}

	public void recorrerArbol(NodoBase raiz){
		while (raiz != null) {
		    if (raiz instanceof NodoIdentificador){
	 	    	
		    	// Compruebo que la variable ha sido declara en el ambito
		    	verificarExistenciaDeVariable(((NodoIdentificador)raiz).getNombre());
		    		
		    }
	
		    /* Hago el recorrido recursivo */
		    if (raiz instanceof  NodoIf){
		    	if (comprobarTipo(((NodoIf)raiz).getPrueba()) != "Boolean")
		    		printError("Error: no se puede probar la expresion en el if");
		    	
		    	recorrerArbol(((NodoIf)raiz).getParteThen());
		    	if(((NodoIf)raiz).getParteElse()!=null){
		    		recorrerArbol(((NodoIf)raiz).getParteElse());
		    	}
		    	
		    }
		    else if (raiz instanceof  NodoRepeat){
		    	recorrerArbol(((NodoRepeat)raiz).getCuerpo());
		    	if (comprobarTipo(((NodoIf)raiz).getPrueba()) != "Boolean")
		    		printError("Error: no se puede probar la expresion en el if");
		    }
		    else if (raiz instanceof  NodoAsignacion){	    	
		    	// Compruebo que la variable a asignar ha sido declara en el ambito
		    	String identificador = ((NodoAsignacion)raiz).getIdentificador();
		    	
		    	if (verificarExistenciaDeVariable(identificador)){	    	
		    		String tipo = tablaSimbolos.getTipo(ultimoAmbito, identificador);
		    		print(tipo +" "+ comprobarTipo(((NodoAsignacion)raiz).getExpresion()));
		    		if(comprobarTipo(((NodoAsignacion)raiz).getExpresion()) != tipo){
		    			printError("Asignacion a variable de diferente tipo");
		    		}
		    	
		    	}
		    }
		    else if (raiz instanceof  NodoEscribir)
		    	recorrerArbol(((NodoEscribir)raiz).getExpresion());
		    else if (raiz instanceof NodoOperacion){
		    	
				NodoBase tipoIzquierdo 	= (NodoBase) ((NodoOperacion)raiz).getOpIzquierdo();
				NodoBase tipoDerecho 	= (NodoBase) ((NodoOperacion)raiz).getOpDerecho();
	
				// Comprobacion de tipo en expresion
				if( comprobarTipo(tipoDerecho) != comprobarTipo(tipoIzquierdo) )
					printError("Tipos diferentes");
		    	
		    }
		    else if (raiz instanceof NodoDeclaracion) {
		    	recorrerArbol(((NodoDeclaracion)raiz).getVariable());
		    } 	    
		    else if (raiz instanceof NodoFuncion) {
		    	ultimoAmbito = ((NodoFuncion)raiz).getNombre();	// Cambio el ambito cuando entro a una funcion    	    	
		    	recorrerArbol(((NodoFuncion)raiz).getSent());
		    } 
		    else if (raiz instanceof NodoProgram) {
		    	if(((NodoProgram)raiz).getFunctions()!=null){
		    		recorrerArbol(((NodoProgram)raiz).getFunctions());
		    	}	
		    	ultimoAmbito = "main";
		    	recorrerArbol(((NodoProgram)raiz).getMain());
		    }  
		    	
		    raiz = raiz.getHermanoDerecha();
		
	  }
		
	}
		
	
	private String comprobarTipo(NodoBase nodo){
		if (nodo instanceof NodoOperacion){
			if(((NodoOperacion)nodo).getOperacion() == tipoOp.and || ((NodoOperacion)nodo).getOperacion() == tipoOp.or){
				// Verificar que tipo izquierdo y derecho sean boolean				
				String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
				String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
				if( tipoIzquierdo == tipoDerecho )
					return tipoIzquierdo;
				
			}else if(((NodoOperacion)nodo).getOperacion() == tipoOp.igual
					|| ((NodoOperacion)nodo).getOperacion() == tipoOp.noigual){
				String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
				String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
				if( tipoIzquierdo == "Boolean" && tipoDerecho == "Boolean" )
					return "Boolean";	
				else 
					return "Int";
			} else if(((NodoOperacion)nodo).getOperacion() == tipoOp.mas 
					|| ((NodoOperacion)nodo).getOperacion() == tipoOp.menos
					|| ((NodoOperacion)nodo).getOperacion() == tipoOp.por
					|| ((NodoOperacion)nodo).getOperacion() == tipoOp.entre){ 
				String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
				String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
				if( tipoIzquierdo == "Int" && tipoDerecho == "Int")									
					return "Int";
				else
					return "Boolean";
				
			} else {
				String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
				String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
				if( tipoIzquierdo == "Int" && tipoDerecho == "Int")
					return "Boolean";
				else 
					return "Int";
			}
		}
		else if(nodo instanceof NodoCallFuncion){
			// Falta que callfuncion tenga tipo y redevolverlo

	    			
		}
		
		else if(nodo instanceof NodoIdentificador){
			
		    String identificador = ((NodoIdentificador)nodo).getNombre();
			    if( verificarExistenciaDeVariable(identificador)){
				String tipoIdentificador = tablaSimbolos.getTipo(ultimoAmbito, identificador);
				return tipoIdentificador;
		    }
		}
		else if(nodo instanceof NodoValor){	
			// falta mejorar
			((NodoValor)nodo).getValor() ;
		}
		
		return "";
	}
	
	private boolean verificarExistenciaDeVariable(String identificador){ 
    	// Compruebo que la variable ha sido declara en el ambito
    	if(!tablaSimbolos.buscarTabla(ultimoAmbito, identificador)){
    		printError("Error Semantico: La variable " + identificador +" en la funcion "+ ultimoAmbito + " no ha sido declarado");
    		return false;
    	} else {
    		return true;
    	}
	}
	
	private void print(Object chain){
		System.out.println(chain);
	}
	
	private void printError(Object chain){		
		System.err.println(chain);
		System.exit(0);
	}	
}

