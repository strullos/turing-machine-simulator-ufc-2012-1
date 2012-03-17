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
#include "regra.h"
#include "../maquina/maquina.h"

class Modulo {
public:
	Modulo(const std::string &path);
	virtual ~Modulo();

	Maquina* executar(Maquina *);
private:
	std::string regra_inicial;
	std::multimap<std::string, Regra*> m_regras;
};

#endif /* MODULO_H_ */
