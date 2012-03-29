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
	bool m_atualiza_var; //!< Se esta regra atualiza o valor de uma variavel
	bool m_envia_var;	//!< Se esta regra passa o valor de uma variavel para o modulo
	std::string m_variavel_atualizada;	//!< A variavel a ser atualizada
	std::string m_variavel_enviada;		//!< A variavel a ser enviada para o modulo
	std::string m_prox_modulo;			//!< O proximo modulo a ser executado
}DescritorRegra;

class RegraDiagrama{
public:
	RegraDiagrama();								//!< Construtor
	~RegraDiagrama();	 														//!< Destrutor
	void inserir(std::string simbolo, 
					std::string modulo_final,
					bool atualiza_var = false,
					std::string var_atualizada = "/0",
					bool envia_var = false,
					std::string var_enviada = "/0");								//!< Insere uma nova regra

	std::string pegar_prox_modulo(std::string simbolo_atual);					//!< Retorna o proximo modulo a ser executado
	DescritorRegra*	pegar_descritor_regra(std::string simbolo_atual);
	std::map<std::string, DescritorRegra*> m_regras;							//!< Hash, contendo o simbolo esperado na cabeca de leitura e as informacoes sobre essa regra
	bool m_qualquer_simbolo;													//!< Determina se essa regra eh do tipo que aceita qualquer simbolo na cabeca de leitura
};
