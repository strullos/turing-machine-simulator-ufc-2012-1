/*
 * diagrama.h
 *
 *  Created on: 17/03/2012
 *      Author: Vitor
 */

#ifndef DIAGRAMA_H_
#define DIAGRAMA_H_

#include <map>
#include <string>

class Modulo{
	Modulo();
};

class Diagrama {
public:
	Diagrama();
	bool carregar_diagrama(std::string arquivo);
	virtual ~Diagrama();
private:
	std::map<std::string,Modulo*> m_modulos_carregados; //< Hash indexado pelo nome do arquivo do m�dulo. Cada m�dulo s� pode ser carregado uma �nica vez
	std::map<std::string,Modulo*> m_modulos; //Hash indexado pelo nome de um m�dulo. Um m�dulo carregado pode ter v�rios nomes (refer�ncias)
};

#endif /* DIAGRAMA_H_ */
