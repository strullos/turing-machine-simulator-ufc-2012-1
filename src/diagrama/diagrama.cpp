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
m_carregado(false),
m_arquivo_mt(false)
{}

/*!
 *
 *	Destrutor; limpa a mem�ria e as estruturas de dados
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
	//Apaga a tabela de m�dulos carregados
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
 *	@param[in]: Uma string contendo o diret�rio do diagrama a ser carregado.
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
		//Obtem o diretorio do arquivo
		unsigned int pos = 0;
		std::string dir;
		//O path do arquivo pode ser definido tanto com "/" quanto com "\"
		//"pos" vai receber a ultima posicao de "/" ou "\", caso o caminho do arquivo contenha esses caracteres.
		//"pos" recebe o menor dos dois, porque se rfind nao encontrar o caractere,
		//retorna std::string::npos, que tem o valor maximo possivel para um elemento do tipo size_t.
		pos = (caminho_arquivo.rfind("/") < caminho_arquivo.rfind("\\")) ? caminho_arquivo.rfind("/") : caminho_arquivo.rfind("\\");
		if(pos != std::string::npos){
			dir = caminho_arquivo.substr(0,pos+1);
		}
		if(caminho_arquivo.find(".mt",caminho_arquivo.size() - 3) != std::string::npos){
			std::cout << "Arquivo .mt detectado." << std::endl;
			m_arquivo_mt = true;
			if(!carregar_modulo(caminho_arquivo, dir)){
				std::cout << "Falha ao carregar MT." << std::endl;
				arquivo.close();
				return false;
			}
		}else{
			while(arquivo.good())
			{
				std::getline(arquivo,linha);
				//Se a linha inicia com "modulo", ent�o deve conter uma instru��o para carregar um m�dulo
				if( linha.find("modulo",0) == 0){
					if(!carregar_modulo(linha, dir)){
						std::cout << "Falha ao carregar modulo." << std::endl;
						arquivo.close();
						return false;
					}
				}else{
					//Caso o contr�rio, supondo um arquivo v�lido, deve conter uma regra do diagrama
					if(linha != "" && linha != "\r" && linha != "\n" && (linha.find("//",0) == std::string::npos))	{
						if(!carregar_regra(linha)){
							std::cout << "Falha ao carregar regras." << std::endl;
							arquivo.close();
							return false;
						}
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
 *	Carrega um m�dulo
 *
 *	@param[in]: A linha contendo a instru��o de carregar o m�dulo
 *	@param[out]: none
 *	return: true, se o m�dulo foi carregado corretamente
 */
