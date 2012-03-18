/*
 * diagrama.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "diagrama.h"

#include <fstream>
#include <iostream>
#include <sstream>

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
					std::cout << "Falha ao carregar modulo." << std::endl;
					arquivo.close();
					return false;
				}
			}else{
				if(linha != "" && linha != "\r" && linha != "\n")	{
					if(!carregar_regras(linha)){
						std::cout << "Falha ao carregar regras." << std::endl;
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
	std::cout << std::endl;
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
	//Essa tabela guarda os mesmos ponteiros da tabela "m_modulos_carregados", ent�o n�o � necess�rio
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
	std::stringstream tokens;
	tokens << linha_modulo;

	std::string nome_modulo = "";
	std::string arquivo_modulo = "";

	size_t aux_pos = linha_modulo.find(' ');
	tokens.ignore(aux_pos, ' ');
	tokens >> nome_modulo >> arquivo_modulo;
	std::cout << "Modulo: " << nome_modulo << " ";
	std::cout << arquivo_modulo << std::endl;

	//Verifica se esse arquivo de modulo ja foi carregado, caso nao tenha sido,
	//um novo modulo eh instanciado e adicionado na tabela de modulos carregados
	if(m_modulos_carregados.find(arquivo_modulo) == m_modulos_carregados.end()){
		Modulo* novo_modulo = new Modulo(arquivo_modulo);
		if(!novo_modulo->inicializar()){
			delete novo_modulo;
			return false;
		}
		m_modulos_carregados[arquivo_modulo] = novo_modulo;
		//Verifica se ja existe um modulo com esse nome, caso nao tenha, um modulo com esse nome ja adicionado na tabela de modulos
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = novo_modulo;
			//Verica se algum modulo ja foi carregado. Se nao tiver sido, o primeiro modulo carregado
			//sera o modulo inicial
			if(m_modulos_carregados.size() == 1){
				m_modulo_inicial = nome_modulo;
			}
		}else{
			std::cout << "Uma referencia com esse nome ja existe. Ignorando nova referencia." << std::endl;
		}
	}else{
		std::cout << "Arquivo de modulo ja carregado. Perparando para adicionar nova referencia." << std::endl;
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = m_modulos_carregados[arquivo_modulo];
		}else{
			std::cout << "Uma referencia com esse nome ja existe. Ignorando nova referencia." << std::endl;
		}
	}
	return true;
}

bool Diagrama::carregar_regras(std::string& linha_regra)
{
	std::stringstream tokens;
	std::string modulo_inicial = "";
	std::vector<std::string> lista_de_simbolos;
	std::string simbolos = "";
	std::string modulo_final = "";

	tokens << linha_regra;
	tokens >> modulo_inicial >> simbolos >> modulo_final;
	std::cout << "Regra: " << modulo_inicial << " ";
	std::cout << simbolos << " ";
	std::cout<< modulo_final << std::endl;

	if( (modulo_inicial.size() == 0) || (simbolos.size() == 0) || (modulo_final.size() == 0) ){
		return false;
	}
	if( (simbolos.find("[") == std::string::npos) || (simbolos.find("]") == std::string::npos) ){
		return false;
	}

	bool qualquer_simbolo = false;
	std::string::iterator string_it;
	std::string aux;
	for(string_it = simbolos.begin(); string_it != simbolos.end(); string_it++){
		aux = (*string_it);
		if( (aux.compare("[") != 0) && (aux.compare(",") != 0) && (aux.compare("]") != 0) ){
			if(aux.compare("*") == 0){
				qualquer_simbolo = true;
			}
			lista_de_simbolos.push_back(aux);
		}
	}

	if(lista_de_simbolos.size() == 0){
		return false;
	}

	Regra *regra = NULL;
	std::map<std::string,Regra*>::iterator regras_it = m_regras.find(modulo_inicial);
	std::vector<std::string>::iterator simbolos_it;
	if(regras_it == m_regras.end()){
		regra = new Regra();
		for(simbolos_it = lista_de_simbolos.begin(); simbolos_it != lista_de_simbolos.end(); simbolos_it++){
			regra->inserir((*simbolos_it),modulo_final);
		}
		m_regras.insert(std::pair<std::string,Regra*>(modulo_inicial,regra));
	}else{
		regra = (*regras_it).second;
		for(simbolos_it = lista_de_simbolos.begin(); simbolos_it != lista_de_simbolos.end(); simbolos_it++){
			regra->inserir((*simbolos_it),modulo_final);
		}
	}
	regra->m_qualquer_simbolo = qualquer_simbolo;
	return true;
}

void Diagrama::print_diagram()
{
	std::string module_name;
	std::map<std::string, Modulo*>::iterator it;
	std::cout << "Diagrama: " << std::endl;
	for(it = m_modulos.begin(); it != m_modulos.end(); it++){
		module_name = (*it).first;
		std::cout << "Module " << module_name << " carregado." << std::endl;
	}
	std::string initial_module;
	std::string symbol;
	std::string last_module;
	std::cout << std::endl;
	std::cout << "Regras: " << std::endl;
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
	std::cout << std::endl;
}

void Diagrama::executar(std::string fita_inicial, unsigned int tamanho_da_fita)
{
	if(m_carregado){
		Maquina* mt = new Maquina(fita_inicial, tamanho_da_fita);
		Modulo* modulo = NULL;
		std::string modulo_atual = m_modulo_inicial;
		std::string prox_modulo;
		std::string simbolo_atual;
		std::cout << "Iniciando." << std::endl;
		std::cout << "Modulo inicial: " << modulo_atual << std::endl;
		std::map<std::string,Regra*>::iterator regra_it;
		std::map<std::string,Modulo*>::iterator modulo_it;
		unsigned int passos = 1;
		bool executando = true;
		std::cout << passos << ": ";
		mt->print_tape();
		passos++;
		while(executando){
			modulo_it = m_modulos.find(modulo_atual);
			if(modulo_it != m_modulos.end()){
				modulo = m_modulos[modulo_atual];
				if(modulo == NULL){
					return;
				}
			}else{
				return;
			}

			if(modulo->executar(mt)){
				std::cout << passos << ": ";
				mt->print_tape();
				passos++;

				regra_it = m_regras.find(modulo_atual);
				if(regra_it != m_regras.end()){
					simbolo_atual = mt->simbolo_atual();
					modulo_atual = (*regra_it).second->pegar_prox_modulo(simbolo_atual);
				}else{
					executando = false;
				}
			}else{
				executando = false;
			}
		}
		delete mt;
	}
}








