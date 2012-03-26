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

/*!
 *	Construtor, inicializa membros.
 *
 *	@param[in]: none
 *	@param[out: none
 *	return: none
 */
Diagrama::Diagrama() :
m_modulo_atual(""),
m_carregado(false)
{}

/*!
 *
 *	Destrutor; limpa a memória e as estruturas de dados
 *
 *	@param[in]: none
 *	@param[out]: none
 *	return: none
 */
Diagrama::~Diagrama()
{
	limpar();
}

/*!
 *
 *	Reseta o diagrama e limpa as estruturas de dados
 *
 *	@param[in]: none
 *	@param[out]: none
 *	return: none
 */
void Diagrama::limpar()
{
	m_modulo_atual.clear();
	m_carregado = false;

	std::map<std::string, Modulo*>::iterator it;
	Modulo* m = NULL;
	//Apaga a tabela de módulos carregados
	while(!m_modulos_carregados.empty()){
		it = m_modulos_carregados.begin();
		m = (*it).second;
		delete (m);
		m_modulos_carregados.erase(it);
	}
	m_modulos_carregados.clear();
	//Essa tabela guarda os mesmos ponteiros da tabela "m_modulos_carregados", entao nao eh necessario
	//deletar esses ponteiros, basta limpar a tabela;
	m_modulos.clear();

	//Apaga a tabela de regras carregadas
	std::map<std::string, RegraDiagrama*>::iterator it2;
	while(!m_regras_modulos.empty()){
		it2 = m_regras_modulos.begin();
		(*it2).second->m_regras.clear();
		delete((*it2).second);
		m_regras_modulos.erase(it2);
	}
	m_regras_modulos.clear();
}

/*!
 *
 *	Carrega um diagrama
 *
 *	@param[in]: Uma string contendo o diretório do diagrama a ser carregado.
 *
 *	@param[out]: none
 *	return: true, se o diagrama foi carregado corretamente
 */
