package compilador;

public class RegistroSimbolo {
	private String identificador;
	private int NumLinea;
	private int DireccionMemoria;
	private String tipo; 							// Para el tipo de dato del identificador 1=int 2=boolean
	
	public RegistroSimbolo(String identificador, int numLinea,
			int direccionMemoria, String tipo) {
		super();
		this.identificador = identificador;
		NumLinea = numLinea;
		DireccionMemoria = direccionMemoria;
		this.tipo = tipo;
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

	public void setDireccionMemoria(int direccionMemoria) {
		DireccionMemoria = direccionMemoria;
	}
}
