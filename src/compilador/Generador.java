package compilador;

import java.util.ArrayList;

import ast.*;

public class Generador {
	/* Ilustracion de la disposicion de la memoria en
	 * este ambiente de ejecucion para el lenguaje Tiny
	 *
	 * |t1	|<- mp (Maxima posicion de memoria de la TM
	 * |t1	|<- desplazamientoTmp (tope actual)
	 * |free|
	 * |free|
	 * |...	|
	 * |x	|
	 * |y	|<- gp
	 * 
	 * */
	
	
	
	/* desplazamientoTmp es una variable inicializada en 0
	 * y empleada como el desplazamiento de la siguiente localidad
	 * temporal disponible desde la parte superior o tope de la memoria
	 * (la que apunta el registro MP).
	 * 
	 * - Se decrementa (desplazamientoTmp--) despues de cada almacenamiento y
	 * 
	 * - Se incrementa (desplazamientoTmp++) despues de cada eliminacion/carga en 
	 *   otra variable de un valor de la pila.
	 * 
	 * Pudiendose ver como el apuntador hacia el tope de la pila temporal
	 * y las llamadas a la funcion emitirRM corresponden a una inserccion 
	 * y extraccion de esta pila
	 */
	private static int desplazamientoTmp = 0;
	private static ArrayList<Integer> localidad_return = new ArrayList<Integer>();
	private static TablaSimbolos tablaSimbolos = null;
	private static String ultimoAmbito;
	private static int saltomain;
	public static void setTablaSimbolos(TablaSimbolos tabla){
		tablaSimbolos = tabla;
	}
	
	public static void generarCodigoObjeto(NodoBase raiz){
		System.out.println();
		System.out.println();
		System.out.println("------ CODIGO OBJETO DEL LENGUAJE TINY GENERADO PARA LA TM ------");
		System.out.println();
		System.out.println();
		generarPreludioEstandar();
		generar(raiz,true);
		/*Genero el codigo de finalizacion de ejecucion del codigo*/   
		UtGen.emitirComentario("Fin de la ejecucion.");
		UtGen.emitirRO("HALT", 0, 0, 0, "");
		System.out.println();
		System.out.println();
		System.out.println("------ FIN DEL CODIGO OBJETO DEL LENGUAJE TINY GENERADO PARA LA TM ------");
	}
	
	//Funcion principal de generacion de codigo
	//prerequisito: Fijar la tabla de simbolos antes de generar el codigo objeto 
	private static void generar(NodoBase nodo , boolean ban ){  //bandera para generar el hermano derecho Verdadero-->Generar herman derecho
	if(tablaSimbolos!=null){
		
		if (nodo instanceof  NodoIf){
			generarIf(nodo);
		}else if (nodo instanceof  NodoRepeat){
			generarRepeat(nodo);
		}else if (nodo instanceof NodoFor){
			generarFor(nodo);
		}else if (nodo instanceof  NodoAsignacion){
			generarAsignacion(nodo);
		}else if (nodo instanceof  NodoLeer){
			generarLeer(nodo);
		}else if (nodo instanceof  NodoEscribir){
			generarEscribir(nodo);
		}else if (nodo instanceof NodoValor){
			generarValor(nodo);
		}else if (nodo instanceof NodoIdentificador){
			generarIdentificador(nodo);
		}else if (nodo instanceof NodoOperacion){
			generarOperacion(nodo);
		}else if (nodo instanceof NodoDeclaracion){
		}else if (nodo instanceof NodoProgram){
			generarProgram(nodo);
		}else if (nodo instanceof NodoFuncion){
			ultimoAmbito = ((NodoFuncion)nodo).getNombre();
			generarFuncion(nodo);
		}else if(nodo instanceof NodoReturn){
			generarReturn(nodo);
		}else if(nodo instanceof NodoCallFuncion){
			generarLlamado(nodo);
		}else{
			System.out.println("BUG: Tipo de nodo a generar desconocido");
		}
		/*Si el hijo de extrema izquierda tiene hermano a la derecha lo genero tambien*/
		if(ban && nodo.TieneHermano())
			generar(nodo.getHermanoDerecha(),true);
	}else
		System.out.println("���ERROR: por favor fije la tabla de simbolos a usar antes de generar codigo objeto!!!");
}

