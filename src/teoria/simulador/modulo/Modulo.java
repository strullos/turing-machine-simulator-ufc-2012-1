package teoria.simulador.modulo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import teoria.simulador.maquina.Maquina;

public class Modulo extends IModulo {
	private String m_estado_atual = null;
	private String m_estado_inicial = null;
	private MultiMap m_quadruplas = null;
	private char m_var = '\0';
	private int m_max_iter = 0;
	private int m_curr_iter = 0;
	public boolean m_recebe_var;

	public Modulo() {
		m_curr_iter = 0;
		m_quadruplas = new MultiValueMap();
		m_recebe_var = false;
	}

	@Override
	public boolean carregar(String arquivo) throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(new FileReader(arquivo));

		try {
			String linha = null;;
			boolean res = false;

			linha = acha_proxima_linha(reader);
			while(linha.startsWith("#")){
				linha = acha_proxima_linha(reader);
			}
			res = processa_cabecalho(linha);

			while( res && (linha = acha_proxima_linha(reader)) != null ) {
				res = processa_quad(linha) || processa_decl_var(linha);
			}

			return res;

		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void inicializar() {
		m_estado_atual = m_estado_inicial;
		m_curr_iter = 0;
	}

	@Override
	public boolean executar_passo(Maquina f) {
		boolean ret = false;
		
		if( m_quadruplas.containsKey(m_estado_atual) ) {
			Iterator<Quadrupla> iter = ((Collection) m_quadruplas.get(m_estado_atual)).iterator();
			Quadrupla quad = null;
			
			while( iter.hasNext() ) {
				Quadrupla temp = iter.next();
				if( temp.simbolo == '*' ||
						temp.simbolo == f.simbolo_atual() || 
						(temp.simbolo == m_var && f.simbolo_atual() == m_var_valor) ) {
					quad = temp;
					break;
				}
			}
			
			if( quad != null && (ret = quad.aplicar_acao(f)) ) m_estado_atual = quad.estado2; 
		}
			
		return ret;
	}

	@Override
	public String estado_atual() {
		return m_estado_atual;
	}

	private boolean processa_cabecalho(String linha) {
		if( linha != null ) {
			StringTokenizer tokenizer = new StringTokenizer(linha);

			if( tokenizer.countTokens() == 3 ) {
				m_estado_inicial = tokenizer.nextToken();
				m_max_iter = Integer.parseInt(tokenizer.nextToken());
				return true;
			}
		}

		return false;
	}

	private boolean processa_decl_var(String linha) {
		if( linha != null ) {
			StringTokenizer tokenizer = new StringTokenizer(linha);
			if( tokenizer.countTokens() == 2 ) {
				tokenizer.nextToken();
				m_var = tokenizer.nextToken().charAt(0);
				m_recebe_var = true;
				return true;
			}
		}
		
		return false;
	}
	
	public boolean recebe_var(){
		return m_recebe_var;
	}

	private boolean processa_quad(String linha) {
		if( linha != null ) {
			StringTokenizer tokenizer = new StringTokenizer(linha);

			if( tokenizer.countTokens() == 4 ) {
				String estado1 = tokenizer.nextToken();
				char simbolo = tokenizer.nextToken().charAt(0);
				String estado2 = tokenizer.nextToken();
				char acao = tokenizer.nextToken().charAt(0);

				if( m_quadruplas.containsKey(estado1) ) {
					Iterator<Quadrupla> iter = ((Collection)m_quadruplas.get(estado1)).iterator();
					while(iter.hasNext()) {
						Quadrupla quad = (Quadrupla) iter.next();
						if( quad.simbolo == '*' || quad.simbolo == simbolo ) {
							return false;
						}
					}
				}

				m_quadruplas.put(estado1, new Quadrupla(simbolo, estado2, acao));
				return true;
			}
		}

		return false;
	}

	private String acha_proxima_linha(BufferedReader reader) throws IOException {
		String ret =  null;

		while( (ret = reader.readLine()) != null && (ret.isEmpty() || ret.equals("\n") || ret.equals("\r") || ret.equals("\r\n")) );

		return ret;
	}

	private class Quadrupla {
		public char simbolo;
		public String estado2;
		public char acao;

		public Quadrupla(char _simbolo, String _estado2, char _acao) {
			simbolo = _simbolo;
			estado2 = _estado2;
			acao = _acao;
		}

		public boolean aplicar_acao(Maquina f) {
			switch(acao) {
			case '>':
				f.mover_direita();
				return true;

			case '<':
				return f.mover_esquerda();

			default:
				if(acao == m_var) {
					f.escrever(m_var_valor);
				} else {
					f.escrever(acao);
				}
				return true;
			}
		}
	}
}
