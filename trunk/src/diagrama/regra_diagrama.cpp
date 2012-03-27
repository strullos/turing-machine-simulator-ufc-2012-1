#include "regra_diagrama.h"

RegraDiagrama::RegraDiagrama(std::string &simbolo, std::string &modulo_final)
{
	m_qualquer_simbolo = false;
	inserir(simbolo,modulo_final);
}	
												 
RegraDiagrama::RegraDiagrama()
{ 
	m_qualquer_simbolo = false; 
}

void RegraDiagrama::inserir(std::string simbolo, std::string modulo_final)
{
	m_regras.insert(std::pair<std::string,std::string>(simbolo,modulo_final));
}

	
std::string RegraDiagrama::pegar_prox_modulo(std::string simbolo_atual)
{
	std::map<std::string,std::string>::iterator it;
		if(m_regras.size() > 0){
			//Se nao importar o siimbolo, retorna o primeiro modulo do hash
			if(m_qualquer_simbolo){
				std::map<std::string,std::string>::iterator it;
				it = m_regras.begin();
				return (*it).second;
			}else{
				//Senao, retorna o proximo modulo
				if(m_regras.find(simbolo_atual) != m_regras.end()){
					return m_regras[simbolo_atual];
				}
			}
		}	
	return "";
}		