bool Diagrama::carregar_modulo(std::string& linha_modulo, const std::string& dir)
{
	//Tokenizamos a linha. Supondo um arquivo v�lido, temos
	//cada poss�vel token separado por espa�os.
	std::stringstream tokens;
	tokens << linha_modulo;

	std::string nome_modulo;
	std::string arquivo_modulo;
	bool modulo_inicial_especificado = false;

	//A instru��o de carregar modulos tem o seguinte formato:
	// modulo A arquivo
	//Como n�o precisamos do token modulo, descartamos os caracteres inicias da string, ate
	//a posicao do primeiro ' '
	size_t aux_pos = linha_modulo.find(' ');
	tokens.ignore(aux_pos, ' ');
	//As palavras restantes se referem ao nome do modulo e o arquivo do qual sera carregado
	if(!m_arquivo_mt){
		//Se nao for um arquivo .mt
		tokens >> nome_modulo >> arquivo_modulo;
		std::cout << "Modulo:\t" << nome_modulo << "\t";
		std::cout << arquivo_modulo << std::endl;
		//Verifica se este modulo eh o modulo inicial
		if(nome_modulo.find("%",0) == 0){
			nome_modulo = nome_modulo.substr(1,nome_modulo.size() - 1);
			modulo_inicial_especificado = true;
			m_modulo_atual = nome_modulo;
			std::cout << "Modulo inicial especificado:\t" << nome_modulo << std::endl;
		}
	}else{
		tokens >> arquivo_modulo;
		nome_modulo = arquivo_modulo;
	}

	//Verifica se esse arquivo de modulo ja foi carregado, caso nao tenha sido,
	//um novo modulo eh instanciado e adicionado na tabela de modulos carregados
	if(m_modulos_carregados.find(arquivo_modulo) == m_modulos_carregados.end()){
		std::string caminho_arquivo = dir;
		caminho_arquivo.append(arquivo_modulo);
		Modulo* novo_modulo = new Modulo(caminho_arquivo);
		if(!novo_modulo->carregar()){
			delete novo_modulo;
			std::cout << "Falha ao inicializar modulo. Abortado." << std::endl;
			return false;
		}
		m_modulos_carregados[arquivo_modulo] = novo_modulo;
		//Verifica se ja existe um modulo com esse nome, caso nao tenha, um modulo com esse nome ja adicionado na tabela de modulos
		if(m_modulos.find(nome_modulo) == m_modulos.end()){
			m_modulos[nome_modulo] = novo_modulo;
			//Verica se algum modulo ja foi carregado ou se um modulo inicial j� foi especificado.
			//Se nao tiver sido, o primeiro modulo carregado
			//sera o modulo inicial
			if((m_modulos_carregados.size() == 1) && (modulo_inicial_especificado == false)){
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
 *	@param[in]: A linha contendo a instru��o de carregar uma regra
 *	@param[out]: none
 *	return: true, se a regra foi carregada corretamente
 */
bool Diagrama::carregar_regra(std::string& linha_regra)
{
	//Tokenizamos a linha contendo a instru��o da regra
	std::stringstream tokens;

	bool atualiza_var = false;
	bool envia_var = false;
	std::string var_atualizada;
	std::string var_enviada;
	std::string modulo_inicial;
	//Podemos ter uma regra relativa a um conjunto de simbolos
	std::vector<std::string> lista_de_simbolos;
	std::string simbolos;
	std::string modulo_final;

	//Linhas de regra tem o seguinte formato:
	// A [a,b,c...] B
	tokens << linha_regra;
	tokens >> modulo_inicial >> simbolos >> modulo_final;

//	std::cout << "Regra: " << modulo_inicial << " ";
//	std::cout << simbolos << " ";
//	std::cout<< modulo_final << std::endl;

	//Verifica se qualquer um dos componentes da regra � vazio. Caso qualquer um seja,
	//o arquivo est� inv�lido.
	if( (modulo_inicial.size() == 0) || (simbolos.size() == 0) || (modulo_final.size() == 0) ){
		std::cout << "Erro lendo instrucao de carregamento de regra: " << std::endl;
		std::cout << linha_regra << std::endl;
		return false;
	}
	//Verifica se a lista de simbolos esta no formato apropriado
	if( (simbolos.find("[") == std::string::npos) || (simbolos.find("]") == std::string::npos) ){
		std::cout << "Erro lendo instrucao de carregamento de regra: " << std::endl;
		std::cout << linha_regra << std::endl;
		return false;
	}

	size_t aux_pos = simbolos.find("=");
	//Verifica se a regra utiliza alguma variavel
	if( aux_pos != std::string::npos){
		atualiza_var = true;
		var_atualizada = simbolos.substr(1,aux_pos - 1);
		//Remove a declara��o de vari�vel da regra. Por exemplo R [x=a,b] S -> R [a,b] S
		simbolos = (simbolos.substr(0,aux_pos - 1)).append(simbolos.substr(aux_pos +1, simbolos.size() - aux_pos +1));
		m_tabela_var.insert(std::pair<std::string,std::string>(var_atualizada, ""));
	}

	aux_pos = modulo_final.find("(");
	if(aux_pos != std::string::npos){
		if(modulo_final.find(")") == std::string::npos){
			std::cout << "Erro lendo instrucao de carregamento de regra: " << std::endl;
			std::cout << linha_regra << std::endl;
			return false;
		}
		envia_var = true;
		var_enviada = modulo_final.substr(aux_pos+1,1);
		modulo_final = modulo_final.substr(0,aux_pos);
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
		std::cout << "Erro lendo instru��o de carregamento de regra: " << std::endl;
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
			regra->inserir((*simbolos_it),modulo_final,atualiza_var,var_atualizada,envia_var,var_enviada);
		}
		//Armazena o conjunto de regras desse modulo
		m_regras_modulos.insert(std::pair<std::string,RegraDiagrama*>(modulo_inicial,regra));
	}else{
		//Se ja houver um conjunto de regras para esse modulo, apenas
		//adiciona as novas regras a esse conjunto
		regra = (*regras_it).second;
		for(simbolos_it = lista_de_simbolos.begin(); simbolos_it != lista_de_simbolos.end(); simbolos_it++){
			regra->inserir((*simbolos_it),modulo_final,atualiza_var,var_atualizada,envia_var,var_enviada);
		}
	}
	//Se a regra era referente a '*', 'qualquer_simbolo' ser� true
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
	if(!m_arquivo_mt){
		std::string initial_module;
		std::string symbol;
		std::string last_module;
		std::cout << std::endl;
		std::cout << "Regras: " << std::endl;
		std::map<std::string, RegraDiagrama*>::iterator it2;
		std::map<std::string, DescritorRegra*>::iterator it3;
		RegraDiagrama *ac = NULL;
		for(it2 = m_regras_modulos.begin(); it2 != m_regras_modulos.end(); it2++){
			initial_module = (*it2).first;
			ac = (*it2).second;
			for(it3 = ac->m_regras.begin(); it3 != ac->m_regras.end(); it3++){
				symbol = (*it3).first;
				last_module = ((*it3).second)->m_prox_modulo;
				std::cout << "(" << initial_module << " , " << symbol << ") -> " << last_module << std::endl;
			}
		}
		std::cout << std::endl;
	}
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
	DescritorRegra *descritor_regra = NULL;
	//Pega o nome do proximo modulo a ser executado
	//e poe 'm_modulo_atual' para receber esse valor
	regra_it = m_regras_modulos.find(m_modulo_atual);
	if(regra_it != m_regras_modulos.end()){
		m_var_atual = "";
		//O simbolo atual na cabeca de leitura da maquina de turing
		simbolo_atual = mt->simbolo_atual();
		descritor_regra = (*regra_it).second->pegar_descritor_regra(simbolo_atual);
		if(descritor_regra != NULL){
			if(descritor_regra->m_atualiza_var){
				m_tabela_var[descritor_regra->m_variavel_atualizada] = simbolo_atual;
			}
			if(descritor_regra->m_envia_var){
				m_var_atual = descritor_regra->m_variavel_enviada;
			}
		}
		m_modulo_atual = (*regra_it).second->pegar_prox_modulo(simbolo_atual);
	}else{
		//Se nao houver nenhuma regra para o estado atual, retorna 'false'
		return false;
	}
	return true;
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
	if(!m_arquivo_mt){
		if(modulo != NULL){
			if( !m_var_atual.empty()){
				std::string valor_var = m_tabela_var[m_var_atual];
				if(valor_var.empty()){
					valor_var = "NULL";
				}
				std::cout << "( " << modulo_atual << " , " << modulo->pega_estado_atual() << " , " << m_var_atual << "=" << valor_var << " )" << "\t\t";
			}else{
				std::cout << "( " << modulo_atual << " , " << modulo->pega_estado_atual() <<  " )" << "\t\t\t";
			}
		}else{
			if( !m_var_atual.empty()){
				std::string valor_var = m_tabela_var[m_var_atual];
				if(valor_var.empty()){
					valor_var = "NULL";
				}
				std::cout << "( " << modulo_atual << " , " <<  m_var_atual << "=" << valor_var << " )" << "\t\t";
			}else{
				std::cout << "( " << modulo_atual << " )" << "\t\t\t";
			}
		}
	}else{
			std::cout << "< " << modulo->pega_estado_atual() << ":\t\t";
	}

	mt->imprimir_fita();
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
		std::string var_modulo;
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
				if(m_modulo_atual.compare("") != 0){
					ultimo_modulo = m_modulo_atual;
					modulo = NULL;
				}
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
				var_modulo = mt->simbolo_atual();
				//Verifica se o modulo recebe uma variavel
				if(modulo->recebe_variavel()){
					//Caso receba, pega o valor dessa variavel e passa para o modulo
					std::map<std::string, std::string>::iterator it;
					it = m_tabela_var.find(m_var_atual);
					if(it != m_tabela_var.end()){
						var_modulo = it->second;
						if(var_modulo.empty()){
							var_modulo = "#";//mt->simbolo_atual();
						}
					}
				}
				//Executa um passo do modulo atual
				if(modulo->executa_passo(mt, var_modulo[0])){
					imprime_config_atual(mt, modulo, m_modulo_atual, passos);
					passos++;
				}else{
					ultimo_modulo = m_modulo_atual;
					//Se n�o houver mais passos, termina a execucao do modulos
					executando_modulo = false;
					//Verifica o proximo modulo a ser executado, se nao houver nenhum, termina a execuca
					//do diagrama
					executando_diagrama = verifica_prox_modulo(mt);
				}
			}
		}

		//Imprime a config. final do diagrama
		std::cout << std::endl;
		passos--;
		std::cout << "Terminada em: " << passos << " passos." << std::endl;
		imprime_config_atual(mt, modulo, ultimo_modulo, passos);
		std::cout << std::endl;

		//Ao terminar, deleta a maquina de turing da memoria
		delete mt;
	}
}
