/*
 * main.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "maquina/maquina.h"

#include <iostream>

int main(int argc, char** argv){
	Maquina *m = new Maquina("abcd", 10);
	m->print_tape();
	m->escrever("d");
	m->print_tape();
	m->mover_direita();
	m->mover_direita();
	m->mover_direita();
	m->mover_direita();
	std::cout << m->simbolo_atual() << std::endl;
	return 0;
}