	private static void generarIf(NodoBase nodo){
    	NodoIf n = (NodoIf)nodo;
		int localidadSaltoElse,localidadSaltoEnd,localidadActual;
		if(UtGen.debug)	UtGen.emitirComentario("-> if");
		/*Genero el codigo para la parte de prueba del IF*/
		generar(n.getPrueba(),true);
		localidadSaltoElse = UtGen.emitirSalto(1);
		UtGen.emitirComentario("If: el salto hacia el else debe estar aqui");
		/*Genero la parte THEN*/
		generar(n.getParteThen(),true);
		localidadSaltoEnd = UtGen.emitirSalto(1);
		UtGen.emitirComentario("If: el salto hacia el final debe estar aqui");
		localidadActual = UtGen.emitirSalto(0);
		UtGen.cargarRespaldo(localidadSaltoElse);
		UtGen.emitirRM_Abs("JEQ", UtGen.AC, localidadActual, "if: jmp hacia else");
		UtGen.restaurarRespaldo();
		/*Genero la parte ELSE*/
		if(n.getParteElse()!=null){
			generar(n.getParteElse(),true);
    	}
		//igualmente debo generar la sentencia que reserve en
		//localidadSaltoEnd al finalizar la ejecucion de un true
		localidadActual = UtGen.emitirSalto(0);
		UtGen.cargarRespaldo(localidadSaltoEnd);
		UtGen.emitirRM_Abs("LDA", UtGen.PC, localidadActual, "if: jmp hacia el final");
		UtGen.restaurarRespaldo();			
		if(UtGen.debug)	UtGen.emitirComentario("<- if");
	}
	
	private static void generarRepeat(NodoBase nodo){
    	NodoRepeat n = (NodoRepeat)nodo;
		int localidadSaltoInicio;
		if(UtGen.debug)	UtGen.emitirComentario("-> repeat");
			localidadSaltoInicio = UtGen.emitirSalto(0);
			UtGen.emitirComentario("repeat: el salto hacia el final (luego del cuerpo) del repeat debe estar aqui");
			/* Genero el cuerpo del repeat */
			generar(n.getCuerpo(),true);
			/* Genero el codigo de la prueba del repeat */
			generar(n.getPrueba(),true);
			UtGen.emitirRM_Abs("JEQ", UtGen.AC, localidadSaltoInicio, "repeat: jmp hacia el inicio del cuerpo");
		if(UtGen.debug)	UtGen.emitirComentario("<- repeat");
	}		
	private static void generarFor(NodoBase nodo){
    	NodoFor n = (NodoFor)nodo;
		int localidadSaltoInicio;
		int localidadSaltoEnd;
		int localidadActual;
		if(UtGen.debug)	UtGen.emitirComentario("-> For");
			generar(n.getAsignacion(),true);
			localidadSaltoInicio = UtGen.emitirSalto(0);
			UtGen.emitirComentario("for: el salto hacia el final (luego del cuerpo) del for debe estar aqui");
			/* Genero el cuerpo del for */
			generar(n.getPrueba(),true);
			localidadSaltoEnd = UtGen.emitirSalto(1);
			generar(n.getCuerpo(),true);
			/* Genero el codigo de la prueba del repeat */
		    generar(n.getPaso(),true);
		    UtGen.emitirRM_Abs("LDA", UtGen.PC, localidadSaltoInicio, "if: jmp hacia el inicio");
		    localidadActual = UtGen.emitirSalto(0);
		    UtGen.cargarRespaldo(localidadSaltoEnd);
		    UtGen.emitirRM_Abs("JEQ", UtGen.AC, localidadActual, "for: jmp hacia el fin del cuerpo");
		    UtGen.restaurarRespaldo();	
		if(UtGen.debug)	UtGen.emitirComentario("<- for");
	}		
		
