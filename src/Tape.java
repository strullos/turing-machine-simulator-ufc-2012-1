/*
 * This class represents the turing machine's tape
 */

public class Tape {
	public Tape(String content)
	{
		m_tape = new StringBuffer();
		m_tape.append("@#").append(content).append('#');
		m_position = 1;
	}
		
	public String toString()
	{
		StringBuffer tape_string = new StringBuffer(m_tape.toString());
		m_tape.insert(m_position, '_');
		int j = 0;		
		for( int i = m_position; i<tape_string.length() ; i++ ) {
			if( tape_string.charAt(i) != '#' )
				j = i;
		}		
		return tape_string.substring(0, j+2);
	};
	
	
	public int getPosition()
	{
		return m_position;
	}
	
	public void writeSymbol(char symbol)
	{
		m_tape.setCharAt(m_position, symbol);
	}
	
	public char readSymbol()
	{
		return m_tape.charAt(m_position);
	}
	
	public int moveHeadLeft()
	{
		if(m_position >= 1){
			m_position--;
		}
		return m_position;
	}
	
	public int moveHeadRight(){
		m_position++;
		return m_position;
	}
	
	private	StringBuffer m_tape; //!< This represents the tape;
	private int m_position;		 //!< This represents the current head position
}
