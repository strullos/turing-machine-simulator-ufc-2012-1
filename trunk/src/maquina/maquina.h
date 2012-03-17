/*
 * maquina.h
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#ifndef MAQUINA_H_
#define MAQUINA_H_

#include <string>

/*


 */
class Maquina {
public:
	Maquina(const std::string& fita, unsigned int tamanho_da_fita); //< Recebe uma string representando a fita e o tamanho total da fita
	virtual ~Maquina() {}; //< Destrutor
	bool mover_esquerda(); //< Move a cabeça de leitura 1 posição para a esquerda / Decrementa m_pos_atual
	bool mover_direita();  //< Move a cabeça de leitura 1 posição para a direita  / Incrementa m_pos_atual
	bool escrever(char simbolo); //< Escreve "simbolo" na fita na posição atual
	char simbolo_atual(); //< Retorna o símbolo da fita na posição atual
	const std::string& fita(); //< Retorna a fita

	void print_tape(); // remove later
private:
	unsigned int m_pos_atual; //< Representa a cabeça de leitura da máquina / a posição atual na fita
	unsigned int m_tamanho_da_fita; //< Tamanho máximo da fita
	std::string m_fita; //< O conteúdo da fita
};

#endif /* MAQUINA_H_ */
