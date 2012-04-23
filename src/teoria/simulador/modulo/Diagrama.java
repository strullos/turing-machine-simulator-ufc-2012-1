package teoria.simulador.modulo;

import java.io.*;
import java.sql.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import teoria.simulador.maquina.Maquina;

public class Diagrama extends Modulo {
	public Diagrama(){
		inicializar();
	}
	
	@Override
	public boolean carregar(String caminho_arquivo) {
		if(m_carregado){
			limpar();
		}
		if(abre_arquivo(caminho_arquivo)){
			if(caminho_arquivo.endsWith(".mt")){
				m_arquivo_mt = true;
			}
			if(caminho_arquivo.endsWith(".dt")){
				String linha_atual;
				for(int i = 0; i < m_linhas_arquivo; i++){
					linha_atual = m_dados_arquivo[i];
					int pos = 0;
					if(caminho_arquivo.lastIndexOf("/") > caminho_arquivo.lastIndexOf("\\")){
						pos = caminho_arquivo.lastIndexOf("/");
					}else{
						pos = caminho_arquivo.lastIndexOf("\\");
					}
					String dir = caminho_arquivo.substring(0, pos+1);
					if(linha_atual.contains("modulo")){
						if(!carregar_modulo(linha_atual, dir)){
							System.out.println("Falha ao carregar modulo");
							return false;
						}
					}else{
						if(!carregar_regra(linha_atual)){
							System.out.println("Falha ao carregar regra");
							return false;
						}
					}
				}
			}
		}else{
			System.out.println("Falha ao carregar arquivo.");
		}
		System.out.println("Diagrama carregado com sucesso.");
		m_carregado = true;
		return true;
	}

	@Override
	public void inicializar() {
		m_regras_modulos = new HashMap<String, RegraDiagrama>();
		m_modulos_carregados = new HashMap<String, Modulo>();		
		m_modulos = new HashMap<String, Modulo>();			
		m_tabela_var = new HashMap<String, String>();		
		m_carregado = false;
	}

	@Override
	public boolean executar_passo(Maquina f) {
		return false;
	}

	@Override
	public String estado_atual() {
		return null;
	}
	
	public boolean carregar_regra(String linha_atual){
		String[] tokens;
		boolean atualiza_var = false;
		String var_atualizada;
		String var_enviada;
		String modulo_inicial;
		Vector<String> lista_de_simbolos;
		String simbolos;
		String modulo_final;
		tokens = linha_atual.split("[\\s0-9+-]+");
		return true;
	}
	/*bool Diagrama::carregar_regra(std::string& linha_regra)
	{
		//Tokenizamos a linha contendo a instrução da regra
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

//		std::cout << "Regra: " << modulo_inicial << " ";
//		std::cout << simbolos << " ";
//		std::cout<< modulo_final << std::endl;

		//Verifica se qualquer um dos componentes da regra é vazio. Caso qualquer um seja,
		//o arquivo está inválido.
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
			//Remove a declaração de variável da regra. Por exemplo R [x=a,b] S -> R [a,b] S
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
		//Se a regra era referente a '*', 'qualquer_simbolo' será true
		regra->m_qualquer_simbolo = qualquer_simbolo;
		return true;
	}*/

	public void limpar(){
		
	}
	
	public boolean carregar_modulo(String linha_atual, String dir)
	{		
		String[] tokens = linha_atual.split("[\\s0-9+-]+");
		String nome_modulo;
		String arquivo_modulo;
		boolean modulo_inicial_especificado = false;
		if(!m_arquivo_mt){
			nome_modulo = tokens[1];
			arquivo_modulo = tokens[2];
			System.out.println("Modulo: \t" +  nome_modulo + "\t" + arquivo_modulo);
			if(nome_modulo.contains("%")){
				nome_modulo = nome_modulo.substring(1);
				modulo_inicial_especificado = true;
				m_modulo_atual = nome_modulo;
			}
		}else{
			arquivo_modulo = tokens[0];
			nome_modulo = arquivo_modulo;
		}
		if(!m_modulos_carregados.containsKey(arquivo_modulo)){
			String caminho_arquivo = dir;
			caminho_arquivo = caminho_arquivo + arquivo_modulo;
			Modulo novo_modulo = new Modulo();
			try {
				if(!novo_modulo.carregar(caminho_arquivo)){
					System.out.println("Falha ao inicializar modulo. Abortado.");
					return false;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			m_modulos_carregados.put(arquivo_modulo, novo_modulo);
			if(!m_modulos.containsKey(nome_modulo)){
				m_modulos.put(nome_modulo, novo_modulo);
				if((m_modulos_carregados.size() == 1) && (modulo_inicial_especificado == false)){
					m_modulo_atual = nome_modulo;
				}
			}else{
				System.out.println("Uma referencia com esse nome ja existe. Ignorando referencia duplicada.");
			}
		}
		else{
			System.out.println("Arquivo de modulo ja carregado. Preparando para adicionar nova referência.");
			if(!m_modulos.containsKey(nome_modulo)){
				m_modulos.put(nome_modulo, m_modulos_carregados.get(arquivo_modulo));
			}else{
				System.out.println("Uma referencia com esse nome ja existe. Ignorando referencia duplicada.");
			}
		}		
		return true;
	}
	
	
	void detecta_num_linhas(String nome_arquivo) throws IOException{
		 FileReader arquivo = new FileReader(nome_arquivo);	
		 BufferedReader arquivo_buffer_linhas = new BufferedReader(arquivo);
		 int num_linhas = 0;
		 arquivo_buffer_linhas = new BufferedReader(arquivo);
		 while( arquivo_buffer_linhas.readLine() != null){
			 num_linhas++;			
		 }		 
		m_linhas_arquivo = num_linhas;		
	}
	
	boolean abre_arquivo(String nome_arquivo){
		 try {
			detecta_num_linhas(nome_arquivo);
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		 FileReader arquivo;
		 try {
				arquivo = new FileReader(nome_arquivo);
			 } catch (FileNotFoundException e) {				
				e.printStackTrace();
				return false;
		 }	
		 BufferedReader arquivo_buffer = new BufferedReader(arquivo);	
		 m_dados_arquivo = new String[m_linhas_arquivo];
		 try {
			for(int i = 0; i < m_linhas_arquivo; i++){
				m_dados_arquivo[i] = arquivo_buffer.readLine();
			}
			} catch (IOException e) {				
				e.printStackTrace();
				return false;
		 }
		 return true;
	}	
	
	
	private HashMap<String, RegraDiagrama> m_regras_modulos; 	//!< Hash indexado pelo nome do modulo, contendo as acoes que ele pode realizar.
	private HashMap<String, Modulo> m_modulos_carregados;		//!< Hash dos modulos carregados, indexado pelo nome do arquivo do modulo.
	private HashMap<String, Modulo> m_modulos; 					//!< Hash dos modulos e suas referencias indexado pelo nome de um modulo.
	private HashMap<String,String> m_tabela_var;				//!< Hash contendo a tabela de variÃ¡veis e seus respectivos valores, indexadas pelo nome da variÃ¡vel
	
	private int m_linhas_arquivo;
	private String[] m_dados_arquivo; 
	private boolean m_arquivo_mt;
	private String m_modulo_atual;
	private boolean m_carregado;


}
