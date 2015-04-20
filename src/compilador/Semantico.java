package compilador;
import java.util.ArrayList;

import ast.*;
public class Semantico {
	
	private TablaSimbolos tablaSimbolos;
	private String ultimoAmbito;	
	public boolean debug = true;
	public boolean anyError = false;
	
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
		    	printError("No se puede probar la expresion en el if");
		    	
		    	recorrerArbol(((NodoIf)raiz).getParteThen());
		    	if(((NodoIf)raiz).getParteElse()!=null){
		    		recorrerArbol(((NodoIf)raiz).getParteElse());
		    	}
		    	
		    }
		    else if (raiz instanceof  NodoRepeat){
		    	recorrerArbol(((NodoRepeat)raiz).getCuerpo());
		    	if (comprobarTipo(((NodoRepeat)raiz).getPrueba()) != "Boolean")
		    		printError("No se puede probar la expresion en el repeat");
		    }
		    else if (raiz instanceof  NodoAsignacion){	    	
		    	// Compruebo que la variable a asignar ha sido declara en el ambito
		    	String identificador = ((NodoAsignacion)raiz).getIdentificador();
		    	
		    	if (verificarExistenciaDeVariable(identificador)){	    	
		    		String tipo = tablaSimbolos.getTipo(ultimoAmbito, identificador);
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
		    else if (raiz instanceof NodoFor) {
		    	NodoBase nodo = (((NodoFor)raiz).getAsignacion());
		
		    	recorrerArbol(nodo);
		    	if (comprobarTipo(((NodoAsignacion)nodo).getExpresion()) != "Int")
		    		printError("No se puede probar la expresion de asignacion en la sentencia for");
		    	if (comprobarTipo(((NodoFor)raiz).getPrueba()) != "Boolean")
		    		printError("No se puede probar la expresion prueba en la sentencia for");
		   
		    	nodo = (((NodoFor)raiz).getPaso());
		    	recorrerArbol(nodo);
		    	if (comprobarTipo(((NodoAsignacion)nodo).getExpresion()) != "Int")
		    		printError("No se puede probar la expresion paso en la sentencia for");		    	
		    	
		    	recorrerArbol(((NodoFor)raiz).getCuerpo());		
		    }
		    else if (raiz instanceof NodoFuncion) {
		    	ultimoAmbito = ((NodoFuncion)raiz).getNombre();	// Cambio el ambito cuando entro a una funcion    	    	
		    	//Buscar el return
		    	if(( (((NodoFuncion)raiz).getTipo())=="Int" || (((NodoFuncion)raiz).getTipo())=="Boolean") && !recorrerFuncion(((NodoFuncion)raiz).getSent(),((NodoFuncion)raiz).getTipo(),((NodoFuncion)raiz).getNombre()))
		    		printError("La funcion "+((NodoFuncion)raiz).getNombre()+" debe contener una clausula RETURN");
		    	recorrerArbol(((NodoFuncion)raiz).getSent());
		    } 
		    else if (raiz instanceof NodoCallFuncion) {	
		    	
	    		ArrayList<String> arrayArgumentos 	= new ArrayList<String>();
		    	String nombreFuncion 				= ((NodoCallFuncion)raiz).getNombre();
		    	if(!tablaSimbolos.buscarAmbito(nombreFuncion)){
			    	if(((NodoCallFuncion)raiz).getArgs() != null){
			    		recorrerArbol((((NodoCallFuncion)raiz).getArgs()));
				    	recorrerArgumentos((((NodoCallFuncion)raiz).getArgs()),arrayArgumentos);
				    	if (!tablaSimbolos.getArrayArguments( nombreFuncion).equals(arrayArgumentos) ){ 
				    		printError("Llamada a funcion "+nombreFuncion+" invalida, debe ser "+nombreFuncion+ "(" +tablaSimbolos.getArrayArguments( nombreFuncion) +",)");
				    	}		    		
			    	} else if (tablaSimbolos.getArrayArguments( nombreFuncion) != null ){
			    		if(tablaSimbolos.getArrayArguments( nombreFuncion).size() != 0 )
			    			printError("LLamada a funcion "+nombreFuncion+" invalida, faltan argumanetos, debe ser "+nombreFuncion+ "(" +tablaSimbolos.getArrayArguments( nombreFuncion) +",)");
			    	}
		    	} else 
		    		printError("La funcion "+nombreFuncion+" no puede ser llamada si no ha sido declarada");
		    	
		    	
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
			if(((NodoOperacion)nodo).getOperacion() 	== tipoOp.and 
				|| ((NodoOperacion)nodo).getOperacion() == tipoOp.or){
				// Verificar que tipo izquierdo y derecho sean boolean				
				String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
				String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
				if( tipoIzquierdo == "Boolean" && tipoDerecho == "Boolean" )
					return "Boolean";	
				else{
					printError("Operandos de diferente tipo");
					return "Otro";
				}
				
			} else if(((NodoOperacion)nodo).getOperacion() 	== tipoOp.igual
					|| ((NodoOperacion)nodo).getOperacion() == tipoOp.noigual){
				String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
				String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
				if( tipoIzquierdo == "Boolean" && tipoDerecho == "Boolean" )
					return "Boolean";	
				else if(tipoIzquierdo=="Int" && tipoDerecho=="Int")
					return "Boolean";
				else {
					printError("Operandos de diferente tipo");
				}
			} else if(((NodoOperacion)nodo).getOperacion() 	== tipoOp.mas 
					|| ((NodoOperacion)nodo).getOperacion() == tipoOp.menos
					|| ((NodoOperacion)nodo).getOperacion() == tipoOp.por
					|| ((NodoOperacion)nodo).getOperacion() == tipoOp.entre){ 
				String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
				String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
				if( tipoIzquierdo == "Int" && tipoDerecho == "Int")									
					return "Int";
				else{
					printError("Operandos de diferente tipo");
					return "Otro";
				}
				
			} else {
				String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
				String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
				if( tipoIzquierdo == "Int" && tipoDerecho == "Int")
					return "Boolean";
				else {
					printError("Operandos de diferente tipo");
					return "Otro";
				}
			}
		}
		else if(nodo instanceof NodoCallFuncion){
			String nombreFuncion =  ((NodoCallFuncion)nodo).getNombre();
			if(!tablaSimbolos.buscarAmbito(nombreFuncion))
				return tablaSimbolos.getTipoFuncion( nombreFuncion ) ;	
			else {
	    		printError("La funcion "+nombreFuncion+" no puede ser llamada si no ha sido declarada");
				return "Otro";
			}
		}		
		else if(nodo instanceof NodoIdentificador){			
		    String identificador = ((NodoIdentificador)nodo).getNombre();
		    if( verificarExistenciaDeVariable(identificador)){
				String tipoIdentificador = tablaSimbolos.getTipo(ultimoAmbito, identificador);
				return tipoIdentificador;
		    }
		}
		else if(nodo instanceof NodoValor){	
			Integer tipo= ((NodoValor)nodo).getTipo();
			if(tipo==0)
				return "Int";			
			if(tipo==1)
				return "Boolean";	
		}
		
		return "";
	}
	private boolean verificarExistenciaDeVariable(String identificador){ 
    	// Compruebo que la variable ha sido declara en el ambito
    	if(!tablaSimbolos.buscarTabla(ultimoAmbito, identificador)){
    		printError("La variable " + identificador +" en la funcion "+ ultimoAmbito + " no ha sido declarado");
    		return false;
    	} else {
    		return true;
    	}
	}
	
	private void recorrerArgumentos(NodoBase nodo, ArrayList<String> arrayArgumentos ){
		if (nodo instanceof NodoIdentificador){
			// Buscar que la variable exista y sea del tipo correcto
			String identificador = ((NodoIdentificador)nodo).getNombre();
		    
			if(verificarExistenciaDeVariable(identificador)){
				String tipoIdentificador = tablaSimbolos.getTipo(ultimoAmbito, identificador);
				arrayArgumentos.add(tipoIdentificador);
			}
			
		} else if (nodo instanceof NodoCallFuncion){
			String tipoDeFunction = tablaSimbolos.getTipoFuncion(((NodoCallFuncion)nodo).getNombre());			
			arrayArgumentos.add(tipoDeFunction);

		} else if ( nodo instanceof NodoOperacion){
			String tipoIzquierdo = comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
			String tipoDerecho = comprobarTipo(((NodoOperacion)nodo).getOpDerecho());
			
			if( tipoIzquierdo == tipoDerecho){
				
				arrayArgumentos.add(tipoDerecho);
			} else{			
				printError("Tipos diferentes");
			}
		} else if (nodo instanceof NodoValor){
			Integer tipo = ((NodoValor)nodo).getTipo(); 
			if (tipo == 0)
				arrayArgumentos.add("Int");
			else if (tipo == 1)
				arrayArgumentos.add("Boolean");
		}
		
		if ( ((NodoBase)nodo).getHermanoDerecha() != null)
				recorrerArgumentos(((NodoBase)nodo).getHermanoDerecha(),arrayArgumentos);
		
	}
	private boolean recorrerFuncion(NodoBase raiz,String Tipo,String nombre){
		boolean ban=false;
		while (raiz != null) {
			if(raiz instanceof NodoReturn){				
			    ban=true;
			    if (comprobarTipo(((NodoReturn)raiz).getExpresion())!=Tipo)
			    	System.err.println("El tipo de dato retornado en la funcion "+nombre+" no corresponde. Debe ser tipo "+Tipo);
			}
			raiz = raiz.getHermanoDerecha();
		}
		return ban;
	}	
	private void printError(Object chain){		
		System.err.println("[Error Semantico]: "+chain);
		this.anyError = true;
	}	
}

