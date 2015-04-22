package compilador;

import java_cup.runtime.*;


%%
%line
%column
%public
%unicode
/* Habilitar la compatibilidad con el interfaz CUP para el generador sintactico*/
%cup
/* Llamar Scanner a la clase que contiene el analizador Lexico */
%class Scanner

/*-- CONSTRUCTOR --*/
%{
	public Scanner(java.io.InputStream r, SymbolFactory sf){
		this(r);
		this.sf=sf;
		lineanum=1;
		debug=false;
	}
	private SymbolFactory sf;
	public int lineanum;
	private boolean debug;

%}

%eofval{
    return sf.newSymbol("EOF",sym.EOF);
%eofval}

/* Acceso a la columna y fila actual de analisis CUP */
%line
%column



digito		= [0-9]
numero		= {digito}+
letra			= [a-zA-Z]
identificador	= [a-zA-Z] [a-zA-Z0-9]*
nuevalinea		= \n | \n\r | \r\n
espacio		= [ \t]+
%%
"true"            {	if(debug) System.out.println("token TRUE");
			return sf.newSymbol("TRUE",sym.TRUE,lineanum);
			}
"false"            {	if(debug) System.out.println("token FALSE");
			return sf.newSymbol("FALSE",sym.FALSE,lineanum);
			}
"for"            {	if(debug) System.out.println("token FOR");
			return sf.newSymbol("FOR",sym.FOR,lineanum);
			}

"if"            {	if(debug) System.out.println("token IF");
			return sf.newSymbol("IF",sym.IF,lineanum);
			}
"then"          { if(debug) System.out.println("token THEN");
			return sf.newSymbol("THEN",sym.THEN,lineanum);
			}
"else"          {	if(debug) System.out.println("token ELSE");
			return sf.newSymbol("ELSE",sym.ELSE,lineanum);
			}
"end"           {	if(debug) System.out.println("token END "+yyline + " " +lineanum);
			return sf.newSymbol("END",sym.END,lineanum);
			}
"repeat"        {	if(debug) System.out.println("token REPEAT");
			return sf.newSymbol("REPEAT",sym.REPEAT,lineanum);
			}
"until"         {	if(debug) System.out.println("token UNTIL");
			return sf.newSymbol("UNTIL",sym.UNTIL,lineanum);
			}
"read"          {	if(debug) System.out.println("token READ");
			return sf.newSymbol("READ",sym.READ,lineanum);
			}
"write"         {	if(debug) System.out.println("token WRITE");
			return sf.newSymbol("WRITE",sym.WRITE,lineanum);
			}
"int"         {	if(debug) System.out.println("token INT");
			return sf.newSymbol("INT",sym.INT,lineanum);
			}
"boolean"         {	if(debug) System.out.println("token BOOLEAN");
			return sf.newSymbol("BOOLEAN",sym.BOOLEAN,lineanum);
			}
"void"        {	if(debug) System.out.println("token VOID");
			return sf.newSymbol("VOID",sym.VOID,lineanum);
			}						
":="            {	if(debug) System.out.println("token ASSIGN " + lineanum);
			return sf.newSymbol("ASSIGN",sym.ASSIGN,lineanum);
			}
"<="             {	if(debug) System.out.println("token LE");
			return sf.newSymbol("LE",sym.LE,lineanum);
			}
">="             {	if(debug) System.out.println("token GE");
			return sf.newSymbol("GE",sym.GE,lineanum);
			}
"!="             {	if(debug) System.out.println("token NE");
			return sf.newSymbol("NE",sym.NE,lineanum);
			}
">"             {	if(debug) System.out.println("token GT");
			return sf.newSymbol("GT",sym.GT,lineanum);
			}
					
"="             {	if(debug) System.out.println("token EQ");
			return sf.newSymbol("EQ",sym.EQ,lineanum);
			}
"<"             {	if(debug) System.out.println("token LT");
			return sf.newSymbol("LT",sym.LT,lineanum);
			}
"+"             {	if(debug) System.out.println("token PLUS");
			return sf.newSymbol("PLUS",sym.PLUS,lineanum);
			}
"-"             {	if(debug) System.out.println("token MINUS");
			return sf.newSymbol("MINUS",sym.MINUS,lineanum);
			}
"*"             {	if(debug) System.out.println("token TIMES");
			return sf.newSymbol("TIMES",sym.TIMES,lineanum);
			}
"/"             {	if(debug) System.out.println("token OVER");
			return sf.newSymbol("OVER",sym.OVER,lineanum);
			}
"("             {	if(debug) System.out.println("token LPAREN");
			return sf.newSymbol("LPAREN",sym.LPAREN,lineanum);
			}
")"             {	if(debug) System.out.println("token RPAREN");
			return sf.newSymbol("RPAREN",sym.RPAREN,lineanum);
			}
"["           {	if(debug) System.out.println("token BRALEFT");
			return sf.newSymbol("BRALEFT",sym.BRALEFT,lineanum);
			}
"]"           {	if(debug) System.out.println("token BRARIGHT");
			return sf.newSymbol("BRARIGHT",sym.BRARIGHT,lineanum);
			}							
","             {	if(debug) System.out.println("token COMMA");
			return sf.newSymbol("COMMA",sym.COMMA,lineanum);
			}			
";"             {	if(debug) System.out.println("token SEMI");
			return sf.newSymbol("SEMI",sym.SEMI,lineanum);
			}
"and"             {	if(debug) System.out.println("token AND");
			return sf.newSymbol("SEMI",sym.AND,lineanum);
			}
"or"             {	if(debug) System.out.println("token OR");
			return sf.newSymbol("SEMI",sym.OR,lineanum);
			}						
"begin"             {	if(debug) System.out.println("token BEGIN");
			return sf.newSymbol("BEGIN",sym.BEGIN,lineanum);
			}
			
"return"             {	if(debug) System.out.println("token RETURN");
			return sf.newSymbol("RETURN",sym.RETURN,lineanum);
			}

{numero}        {	if(debug) System.out.println("token NUM");
			return sf.newSymbol("NUM",sym.NUM,new Integer(yytext()));
			}
{identificador}	{	if(debug) System.out.println("token ID");
				return sf.newSymbol("ID",sym.ID,new String(yytext()));
			}
{nuevalinea}       {lineanum++;}
{espacio}    { /* saltos espacios en blanco*/}
"{"[^}]+"}"  { /* salto comentarios */ if(debug) System.out.println("token COMENTARIO"); }
.               {System.err.println("[Error Lexico] Caracter ilegal encontrado: " + yytext() +" linea " + lineanum + "\n");}