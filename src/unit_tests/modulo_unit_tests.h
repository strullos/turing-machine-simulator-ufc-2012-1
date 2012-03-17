/*
 * modulo_unit_test.h
 *
 *  Created on: 17/03/2012
 *      Author: felipe
 */

#ifndef MODULO_UNIT_TEST_H_
#define MODULO_UNIT_TEST_H_
#include <iostream>
#include "../modulo/modulo.h"

void modulo_unit_tests()
{
	Maquina *m = new Maquina("ab", 1000);
	Modulo modulo("r.mt");
	modulo.inicializar();
	modulo.executar(m);

	if( m->simbolo_atual() != 'b' ) {
		std::cout << "Erro!" << std::endl;
	}
}


#endif /* MODULO_UNIT_TEST_H_ */