	private static void generarAsignacion(NodoBase nodo){
		NodoAsignacion n = (NodoAsignacion)nodo;
		int direccion;
		if(UtGen.debug)	UtGen.emitirComentario("-> asignacion");		
		if(n.getPosicion() == null)
		{
			/* Genero el codigo para la expresion a la derecha de la asignacion */
			generar(n.getExpresion(),true);
			/* Ahora almaceno el valor resultante */
			direccion = tablaSimbolos.getDireccion(ultimoAmbito,n.getIdentificador());
			UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "asignacion: almaceno el valor para el id "+n.getIdentificador());
		}
		else
		{
			generar(n.getPosicion(),true);
			UtGen.emitirRM("ST", UtGen.AC, desplazamientoTmp--, UtGen.MP, "op: push en la pila tmp el resultado del desplazamiento");
			generar(n.getExpresion(),true);
			direccion = tablaSimbolos.getDireccion(ultimoAmbito,n.getIdentificador());
			UtGen.emitirRM("LD", UtGen.GP, ++desplazamientoTmp, UtGen.MP, "op: push en la pila tmp el resultado del desplazamiento");
			UtGen.emitirRM("ST", UtGen.AC, direccion,UtGen.GP, "asignacion: almaceno el valor para el id "+n.getIdentificador());
			UtGen.emitirRM("LDC",UtGen.GP,0,0,"cargo constante 0 en registro DESP");
		}
		
