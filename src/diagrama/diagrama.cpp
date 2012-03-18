/*
 * diagrama.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "diagrama.h"

#include <fstream>
#include <iostream>

Diagrama::Diagrama() {}

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
					std::cout << "Falha ao carregar m�dulo." << std::endl;
					arquivo.close();
					return false;
				}
			}else{
				if(linha != "")	{
					if(!carregar_acoes(linha)){
						std::cout << "Falha ao carregar a��es." << std::endl;
						arquivo.close();
						return false;
					}
				}
			}
		}
	}else{
		std::cout << "Falha ao abrir o arquivo." << std::endl;
		return false;
	}
	std::cout << "Diagrama carregado com sucesso." << std::endl;
	arquivo.close();
	return true;
}

Diagrama::~Diagrama()
{
	std::map<std::string, Modulo*>::iterator it;
	Modulo* m = NULL;
	while(!m_modulos_carregados.empty()){
		it = m_modulos_carregados.begin();
		m = (*it).second;
		delete (m);
		m_modulos_carregados.erase(it);
	}
	m_modulos_carregados.clear();
	//Essa tabela guarda os mesmos ponteiros da tabela "m_modulos_carregados", ent�o n�o � necess�rio
	//deletar esses ponteiros, basta limpar a tabela;
	m_modulos.clear();
	m_acoes_diagrama.clear();
}

bool Diagrama::carregar_modulo(std::string& linha_modulo)
{
	std::string nome_modulo = "";
	std::string arquivo_modulo = "";
	size_t aux_pos;
	aux_pos = linha_modulo.find(" ");
	if(aux_pos == std::string::npos){
		std::cout << "Arquivo inv�lido." << std::endl;
		return false;
	}
	//Remove "modulo" da linha
	remover_valor_da_linha(linha_modulo);
	remover_espacos_brancos(linha_modulo);

	//Pega o nome do m�dulo
	if(!pegar_remover_valor_da_linha(linha_modulo,nome_modulo))
		return false;
	remover_espacos_brancos(linha_modulo);
	std::cout << "Modulo: " << nome_modulo;

	//Pega o nome do arquivo do m�dulo
	arquivo_modulo = linha_modulo;
	std::cout << "  Arquivo: " << arquivo_modulo << std::endl;

	//Verifica se esse arquivo de m�dulo j� foi carregado, caso n�o tenha sido,
	//um novo m�dulo � instanciado e adicionado � tabela de m�dulos carregados
	if(m_modulos_carregados.find(arquivo_modulo) == m_modulos_carregados.end()){
		Modulo* novo_modulo = new Modulo(arquivo_modulo);
		m_modulos_carregados[arquivo_modulo] = novo_modulo;
		//Verifica se j� existe um m�dulo com esse nome, caso n�o tenha, um m�dulo com esse nome � adicionado � tabela de m�dulos
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = novo_modulo;
		}else{
			std::cout << "Uma refer�ncia com esse nome j� existe. Ignorando nova refer�ncia..." << std::endl;
		}
	}else{
		std::cout << "Arquivo de m�dulo j� carregado. Adicionando nova refer�ncia..." << std::endl;
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = m_modulos_carregados[arquivo_modulo];
		}else{
			std::cout << "Uma refer�ncia com esse nome j� existe. Ignorando nova refer�ncia..." << std::endl;
		}
	}
	return true;
}

bool Diagrama::carregar_acoes(std::string& linha_ac)
{
	std::string modulo_inicial;
	std::string simbolo;
	std::string modulo_final;
	size_t aux_pos;
	if(!pegar_remover_valor_da_linha(linha_ac,modulo_inicial))
		return false;

	if(m_modulos.find(modulo_inicial) == m_modulos.end()){
		std::cout << "Modulo " << modulo_inicial << " n�o declarado. Abortando..." << std::endl;
		return false;
	}
//	std::cout << "Ac para o Modulo: " << modulo_inicial << std::endl;


	aux_pos = linha_ac.find("[");
	if(aux_pos == std::string::npos){
		std::cout << "Arquivo inv�lido. " << std::endl;
		return false;
	}
	linha_ac = linha_ac.substr(aux_pos+1,linha_ac.size() - (aux_pos-1));

	aux_pos = linha_ac.find("]");
	simbolo = linha_ac.substr(0,aux_pos);
//	std::cout << "Ao ler simbolo: " << simbolo << std::endl;

	remover_valor_da_linha(linha_ac);
	remover_espacos_brancos(linha_ac);

	modulo_final = linha_ac;
	if(m_modulos.find(modulo_inicial) == m_modulos.end()){
		std::cout << "Modulo " << modulo_inicial << " n�o declarado. Abortando..." << std::endl;
		return false;
	}
//	std::cout << "Para o modulo: " << modulo_final << std::endl;

	AcDiagrama nova_ac;
	nova_ac.m_ac[simbolo] = modulo_final;
	m_acoes_diagrama.insert(std::pair<std::string,AcDiagrama>(modulo_inicial,nova_ac));

	std::cout << "Ac�es carregadas com sucesso." << std::endl;
	std::cout << std::endl;
	return true;
}

bool Diagrama::remover_espacos_brancos(std::string& linha) {
	size_t aux_pos;
	if(linha.compare(0,1," ") != 0){
		return false;
	}
	//Remove os espa�os em branco depois de "modulo"
	aux_pos = linha.find_first_not_of(" ");
	if(aux_pos == std::string::npos){
		std::cout << "Arquivo inv�lido." << std::endl;
		return false;
	}
	linha = linha.substr(aux_pos,linha.size() - aux_pos);
	return true;
}

bool Diagrama::remover_valor_da_linha(std::string& linha)
{
	size_t aux_pos;
	if(linha.compare(0,1," ") == 0){
		return false;
	}
	aux_pos = linha.find(" ");
	if(aux_pos == std::string::npos){
		std::cout << "Arquivo inv�lido." << std::endl;
		return false;
	}
	linha = linha.substr(aux_pos,linha.size() - aux_pos);
	return true;
}

bool Diagrama::pegar_remover_valor_da_linha(std::string& linha, std::string& valor)
{
	size_t aux_pos;
	if(linha.compare(0,1," ") == 0){
		return false;
	}
	aux_pos = linha.find(" ");
	if(aux_pos == std::string::npos){
		std::cout << "Arquivo inv�lido." << std::endl;
		return false;
	}
	valor = linha.substr(0,aux_pos);
	linha = linha.substr(aux_pos,linha.size() - aux_pos);
	return true;
}








