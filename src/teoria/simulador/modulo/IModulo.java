package teoria.simulador.modulo;
import java.io.FileNotFoundException;

import teoria.simulador.maquina.Maquina;

public abstract class IModulo {
	protected char m_var_valor = '\0'; 
	
	public abstract boolean carregar(String arquivo) throws FileNotFoundException;
	public abstract void inicializar();
	public abstract boolean executar_passo(Maquina f);
	public abstract String estado_atual();
	
	public void set_valor_var(char v) {
		m_var_valor = v;
	}
}
