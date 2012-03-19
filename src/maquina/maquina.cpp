/*
 * maquina.cpp
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#include "maquina.h"

#include <iostream>

Maquina::Maquina(const std::string& fita)
{
	m_tamanho_da_fita = 2 + fita.size() + 1;
	//Inicializa a fita como definida na nossa convencao (@, #, etc)
	m_fita.append("@#" + fita);
	m_fita.resize(m_tamanho_da_fita,'#');

	// Poe a cabeca de leitura na posicao inicial, no caso: 2
	m_pos_atual = 1;
}

//Move a cabeca de leitura para a esquerda e retorna 'true' caso nao haja problemas
bool Maquina::mover_esquerda()
{
	if(m_pos_atual > 0){
		m_pos_atual--;
		return true;
	}
	return false;
}

//Move a cabeca de leitura para a direita e retorna 'true' caso nao haja problemas
bool Maquina::mover_direita()
{
	//Para uma fita de tamanho n, temos que a posicao da cabeca de leitura
	//varia de 0 a n-1. Caso a pos atual seja maior que a fita, o tamanho da fita dobra
	m_pos_atual++;
	if(m_pos_atual >= m_tamanho_da_fita){
		m_tamanho_da_fita = 2*m_tamanho_da_fita;
		m_fita.resize(m_tamanho_da_fita,'#');
	}
	return true;
}


bool Maquina::escrever(char simbolo)
{
	//Substitui o simbolo na posicao atual da cabeca de leitura
	if( m_pos_atual > 0 && m_pos_atual < m_fita.size() ) {
		m_fita[m_pos_atual] = simbolo;
		return true;
	}

	return false;
}


char Maquina::simbolo_atual()
{
	std::string::iterator it;
	it = m_fita.begin();
	//Avanca o iterador da string para o
	//caractere na posicao atual da cabeca de leitura
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

