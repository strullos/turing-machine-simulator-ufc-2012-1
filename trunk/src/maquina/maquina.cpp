/*
 * maquina.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "maquina.h"

#include <iostream>

Maquina::Maquina(const std::string& fita, unsigned int tamanho_da_fita)
{
	//Inicializa a fita como definida na nossa convenção (@, #, etc)
	m_fita.append("@#" + fita);
	m_fita.resize(tamanho_da_fita,'#');
	m_tamanho_da_fita = tamanho_da_fita;

	// Poe a cabeça de leitura na posição inicial, no caso: 2
	m_pos_atual = 2;
}

//Move a cabeça de leitura para a esquerda e retorna 'true' caso não haja problemas
bool Maquina::mover_esquerda()
{
	if(m_pos_atual > 0){
		m_pos_atual--;
		return true;
	}
	return false;
}

//Move a cabeça de leitura para a direita e retorna 'true' caso não haja problemas
bool Maquina::mover_direita()
{
	//Para uma fita de tamanho n, temos que a posição da cabeça de leitura
	//varia de 0 a n-1, por isso essa verificação
	if(m_pos_atual < m_tamanho_da_fita - 1){
		m_pos_atual++;
		return true;
	}
	return false;
}


bool Maquina::escrever(char simbolo[1])
{
//	std::string::iterator it1;
//	std::string::iterator it2;
//	it1 = m_fita.begin();
//	it2 = m_fita.begin();
//	std::advance(it1,m_pos_atual);
//	std::advance(it2,m_pos_atual+1);
//	m_fita.replace(it1,it2,simbolo.c_str(),1);

	//Substitui o simbolo na posição atual da cabeça de leitura
	m_fita.replace(m_pos_atual,1,simbolo);
	return true;
}


char Maquina::simbolo_atual()
{
	std::string::iterator it;
	it = m_fita.begin();
	//Avança o iterador da string para o
	//caractere na posiçao atual da cabeça de leitura
	std::advance(it,m_pos_atual);
	return (*it);
}


const std::string& Maquina::fita()
{
	return m_fita;
}

void Maquina::print_tape(){
	std::string::iterator it;
	unsigned int curr_pos = 0;
	for(it = m_fita.begin(); it != m_fita.end(); it++){
		if(curr_pos == m_pos_atual){
			std::cout << "_";
		}
		std::cout << (*it);
		if(curr_pos != m_pos_atual - 1){
			std::cout << " ";
		}
		curr_pos++;
	}
	std::cout << std::endl;
}

