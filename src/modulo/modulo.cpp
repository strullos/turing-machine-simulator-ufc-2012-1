/*
 * modulo.cpp
 *
 *  Created on: 17/03/2012
 *      Author: felipe
 */

#include <fstream>
#include <sstream>
#include "modulo.h"

Modulo::Modulo(const std::string &arquivo) : m_arquivo(arquivo), m_linha_incorreta(0)
{

}

Modulo::~Modulo()
{

}

bool Modulo::executar(Maquina *m)
{
	std::string estado_atual = m_estado_inicial;
	std::multimap<std::string, Regra>::const_iterator it;

	// Enquanto puder encontrar uma regra com o estado1 igual ao estado atual...
	while( (it = m_regras.find(estado_atual)) != m_regras.end() ) {

		// Procura uma regra com estado1 = estado_atual e simbolo = simbolo_atual
		for( ; it->first != estado_atual || it != m_regras.end() ; it++) {
			const Regra &regra_atual = it->second;

			if( regra_atual.simbolo == m->simbolo_atual() ) {
				if( !aplica_regra(m, regra_atual) ) {
					return false;
				}
				// Se conseguiu aplicar a regra, passa para o proximo estado
				estado_atual = regra_atual.estado2;
				break;
			}
		}
	}

	return true;
}

bool Modulo::inicializar()
{
	std::ifstream fs(m_arquivo.c_str());
	std::string estado_inicial, tamanho_fita, num_iteracoes;
	std::string estado1, estado2;
	char acao, simbolo;

	// Se nao conseguiu abrir o arquivo, retorna erro
	if( !fs ) {
		return false;
	}

	m_linha_incorreta = 1;
	m_regras.clear();
	// Le cabecalho - se a primeira linha do arquivo nao estiver no formato certo, retorna erro
	fs >> estado_inicial >> tamanho_fita >> num_iteracoes;
	if( !fs.good() ) {
		return false;
	}

	m_estado_inicial = estado_inicial;
	// Le as quadruplas e gera uma regra para cada quadrupla lida
	while( !fs.eof() ) {
		++m_linha_incorreta;
		fs >> estado1 >> simbolo >> estado2 >> acao;

		// Se nao conseguiu ler uma linha no formato das quadruplas...
		if( !fs.good() ) {
			// Verifica se eh uma linha em branco - se nao for, retorna erro
			std::string line;

			std::getline(fs, line);
			if( !line.empty() ) {
				return false;
			}
		} else {
			Regra regra_atual = { simbolo, estado2, acao };
			m_regras.insert(std::pair<std::string, Regra>( estado_inicial, regra_atual ));
		}
	}

	return true;
}

bool Modulo::aplica_regra(Maquina *m, const Regra &r)
{
	bool ret;

	if( !m ) {
		return false;
	}

	switch(r.acao) {
	case '>':
		ret = m->mover_direita();
		break;

	case '<':
		ret = m->mover_esquerda();
		break;

	default:
		ret = m->escrever(r.simbolo);
		break;
	}

	return ret;
}

unsigned int Modulo::linha_incorreta()
{
	return m_linha_incorreta;
}
