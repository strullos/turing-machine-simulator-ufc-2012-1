package teoria.simulador.unit_tests;

import java.io.FileNotFoundException;

import teoria.simulador.maquina.Maquina;
import teoria.simulador.modulo.*;

public class UnitTests {

	public static void main(String[] args) {
		Maquina f1 = new Maquina("aabb");
		
		if( f1.toString().equals("@_#aabb#") ) {
			System.out.println("Passou teste 1");
		} else {
			System.out.println("Falhou teste 1- " + f1.toString() + " esperado");
		}
		
		f1.mover_direita();
		if( f1.toString().equals("@#_aabb#") ) {
			System.out.println("Passou teste 2");
		} else {
			System.out.println("Falhou teste 2- " + f1.toString() + " esperado");
		}
		
		f1.mover_esquerda();
		if( f1.toString().equals("@_#aabb#") ) {
			System.out.println("Passou teste 3");
		} else {
			System.out.println("Falhou teste 3- " + f1.toString() + " esperado");
		}
		
		f1.mover_esquerda();
		if( f1.mover_esquerda() ) {
			System.out.println("Falhou teste 4");
		} else {
			System.out.println("Passou teste 4");
		}
		
		f1.mover_direita();
		if( f1.simbolo_atual() == '#' ) {
			System.out.println("Passou teste 5");
		} else {
			System.out.println("Falhou teste 5- " + f1.simbolo_atual() + " esperado");
		}
		
		f1.escrever('e');
		if( f1.toString().equals("@_eaabb#") ) {
			System.out.println("Passou teste 6");
		} else {
			System.out.println("Falhou teste 6 - " + f1.toString() + " esperado");
		}
		
		f1.mover_direita();
		f1.mover_direita();
		f1.mover_direita();
		f1.mover_direita();
		f1.mover_direita();
		f1.mover_direita();
		if( f1.toString().equals("@eaabb#_#") ) {
			System.out.println("Passou teste 7");
		} else {
			System.out.println("Falhou teste 7 - " + f1.toString() + " esperado");
		}
		
		IModulo m = new Modulo();
		Maquina f2 = new Maquina("aabb");
		try {
			if( m.carregar("ex.mt") ) {
				m.inicializar();
				while( m.executar_passo(f2) ) {
					System.out.println(m.estado_atual() + " : " + f2.toString());
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		IModulo m2 = new Modulo();
		Maquina f3 = new Maquina("aaa");
		try {
			if( m2.carregar("wx.mt") ) {
				m2.inicializar();
				m2.set_valor_var('b');
				while( m2.executar_passo(f3) ) {
					System.out.println(m2.estado_atual() + " : " + f3.toString());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}

