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
	bool mover_esquerda(); //< Move a cabeca de leitura 1 posicao para a esquerda / Decrementa m_pos_atual
	bool mover_direita();  //< Move a cabeca de leitura 1 posicao para a direita  / Incrementa m_pos_atual
	bool escrever(char simbolo); //< Escreve "simbolo" na fita na posicao atual
	char simbolo_atual(); //< Retorna o simbolo da fita na posicao atual
	const std::string& fita(); //< Retorna a fita

	void imprimir_fita(); // imprime a fita no estado atual
private:
	unsigned int m_pos_atual; //< Representa a cabeca de leitura da m�quina / a posicao atual na fita
	unsigned int m_tamanho_da_fita; //< Tamanho maximo da fita
	std::string m_fita; //< O conteudo da fita
};

#endif /* MAQUINA_H_ */
