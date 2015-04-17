package ast;

public class Util {
	
	static int sangria = 0;
	static int contador=0;
	
	//Imprimo en modo texto con sangrias el AST
	public static void imprimirAST(NodoBase raiz){
		  sangria+=2;
		  while (raiz != null) {
		    printSpaces();
		    if (raiz instanceof  NodoIf)
		    	System.out.println("If");
		    else if (raiz instanceof  NodoRepeat)
		    	System.out.println("Repeat");
		    
		    else if (raiz instanceof  NodoAsignacion)
		    	System.out.println("Asignacion a: "+((NodoAsignacion)raiz).getIdentificador());

		    else if (raiz instanceof  NodoLeer)  
		    	System.out.println("Lectura: "+((NodoLeer)raiz).getIdentificador());

		    else if (raiz instanceof  NodoEscribir)
		    	System.out.println("Escribir");
		    
		    else if (raiz instanceof  NodoDeclaracion)
		    	System.out.println("Declaracion");		    
		    else if (raiz instanceof  NodoFor)
		    	System.out.println("Bucle For");			    
		    
		    else if (raiz instanceof NodoOperacion
		    		|| raiz instanceof NodoValor
		    		|| raiz instanceof NodoIdentificador)
		    	imprimirNodo(raiz);
		    else if (raiz instanceof NodoFuncion){
		    	System.out.println("Funcion: "+((NodoFuncion)raiz).getNombre()+ "   Tipo: "+((NodoFuncion)raiz).getTipo());	
		    }
		    else if (raiz instanceof NodoProgram){
		    	System.out.println("Estructura Principal");	
		    }
		    else if (raiz instanceof NodoCallFuncion){
		    	System.out.println("-> Llamado a funcion: "+((NodoCallFuncion)raiz).getNombre());	
		    }

		   
		    else if (raiz instanceof NodoReturn){
		    	System.out.print("-> Retornar: ");	
		    }
		    else System.out.println("Tipo de nodo desconocido");		    
		    /* Hago el recorrido recursivo */
		    if (raiz instanceof  NodoIf){
		    	printSpaces();
		    	System.out.println("**Prueba IF**");
		    	imprimirAST(((NodoIf)raiz).getPrueba());
		    	printSpaces();
		    	System.out.println("**Then IF**");
		    	imprimirAST(((NodoIf)raiz).getParteThen());
		    	if(((NodoIf)raiz).getParteElse()!=null){
		    		printSpaces();
		    		System.out.println("**Else IF**");
		    		imprimirAST(((NodoIf)raiz).getParteElse());
		    	}
		    }else if(raiz instanceof NodoLeer){
		    	printSpaces();
		    	if(((NodoLeer)raiz).getPosicion()!=null){
		    	System.out.println("**Posicion a Leer **");
		    	imprimirAST(((NodoLeer)raiz).getPosicion());
		    	}
		    }else if (raiz instanceof NodoFor){
		    	printSpaces();
		    	System.out.println("**Asignacion For**");
		    	imprimirAST(((NodoFor)raiz).getAsignacion());
		    	printSpaces();
		    	System.out.println("**Prueba For**");
		    	imprimirAST(((NodoFor)raiz).getPrueba());
		    	printSpaces();
		    	System.out.println("**Paso For**");
		    	imprimirAST(((NodoFor)raiz).getPaso());
		    	printSpaces();
		    	System.out.println("**Bloque**");
		    	imprimirAST(((NodoFor)raiz).getCuerpo());
		    }		    
		    else if (raiz instanceof NodoProgram){
		    	printSpaces();
		    	if(((NodoProgram)raiz).getFunctions()!=null){
		    		printSpaces();
		    		System.out.println("Bloque de Funciones");
		    		imprimirAST(((NodoProgram)raiz).getFunctions());
		    	}
		    	printSpaces();
		    	System.out.println("MAIN");
		    	imprimirAST(((NodoProgram)raiz).getMain());
		    	
		    	
		    }
		    else if (raiz instanceof NodoFuncion){
		    	printSpaces();
		    	if(((NodoFuncion)raiz).getArgs()!=null){
		    		contador=0;
		    		System.out.println("Argumentos");
		    		imprimirAST(((NodoFuncion)raiz).getArgs());
		    		((NodoFuncion)raiz).setNum(contador);
		    		System.out.println("Cantidad de Argumentos de Funcion: "+((NodoFuncion)raiz).getNum());
		    		
		    	}else if(((NodoFuncion)raiz).getArgs()==null && ((NodoFuncion)raiz).getSent()!=null)
		    		System.out.println("-> Sin Argumentos");
		    	if(((NodoFuncion)raiz).getSent()!=null){
		    		System.out.println("Sentencias");
			    	imprimirAST(((NodoFuncion)raiz).getSent());
		    	}
		    }
		    else if (raiz instanceof NodoCallFuncion){
		    	printSpaces();
		    	if(((NodoCallFuncion)raiz).getArgs()!=null){
		    		System.out.println("Argumentos");
		    		imprimirAST(((NodoCallFuncion)raiz).getArgs());
		    	}
		    	
		    }
		    else if (raiz instanceof NodoReturn){
		    	imprimirAST(((NodoReturn)raiz).getExpresion());
		    }
		    else if (raiz instanceof  NodoRepeat){
		    	printSpaces();
		    	System.out.println("**Cuerpo REPEAT**");
		    	imprimirAST(((NodoRepeat)raiz).getCuerpo());
		    	printSpaces();
		    	System.out.println("**Prueba REPEAT**");
		    	imprimirAST(((NodoRepeat)raiz).getPrueba());
		    }
		    else if (raiz instanceof  NodoAsignacion){
		    	if(((NodoAsignacion)raiz).getPosicion()!=null){
		    		System.out.println("En la Posicion");
		    		imprimirAST(((NodoAsignacion)raiz).getPosicion());
		    	}
		    	System.out.println("**El valor/epresion**");
		    	imprimirAST(((NodoAsignacion)raiz).getExpresion());
		    }
		    else if (raiz instanceof  NodoEscribir)
		    	imprimirAST(((NodoEscribir)raiz).getExpresion());
		  
		    else if (raiz instanceof NodoOperacion){
		    	printSpaces();
		    	System.out.println("**Expr Izquierda Operacion**");
		    	imprimirAST(((NodoOperacion)raiz).getOpIzquierdo());
		    	printSpaces();
		    	System.out.println("**Expr Derecha Operacion**");		    	
		    	imprimirAST(((NodoOperacion)raiz).getOpDerecho());
		    }
		    else if (raiz instanceof  NodoDeclaracion){
	    		printSpaces();
	    		System.out.println("**Tipo **");
	    		printSpaces();
	    		System.out.println(((NodoDeclaracion)raiz).getTipo() );	    		
	    		imprimirAST(((NodoDeclaracion)raiz).getVariable());
		    }
		    else if(raiz instanceof NodoIdentificador){
		    	if(((NodoIdentificador)raiz).getSiguiente() != null) // Compruebo que el identificador tenga hermanos 
		    		imprimirAST(((NodoIdentificador)raiz).getSiguiente());
		    	if(((NodoIdentificador)raiz).getExpresion() != null) // Compruebo que el identificador tenga hermanos 
		    		imprimirAST(((NodoIdentificador)raiz).getExpresion());
		    	
		    }		    
		    raiz = raiz.getHermanoDerecha();
		  }
		  sangria-=2;
		}

/* Imprime espacios con sangria */
static void printSpaces()
{ int i;
  for (i=0;i<sangria;i++)
	  System.out.print(" ");
}

/* Imprime informacion de los nodos */
static void imprimirNodo( NodoBase raiz )
{
	if(	raiz instanceof NodoRepeat
		||	raiz instanceof NodoLeer
		||	raiz instanceof NodoEscribir  ){
		System.out.println("palabra reservada: "+ raiz.getClass().getName());
	}
	
	if(	raiz instanceof NodoAsignacion )
		System.out.println(":=");
	
	if(	raiz instanceof NodoOperacion ){
		tipoOp sel=((NodoOperacion) raiz).getOperacion();
		if(sel==tipoOp.menor)
			System.out.println("<"); 
		if(sel==tipoOp.igual)
			System.out.println("=");
		if(sel==tipoOp.mas)
			System.out.println("+");
		if(sel==tipoOp.menos)
			System.out.println("-");
		if(sel==tipoOp.por)
			System.out.println("*");
		if(sel==tipoOp.entre)
			System.out.println("/");
		if(sel==tipoOp.mayorigual)
			System.out.println(">=");
		if(sel==tipoOp.menorigual)
			System.out.println("<=");
		if(sel==tipoOp.mayor)
			System.out.println(">");
		if(sel==tipoOp.noigual)
			System.out.println("!=");
	}

	if(	raiz instanceof NodoValor ){
		System.out.println("NUM, val= "+ ((NodoValor)raiz).getValor());
	}

	if(	raiz instanceof NodoIdentificador ){
		String variable;
		contador+=1;
		variable = "ID, nombre= "+ ((NodoIdentificador)raiz).getNombre();
		if (((NodoIdentificador)raiz).getTamano() != null)
			variable += " -vector, tamano= " + ((NodoIdentificador)raiz).getTamano();
		System.out.println(variable);
		
	}

}


}
