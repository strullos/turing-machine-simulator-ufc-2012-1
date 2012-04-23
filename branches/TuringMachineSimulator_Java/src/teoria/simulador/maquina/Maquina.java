package teoria.simulador.maquina;

public class Maquina {
	private StringBuffer m_fita;
	private int m_posicao;
	
	public Maquina(String entrada) {
		m_fita = new StringBuffer();
		m_fita.append("@#").append(entrada).append('#');
		m_posicao = 1;
	}
	
	public boolean mover_esquerda()
	{
		if( m_posicao > 0 ) {
			m_posicao--;
			return true;
		}
		
		return false;
	}
	
	public void mover_direita()
	{
		m_posicao++;
		if( m_posicao == m_fita.length()-1 ) {
			m_fita.append('#');
		}
	}
	
	public void escrever(char s)
	{
		m_fita.setCharAt(m_posicao, s);
	}
	
	public char simbolo_atual()
	{
		return m_fita.charAt(m_posicao);
	}
	
	public String toString()
	{
		StringBuffer fita_repr = new StringBuffer(m_fita.toString());
		fita_repr.insert(m_posicao, '_');
		int j = 0;
		
		for( int i = m_posicao; i<fita_repr.length() ; i++ ) {
			if( fita_repr.charAt(i) != '#' )
				j = i;
		}
		
		return fita_repr.substring(0, j+2);
	}
}
