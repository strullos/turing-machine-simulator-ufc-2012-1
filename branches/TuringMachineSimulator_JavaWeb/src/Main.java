import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import teoria.simulador.maquina.Maquina;
import teoria.simulador.modulo.Diagrama;
import teoria.simulador.modulo.IModulo;
import teoria.simulador.modulo.ModuloFactory;



public class Main {
	public static void main(String[] args) throws FileNotFoundException {	
		Diagrama d = new Diagrama();
		d.carregar_diagrama_buffer("q0 1000 1000\n q0 * q1 >");
		d.executar("aabb");
//		if( args.length == 2) {			
//			
//			//d.carregar(args[0]);			
//			//d.executar(args[1]);
//		
//		} else {
//			System.out.println("Para rodar um diagrama, passe o nome do arquivo por parametro e o estado inicial da fita.");
//			System.out.println("O modulo inicial pode ser especificado com um '%' antes do nome do modulo.");
//			System.out.println("Caso nenhum modulo inicial seja especificado, o primeiro modulo a ser carregado sera o inicial.");
//			return ;
//		}		
	}	
}
