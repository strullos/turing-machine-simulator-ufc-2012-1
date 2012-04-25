package teoria.simulador.modulo;

import java.util.HashMap;
import teoria.simulador.modulo.DescritorRegra;

public class RegraDiagrama {
	public RegraDiagrama(){
		m_qualquer_simbolo = false;
		m_regras = new HashMap<String, DescritorRegra>();
	}
	public void inserir(String simbolo, String modulo_final, boolean atualiza_var, String var_atualizada,boolean envia_var, String var_enviada )
	{
		DescritorRegra nova_regra = new DescritorRegra();
		nova_regra.m_prox_modulo = modulo_final;
		nova_regra.m_prox_modulo = modulo_final;
		nova_regra.m_atualiza_var = atualiza_var;
		nova_regra.m_envia_var = envia_var;
		nova_regra.m_variavel_atualizada = var_atualizada;
		nova_regra.m_variavel_enviada = var_enviada;		
		m_regras.put(simbolo, nova_regra);
	}
	String pegar_prox_modulo(String simbolo_atual){
		if(!m_regras.isEmpty()){
			if(m_regras.containsKey(simbolo_atual)){
				return m_regras.get(simbolo_atual).m_prox_modulo;
			}else{
				if(m_qualquer_simbolo){
					return m_regras.values().iterator().next().m_prox_modulo;
				}
			}
		}
		return ""; 
	}
	DescritorRegra pegar_descritor_regra(String simbolo_atual){
		if(!m_regras.isEmpty()){
			if(m_regras.containsKey(simbolo_atual)){
				return m_regras.get(simbolo_atual);
			}else{
				if(m_qualquer_simbolo){
					return m_regras.values().iterator().next();
				}
			}
		}
		return null;
	}
	
	public HashMap<String,DescritorRegra> m_regras;
	public boolean m_qualquer_simbolo;
}