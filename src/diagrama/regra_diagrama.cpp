#include "regra_diagrama.h"

RegraDiagrama::RegraDiagrama()
{
	m_qualquer_simbolo = false;
}	

void RegraDiagrama::inserir(std::string simbolo, std::string modulo_final,
		bool atualiza_var,
		std::string var_atualizada,
		bool envia_var,
		std::string var_enviada)
{
	DescritorRegra *nova_regra = new DescritorRegra();
	nova_regra->m_prox_modulo = modulo_final;
	nova_regra->m_atualiza_var = atualiza_var;
	nova_regra->m_envia_var = envia_var;
	nova_regra->m_variavel_atualizada = var_atualizada;
	nova_regra->m_variavel_enviada = var_enviada;
	m_regras.insert(std::pair<std::string,DescritorRegra*>(simbolo,nova_regra));
}

	
std::string RegraDiagrama::pegar_prox_modulo(std::string simbolo_atual)
{
	if(m_regras.size() > 0){
		std::map<std::string, DescritorRegra*>::iterator it;
		//Se nao importar o siimbolo, retorna o primeiro modulo do hash
		if(m_qualquer_simbolo){
			it = m_regras.begin();
			return ((*it).second)->m_prox_modulo;
		}else{
			//Senao, retorna o proximo modulo
			if(m_regras.find(simbolo_atual) != m_regras.end()){
				return m_regras[simbolo_atual]->m_prox_modulo;
			}
		}
	}
	return "";
}

RegraDiagrama::~RegraDiagrama()
{
}

DescritorRegra* RegraDiagrama::pegar_descritor_regra(std::string simbolo_atual)
{
	std::map<std::string,std::string>::iterator it;
	if(m_regras.size() > 0){
		//Retorna o descritor da regra
		if(m_regras.find(simbolo_atual) != m_regras.end()){
			return m_regras[simbolo_atual];
		}
	}
	return NULL;
}
		
