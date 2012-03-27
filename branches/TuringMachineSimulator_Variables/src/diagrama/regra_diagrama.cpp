#include "regra_diagrama.h"

RegraDiagrama::RegraDiagrama(std::string &simbolo, std::string &modulo_final, std::string variavel)
{
	m_qualquer_simbolo = false;
	inserir(simbolo,modulo_final,variavel);
}	
												 
RegraDiagrama::RegraDiagrama()
{ 
	m_qualquer_simbolo = false; 
}

void RegraDiagrama::inserir(std::string simbolo, std::string modulo_final, std::string variavel)
{
	DescritorRegra *nova_regra = new DescritorRegra();
	nova_regra->m_prox_modulo = modulo_final;
	if(!variavel.empty()){
		nova_regra->m_passa_var = true;
		nova_regra->m_variavel = variavel;
	}else{
		nova_regra->m_passa_var = false;
	}
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
		
