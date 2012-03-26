/*
 * modulo.cpp
 *
 *  Created on: 17/03/2012
 *      Author: felipe
 */

#include <fstream>
#include <sstream>
#include "modulo.h"

Modulo::Modulo(const std::string &arquivo)
: m_arquivo(arquivo),
  m_linha_incorreta(0),
  m_inicializado(false),
  m_var('\0'),
  m_var_value('\0')
{

}

Modulo::~Modulo()
{

}

bool Modulo::carregar()
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
			m_estado_atual = estado_inicial;
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
				if( (simbolo == '*' && m_regras.find(estado1) == m_regras.end()) ||
					(simbolo != '*' && !procura_regra(estado1, simbolo)) ) {
					Regra regra_atual = { simbolo, estado2, acao };
					std::pair<std::string, Regra> elem(estado1, regra_atual);

					m_regras.insert( elem );
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
	m_inicializado = resultado_ok;
	return resultado_ok;
}


bool Modulo::inicializar()
{
	if(m_inicializado){
		m_estado_atual = m_estado_inicial;
		return true;
	}
	return false;
}

//Executa um passo do modulo
bool Modulo::executa_passo(Maquina* m, char var_value)
{
	m_var_value = var_value;
	const Regrap regra = procura_regra(m_estado_atual, m->simbolo_atual());

	if( !regra ) {
		return false;
	} else {
		if( !aplica_regra(m, regra) ) {
			return false;
		}
		m_estado_atual = regra->estado2;
	}
	return true;
}



bool Modulo::aplica_regra(Maquina *m, const Regrap r)
{
	if( !m ) {
		return false;
	}
	bool ret;

	switch(r->acao) {
	case '>':
		ret = m->mover_direita();
		break;

	case '<':
		ret = m->mover_esquerda();
		break;

	default:
		if( r->acao == m_var ) {
			ret = m->escrever(m_var_value);
		} else {
			ret = m->escrever(r->acao);
		}
		break;
	}

	return ret;
}

unsigned int Modulo::linha_incorreta()
{
	return m_linha_incorreta;
}

const Regrap Modulo::procura_regra(std::string estado, char simbolo)
{
	Regrap regra = NULL;
	std::multimap<std::string, Regra>::iterator it = m_regras.find(estado);

	for( ; it != m_regras.end() && it->first == estado ; it++) {
		Regrap r = &(it->second);
		if( r->simbolo == '*' ||
			r->simbolo == simbolo ||
			(r->simbolo == m_var && m_var_value == simbolo) ) {
			regra = r;
			break;
		}
	}

	return regra;
}

bool Modulo::processa_cabecalho(std::string linha)
{
	return false;
}

bool Modulo::processa_variavel(std::string linha)
{
	return false;
}

std::string Modulo::pega_estado_atual()
{
	return m_estado_atual;
}

bool Modulo::processa_regra(std::string linha)
{
	return false;
}

