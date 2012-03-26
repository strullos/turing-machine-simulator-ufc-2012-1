/*!
 * Representa uma regra do diagrama.
 * Cada regra eh referente apenas a um unico modulo e eh composta por um conjunto de
 * simbolos de leitura e Modulos associados.
 * Uma regra do diagrama especifica que, especifica o proximo modulo a ser executado, de
 * acordo com o simbolo atual na cabeca de leitura.
 *
 * Obs: Para evitar a criacao de mais arquivos, essa classe foi definida toda inline.
 */

#include <string>
#include <map>

class RegraDiagrama{
public:
	RegraDiagrama(std::string &simbolo, 
					std::string &modulo_final);		//!< Construtor
	RegraDiagrama();	 						//!< Destrutor
	void inserir(std::string simbolo, 
					std::string modulo_final);		//!< Insere uma nova regra

	std::string pegar_prox_modulo(std::string simbolo_atual);		//!< Retorna o proximo modulo a ser executado
	std::map<std::string, std::string> m_regras;				//!< Hash, contendo o simbolo esperado na cabeca de leitura e o nome do proximo modulo
	bool m_qualquer_simbolo;						//!< Determina se essa regra eh do tipo que aceita qualquer simbolo na cabeca de leitura
};
