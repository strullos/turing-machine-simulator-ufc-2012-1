import java.awt.Dimension;
import java.io.FileNotFoundException;

import javax.swing.JFrame;



import teoria.simulador.maquina.Maquina;
import teoria.simulador.modulo.Diagrama;
import teoria.simulador.modulo.IModulo;
import teoria.simulador.modulo.ModuloFactory;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
//		TestMain m = new TestMain();
//		JFrame j = new JFrame();
//		j.resize(new Dimension(800,600));
//		j.show();
//		j.add(m);+
//		if( args.length == 2) {			
//			Diagrama d = new Diagrama();
//			d.carregar(args[0]);			
//			d.executar(args[1]);
//		} else {
//			System.out.println("Para rodar um diagrama, passe o nome do arquivo por parametro e o estado inicial da fita.");
//			System.out.println("O modulo inicial pode ser especificado com um '%' antes do nome do modulo.");
//			System.out.println("Caso nenhum modulo inicial seja especificado, o primeiro modulo a ser carregado sera o inicial.");
//			return ;
//		}		
		TestMain m = new TestMain();
		JFrame test = new JFrame();
		test.getContentPane().add(m);
		test.show();
	}
}
