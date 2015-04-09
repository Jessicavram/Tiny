package compilador;

import ast.*;
import java_cup.runtime.*;

public class Compilador {
	
	/***********
	SymbolFactory es una nueva caracteristica que ha sido a�adida a las version 11a de cup, la cual facilita la implementacion de clases Symbol personalizadas
	, esto debido a que dicha clase no provee mucha informaci�n de contexto que podria ser util para el analisis semantico o ayudar en la construccion del AST
	Mas informacion en: http//4thmouse.com/index.php/2007/02/15/using-custom-symbols-in-cup/
	***********/

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("deprecation")
		SymbolFactory sf = new DefaultSymbolFactory();
		parser parser_obj;
		if (args.length==0) 
			parser_obj=new parser(new Scanner(System.in,sf),sf);
		else 
			parser_obj=new parser(new Scanner(new java.io.FileInputStream(args[0]),sf),sf);
		UtGen.debug=true; //NO muestro mensajes de depuracion del generador (UTGen) para que el codigo sea compatible con la version visual de la TM
		//Para ver depuracion de analisis sintactico se debe ir al parser.java y colocar modoDepuracion en true
		parser_obj.parse();
		NodoBase root=parser_obj.action_obj.getASTroot();
		System.out.println();
		System.out.println("IMPRESION DEL AST GENERADO");
		System.out.println();
		ast.Util.imprimirAST(root);
		TablaSimbolos ts = new TablaSimbolos();
		ts.cargarTabla(root);
		ts.ImprimirClaves();
	    //REALIZAR ACA ANALISIS SEMANTICO
		Generador.setTablaSimbolos(ts);
		Generador.generarCodigoObjeto(root);
	}

}