bool Diagrama::carregar_diagrama(std::string caminho_arquivo)
{
	if(m_carregado){
		limpar();
	}
	std::ifstream arquivo;
	arquivo.open(caminho_arquivo.c_str(), std::ifstream::in);
	std::string linha;

	if(arquivo.is_open()){
		while(arquivo.good())
		{
			std::getline(arquivo,linha);
			//Se a linha inicia com "modulo", então deve conter uma instrução para carregar um módulo
			if( linha.find("modulo",0) == 0){
				if(!carregar_modulo(linha)){
					std::cout << "Falha ao carregar modulo." << std::endl;
					arquivo.close();
					return false;
				}
			}else{
				//Caso o contrário, supondo um arquivo válido, deve conter uma regra do diagrama
				if(linha != "" && linha != "\r" && linha != "\n")	{
					if(!carregar_regra(linha)){
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


/*!
 *
 *	Carrega um módulo
 *
 *	@param[in]: A linha contendo a instrução de carregar o módulo
 *	@param[out]: none
 *	return: true, se o módulo foi carregado corretamente
 */
bool Diagrama::carregar_modulo(std::string& linha_modulo)
{
	//Tokenizamos a linha. Supondo um arquivo válido, temos
	//cada possível token separado por espaços.
	std::stringstream tokens;
	tokens << linha_modulo;

	std::string nome_modulo = "";
	std::string arquivo_modulo = "";

	//A instrução de carregar modulos tem o seguinte formato:
	// modulo A arquivo
	//Como não precisamos do token modulo, descartamos os caracteres inicias da string, ate
	//a posicao do primeiro ' '
	size_t aux_pos = linha_modulo.find(' ');
	tokens.ignore(aux_pos, ' ');

	//As palavras restantes se referem ao nome do modulo e o arquivo do qual sera carregado
	tokens >> nome_modulo >> arquivo_modulo;
	std::cout << "Modulo: " << nome_modulo << " ";
	std::cout << arquivo_modulo << std::endl;

	//Verifica se esse arquivo de modulo ja foi carregado, caso nao tenha sido,
	//um novo modulo eh instanciado e adicionado na tabela de modulos carregados
	if(m_modulos_carregados.find(arquivo_modulo) == m_modulos_carregados.end()){
		Modulo* novo_modulo = new Modulo(arquivo_modulo);
		if(!novo_modulo->carregar()){
			delete novo_modulo;
			std::cout << "Falha ao inicializar modulo. Abortado." << std::endl;
			return false;
		}
		m_modulos_carregados[arquivo_modulo] = novo_modulo;
		//Verifica se ja existe um modulo com esse nome, caso nao tenha, um modulo com esse nome ja adicionado na tabela de modulos
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = novo_modulo;
			//Verica se algum modulo ja foi carregado. Se nao tiver sido, o primeiro modulo carregado
			//sera o modulo inicial
			if(m_modulos_carregados.size() == 1){
				m_modulo_atual = nome_modulo;
			}
		}else{
			std::cout << "Uma referencia com esse nome ja existe. Ignorando referencia duplicada." << std::endl;
		}
	}else{
		std::cout << "Arquivo de modulo ja carregado. Preparando para adicionar nova referencia." << std::endl;
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = m_modulos_carregados[arquivo_modulo];
		}else{
			std::cout << "Uma referencia com esse nome ja existe. Ignorando referencia duplicada." << std::endl;
		}
	}
	return true;
}

/*!
 *
 *	Carrega uma regra
 *
 *	@param[in]: A linha contendo a instrução de carregar uma regra
 *	@param[out]: none
 *	return: true, se a regra foi carregada corretamente
 */
bool Diagrama::carregar_regra(std::string& linha_regra)
{
	//Tokenizamos a linha contendo a instrução da regra
	std::stringstream tokens;


	std::string modulo_inicial = "";
	//Podemos ter uma regra relativa a um conjunto de simbolos
	std::vector<std::string> lista_de_simbolos;
	std::string simbolos = "";
	std::string modulo_final = "";

	//Linhas de regra tem o seguinte formato:
	// A [a,b,c...] B
	tokens << linha_regra;
	tokens >> modulo_inicial >> simbolos >> modulo_final;

//	std::cout << "Regra: " << modulo_inicial << " ";
//	std::cout << simbolos << " ";
//	std::cout<< modulo_final << std::endl;

	//Verifica se qualquer um dos componentes da regra é vazio. Caso qualquer um seja,
	//o arquivo está inválido.
	if( (modulo_inicial.size() == 0) || (simbolos.size() == 0) || (modulo_final.size() == 0) ){
		std::cout << "Erro lendo instrução de carregamento de regra: " << std::endl;
		std::cout << linha_regra << std::endl;
		return false;
	}

	//Verifica se a lista de simbolos esta no formato apropriado
	if( (simbolos.find("[") == std::string::npos) || (simbolos.find("]") == std::string::npos) ){
		std::cout << "Erro lendo instrução de carregamento de regra: " << std::endl;
		std::cout << linha_regra << std::endl;
		return false;
	}


	bool qualquer_simbolo = false;
	std::string::iterator string_it;
	std::string aux;
	//O conjunto de simbolos e percorrido
	for(string_it = simbolos.begin(); string_it != simbolos.end(); string_it++){
		aux = (*string_it);
		//Se o caractere for diferente dos caracteres de controle, entao deve ser um caractere
		//referente a um simbolo
		if( (aux.compare("[") != 0) && (aux.compare(",") != 0) && (aux.compare("]") != 0) ){
			//Se o simbolo referente a regra for '*', entao essa regra vale para qualquer simbolo
			if(aux.compare("*") == 0){
				qualquer_simbolo = true;
			}
			//Adiciona o simbolo na lista de simbolos
			lista_de_simbolos.push_back(aux);
		}
	}
	//Se a lista de simbolos estiver vazia, o arquivo nao eh valido
	if(lista_de_simbolos.size() == 0){
		std::cout << "Erro lendo instrução de carregamento de regra: " << std::endl;
		std::cout << linha_regra << std::endl;
		return false;
	}

	//Adiciona a nova regra
	RegraDiagrama *regra = NULL;
	std::map<std::string,RegraDiagrama*>::iterator regras_it = m_regras_modulos.find(modulo_inicial);
	std::vector<std::string>::iterator simbolos_it;
	//Se ainda nao existe um conjunto de regras para o modulo definido por modulo inicial
	if(regras_it == m_regras_modulos.end()){
		//Entao, criar esse conjunto e adicionar na tabela de regras, indexado por 'modulo_inicial'
		regra = new RegraDiagrama();
		//Para cada simbolo, inserir uma entrada nas regras para esse modulo
		for(simbolos_it = lista_de_simbolos.begin(); simbolos_it != lista_de_simbolos.end(); simbolos_it++){
			regra->inserir((*simbolos_it),modulo_final);
		}
		//Armazena o conjunto de regras desse modulo
		m_regras_modulos.insert(std::pair<std::string,RegraDiagrama*>(modulo_inicial,regra));
	}else{
		//Se ja houver um conjunto de regras para esse modulo, apenas
		//adiciona as novas regras a esse conjunto
		regra = (*regras_it).second;
		for(simbolos_it = lista_de_simbolos.begin(); simbolos_it != lista_de_simbolos.end(); simbolos_it++){
			regra->inserir((*simbolos_it),modulo_final);
		}
	}
	//Se a regra era referente a '*', 'qualquer_simbolo' será true
	regra->m_qualquer_simbolo = qualquer_simbolo;
	return true;
}

/*!
 *
 *	Imprime o diagrama
 *
 *	@param[in]: none
 *	@param[out]: none
 *	return: none
 */
void Diagrama::imprime_diagrama()
{
	std::string module_name;
	std::map<std::string, Modulo*>::iterator it;
//	std::cout << "Diagrama: " << std::endl;
//	std::cout << "Modulos: " << std::endl;
//	for(it = m_modulos.begin(); it != m_modulos.end(); it++){
//		module_name = (*it).first;
//		std::cout << "Modulo " << module_name << " inicializado." << std::endl;
//	}
	std::string initial_module;
	std::string symbol;
	std::string last_module;
	std::cout << std::endl;
	std::cout << "Regras: " << std::endl;
	std::map<std::string, RegraDiagrama*>::iterator it2;
	std::map<std::string, std::string>::iterator it3;
	RegraDiagrama *ac = NULL;
	for(it2 = m_regras_modulos.begin(); it2 != m_regras_modulos.end(); it2++){
		initial_module = (*it2).first;
		ac = (*it2).second;
		for(it3 = ac->m_regras.begin(); it3 != ac->m_regras.end(); it3++){
			symbol = (*it3).first;
			last_module = (*it3).second;
			std::cout << "(" << initial_module << " , " << symbol << ") -> " << last_module << std::endl;
		}
	}
	std::cout << std::endl;
}

/*!
 *
 *	Executa o diagrama
 *
 *	@param[in]: Uma string contendo o estado inicial da fita
 *	@param[in]: O tamanho maximo da fita
 *	@param[out]: none
 *	return: none
 */
void Diagrama::executar(std::string fita_inicial)
{
	if(m_carregado){
		//Instancia uma nova maquina de turing, com os parametros recebidos
		Maquina* mt = new Maquina(fita_inicial);
		Modulo* modulo = NULL;
		std::map<std::string,Modulo*>::iterator modulo_it;
		std::string ultimo_modulo;
		bool executando_diagrama = true;
		bool executando_modulo = false;
		unsigned int passos = 0;

		std::cout << "Iniciando." << std::endl;
		std::cout << "Modulo inicial: " <<  m_modulo_atual << std::endl;
		std::cout << "Fita: ";
		//Imprime o estado inicial da fita da maquina de turing
		mt->imprimir_fita();
		std::cout << std::endl;
		//Inicia a execucao, o modulo atual inicial eh definido pela variavel
		//'m_modulo_inicial'
		while(executando_diagrama){
			//Pega o modulo atual
			modulo_it = m_modulos.find(m_modulo_atual);
			//Se o modulo atual nao estiver na tabela de modulos, entao retorna.
			if(modulo_it == m_modulos.end()){
				ultimo_modulo = m_modulo_atual;
				modulo = NULL;
				break;
			}
			modulo = (modulo_it->second);
			if(!modulo->inicializar()){
				break;
			}
			executando_modulo = true;
			imprime_config_atual(mt, modulo, m_modulo_atual, passos);
			passos++;
			while(executando_modulo){
				//Executa um passo do modulo atual
				if(modulo->executa_passo(mt)){
					imprime_config_atual(mt, modulo, m_modulo_atual, passos);
					passos++;
				}else{
					ultimo_modulo = m_modulo_atual;
					//Se não houver mais passos, termina a execucao do modulo
					executando_modulo = false;
					//Verifica o proximo modulo a ser executado, se nao houver nenhum, termina a execuca
					//do diagrama
					executando_diagrama = verifica_prox_modulo(mt);
				}
			}
		}

		//Imprime a config. final do diagrama
		std::cout << std::endl;
		std::cout << "Terminada em: " << passos << " passos." << std::endl;
		imprime_config_atual(mt, modulo, ultimo_modulo, --passos);
		std::cout << std::endl;

		//Ao terminar, deleta a maquina de turing da memoria
		delete mt;
	}
}

/*!
 *
 *	Imprime a configuracao atual do diagrama
 *
 *	@param[in]: Um ponteiro para a maquina de turing em execucao
 *	@param[in]: Um ponteiro para o modulo atual em execucao
 *	return: none
 */
void Diagrama::imprime_config_atual(Maquina *mt, Modulo *modulo, const std::string& modulo_atual, const unsigned int &passos)
{
	std::cout << passos << ":\t";
	if(modulo != NULL){
		std::cout << "( " << modulo_atual << " , " << modulo->pega_estado_atual() <<  " )" << "\t\t";
	}else{
		std::cout << "( " << modulo_atual << " )" << "\t\t";
	}
	mt->imprimir_fita();
}

/*!
 *
 *	Verifica, de acordo com a configuracao atual, qual o proximo modulo a ser executado
 *
 *	@param[in]: Um ponteiro para a maquina de turing em execucao *
 *	return: true, se um proximo modulo pode ser executado, false do contrario
 */
bool Diagrama::verifica_prox_modulo(Maquina *mt)
{
	std::string simbolo_atual = "";
	std::map<std::string,RegraDiagrama*>::iterator regra_it;
	//Pega o nome do proximo modulo a ser executado
	//e poe 'm_modulo_atual' para receber esse valor
	regra_it = m_regras_modulos.find(m_modulo_atual);
	if(regra_it != m_regras_modulos.end()){
		//O simbolo atual na cabeca de leitura da maquina de turing
		simbolo_atual = mt->simbolo_atual();
		m_modulo_atual = (*regra_it).second->pegar_prox_modulo(simbolo_atual);
	}else{
		//Se nao houver nenhuma regra para o estado atual, retorna 'false'
		return false;
	}
	return true;
}


