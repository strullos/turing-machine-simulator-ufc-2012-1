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
	std::map<std::string,Modulo*> m_modulos_carregados; //< Hash indexado pelo nome do arquivo do módulo. Cada módulo só pode ser carregado uma única vez
	std::map<std::string,Modulo*> m_modulos; //Hash indexado pelo nome de um módulo. Um módulo carregado pode ter vários nomes (referências)
};

#endif /* DIAGRAMA_H_ */
