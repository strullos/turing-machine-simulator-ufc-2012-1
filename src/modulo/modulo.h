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

	bool inicializar();
	bool executar(Maquina *m, char var_value = '\0');
	unsigned int linha_incorreta();

private:
	std::string m_estado_inicial;
	std::string m_arquivo;
	std::multimap<std::string, Regra> m_regras;
	unsigned int m_linha_incorreta;
	bool m_inicializado;
	char m_var, m_var_value;

	bool aplica_regra(Maquina *m, const Regrap r);
	const Regrap procura_regra(std::string estado, char simbolo);
	bool processa_cabecalho(std::string linha);
	bool processa_variavel(std::string linha);
	bool processa_regra(std::string linha);
};

#endif /* MODULO_H_ */
