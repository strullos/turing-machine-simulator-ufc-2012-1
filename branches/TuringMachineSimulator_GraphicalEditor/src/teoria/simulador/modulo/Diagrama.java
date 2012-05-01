package teoria.simulador.modulo;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
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
				int pos = 0;
				System.out.println("Arquivo .mt detectado.");
				m_arquivo_mt = true;
				if(caminho_arquivo.lastIndexOf("/") > caminho_arquivo.lastIndexOf("\\")){
					pos = caminho_arquivo.lastIndexOf("/");
				}else{
					pos = caminho_arquivo.lastIndexOf("\\");
				}
				String dir = caminho_arquivo.substring(0, pos+1);
				if(!carregar_modulo(caminho_arquivo, dir)){
					System.out.println("Falha ao carregar MT.");					
					return false;
				}
			}
			if(caminho_arquivo.endsWith(".dt")){
				String linha_atual;
				for(int i = 0; i < m_linhas_arquivo; i++){					
					linha_atual = m_dados_arquivo[i];
					if(!linha_atual.startsWith("#")){
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
							if(!linha_atual.isEmpty() && (!linha_atual.equals("\n"))) {
								if(!carregar_regra(linha_atual)){
									System.out.println("Falha ao carregar regra");
									return false;
								}
							}
						}
					}
				}
			}
		}else{
			System.out.println("Falha ao carregar arquivo.");
			return false;
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
		m_var_atual = "";
	}

	@Override
	public boolean executar_passo(Maquina f) {
		return false;
	}

	@Override
	public String estado_atual() {
		return null;
	}
	
	public boolean carregar_regra(String linha_regra){
		String[] tokens;
		boolean atualiza_var = false;
		boolean envia_var = false;
		String var_atualizada = "";
		String var_enviada = "";
		String modulo_inicial = "";
		Vector<String> lista_de_simbolos = new Vector<String>();
		String simbolos = "";
		String modulo_final = "";
		tokens = linha_regra.split("\\s");
		modulo_inicial = tokens[0];
		simbolos = tokens[1];
		modulo_final = tokens[2];
		
		if(modulo_inicial.isEmpty() || simbolos.isEmpty() || modulo_final.isEmpty() ){
			System.out.println("Erro lendo instrução de carregamento de regra. Regra incompleta: " + linha_regra);
			return false;
		}
		if( (!simbolos.contains("[")) || (!simbolos.contains("]")) ){
			System.out.println("Erro lendo instrução de carregamento de regra. Erro de sintaxe: " + linha_regra);
			return false;
		}		
		int aux_pos;
		if(simbolos.contains("=")){
			aux_pos = simbolos.indexOf("=");
			atualiza_var = true;
			var_atualizada = simbolos.substring(1,aux_pos);
			simbolos = (simbolos.substring(0,aux_pos-1) + (simbolos.substring(aux_pos + 1, simbolos.length() - aux_pos + 1)) );
			m_tabela_var.put(var_atualizada, "");
		}
		if(modulo_final.contains("(")){
			aux_pos = modulo_final.indexOf("(");
			if(!modulo_final.contains(")")){
				System.out.println("Erro lendo instrucao de carregamento de regra. Erro de sintaxe: " + linha_regra);
				return false;
			}
			envia_var = true;
			var_enviada = modulo_final.substring(aux_pos+1, modulo_final.length()-1);
			modulo_final = modulo_final.substring(0,aux_pos);
		}
		boolean qualquer_simbolo = false;		
		char aux;
		for(int i = 0; i < simbolos.length(); i++){
			aux = simbolos.charAt(i);
			if((aux != '[') && (aux != ',') && (aux != ']')){
				if(aux == '*'){
					qualquer_simbolo = true;
				}					
				lista_de_simbolos.addElement(simbolos.substring(i, i+1));
			}
		}
		if(lista_de_simbolos.isEmpty()){
			System.out.println("Erro lendo instrucao de carregamento de regra. Falha ao parsear simbolos: " + linha_regra);
			return false;
		}
		RegraDiagrama regra;
		//Se ainda nao existe um conjunto de regras para o modulo definido por modulo inicial
		if(!m_regras_modulos.containsKey(modulo_inicial)){
			//Entao, criar esse conjunto e adicionar na tabela de regras, indexado por 'modulo_inicial'
			regra = new RegraDiagrama();
			//Para cada simbolo, inserir uma entrada nas regras para esse modulo
			for(int i = 0; i < lista_de_simbolos.size(); i++){
				regra.inserir(lista_de_simbolos.elementAt(i), modulo_final, atualiza_var, var_atualizada, envia_var, var_enviada);					
			}
			//Armazena o conjunto de regras desse modulo
			m_regras_modulos.put(modulo_inicial, regra);
		}else{
			//Se ja houver um conjunto de regras para esse modulo, apenas
			//adiciona as novas regras a esse conjunto
			regra = m_regras_modulos.get(modulo_inicial);
			for(int i = 0; i < lista_de_simbolos.size(); i++){
				regra.inserir(lista_de_simbolos.elementAt(i), modulo_final, atualiza_var, var_atualizada, envia_var, var_enviada);					
			}				
		}
		//Se a regra era referente a '*', 'qualquer_simbolo' será true
		regra.m_qualquer_simbolo = qualquer_simbolo;
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
	public void imprime_diagrama()
	{	
		if(!m_arquivo_mt){
			String modulo_inicial;
			String simbolo;
			String modulo_final;
			System.out.println("");
			System.out.println("Regras: ");
			RegraDiagrama regra;
			
			Iterator<Entry<String, RegraDiagrama>> iter = m_regras_modulos.entrySet().iterator();
			Iterator<Entry<String, DescritorRegra>> desc_iter;
			Entry<String, RegraDiagrama> item_regra;
			while(iter.hasNext()){				
				item_regra =  iter.next();
				modulo_inicial = item_regra.getKey();
				regra = item_regra.getValue();
				desc_iter = regra.m_regras.entrySet().iterator();
				while(desc_iter.hasNext()){
					Entry<String, DescritorRegra> item_descritor;
					item_descritor = desc_iter.next();
					simbolo = item_descritor.getKey();
					modulo_final = item_descritor.getValue().m_prox_modulo;
					System.out.println("( " + modulo_inicial + " , " + simbolo + ") -> " + modulo_final);
				}				
			}	
			System.out.println();
		}
	}	

	public void limpar(){
		
	}
	
	public boolean carregar_modulo(String linha_atual, String dir)
	{		
		String[] tokens = linha_atual.split("\\s");
		String nome_modulo = "";
		String arquivo_modulo = "";
		boolean modulo_inicial_especificado = false;
		if(!m_arquivo_mt){
			nome_modulo = tokens[1];
			arquivo_modulo = tokens[2];
			System.out.println("Modulo: \t" +  nome_modulo + "\t" + arquivo_modulo);
			if(nome_modulo.contains("%")){
				nome_modulo = nome_modulo.substring(1,nome_modulo.length());
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
	
	/*!
	 *
	 *	Verifica, de acordo com a configuracao atual, qual o proximo modulo a ser executado
	 *
	 *	@param[in]: Um ponteiro para a maquina de turing em execucao *
	 *	return: true, se um proximo modulo pode ser executado, false do contrario
	 */
	
	public boolean verifica_prox_modulo(Maquina mt){
		
		String simbolo_atual = "";
		DescritorRegra descritor_regra;
		RegraDiagrama regra;
		if(m_regras_modulos.containsKey(m_modulo_atual)){
			regra = m_regras_modulos.get(m_modulo_atual);
			m_var_atual = "";
			simbolo_atual = String.valueOf(mt.simbolo_atual());
			descritor_regra = regra.pegar_descritor_regra(simbolo_atual);
			if(descritor_regra != null){
				if(descritor_regra.m_atualiza_var){
					m_tabela_var.put(descritor_regra.m_variavel_atualizada, simbolo_atual);
				}
				if(descritor_regra.m_envia_var){
					m_var_atual = descritor_regra.m_variavel_enviada;
				}
				m_modulo_atual = regra.pegar_prox_modulo(simbolo_atual);
			}else{
				return false;
			}
			return true;
		}
		return false;
	}
	
	/*!
	 *
	 *	Imprime a configuracao atual do diagrama
	 *
	 *	@param[in]: Um ponteiro para a maquina de turing em execucao
	 *	@param[in]: Um ponteiro para o modulo atual em execucao
	 *	return: none
	 */
	public void imprime_config_atual(Maquina mt, Modulo modulo, String modulo_atual, int passos){
		System.out.print(passos + ":\t");
		if(!m_arquivo_mt){
			if(modulo != null){
				if(!m_var_atual.isEmpty()){
					String valor_var = m_tabela_var.get(m_var_atual);					
					if(valor_var.isEmpty()){
						valor_var = "NULL";
					}						
					
					System.out.print("( " + modulo_atual + " , " + modulo.estado_atual() + " , " + m_var_atual + "=" + valor_var + " )" + "\t\t");
				}else{
					System.out.print("( " + modulo_atual + " , " + modulo.estado_atual() + " )" + "\t\t\t");
				}
			}else{
				if( !m_var_atual.isEmpty() ){
					String valor_var = m_tabela_var.get(m_var_atual);
					if(valor_var.isEmpty()){
						valor_var = "NULL";
					}
					System.out.print("( " + modulo_atual + " , " +  m_var_atual + "=" + valor_var + " )" + "\t\t");
				}else{
					System.out.print("( " + modulo_atual + " )" + "\t\t\t");
				}
			}
		}else{
			System.out.print("< " + modulo.estado_atual() + ":\t\t");
		}
		System.out.println(mt.toString());
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
	public void executar(String fita_inicial){
		if(m_carregado){
			Maquina mt = new Maquina(fita_inicial);
			Modulo modulo = null;
			String ultimo_modulo = "";
			String var_modulo = "";
			boolean executando_diagrama = true;
			boolean executando_modulo = false;
			int passos = 0;
			System.out.println("Iniciando.");
			System.out.println("Modulo inicial: " + m_modulo_atual);
			System.out.println("Fita: " + mt.toString());
			System.out.println("");
			while(executando_diagrama){
				modulo = m_modulos.get(m_modulo_atual);
				if(modulo == null){
					if(!m_modulo_atual.equals("")){
						ultimo_modulo = m_modulo_atual;
						modulo = null;
					}
					break;
				}
				modulo.inicializar();
				executando_modulo = true;
				imprime_config_atual(mt, modulo, m_modulo_atual, passos);
				passos++;
				while(executando_modulo){
					var_modulo = String.valueOf(mt.simbolo_atual());
					//TODO: Variaveis
					if(modulo.recebe_var()){
						//Caso receba, pega o valor dessa variavel e passa para o modulo						
						if(m_tabela_var.containsKey(m_var_atual)){
							var_modulo = m_tabela_var.get(m_var_atual);
							if(var_modulo.isEmpty()){
								var_modulo = "#";
							}
							modulo.set_valor_var(var_modulo.charAt(0));
						}						
					}					
					if(modulo.executar_passo(mt)){
						imprime_config_atual(mt,modulo,m_modulo_atual,passos);
						passos++;
					}else{
						ultimo_modulo = m_modulo_atual;
						executando_modulo = false;
						executando_diagrama = verifica_prox_modulo(mt);
					}
				}			
			}
			System.out.println("");
			passos--;
			System.out.println("Terminada em: " + passos + " passos.");
			imprime_config_atual(mt, modulo, ultimo_modulo, passos);
			System.out.println("");			
		}
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
	private String m_var_atual;


}
