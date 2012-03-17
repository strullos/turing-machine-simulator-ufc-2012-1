/*
 * diagrama.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "diagrama.h"

#include <fstream>
#include <iostream>

Diagrama::Diagrama() {

}

bool Diagrama::carregar_diagrama(std::string caminho_arquivo)
{
	std::ifstream arquivo;
	arquivo.open(caminho_arquivo.c_str(), std::ifstream::in);
	std::string linha;

	if(arquivo.is_open()){
		while(arquivo.good())
		{
			std::getline(arquivo,linha);
			if( linha.find("modulo",0) == 0){
				if(!carregar_modulo(linha)){
					std::cout << "Falha ao carregar" << std::endl;
					arquivo.close();
					return false;
				}
			}
		}
	}
	std::cout << "Diagrama carregado com sucesso" << std::endl;
	return true;
}

Diagrama::~Diagrama()
{
	// TODO Auto-generated destructor stub
}

bool Diagrama::carregar_modulo(std::string& linha_modulo)
{
	std::string nome_modulo = "";
	std::string arquivo_modulo = "";
	size_t espaco_pos;
	espaco_pos = linha_modulo.find(" ");
	if(espaco_pos == std::string::npos){
		std::cout << "Arquivo inválido" << std::endl;
		return false;
	}

	//Remove modulo da linha
	linha_modulo = linha_modulo.substr(espaco_pos,linha_modulo.size());

	//Remove os espaços em branco depois de módulo
	espaco_pos = linha_modulo.find_first_not_of(" ");
	if(espaco_pos == std::string::npos){
		std::cout << "Arquivo inválido" << std::endl;
		return false;
	}
	linha_modulo = linha_modulo.substr(espaco_pos,linha_modulo.size() - espaco_pos);

	//Pega o nome do módulo
	espaco_pos = linha_modulo.find(" ");
	if(espaco_pos == std::string::npos){
		std::cout << "Arquivo inválido" << std::endl;
		return false;
	}
	nome_modulo = linha_modulo.substr(0,espaco_pos);
	std::cout << nome_modulo << std::endl;

	//Remove o nome do módulo da string
	linha_modulo = linha_modulo.substr(espaco_pos, linha_modulo.size() - espaco_pos);

	//Pega o nome do arquivo do módulo
	espaco_pos = linha_modulo.find_first_not_of(" ");
	if(espaco_pos == std::string::npos){
		std::cout << "Arquivo inválido" << std::endl;
		return false;
	}

	linha_modulo = linha_modulo.substr(espaco_pos, linha_modulo.size() - espaco_pos);
	arquivo_modulo = linha_modulo;
	std::cout << arquivo_modulo << std::endl;

	//Verifica se esse arquivo de módulo já foi carregado, caso não tenha sido,
	//um novo módulo é instanciado e adicionado à tabela de módulos carregados
	if(m_modulos_carregados.find(arquivo_modulo) == m_modulos_carregados.end()){
		Modulo* novo_modulo = new Modulo(arquivo_modulo);
		m_modulos_carregados[arquivo_modulo] = novo_modulo;
		//Verifica se já existe um módulo com esse nome, caso não tenha, um módulo com esse nome é adicionado à tabela de módulos
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = novo_modulo;
		}else{
			std::cout << "Um módulo com esse nome já foi carregado. Ignorando a instância repetida..." << std::endl;
		}
	}else{
		std::cout << "Arquivo de módulo já carregado. Ignorando.." << std::endl;
	}
	return true;
}

bool Diagrama::configurar_acoes(std::string& linha_acao)
{
	return true;
}





