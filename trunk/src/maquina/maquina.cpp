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
	//Inicializa a fita como definida na nossa conven��o (@, #, etc)
	m_fita.append("@#" + fita);
	m_fita.resize(tamanho_da_fita,'#');
	m_tamanho_da_fita = tamanho_da_fita;

	// Poe a cabe�a de leitura na posi��o inicial, no caso: 2
	m_pos_atual = 2;
}

//Move a cabe�a de leitura para a esquerda e retorna 'true' caso n�o haja problemas
bool Maquina::mover_esquerda()
{
	if(m_pos_atual > 0){
		m_pos_atual--;
		return true;
	}
	return false;
}

//Move a cabe�a de leitura para a direita e retorna 'true' caso n�o haja problemas
bool Maquina::mover_direita()
{
	//Para uma fita de tamanho n, temos que a posi��o da cabe�a de leitura
	//varia de 0 a n-1, por isso essa verifica��o
	if(m_pos_atual < m_tamanho_da_fita - 1){
		m_pos_atual++;
		return true;
	}
	return false;
}


bool Maquina::escrever(char simbolo)
{
//	std::string::iterator it1;
//	std::string::iterator it2;
//	it1 = m_fita.begin();
//	it2 = m_fita.begin();
//	std::advance(it1,m_pos_atual);
//	std::advance(it2,m_pos_atual+1);
//	m_fita.replace(it1,it2,simbolo.c_str(),1);

	if( m_pos_atual > 0 && m_pos_atual < m_fita.size() ) {
		m_fita[m_pos_atual] = simbolo;
		return true;
	}

	return false;
	/*

	char s[1];
	s[0] = simbolo;
	//Substitui o simbolo na posi��o atual da cabe�a de leitura
	if(m_pos_atual > 0){
		m_fita.replace(m_pos_atual,1,s);
		return true;
	}
	return false;
	*/
	return true;
}


char Maquina::simbolo_atual()
{
	std::string::iterator it;
	it = m_fita.begin();
	//Avan�a o iterador da string para o
	//caractere na posi�ao atual da cabe�a de leitura
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

