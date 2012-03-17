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

class Modulo {
public:
	Modulo(const std::string &arquivo);
	virtual ~Modulo();

	bool inicializar();
	bool executar(Maquina *m);
	unsigned int linha_incorreta();
private:
	typedef struct {
		char simbolo;
		std::string estado2;
		char acao;
	}Regra;

	std::string m_estado_inicial;
	std::string m_arquivo;
	std::multimap<std::string, Regra> m_regras;
	unsigned int m_linha_incorreta;

	bool aplica_regra(Maquina *m, const Regra &r);
};

#endif /* MODULO_H_ */
