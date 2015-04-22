package compilador;

import java.util.*;


import ast.*;

public class TablaSimbolos {
	private HashMap <String,String> tablaTipo;
	private HashMap <String, Integer> tablaiMem;
	private HashMap <String, Integer> tablaPrimerParametro;	
	private HashMap<String, HashMap<String, RegistroSimbolo>> tabla;
	private HashMap<String, RegistroSimbolo> tablaAmbito;
	private int direccion;  //Contador de las localidades de memoria asignadas a la tabla
	private String ultimoTipo;
	private String ultimoAmbito;
	public boolean error = false;
	
	// Auxiliares
	public ArrayList<String> arrayArgumentos;
	private HashMap <String,ArrayList<String>> tablaConArgumentos;
	
	public TablaSimbolos() {
		super();
		tabla = new HashMap<String, HashMap<String, RegistroSimbolo>>();
		tablaTipo = new HashMap<String, String>();
		tablaConArgumentos = new HashMap<String, ArrayList<String>>();
		tablaiMem = new HashMap <String, Integer>();
		tablaPrimerParametro = new HashMap <String, Integer>();				
		direccion=0;
	}
	
	public void cargarTabla(NodoBase raiz){
		while (raiz != null) {
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
	    	// Inserto el primer identificador y busco guardo el tipo de la declaracion que estoy recorriendo
	    	ultimoTipo = ((NodoDeclaracion)raiz).getTipo();  
	    	NodoBase nodo	=  ((NodoDeclaracion) raiz).getVariable(); 
	    	
	    	cargarIdentificadores((NodoIdentificador)nodo);
	    	
	    } 	    
	    else if (raiz instanceof NodoFuncion) {
	    	
	    	String nombreFuncion = ((NodoFuncion)raiz).getNombre();
	    	if (buscarAmbito(nombreFuncion)){
		    	ultimoAmbito = ((NodoFuncion)raiz).getNombre();	// Cambio el ambito cuando entro a una funcion
		    	if(ultimoAmbito != "MAIN")
		    		tablaTipo.put(ultimoAmbito, ((NodoFuncion)raiz).getTipo());
		    			    	
		    	if( ((NodoFuncion)raiz).getArgs() != null){
		    		cargarTabla(((NodoFuncion)raiz).getArgs());
		    		arrayArgumentos = new ArrayList<String>();
		    		cargarAgumentos(((NodoFuncion)raiz).getArgs());
		    		tablaConArgumentos.put(ultimoAmbito, arrayArgumentos);
		    	}
		    	cargarTabla(((NodoFuncion)raiz).getSent());
	    	} else 
	    		printError("La funcion "+nombreFuncion+ " ya ha sido declarada");

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
	}
	
	public void cargarIdentificadores(NodoIdentificador identificador){
    	Integer tamano 	= 1;
    	// Si es un vector
    	if(((NodoIdentificador)identificador).getTamano() != null)
    		tamano = ((NodoIdentificador)identificador).getTamano();
    	
    	if(!InsertarSimbolo(identificador.getNombre(),ultimoAmbito, ultimoTipo,tamano))
    		printError("Variable " +identificador.getNombre()+" ya ha sido declarada en el ambito " + ultimoAmbito);
    	if(identificador.getSiguiente() != null) // Compruebo que el identificador tenga hermanos 
    		cargarIdentificadores((NodoIdentificador)identificador.getSiguiente());	  	
	}
	
	private void cargarAgumentos(NodoBase nodo){
    	String tipoArgumento = ((NodoDeclaracion)nodo).getTipo();
    	arrayArgumentos.add(tipoArgumento);
		if (((NodoDeclaracion)nodo).getHermanoDerecha() != null)
			cargarAgumentos(((NodoDeclaracion)nodo).getHermanoDerecha());
	}
	
	//true es nuevo no existe se insertara, false ya existe NO se vuelve a insertar 
	public boolean InsertarSimbolo(String identificador, String ambito, String tipo,Integer tamano){
		boolean array = false;
		if(tamano == 0){
			printError("El vector " +identificador+"  no puede ser declarado de tamaño 0" );
			return false;
		} else if(tamano > 1)
			array = true;
		RegistroSimbolo simbolo;
		// Si existe el ambito busco su tabla con los simbolos
		if(tabla.containsKey(ambito)){
			tablaAmbito = tabla.get(ambito);
			if(tablaAmbito.containsKey(identificador)){
				return false; // si existe no lo creo
			} else {
				simbolo= new RegistroSimbolo(identificador,-1, direccion, tipo,array,false);
				direccion = direccion + tamano;
				tablaAmbito.put(identificador, simbolo);
				return true;
			}	
		} else {
			// Si el ambito no existe creo la nueva tabla para ambito			
			tablaAmbito = new HashMap<String, RegistroSimbolo>();
			simbolo= new RegistroSimbolo(identificador,-1, direccion, tipo,array,false);
			tablaPrimerParametro.put(ambito, direccion);
			direccion = direccion + tamano;
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

	public int getDireccion(String ambito, String Clave ){
	    tablaAmbito = tabla.get(ambito);
	    RegistroSimbolo simbolo = tablaAmbito.get(Clave);
	    return simbolo.getDireccionMemoria();
	}
	
 	public String getTipo(String ambito, String identificador){
		this.tablaAmbito = tabla.get(ambito);
		RegistroSimbolo simbolo=(RegistroSimbolo)tablaAmbito.get(identificador);
		return simbolo.getTipo();
	}

	public boolean getIfArray(String ambito, String identificador){
		this.tablaAmbito = tabla.get(ambito);
		RegistroSimbolo simbolo=(RegistroSimbolo)tablaAmbito.get(identificador);
		return simbolo.getArray();
	}	
	
	public boolean buscarTabla(String ambito, String identificador){
		if(tabla.containsKey(ambito)){
			tablaAmbito = tabla.get(ambito);
			if(tablaAmbito.containsKey(identificador)){
				return true; // si existe no lo creo
			} else {
				return false;
			}	
		} else {
			return false;
		}		
	}
	
	public String getTipoFuncion(String funcion){		
		return tablaTipo.get(funcion);
	}
	
	public ArrayList<String> getArrayArguments(String ambito){
		return tablaConArgumentos.get(ambito);
	}
	
	public boolean buscarAmbito(String ambito){	
		return !tablaTipo.containsKey(ambito);
	}
		
	public void setUltimoAmbito(String Ambito)
	{
		ultimoAmbito = Ambito;
	}
	
	public void setiMem(String ambito, Integer linea){
		tablaiMem.put(ambito, linea);
	}
	
	public Integer getiMem(String ambito){
		return tablaiMem.get(ambito);
	}
	
	public Integer getPrimerArgumento(String ambito){
		Integer x = 1;
		x = tablaPrimerParametro.get(ambito);
		return x;
	}
	
	public boolean getInizializacion(String ambito, String Clave ){
	    tablaAmbito = tabla.get(ambito);
	    RegistroSimbolo simbolo = tablaAmbito.get(Clave);
	    return simbolo.getInicializado();
	}
	
	public void setInizializacion(String ambito, String identificador,boolean inicializado ){
		tablaAmbito = tabla.get(ambito);
	    RegistroSimbolo simbolo = tablaAmbito.get(identificador);
	    tablaAmbito.remove(identificador);
	    	    	    
	    simbolo.setInicializado(inicializado);
	    tablaAmbito.put(identificador, simbolo);
	    tabla.put(ambito, tablaAmbito);
	}	
	
	private void printError(Object chain){		
		System.err.println("[Error Semantico]: "+chain);
		error = true;
	}		
	
	public boolean getError(){
		return error;
	}
	/*
	 * TODO:
	 * 1. Crear lista con las lineas de codigo donde la variable es usada.
	 * */
}

