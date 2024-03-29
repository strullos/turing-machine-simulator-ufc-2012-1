/*
 * diagrama_unit_test.h
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#ifndef DIAGRAMA_UNIT_TESTS_H_
#define DIAGRAMA_UNIT_TESTS_H_

#include "../diagrama/diagrama.h"

void diagrama_unit_tests(){
	Diagrama *d = new Diagrama();
	d->carregar_diagrama("diagrama2.dt");

	d->print_diagram();
	d->executar("aba",10);
	std::cout << "----" << std::endl;
	d->carregar_diagrama("diagrama3.dt");
	d->print_diagram();
	d->executar("aba",10);

	std::cout << "----" << std::endl;
	d->carregar_diagrama("shiftString.dt");
	d->print_diagram();
	d->executar("aba",10);
	delete d;
}


#endif /* DIAGRAMA_UNIT_TESTS_H_ */
