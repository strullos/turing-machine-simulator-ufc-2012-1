import java.io.FileNotFoundException;

import teoria.simulador.maquina.Maquina;
import teoria.simulador.modulo.Diagrama;
import teoria.simulador.modulo.IModulo;
import teoria.simulador.modulo.ModuloFactory;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		IModulo raiz = null;
		Maquina fita = null;
		Diagrama d = new Diagrama();
		d.carregar("E:/Comp 2012.1/Teoria da Computação/Implementações/branches/TMSimulator_Java/shiftString.dt");
//		
//		if( args.length == 2) {
//			raiz = ModuloFactory.construir(args[0]);
//			fita = new Fita(args[2]);
//		} else {
//			System.out.println("Para rodar um diagrama, passe o nome do arquivo por parametro e o estado inicial da fita.");
//			System.out.println("O modulo inicial pode ser especificado com um '%' antes do nome do modulo.");
//			System.out.println("Caso nenhum modulo inicial seja especificado, o primeiro modulo a ser carregado sera o inicial.");
//			return ;
//		}
//		
//		if( raiz == null ) {
//			System.out.println("Falha ao carregar maquina!");
//			return ;
//		}
//		
//		while( raiz.executar_passo(fita) ) {
//			System.out.println(raiz.estado_atual());
//		}
	}
}
