/*
 * main.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */



#include "diagrama/diagrama.h"

#include <iostream>
#include <sstream>

int main(int argc, char** argv){

	if(argc < 3){
		std::cout << "Para rodar um diagrama, passe o nome do arquivo por parametro e o estado inicial da fita." << std::endl;
		std::cout << "O modulo inicial sera o primeiro modulo a ser carregado no diagrama." << std::endl;
	}else{
		std::cout << std::endl;
		std::string arquivo = argv[1];
		std::string fita = argv[2];
		Diagrama *d = new Diagrama();
		if(d->carregar_diagrama(arquivo)){
			d->imprime_diagrama();
			d->executar(fita);
		}
		delete d;
	}
	return 0;
}

