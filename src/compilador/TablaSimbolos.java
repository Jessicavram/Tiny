package compilador;

import java.util.*;


import ast.*;

public class TablaSimbolos {
	private HashMap<String, HashMap<String, RegistroSimbolo>> tabla;
	private HashMap<String, RegistroSimbolo> tablaAmbito;
	private int direccion;  //Contador de las localidades de memoria asignadas a la tabla
	private String ultimoTipo;
	
	public TablaSimbolos() {
		super();
		tabla = new HashMap<String, HashMap<String, RegistroSimbolo>>();
		direccion=0;
	}

	public void cargarTabla(NodoBase raiz){
		while (raiz != null) {
		
	    if (raiz instanceof NodoIdentificador){
			
	    	InsertarSimbolo(((NodoIdentificador)raiz).getNombre(),"main", ultimoTipo); // cableado momentaneo
	    	if(((NodoIdentificador)raiz).getSiguiente() != null) // Compruebo que el identificador tenga hermanos 
	    		cargarTabla(((NodoIdentificador)raiz).getSiguiente());	    	
	    	//TODO: A�adir el numero de linea y localidad de memoria correcta
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
	    	cargarTabla(((NodoOperacion)raiz).getOpIzquierdo());
	    	cargarTabla(((NodoOperacion)raiz).getOpDerecho());
	    }
	    else if (raiz instanceof NodoDeclaracion) {
	    	ultimoTipo = ((NodoDeclaracion)raiz).getTipo();
	    	NodoBase nodo=  ((NodoDeclaracion) raiz).getVariable();
	    	
	    	InsertarSimbolo(((NodoIdentificador)nodo).getNombre(),"main", ultimoTipo); // cableado momentaneo
	    	
	    	if(((NodoIdentificador)nodo).getSiguiente() != null) // Compruebo que el identificador tenga hermanos 
	    		cargarTabla(((NodoIdentificador)nodo).getSiguiente());
	    	
	    } 	    
	    else if (raiz instanceof NodoFuncion) {
	    	cargarTabla(((NodoFuncion)raiz).getSent());
	    } 
	    else if (raiz instanceof NodoProgram) {
	    	cargarTabla(((NodoProgram)raiz).getMain());
	    } 	    	
	    raiz = raiz.getHermanoDerecha();
		
	  }
	}
	
	//true es nuevo no existe se insertara, false ya existe NO se vuelve a insertar 
	public boolean InsertarSimbolo(String identificador, String ambito, String tipo){
		RegistroSimbolo simbolo;
		

		// Si existe el ambito busco su tabla con los simbolos
		if(tabla.containsKey(ambito)){
			tablaAmbito = tabla.get(ambito);
			if(tablaAmbito.containsKey(identificador)){
				return false; // si existe no lo creo
			} else {
				simbolo= new RegistroSimbolo(identificador, -1, direccion++, tipo);
				tablaAmbito.put(identificador, simbolo);
				return true;
			}	
		} else {
			// Si el ambito no existe creo la nueva tabla para ambito
			tablaAmbito = new HashMap<String, RegistroSimbolo>();
			simbolo= new RegistroSimbolo(identificador, -1, direccion++, tipo);
			tablaAmbito.put(identificador, simbolo);			
			tabla.put(ambito, tablaAmbito);
			return true;
		}
	}
	
	public RegistroSimbolo BuscarSimbolo(String identificador, HashMap<String, RegistroSimbolo> tablaAmbito ){
		RegistroSimbolo simbolo=(RegistroSimbolo)tablaAmbito.get(identificador);
		return simbolo;
	}	
	
	public void ImprimirClaves(){
		System.out.println("*** Tabla de Simbolos ***");
		System.out.println(tabla.size());
		for( Iterator <String>it = tabla.keySet().iterator(); it.hasNext();) { 
            String ambito = (String)it.next();
            System.out.println("Ambito: " + ambito);            
    		this.tablaAmbito = tabla.get(ambito);
    		for( Iterator <String>iteradorInterno = tablaAmbito.keySet().iterator(); iteradorInterno.hasNext();) {
    			String identificador = (String)iteradorInterno.next();
    			System.out.println("\tConsegui Key: "+ BuscarSimbolo(identificador, tablaAmbito).getIdentificador() +" con direccion: " + BuscarSimbolo(identificador, tablaAmbito).getDireccionMemoria() +" tipo: "+BuscarSimbolo(identificador, tablaAmbito).getTipo() + " numero de linea: "+BuscarSimbolo(identificador, tablaAmbito).getNumLinea());
    		}   
		}
	}

	public int getDireccion(String Clave){
//		return BuscarSimbolo(Clave).getDireccionMemoria();
		return 1;
	}
	
	public RegistroSimbolo getTipo(String ambito, String identificador){
		this.tablaAmbito = tabla.get(ambito);
		RegistroSimbolo simbolo=(RegistroSimbolo)tablaAmbito.get(identificador);
		return simbolo;
	}
	/*
	 * TODO:
	 * 1. Crear lista con las lineas de codigo donde la variable es usada.
	 * */
}
