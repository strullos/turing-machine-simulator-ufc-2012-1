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
	bool continua = true;
	std::string estado_atual = m_estado_inicial;
	std::multimap<std::string, Regra>::const_iterator it;

	// Enquanto puder encontrar uma regra com o estado1 igual ao estado atual...
	while( continua && (it = m_regras.find(estado_atual)) != m_regras.end() ) {
		// Se o laco for nao encontrar nenhuma regra que corresponda ao simbolo atual da maquina, a maquina para
		continua = false;

		// Procura uma regra com estado1 = estado_atual e simbolo = simbolo_atual(ou simbolo coringa '*')
		for( ; it != m_regras.end() && it->first == estado_atual ; it++) {
			const Regra &regra_atual = it->second;

			if( regra_atual.simbolo == m->simbolo_atual() || regra_atual.simbolo == '*' ) {
				continua = true;
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
	bool resultado_ok = true;
	std::ifstream fs(m_arquivo.c_str());
	std::string estado_inicial;
	std::string estado1, estado2;
	std::string linha;
	std::stringstream ss;
	int tamanho_fita, num_iteracoes;
	char acao, simbolo;

	// Se nao conseguiu abrir o arquivo, retorna erro
	if( !fs ) {
		resultado_ok = false;
	} else {
		m_linha_incorreta = 1;
		m_regras.clear();

		std::getline(fs, linha);
		ss << linha;
		// Le cabecalho - se a primeira linha do arquivo nao estiver no formato certo, retorna erro
		ss >> estado_inicial >> tamanho_fita >> num_iteracoes;
		if( ss.fail() ) {
			resultado_ok = false;
		} else {
			m_estado_inicial = estado_inicial;
		}
		ss.clear();
	}

	while( resultado_ok && !fs.eof() ) {
		std::getline(fs, linha);
		if( !linha.empty() && linha != "\r" && linha != "\n") {
			ss << linha;
			ss >> estado1 >> simbolo >> estado2 >> acao;
			if( !ss.fail() ) {
				if( simbolo != '*' || m_regras.find(estado1) == m_regras.end() ) {
					Regra regra_atual = { simbolo, estado2, acao };
					m_regras.insert(std::pair<std::string, Regra>( estado_inicial, regra_atual ));
				} else {
					resultado_ok = false;
				}
			} else {
				resultado_ok = false;
			}
			ss.clear();
		}
		++m_linha_incorreta;
	}

	fs.close();
	return resultado_ok;
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
		ret = m->escrever(r.acao);
		break;
	}

	return ret;
}

unsigned int Modulo::linha_incorreta()
{
	return m_linha_incorreta;
}
