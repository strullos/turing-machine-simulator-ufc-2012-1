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

typedef struct DescritorRegra_t
{
	bool m_passa_var;
	std::string m_variavel;
	std::string m_prox_modulo;
}DescritorRegra;

class RegraDiagrama{
public:
	RegraDiagrama(std::string &simbolo, 
					std::string &modulo_final,
					std::string variavel = "");					//!< Construtor
	RegraDiagrama();	 											//!< Destrutor
	void inserir(std::string simbolo, 
					std::string modulo_final,
					std::string variavel = "");						//!< Insere uma nova regra

	std::string pegar_prox_modulo(std::string simbolo_atual);		//!< Retorna o proximo modulo a ser executado
	DescritorRegra*	pegar_descritor_regra(std::string simbolo_atual);
	std::map<std::string, DescritorRegra*> m_regras;							//!< Hash, contendo o simbolo esperado na cabeca de leitura e as informacoes sobre essa regra
	bool m_qualquer_simbolo;										//!< Determina se essa regra eh do tipo que aceita qualquer simbolo na cabeca de leitura
};
