COMPILER=g++
CXFLAGS=-O3 -Wall
UNAME := $(shell uname -s)
EXECUTABLE=diagrama_turing

diagrama_turing: main.o maquina.o diagrama.o modulo.o regra_diagrama.o
	$(COMPILER) $(CXFLAGS) main.o maquina.o diagrama.o regra_diagrama.o modulo.o -o $(EXECUTABLE)

regra_diagrama.o: src/diagrama/regra_diagrama.cpp 
	$(COMPILER) $(CXFLAGS) -c src/diagrama/regra_diagrama.cpp

diagrama.o: src/diagrama/diagrama.cpp
	$(COMPILER) $(CXFLAGS) -c src/diagrama/diagrama.cpp 

modulo.o: src/modulo/modulo.cpp
	$(COMPILER) $(CXFLAG) -c src/modulo/modulo.cpp

main.o: src/main.cpp
	$(COMPILER) $(CXFLAGS) -c src/main.cpp

maquina.o: src/maquina/maquina.cpp
	$(COMPILER) $(CXFLAGS) -c src/maquina/maquina.cpp

all: $(EXECUTABLE)

clean: 
	rm -rf *.o

rmproper: clean
	rm diagrama_turing.exe



