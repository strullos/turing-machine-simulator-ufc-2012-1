CXFLAGS=-O3 -Wall

maquina.o: maquina/maquina.cpp
	g++ $(CXFLAGS) -o maquina/maquina.o maquina/maquina.cpp 
	
modulo.o : maquina.o, modulo/modulo.cpp
	g++ $(CXFLAGS) -o modulo/modulo.o modulo/modulo.cpp maquina/maquina.o
	
diagrama.o: maquina.o, modulo.o, diagrama/diagrama.cpp
	g++ $(CXFLAGS) -o diagrama/diagrama.o diagrama/diagrama.cpp maquina/maquina.o modulo/modulo.o
	
main.o: diagrama.o, modulo.o, maquina.o, main.cpp
	g++ $(CXFLAGS) -o main.o main.cpp diagrama/diagrama.o maquina/maquina.o modulo/modulo.o
	
diagrama_turing.exe: main.o
	g++ $(CXFLAGS) -o diagrama_turing.exe main.o
	
all: diagrama_turing.exe
clean: 
	rm -rf *.o
rmproper: clean
	rm diagrama_turing.exe