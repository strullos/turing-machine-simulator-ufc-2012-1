/*
 * diagrama.h
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#ifndef DIAGRAMA_H_
#define DIAGRAMA_H_

#include <map>
#include <string>
#include <vector>

#include "../modulo/modulo.h"

//Representa uma ação do diagrama
class Regra{
public:
	Regra(std::string &simbolo, std::string &modulo_final){
		m_qualquer_simbolo = false;
		inserir(simbolo,modulo_final);
	}
	Regra(){ m_qualquer_simbolo = false; };

	void inserir(std::string simbolo, std::string modulo_final){
		m_acoes.insert(std::pair<std::string,std::string>(simbolo,modulo_final));
	}

	std::string pegar_prox_modulo(std::string simbolo_atual){
		std::map<std::string,std::string>::iterator it;
		if(m_acoes.size() > 0){
			if(m_qualquer_simbolo){
				std::map<std::string,std::string>::iterator it;
				it = m_acoes.begin();
				return (*it).second;
			}else{
				if(m_acoes.find(simbolo_atual) != m_acoes.end()){
					return m_acoes[simbolo_atual];
				}
			}
			return "";
		}else{
			return "";
		}

	}
	std::map<std::string, std::string> m_acoes;
	bool m_qualquer_simbolo;
};

class Diagrama {
public:
	Diagrama();
	bool carregar_diagrama(std::string arquivo);
	virtual ~Diagrama();
	void print_diagram(); //remove later

	void executar(std::string fita_inicial, unsigned int tamanho_da_fita);
private:
	bool carregar_modulo(std::string& linha_modulo);
	bool carregar_acoes(std::string& linha_acao);
	bool remover_espacos_brancos(std::string& linha);
	bool remover_valor_da_linha(std::string& linha);
	bool pegar_remover_valor_da_linha(std::string&linha, std::string& valor);

	std::map<std::string, Regra*> m_regras; //< Hash indexado pelo nome do módulo, contendo as ações que ele pode realizar.
	std::map<std::string,Modulo*> m_modulos_carregados; //< Hash indexado pelo nome do arquivo do módulo. Cada módulo só pode ser carregado uma única vez
	std::map<std::string,Modulo*> m_modulos; //Hash indexado pelo nome de um módulo. Um módulo carregado pode ter vários nomes (referências)

	std::string m_modulo_inicial;

	bool m_carregado;
};

#endif /* DIAGRAMA_H_ */
