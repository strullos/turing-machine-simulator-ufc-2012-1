/*
 * Regra.h
 *
 *  Created on: 17/03/2012
 *      Author: felipe
 */

#ifndef REGRA_H_
#define REGRA_H_

class Regra {
public:
	Regra(const std::string &_estado_inicial, char _simbolo, const std::string &_estado_final, char _acao);
	virtual ~Regra();



private:
	std::string m_estado_inicial, m_estado_final;
	char m_simbolo, m_acao;
};

#endif /* REGRA_H_ */
