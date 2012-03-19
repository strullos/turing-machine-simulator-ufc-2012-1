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
 * Cada regra � referente apenas a um �nico m�dulo e � composta por um conjunto de
 * s�mbolos de leitura e M�dulos associados.
 * Uma regra do diagrama especifica que, especifica o pr�ximo m�dulo a ser executado, de
 * acordo com o s�mbolo atual na cabe�a de leitura.
 *
 * Obs: Para evitar a cria��o de mais arquivos, essa classe foi definida toda inline.
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
			//Se n�o importar o s�mbolo, retorna o primeiro m�dulo do hash
			if(m_qualquer_simbolo){
				std::map<std::string,std::string>::iterator it;
				it = m_regras.begin();
				return (*it).second;
			}else{
				//Se n�o, retorna o m�dulo aproximado
				if(m_regras.find(simbolo_atual) != m_regras.end()){
					return m_regras[simbolo_atual];
				}
			}
		}
		return "";
	}													//!< Retorna o pr�ximo m�dulo a ser executado
	std::map<std::string, std::string> m_regras;		//!< Hash, contendo o s�mbolo esperado na cabe�a de leitura e o nome do pr�ximo m�dulo
	bool m_qualquer_simbolo;							//!< Determina se essa regra � do tipo que aceita qualquer s�mbolo na cabe�a de leitura
};

/*!
 * Representa um diagrama. � composta por um conjunto de m�dulos e regras.
 * Respons�vel pelo carregamento e inicializa��o dos m�dulos e das regras.
 * Controla tamb�m a execu��o de um diagrama.
 *
 * Esta classe cont�m 3 containers principais:"
 * -m_modulos_carregados: Um mapa indexado pelo nome do m�dulo, tal que cada entrada armazena um ponteiro para um m�dulo.
 * -m_modulos: Um mapa indexado pelo nome do m�dulo, tal que cada entrada tamb�m armazena um ponteiro para um m�dulo.
 * -m_regras: Um mapa indexado pelo nome do m�dulo, tal que cada entrada armazena um objeto Regras que cont�m as regras
 * relativas a cada m�dulo.
 *
 * Cada m�dulo s� � carregado uma �nica vez, mas um m�dulo pode ter v�rias refer�ncias no diagrama.
 * Exemplo: R1 r.mt , R2 r.mt, etc...
 * Antes de um novo m�dulo ser adicionado na tabela, � verificado se o m�dulo j� foi carregado. Caso tenha
 * sido, apenas uma nova refer�ncia � adicionada � tabela.
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
	bool carregar_modulo(std::string& linha_modulo); 	//!< Carrega um m�dulo especicado num arquivo de diagrama
	bool carregar_regra(std::string& linha_regra); 		//!< Carrega uma regra especicada num arquivo de diagrama
	void limpar();										//!< Reseta os valores do diagrama

	std::map<std::string, RegraDiagrama*> m_regras_modulos; 	//!< Hash indexado pelo nome do m�dulo, contendo as a��es que ele pode realizar.
	std::map<std::string,Modulo*> m_modulos_carregados; //!< Hash dos m�dulos carregados, indexado pelo nome do arquivo do m�dulo.
	std::map<std::string,Modulo*> m_modulos; 			//!< Hash dos m�dulos e suas refer�ncias indexado pelo nome de um m�dulo.

	std::string m_modulo_inicial; 						//!< Armazena o nome do m�dulo inicial
	bool m_carregado;									//!< Determina se o diagrama especificado foi carregado com sucesso
};

#endif /* DIAGRAMA_H_ */
