/*
 * diagrama.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "diagrama.h"

#include <fstream>
#include <iostream>

Diagrama::Diagrama()
{
	m_modulo_inicial = "";
	m_carregado = false;
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
					std::cout << "Falha ao carregar módulo." << std::endl;
					arquivo.close();
					return false;
				}
			}else{
				if(linha != "")	{
					if(!carregar_acoes(linha)){
						std::cout << "Falha ao carregar ações." << std::endl;
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
	m_carregado = true;
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
	//Essa tabela guarda os mesmos ponteiros da tabela "m_modulos_carregados", então não é necessário
	//deletar esses ponteiros, basta limpar a tabela;
	m_modulos.clear();

	std::map<std::string, Regra*>::iterator it2;
	while(!m_regras.empty()){
		it2 = m_regras.begin();
		(*it2).second->m_acoes.clear();
		delete((*it2).second);
		m_regras.erase(it2);
	}

	m_regras.clear();
}

bool Diagrama::carregar_modulo(std::string& linha_modulo)
{
	std::string nome_modulo = "";
	std::string arquivo_modulo = "";
	size_t aux_pos;
	aux_pos = linha_modulo.find(" ");
	if(aux_pos == std::string::npos){
		std::cout << "Arquivo inválido." << std::endl;
		return false;
	}
	//Remove "modulo" da linha
	remover_valor_da_linha(linha_modulo);
	remover_espacos_brancos(linha_modulo);

	//Pega o nome do módulo
	if(!pegar_remover_valor_da_linha(linha_modulo,nome_modulo))
		return false;
	remover_espacos_brancos(linha_modulo);
	std::cout << "Modulo: " << nome_modulo;

	//Pega o nome do arquivo do módulo
	arquivo_modulo = linha_modulo;
	std::cout << "  Arquivo: " << arquivo_modulo << std::endl;

	//Verifica se esse arquivo de módulo já foi carregado, caso não tenha sido,
	//um novo módulo é instanciado e adicionado à tabela de módulos carregados
	if(m_modulos_carregados.find(arquivo_modulo) == m_modulos_carregados.end()){
		Modulo* novo_modulo = new Modulo(arquivo_modulo);
		m_modulos_carregados[arquivo_modulo] = novo_modulo;
		//Verifica se já existe um módulo com esse nome, caso não tenha, um módulo com esse nome é adicionado à tabela de módulos
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = novo_modulo;
			//Verica se algum módulo já foi carregado. Se não tiver sido, o primeiro módulo carregado
			//será o módulo inicial
			if(m_modulos_carregados.size() == 1){
				m_modulo_inicial = nome_modulo;
			}
		}else{
			std::cout << "Uma referência com esse nome já existe. Ignorando nova referência..." << std::endl;
		}
	}else{
		std::cout << "Arquivo de módulo já carregado. Adicionando nova referência..." << std::endl;
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = m_modulos_carregados[arquivo_modulo];
		}else{
			std::cout << "Uma referência com esse nome já existe. Ignorando nova referência..." << std::endl;
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
		std::cout << "Modulo " << modulo_inicial << " não declarado. Abortando..." << std::endl;
		return false;
	}
//	std::cout << "Ac para o Modulo: " << modulo_inicial << std::endl;


	aux_pos = linha_ac.find("[");
	if(aux_pos == std::string::npos){
		std::cout << "Arquivo inválido. " << std::endl;
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
		std::cout << "Modulo " << modulo_inicial << " não declarado. Abortando..." << std::endl;
		return false;
	}
//	std::cout << "Para o modulo: " << modulo_final << std::endl;

	Regra *nova_ac = NULL;
	std::map<std::string,Regra*>::iterator it = m_regras.find(modulo_inicial);
	if(it == m_regras.end()){
		nova_ac = new Regra(simbolo,modulo_final);
		m_regras.insert(std::pair<std::string,Regra*>(modulo_inicial,nova_ac));
	}else{
		Regra* ac = (*it).second;
		ac->inserir(simbolo,modulo_final);
	}

	return true;
}

bool Diagrama::remover_espacos_brancos(std::string& linha) {
	size_t aux_pos;
	if(linha.compare(0,1," ") != 0){
		return false;
	}
	//Remove os espaços em branco depois de "modulo"
	aux_pos = linha.find_first_not_of(" ");
	if(aux_pos == std::string::npos){
		std::cout << "Arquivo inválido." << std::endl;
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
		std::cout << "Arquivo inválido." << std::endl;
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
		std::cout << "Arquivo inválido." << std::endl;
		return false;
	}
	valor = linha.substr(0,aux_pos);
	linha = linha.substr(aux_pos,linha.size() - aux_pos);
	return true;
}

void Diagrama::print_diagram()
{
	std::string module_name;
	std::map<std::string, Modulo*>::iterator it;
	std::cout << "Loaded modules: " << std::endl;
	for(it = m_modulos.begin(); it != m_modulos.end(); it++){
		module_name = (*it).first;
		std::cout << "Module " << module_name << " loaded." << std::endl;
	}
	std::string initial_module;
	std::string symbol;
	std::string last_module;
	std::cout << std::endl;
	std::cout << "Actions: " << std::endl;
	std::map<std::string, Regra*>::iterator it2;
	std::map<std::string, std::string>::iterator it3;
	Regra *ac = NULL;
	for(it2 = m_regras.begin(); it2 != m_regras.end(); it2++){
		initial_module = (*it2).first;
		ac = (*it2).second;
		for(it3 = ac->m_acoes.begin(); it3 != ac->m_acoes.end(); it3++){
			symbol = (*it3).first;
			last_module = (*it3).second;
			std::cout << "(" << initial_module << " , " << symbol << ") -> " << last_module << std::endl;
		}
	}
}

void Diagrama::executar(std::string fita_inicial, unsigned int tamanho_da_fita)
{
	if(m_carregado){
		Maquina* mt = new Maquina(fita_inicial, tamanho_da_fita);
		Modulo* modulo = NULL;
		std::string modulo_atual = m_modulo_inicial;
		std::string prox_modulo;
		std::string simbolo_na_fita;
		std::cout << "Modulo inicial: " << modulo_atual << std::endl;
		bool executando = true;

		mt->print_tape();
		modulo = m_modulos[modulo_atual];
		executando = modulo->executar(mt);
		mt->print_tape();
//		while(executando){
//			simbolo_na_fita = mt->simbolo_atual();
//
//			if(modulo == NULL){
//				return;
//			}
//
//
//			prox_modulo = m_regras[modulo_atual]->m_acoes[simbolo_na_fita];
//			modulo_atual
//		}





		delete mt;
	}
}








