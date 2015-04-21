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
			/* Hago el recorrido recursivo */
			if (raiz instanceof NodoIdentificador){	 	
				 verificarIdentificador(raiz);		    		
		    }	
			else if (raiz instanceof  NodoIf)
		    	verificarIf(raiz);
		    else if (raiz instanceof  NodoRepeat)
		    	verificarRepeat(raiz);
		    else if (raiz instanceof  NodoAsignacion)	    	
		    	verificarAsignacion(raiz);		    	
		    else if (raiz instanceof  NodoEscribir)
		    	verificarEscribir(raiz);    
		    else if (raiz instanceof NodoOperacion)		    	
		    	verificarOperacion(raiz);
		    else if (raiz instanceof NodoDeclaracion) 
		    	verificarDeclaracion(raiz); 
		    else if (raiz instanceof NodoFor) 
		    	verificarFor(raiz);
		    else if (raiz instanceof NodoFuncion)
		    	verificarFuncion(raiz);		     
		    else if (raiz instanceof NodoCallFuncion) 	
		    	verificarCallFuncion(raiz);		    
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
		
	// Nodos INIT	
	
	private void verificarIf(NodoBase nodo){
    	if (comprobarTipo(((NodoIf)nodo).getPrueba()) != "Boolean")
    		printError("No se puede probar la expresion en el if");
    	
    	recorrerArbol(((NodoIf)nodo).getParteThen());
    	if(((NodoIf)nodo).getParteElse()!=null){
    		recorrerArbol(((NodoIf)nodo).getParteElse());
    	}  			
	}
	
	private void verificarRepeat(NodoBase nodo){
    	recorrerArbol(((NodoRepeat)nodo).getCuerpo());
    	if (comprobarTipo(((NodoRepeat)nodo).getPrueba()) != "Boolean")
    		printError("No se puede probar la expresion en el repeat");		
	}
	
	private void verificarAsignacion(NodoBase nodo){
		//Compruebo que la variable a asignar ha sido declara en el ambito		
		recorrerAsignacion(nodo);
	}

	private void verificarEscribir(NodoBase nodo){
		recorrerArbol(((NodoEscribir)nodo).getExpresion());
	}

	private void verificarDeclaracion(NodoBase nodo){
//		recorrerArbol(((NodoDeclaracion)nodo).getVariable());
	}
	
	private void verificarOperacion(NodoBase nodo){
		NodoBase tipoIzquierdo 	= (NodoBase) ((NodoOperacion)nodo).getOpIzquierdo();
		NodoBase tipoDerecho 	= (NodoBase) ((NodoOperacion)nodo).getOpDerecho();

		// Comprobacion de tipo en expresion
		if( comprobarTipo(tipoDerecho) != comprobarTipo(tipoIzquierdo) )
			printError("Tipos diferentes");	
	}
	
	private void verificarFor(NodoBase raiz){
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
	
	private void verificarFuncion(NodoBase nodo){
    	ultimoAmbito = ((NodoFuncion)nodo).getNombre();	// Cambio el ambito cuando entro a una funcion    	    	
    	//Buscar el return
    	if(( (((NodoFuncion)nodo).getTipo())=="Int" || (((NodoFuncion)nodo).getTipo())=="Boolean") 
    			&& !recorrerFuncion(((NodoFuncion)nodo).getSent(),((NodoFuncion)nodo).getTipo(),((NodoFuncion)nodo).getNombre()))
    		printError("La funcion "+((NodoFuncion)nodo).getNombre()+" debe contener una clausula RETURN");
    	recorrerArbol(((NodoFuncion)nodo).getSent());
	}
	
	private void verificarCallFuncion(NodoBase nodo){
		ArrayList<String> arrayArgumentos 	= new ArrayList<String>();
    	String nombreFuncion 				= ((NodoCallFuncion)nodo).getNombre();
    	NodoBase argumentos 				= ((NodoCallFuncion)nodo).getArgs();
    	
    	// Si la funcion ha sido declarada
    	if(verificarExistenciaDeFuncion(nombreFuncion)){
    		// Si la funcion tiene argumentos
	    	if(argumentos != null){
	    		recorrerArbol((argumentos));			    		
		    	recorrerArgumentos((argumentos),arrayArgumentos);
		    	// Si la funcion recibe argumentos
		    	if (tablaSimbolos.getArrayArguments( nombreFuncion) != null) {
			    	if (!tablaSimbolos.getArrayArguments( nombreFuncion).equals(arrayArgumentos) ){ 
			    		printError("Llamada a funcion "+nombreFuncion+" invalida, debe ser "+nombreFuncion+ "(" +tablaSimbolos.getArrayArguments( nombreFuncion) +",)");
			    	}		
		    	} else {
		    		printError("Llamada a funcion "+nombreFuncion+" invalida, debe ser "+nombreFuncion+ "()");
		    	}
	    	} else if (tablaSimbolos.getArrayArguments( nombreFuncion) != null ){
	    		if(tablaSimbolos.getArrayArguments( nombreFuncion).size() != 0 )
	    			printError("LLamada a funcion "+nombreFuncion+" invalida, faltan argumanetos, debe ser "+nombreFuncion+ "(" +tablaSimbolos.getArrayArguments( nombreFuncion) +",)");
	    	}
    	} 
    	
    	
	}
		
	// Nodos END
	
	private String comprobarTipo(NodoBase nodo){
		if (nodo instanceof NodoOperacion){
			
			String tipoIzquierdo 	= comprobarTipo(((NodoOperacion)nodo).getOpIzquierdo());
			String tipoDerecho 		= comprobarTipo(((NodoOperacion)nodo).getOpDerecho());			
			tipoOp operador 		= ((NodoOperacion)nodo).getOperacion();
			
			if(operador == tipoOp.and || operador == tipoOp.or){
				// Si el operador es and o or ambos operando deben ser Boolean			
				if( tipoIzquierdo == "Boolean" && tipoDerecho == "Boolean" )
					return "Boolean";	
				else{
					printError("Operandos de diferente tipo");
					return "TyperEror";
				}
				
			} else if(operador == tipoOp.igual || operador == tipoOp.noigual){
				// Si el operador es igual o diferente debe ser ambos operandos Boolean o Int 
				if( tipoIzquierdo == "Boolean" && tipoDerecho == "Boolean" )
					return "Boolean";	
				else if(tipoIzquierdo=="Int" && tipoDerecho=="Int")
					return "Boolean";
				else {
					printError("Operandos de diferente tipo");
					return "TypeError";
				}
			} else if( operador 	== tipoOp.mas 
					|| operador == tipoOp.menos
					|| operador == tipoOp.por
					|| operador == tipoOp.entre){ 
				// Si el operador es + - * / ambos operandos tipos deben ser enteros
				if( tipoIzquierdo == "Int" && tipoDerecho == "Int")									
					return "Int";
				else{
					printError("Operandos de diferente tipo");
					return "TypeError";
				}
				
			} else {
				// Si no son ninguno de los anteriores son < <= > >= y tienen que ser ambos operandos Int
				if( tipoIzquierdo == "Int" && tipoDerecho == "Int")
					return "Boolean";
				else {
					printError("Operandos de diferente tipo");
					return "TypeError";
				}
			}
		}
		else if(nodo instanceof NodoCallFuncion){
			// Si es una funcion verificar que ha sido declarada y retornar tipo buscando en la tabla de simbolos
			String nombreFuncion =  ((NodoCallFuncion)nodo).getNombre();
			if(verificarExistenciaDeFuncion(nombreFuncion))
				return tablaSimbolos.getTipoFuncion( nombreFuncion );	
			else 
				return "TypeError";
	
		}		
		else if(nodo instanceof NodoIdentificador){
			// Si es un identificador verificar que ha sido declarada en el ambito y retornar tipo buscando en la tabla de simbolos		
		    String identificador = ((NodoIdentificador)nodo).getNombre();
		    verificarIdentificador(nodo);
		    if( verificarExistenciaDeVariable(identificador)){
				String tipoIdentificador = tablaSimbolos.getTipo(ultimoAmbito, identificador);
				return tipoIdentificador;
		    }
		    else
		    	return "TypeError";
		}
		else if(nodo instanceof NodoValor){	
			// Si es nodo valor retornar tipo de valor e.g. 1,2,3,4 o true o false
			Integer tipo= ((NodoValor)nodo).getTipo();
			if(tipo==0)
				return "Int";			
			if(tipo==1)
				return "Boolean";	
		}
		
		return "TypeError";
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
	
	private boolean verificarExistenciaDeFuncion(String nombreFuncion){
		if(tablaSimbolos.buscarAmbito(nombreFuncion)){
   			printError("La funcion "+nombreFuncion+" no ha sido declarada");
			return false;		
		} else 
			return true;
	}
	
	private void recorrerArgumentos(NodoBase nodo, ArrayList<String> arrayArgumentos ){
		if (nodo instanceof NodoIdentificador){
			recorrerArbol(nodo);
			
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
	
	private void recorrerAsignacion(NodoBase nodo){
    	String identificador = ((NodoAsignacion)nodo).getIdentificador();
    	
    	if (verificarExistenciaDeVariable(identificador)){	    	
    		String tipo = tablaSimbolos.getTipo(ultimoAmbito, identificador);
    		if(comprobarTipo(((NodoAsignacion)nodo).getExpresion()) != tipo){
    			printError("Asignacion a variable de diferente tipo");
    		}
    	}
    	boolean ifArray = tablaSimbolos.getIfArray(ultimoAmbito, identificador);	
    	if(ifArray){
    		if ( ((NodoAsignacion)nodo).getPosicion() == null) {
    			printError("El identificador " + identificador + " es vector y debe ser llamado usado: x[]");
    		} else{
    			recorrerArbol(((NodoAsignacion)nodo).getPosicion());
    			if (comprobarTipo(((NodoAsignacion)nodo).getPosicion()) != "Int") {
    				printError("El indice del vector "+identificador+" debe ser tipo Int");
    			}
    		}
    	} else {
    		if( ((NodoAsignacion)nodo).getPosicion() != null ){
    			printError("El identificador " + identificador + " no ha sido declarado como vector");
    		}
    	}
	}

	private boolean verificarIdentificador(NodoBase nodo){
		String nombre = ((NodoIdentificador)nodo).getNombre();	
		boolean retorno = true;
		if (verificarExistenciaDeVariable(nombre)){    	
			boolean esArray = tablaSimbolos.getIfArray(ultimoAmbito, nombre);
	    	if(esArray){
	    		if(((NodoIdentificador)nodo).getExpresion() == null )
	    			printError("El identificador " + nombre + " es vector y debe ser declarado usado: "+nombre+"[]");
	    		else {
	    			recorrerArbol(((NodoIdentificador)nodo).getExpresion());
	    			if (comprobarTipo(((NodoIdentificador)nodo).getExpresion()) != "Int") {
	    				printError("El indice del vector "+nombre+" debe ser tipo Int");
	    			}
	    		}
	    	} else {
	    		if( ((NodoIdentificador)nodo).getExpresion() != null ){
	    			printError("El identificador " + nombre + " no ha sido declarado como vector");
	    		}
	    	}
	    	
		} else
			retorno = false;
		
		return retorno;
	}
	
	private void printError(Object chain){		

		
		System.err.println("[Error Semantico]: "+chain);
		this.anyError = true;
	}	
}

