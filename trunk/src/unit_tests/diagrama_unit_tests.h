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
	d->carregar_diagrama("diagrama.dt");
	delete d;
}


#endif /* DIAGRAMA_UNIT_TESTS_H_ */