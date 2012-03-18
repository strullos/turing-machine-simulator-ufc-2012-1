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

#include "../modulo/modulo.h"

//Representa uma ação do diagrama
class AcDiagrama{
public:
	AcDiagrama(std::string &simbolo, std::string &modulo_final){
		inserir(simbolo,modulo_final);
	}
	void inserir(std::string simbolo, std::string modulo_final){
		m_ac.insert(std::pair<std::string,std::string>(simbolo,modulo_final));
	}
	std::map<std::string, std::string> m_ac;
};

class Diagrama {
public:
	Diagrama();
	bool carregar_diagrama(std::string arquivo);
	virtual ~Diagrama();
	void print_diagram(); //remove later
private:
	bool carregar_modulo(std::string& linha_modulo);
	bool carregar_acoes(std::string& linha_acao);
	bool remover_espacos_brancos(std::string& linha);
	bool remover_valor_da_linha(std::string& linha);
	bool pegar_remover_valor_da_linha(std::string&linha, std::string& valor);
	std::map<std::string, AcDiagrama*> m_acoes_diagrama; //< Hash indexado pelo nome do módulo, contendo as ações que ele pode realizar.
	std::map<std::string,Modulo*> m_modulos_carregados; //< Hash indexado pelo nome do arquivo do módulo. Cada módulo só pode ser carregado uma única vez
	std::map<std::string,Modulo*> m_modulos; //Hash indexado pelo nome de um módulo. Um módulo carregado pode ter vários nomes (referências)
};

#endif /* DIAGRAMA_H_ */