		if(UtGen.debug)	UtGen.emitirComentario("<- asignacion");
	}
	
	private static void generarLeer(NodoBase nodo){
		NodoLeer n = (NodoLeer)nodo;
		int direccion;
		if(UtGen.debug)	UtGen.emitirComentario("-> leer");
		if(n.getPosicion() == null)
		{
			UtGen.emitirRO("IN", UtGen.AC, 0, 0, "leer: lee un valor entero ");
			direccion = tablaSimbolos.getDireccion(ultimoAmbito,n.getIdentificador());
			UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "leer: almaceno el valor entero leido en el id "+n.getIdentificador());
		}
		else
		{
			generar(n.getPosicion(),true);
			UtGen.emitirRO("ADD",UtGen.GP,UtGen.GP,UtGen.AC,"sumo despazamiento al registro GP");
			UtGen.emitirRO("IN", UtGen.AC, 0, 0, "leer: lee un valor entero ");
			direccion = tablaSimbolos.getDireccion(ultimoAmbito,n.getIdentificador());
			UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "leer: almaceno el valor entero leido en el id "+n.getIdentificador());
			UtGen.emitirRM("LDC",UtGen.GP,0,0,"cargo constante 0 en registro GP");
		}
		if(UtGen.debug)	UtGen.emitirComentario("<- leer");
	}
	
	private static void generarEscribir(NodoBase nodo){
		NodoEscribir n = (NodoEscribir)nodo;
		if(UtGen.debug)	UtGen.emitirComentario("-> escribir");
		/* Genero el codigo de la expresion que va a ser escrita en pantalla */
		generar(n.getExpresion(),true);
		/* Ahora genero la salida */
		UtGen.emitirRO("OUT", UtGen.AC, 0, 0, "escribir: genero la salida de la expresion");
		if(UtGen.debug)	UtGen.emitirComentario("<- escribir");
	}
	
	private static void generarValor(NodoBase nodo){
    	NodoValor n = (NodoValor)nodo;
    	if(UtGen.debug)	UtGen.emitirComentario("-> constante");
    	UtGen.emitirRM("LDC", UtGen.AC, n.getValor(), 0, "cargar constante: "+n.getValor().toString());
    	if(UtGen.debug)	UtGen.emitirComentario("<- constante");
	}
	
	private static void generarIdentificador(NodoBase nodo){
		NodoIdentificador n = (NodoIdentificador)nodo;
		int direccion;
		if(UtGen.debug)	UtGen.emitirComentario("-> identificador");
		direccion = tablaSimbolos.getDireccion(ultimoAmbito,n.getNombre());
		
		if(n.getExpresion()!=null)
		{
			generar(n.getExpresion(),true);
			UtGen.emitirRO("ADD",UtGen.GP,UtGen.GP,UtGen.AC,"sumo despazamiendo al registro GP");
			UtGen.emitirRM("LD", UtGen.AC, direccion,UtGen.GP, "cargar valor de identificador: "+n.getNombre());
			UtGen.emitirRM("LDC",UtGen.GP,0,0,"cargo constante 0 en el resgitro GP");
		}
		else
		UtGen.emitirRM("LD", UtGen.AC, direccion, UtGen.GP, "cargar valor de identificador: "+n.getNombre());

		if(UtGen.debug)	UtGen.emitirComentario("-> identificador");
	}

	private static void generarOperacion(NodoBase nodo){
		NodoOperacion n = (NodoOperacion) nodo;
		if(UtGen.debug)	UtGen.emitirComentario("-> Operacion: " + n.getOperacion());
		
		generar(n.getOpIzquierdo(),true);
		/* Almaceno en la pseudo pila de valor temporales el valor de la operacion izquierda */
		UtGen.emitirRM("ST", UtGen.AC, desplazamientoTmp--, UtGen.MP, "op: push en la pila tmp el resultado expresion izquierda");
		/* Genero la expresion derecha de la operacion */
		generar(n.getOpDerecho(),true);
		/* Ahora cargo/saco de la pila el valor izquierdo */
		UtGen.emitirRM("LD", UtGen.AC1, ++desplazamientoTmp, UtGen.MP, "op: pop o cargo de la pila el valor izquierdo en AC1");
		switch(n.getOperacion()){
			case    or:
			case	mas:	UtGen.emitirRO("ADD", UtGen.AC, UtGen.AC1, UtGen.AC, "op: +");		
							break;
			case	menos:	UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: -");
							break;
			case    and:
			case	por:	UtGen.emitirRO("MUL", UtGen.AC, UtGen.AC1, UtGen.AC, "op: *");
							break;
			case	entre:	UtGen.emitirRO("DIV", UtGen.AC, UtGen.AC1, UtGen.AC, "op: /");
							break;		
			case	menor:	UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: <");
							UtGen.emitirRM("JLT", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC<0)");
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;
			case menorigual:UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: <=");
							UtGen.emitirRM("JLE", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC<=0)");
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;
			case mayorigual:UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: >=");
							UtGen.emitirRM("JGE", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC>=0)");
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;
			case    mayor:	UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: >");
							UtGen.emitirRM("JGT", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC>0)");
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;
			case  noigual:	UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: !=");
							UtGen.emitirRM("JNE", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC!=0)");
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;
			case	igual:	UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: ==");
							UtGen.emitirRM("JEQ", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC==0)");
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;	
			default:
							UtGen.emitirComentario("BUG: tipo de operacion desconocida");
		}
		if(UtGen.debug)	UtGen.emitirComentario("<- Operacion: " + n.getOperacion());
	}
	private static void generarProgram(NodoBase nodo){
		if(((NodoProgram) nodo).getFunctions()!=null){
			generar(((NodoProgram) nodo).getFunctions(),true);
	}
		if(((NodoProgram) nodo).getMain()!=null){
			ultimoAmbito = "main";			
			//iniciar la ejecucion en la linea #line main
			int pos = UtGen.emitirSalto(0);
			UtGen.cargarRespaldo(saltomain);
			UtGen.emitirRM("LDA", UtGen.PC, pos,UtGen.GP, "Salto incodicional al main");
			UtGen.restaurarRespaldo();
			generar(((NodoProgram) nodo).getMain(),true);
			pos=UtGen.emitirSalto(0);
			for	(int i=0; i<localidad_return.size();i++){
				UtGen.cargarRespaldo(localidad_return.get(i));
				UtGen.emitirRM("LDA", UtGen.PC, pos, UtGen.GP, "salto del return");
				UtGen.restaurarRespaldo();
			}
			localidad_return.clear();
		}
	}
	private static void generarFuncion(NodoBase nodo){
		//aqui debo de poner el Imen a la tabla
			desplazamientoTmp = -40;
			ultimoAmbito=((NodoFuncion)nodo).getNombre();
			int pos=UtGen.emitirSalto(0);
			tablaSimbolos.setiMem(ultimoAmbito,pos );
			NodoFuncion n = (NodoFuncion)nodo;
			if(n.getSent()!=null)
				generar(n.getSent(),true);			
			//coloco todos los saltos de los return ya que se donde termina la funcion
			pos=UtGen.emitirSalto(0);
			for	(int i=0; i<localidad_return.size();i++){
				UtGen.cargarRespaldo(localidad_return.get(i));
				UtGen.emitirRM("LDA", UtGen.PC, pos, UtGen.GP, "salto del return");
				UtGen.restaurarRespaldo();
			}
			localidad_return.clear();
			//Salto incondicional a donde quede
			UtGen.emitirRM("LDA", UtGen.PC, 0,UtGen.NL, "Salto incodicional a donde fue llamada la funcion");
			desplazamientoTmp=0;
	}
	
	//TODO: enviar preludio a archivo de salida, obtener antes su nombre
	private static void generarPreludioEstandar(){
		UtGen.emitirComentario("Compilacion TINY para el codigo objeto TM");
		UtGen.emitirComentario("Archivo: "+ "NOMBRE_ARREGLAR");
		/*Genero inicializaciones del preludio estandar*/
		/*Todos los registros en tiny comienzan en cero*/
		UtGen.emitirComentario("Preludio estandar:");
		UtGen.emitirRM("LD", UtGen.MP, 0, UtGen.AC, "cargar la maxima direccion desde la localidad 0");
		UtGen.emitirRM("ST", UtGen.AC, 0, UtGen.AC, "limpio el registro de la localidad 0");
		//UtGen.emitirRM("LDC",UtGen.MP, 1023,UtGen.AC,"Colocar tope de la meoria");
		//iniciar la ejecucion en la linea #line main
		saltomain = UtGen.emitirSalto(1);
	}
	private static void generarArgumentos(NodoBase nodo){
		//Recupera los argumentos de derecha a izquierda
		//ejem fun (int a,int b, int c)
		//Supone que en la pila vienen de es
		//a misma forma a,b,c por lo que el que esta de ultimo es el argumento int c
		NodoDeclaracion n = (NodoDeclaracion)nodo;
		int direccion;		
		if((n.getHermanoDerecha())!= null)
			generarArgumentos((n.getHermanoDerecha()));
		direccion = tablaSimbolos.getDireccion(ultimoAmbito,((NodoIdentificador)n.getVariable()).getNombre());
		//Recuperar el valor de la pila temporal
		UtGen.emitirRM("LD", UtGen.AC, ++desplazamientoTmp, UtGen.MP, "arg: Recuperar de la pila Temporal el valor del arguento, y lo guardo en AC");
		//Guardar el valor recuperado en la vararible argumento
		UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "argumento: almaceno el valor para el id "+((NodoIdentificador)n.getVariable()).getNombre());
		
	}
	private static void generarReturn(NodoBase nodo){
		if(((NodoReturn)nodo).getExpresion()!=null)
		       generar(((NodoReturn)nodo).getExpresion(),true);
		//la setencia anterior deja en AC el valor retornado		
		//Guargo una posicion para saltar a la linea donde termina la funcion
		localidad_return.add(UtGen.emitirSalto(1));

	}
	private static void generarLlamado(NodoBase nodo){
		//cargar las variables
		NodoCallFuncion n = (NodoCallFuncion)nodo;
		if (n.getArgs()!=null){	
			NodoBase aux = n.getArgs();
			int pos = tablaSimbolos.getPrimerArgumento(((NodoCallFuncion)nodo).getNombre());
			do{			
				generar(aux,false); //deja es AC el valor 
				UtGen.emitirRM("ST", UtGen.AC, pos, UtGen.GP, "llamado: guarda el valor del argumento");	
				pos+=1;
				aux=aux.getHermanoDerecha();
			}while(aux!=null);
		}	
		//Poner en NL la linea actual + 1
		UtGen.emitirRM("LDA", UtGen.NL, 1, UtGen.PC, "(AC=Pos actual + 1)");
		
		//saltar a la linea donde empieza la funcion
		int pos = tablaSimbolos.getiMem(((n.getNombre())));
		UtGen.emitirRM("LDA", UtGen.PC, pos,UtGen.GP, "Salto a la primera linea de la funcion");
	}

}
