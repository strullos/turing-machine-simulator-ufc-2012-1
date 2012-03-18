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
	Maquina(const std::string& fita); //< Recebe uma string representando a fita e o tamanho total da fita
	virtual ~Maquina() {}; //< Destrutor
	bool mover_esquerda(); //< Move a cabe�a de leitura 1 posi��o para a esquerda / Decrementa m_pos_atual
	bool mover_direita();  //< Move a cabe�a de leitura 1 posi��o para a direita  / Incrementa m_pos_atual
	bool escrever(char simbolo); //< Escreve "simbolo" na fita na posi��o atual
	char simbolo_atual(); //< Retorna o s�mbolo da fita na posi��o atual
	const std::string& fita(); //< Retorna a fita

	void print_tape(); // remove later
private:
	unsigned int m_pos_atual; //< Representa a cabe�a de leitura da m�quina / a posi��o atual na fita
	unsigned int m_tamanho_da_fita; //< Tamanho m�ximo da fita
	std::string m_fita; //< O conte�do da fita
};

#endif /* MAQUINA_H_ */
