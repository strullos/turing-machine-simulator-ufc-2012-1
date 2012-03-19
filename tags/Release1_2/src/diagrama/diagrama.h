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

/*!
 * Representa uma regra do diagrama.
 * Cada regra é referente apenas a um único módulo e é composta por um conjunto de
 * símbolos de leitura e Módulos associados.
 * Uma regra do diagrama especifica que, especifica o próximo módulo a ser executado, de
 * acordo com o símbolo atual na cabeça de leitura.
 *
 * Obs: Para evitar a criação de mais arquivos, essa classe foi definida toda inline.
 */
class RegraDiagrama{
public:
	RegraDiagrama(std::string &simbolo, std::string &modulo_final){
		m_qualquer_simbolo = false;
		inserir(simbolo,modulo_final);
	}													 //!< Construtor
	RegraDiagrama(){ m_qualquer_simbolo = false; };		 //!< Destrutor

	void inserir(std::string simbolo, std::string modulo_final){
		m_regras.insert(std::pair<std::string,std::string>(simbolo,modulo_final));
	}													//!< Insere uma nova regra

	std::string pegar_prox_modulo(std::string simbolo_atual){
		std::map<std::string,std::string>::iterator it;
		if(m_regras.size() > 0){
			//Se não importar o símbolo, retorna o primeiro módulo do hash
			if(m_qualquer_simbolo){
				std::map<std::string,std::string>::iterator it;
				it = m_regras.begin();
				return (*it).second;
			}else{
				//Se não, retorna o módulo aproximado
				if(m_regras.find(simbolo_atual) != m_regras.end()){
					return m_regras[simbolo_atual];
				}
			}
		}
		return "";
	}													//!< Retorna o próximo módulo a ser executado
	std::map<std::string, std::string> m_regras;		//!< Hash, contendo o símbolo esperado na cabeça de leitura e o nome do próximo módulo
	bool m_qualquer_simbolo;							//!< Determina se essa regra é do tipo que aceita qualquer símbolo na cabeça de leitura
};

/*!
 * Representa um diagrama. É composta por um conjunto de módulos e regras.
 * Responsável pelo carregamento e inicialização dos módulos e das regras.
 * Controla também a execução de um diagrama.
 *
 * Esta classe contém 3 containers principais:"
 * -m_modulos_carregados: Um mapa indexado pelo nome do módulo, tal que cada entrada armazena um ponteiro para um módulo.
 * -m_modulos: Um mapa indexado pelo nome do módulo, tal que cada entrada também armazena um ponteiro para um módulo.
 * -m_regras: Um mapa indexado pelo nome do módulo, tal que cada entrada armazena um objeto Regras que contém as regras
 * relativas a cada módulo.
 *
 * Cada módulo só é carregado uma única vez, mas um módulo pode ter várias referências no diagrama.
 * Exemplo: R1 r.mt , R2 r.mt, etc...
 * Antes de um novo módulo ser adicionado na tabela, é verificado se o módulo já foi carregado. Caso tenha
 * sido, apenas uma nova referência é adicionada à tabela.
 *
 *
 */
class Diagrama {
public:
	Diagrama(); 										//!< Construtor
	virtual ~Diagrama(); 								//!< Destrutor; Limpa todas as estruturas de dados

	bool carregar_diagrama(std::string arquivo); 		//!< Carrega um diagrama especificad por 'arquivo'
	void print_diagram(); 								//!< Imprime o diagrama atualmente carregado
	void executar(std::string fita_inicial); 			//!< Executa o diagrama carregado.
private:
	bool carregar_modulo(std::string& linha_modulo); 	//!< Carrega um módulo especicado num arquivo de diagrama
	bool carregar_regra(std::string& linha_regra); 		//!< Carrega uma regra especicada num arquivo de diagrama
	void limpar();										//!< Reseta os valores do diagrama

	std::map<std::string, RegraDiagrama*> m_regras_modulos; 	//!< Hash indexado pelo nome do módulo, contendo as ações que ele pode realizar.
	std::map<std::string,Modulo*> m_modulos_carregados; //!< Hash dos módulos carregados, indexado pelo nome do arquivo do módulo.
	std::map<std::string,Modulo*> m_modulos; 			//!< Hash dos módulos e suas referências indexado pelo nome de um módulo.

	std::string m_modulo_inicial; 						//!< Armazena o nome do módulo inicial
	bool m_carregado;									//!< Determina se o diagrama especificado foi carregado com sucesso
};

#endif /* DIAGRAMA_H_ */
