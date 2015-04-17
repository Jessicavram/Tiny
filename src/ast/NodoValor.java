package ast;

public class NodoValor extends NodoBase {
	private Integer valor;
	private Integer tipo;

	public NodoValor(Integer valor) {
		super();
		this.valor = valor;
		this.tipo=0;
	}
	public NodoValor(Integer valor,Integer tipo){
		super();
		this.valor=valor;
		this.tipo=tipo;
	}
	public NodoValor() {
		super();
	}
	
	public Integer getValor() {
		return valor;
	}
	public Integer getTipo(){
		return tipo;
	}

}
