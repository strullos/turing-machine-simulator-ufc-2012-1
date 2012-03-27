/*
 * maquina_unit_tests.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "../maquina/maquina.h"

#include <iostream>

void maquina_unit_tests()
{
	Maquina *m = new Maquina("abcd", 10);
	m->escrever('d');
	std::cout << m->simbolo_atual() << std::endl;
	m->mover_direita();
	m->mover_direita();
	m->mover_direita();
	m->mover_direita();
	std::cout << m->simbolo_atual() << std::endl;
	m->imprimir_fita();
	m->mover_esquerda();
	m->mover_esquerda();
	m->mover_esquerda();
	m->mover_esquerda();
	m->imprimir_fita();
	m->mover_esquerda();
	m->mover_esquerda();
	m->imprimir_fita();

	delete m;
}

