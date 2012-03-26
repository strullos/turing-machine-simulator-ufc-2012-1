/*
 * diagrama.h
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#ifndef DIAGRAMA_H_
#define DIAGRAMA_H_

#include <vector>

#include "regra_diagrama.h"
#include "../modulo/modulo.h"

/*!
 * Representa um diagrama. Composta por um conjunto de modulos e regras.
 * Responsavel pelo carregamento e inicializacao dos modulos e das regras.
 * Controla tambem a execucao de um diagrama.
 *
 * Esta classe contem 3 containers principais:"
 * -m_modulos_carregados: Um mapa indexado pelo nome do modulo, tal que cada entrada armazena um ponteiro para um modulo.
 * -m_modulos: Um mapa indexado pelo nome do modulo, tal que cada entrada tambem armazena um ponteiro para um modulo.
 * -m_regras: Um mapa indexado pelo nome do modulo, tal que cada entrada armazena um objeto Regras que contem as regras
 * relativas a cada modulo.
 *
 * Cada modulo so eh carregado uma unica vez, mas um modulo pode ter varias referencias no diagrama.
 * Exemplo: R1 r.mt , R2 r.mt, etc...
 * Antes de um novo modulo ser adicionado na tabela, eh verificado se o modulo ja foi carregado. Caso tenha
 * sido, apenas uma nova referencia eh adicionada na tabela.
 *
 *
 */
class Diagrama {
public:
	Diagrama(); 												//!< Construtor
	virtual ~Diagrama(); 										//!< Destrutor; Limpa todas as estruturas de dados

	bool carregar_diagrama(std::string arquivo); 				//!< Carrega um diagrama especificad por 'arquivo'
	void imprime_diagrama(); 									//!< Imprime o diagrama atualmente carregado
	void executar(std::string fita_inicial); 					//!< Executa o diagrama carregado.
private:
	bool carregar_modulo(std::string& linha_modulo,
							const std::string& path); 			//!< Carrega um modulo especificado num arquivo de diagrama
	bool carregar_regra(std::string& linha_regra); 				//!< Carrega uma regra especificada num arquivo de diagrama
	void limpar();												//!< Reseta os valores do diagrama
	void imprime_config_atual(Maquina *mt, Modulo *modulo,		//!< Imprime a configuracao atual do Diagrama
			const std::string &modulo_atual, const unsigned int &passos);
	bool verifica_prox_modulo(Maquina *mt);

	std::map<std::string, RegraDiagrama*> m_regras_modulos; 	//!< Hash indexado pelo nome do modulo, contendo as acoes que ele pode realizar.
	std::map<std::string,Modulo*> m_modulos_carregados; 		//!< Hash dos modulos carregados, indexado pelo nome do arquivo do modulo.
	std::map<std::string,Modulo*> m_modulos; 					//!< Hash dos modulos e suas referencias indexado pelo nome de um modulo.

	std::string m_modulo_atual;									//!< Armazena o nome do modulo atual, sendo executado
	bool m_carregado;											//!< Determina se o diagrama especificado foi carregado com sucesso
	bool m_arquivo_mt;											//!< Verifica se o arquivo é de diagrama ou de um módulo
};

#endif /* DIAGRAMA_H_ */
