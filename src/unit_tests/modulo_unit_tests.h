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
	Maquina *m1 = new Maquina("ab", 1000);
	Modulo modulo1("r.mt");
	modulo1.inicializar();
	modulo1.executar(m1);

	if( m1->simbolo_atual() != 'b' ) {
		std::cout << "Erro! Teste 1" << std::endl;
	}

	Maquina *m2 = new Maquina("ab", 1000);
	Modulo modulo2("r#.mt");
	modulo2.inicializar();
	modulo2.executar(m2);

	if( m2->simbolo_atual() != '#' ) {
		std::cout << "Erro! Teste 2" << std::endl;
	}
}


#endif /* MODULO_UNIT_TEST_H_ */
