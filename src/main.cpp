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

	if(argc < 4){
		std::cout << "Para rodar um diagrama, passe o nome do arquivo por parametro, o estado inicial da fita" << std::endl;
		std::cout << "e o tamanho maximo da fita." << std::endl;
		std::cout << "O modulo inicial sera o primeiro modulo a ser carregado no diagrama." << std::endl;
	}else{
		std::cout << std::endl;
		std::string arquivo = argv[1];
		std::string fita = argv[2];
		std::string tamanho = argv[3];
		Diagrama *d = new Diagrama();
		d->carregar_diagrama(arquivo);

		std::stringstream ss;
		ss << tamanho;
		unsigned int tam;
		ss >> tam;
		d->print_diagram();
		d->executar(fita,tam);

		delete d;
	}
	return 0;
}

