/*
 * modulo.h
 *
 *  Created on: 17/03/2012
 *      Author: felipe
 */

#ifndef MODULO_H_
#define MODULO_H_
#include <string>
#include <map>
#include "../maquina/maquina.h"

typedef struct {
	char simbolo;
	std::string estado2;
	char acao;
}Regra, *Regrap;

class Modulo {
public:
	Modulo(const std::string &arquivo);
	virtual ~Modulo();

	bool carregar();
	bool inicializar();
	bool executa_passo(Maquina *m, char var_value = '\0');
	bool recebe_variavel();
	std::string pega_estado_atual();
	unsigned int linha_incorreta();

private:
	std::string m_estado_inicial;
	std::string m_estado_atual;
	std::string m_arquivo;
	std::multimap<std::string, Regra> m_regras;
	unsigned int m_linha_atual;
	bool m_inicializado;
	bool m_recebe_var;
	char m_var;
	char m_var_value;

	bool aplica_regra(Maquina *m, const Regrap r);
	const Regrap procura_regra(std::string estado, char simbolo);
	bool processa_cabecalho(std::string linha);
	bool processa_declaracao_variavel(std::string linha);
	bool processa_regra(std::string linha);
};

#endif /* MODULO_H_ */
