package compilador;

public class RegistroSimbolo {
	private String identificador;
	private int NumLinea;
	private int DireccionMemoria;
	private String tipo; 							// Para el tipo de dato del identificador 1=int 2=boolean
	private boolean array;
	
	public RegistroSimbolo(String identificador, int numLinea,
			int direccionMemoria, String tipo, boolean array) {
		super();
		this.identificador = identificador;
		NumLinea = numLinea;
		DireccionMemoria = direccionMemoria;
		this.tipo = tipo;
		this.array = array;
	}

	public String getIdentificador() {
		return identificador;
	}

	public int getNumLinea() {
		return NumLinea;
	}

	public int getDireccionMemoria() {
		return DireccionMemoria;
	}
	
	public String getTipo(){
		return tipo;
	}

	public boolean getArray(){
		return array;
	}
	
	public void setDireccionMemoria(int direccionMemoria) {
		DireccionMemoria = direccionMemoria;
	}
}
