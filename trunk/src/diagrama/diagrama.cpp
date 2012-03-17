/*
 * diagrama.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "diagrama.h"

#include <fstream>
#include <iostream>

Modulo::Modulo() {
}

Diagrama::Diagrama() {

}

bool Diagrama::carregar_diagrama(std::string caminho_arquivo)
{
	std::ifstream arquivo;
	arquivo.open(caminho_arquivo.c_str(), std::ifstream::in);
	std::string linha;

	std::string nome_modulo;
	std::string arquivo_modulo;
	int espaco1;
	if(arquivo.is_open()){
		while(arquivo.good())
		{
			std::getline(arquivo,linha);
			if( linha.find("modulo",0) == 0){
				espaco1 = linha.find(" ");
				if(espaco1 == std::string::npos){
					std::cout << "Arquivo inválido" << std::endl;
					return -1;
				}

				//Remove modulo da linha
				linha = linha.substr(espaco1,linha.size());

				//Remove os espaços em branco depois de módulo
				espaco1 = linha.find_first_not_of(" ");
				if(espaco1 == std::string::npos){
					std::cout << "Arquivo inválido" << std::endl;
					return -1;
				}
				linha = linha.substr(espaco1,linha.size() - espaco1);

				//Pega o nome do módulo
				espaco1 = linha.find(" ");
				if(espaco1 == std::string::npos){
					std::cout << "Arquivo inválido" << std::endl;
					return -1;
				}
				nome_modulo = linha.substr(0,espaco1);
				std::cout << nome_modulo << std::endl;

				//Remove o nome do módulo da string
				linha = linha.substr(espaco1, linha.size() - espaco1);

				//Pega o nome do arquivo do módulo
				espaco1 = linha.find_first_not_of(" ");
				if(espaco1 == std::string::npos){
					std::cout << "Arquivo inválido" << std::endl;
					return -1;
				}

				linha = linha.substr(espaco1, linha.size() - espaco1);
				arquivo_modulo = linha;
				std::cout << arquivo_modulo << std::endl;
			}
		}
		arquivo.close();
		return true;
	}
	std::cout << "falha ao carregar" << std::endl;
	return false;
}

Diagrama::~Diagrama()
{
	// TODO Auto-generated destructor stub
}




