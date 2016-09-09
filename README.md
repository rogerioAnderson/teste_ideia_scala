#Versao Java
	Java(TM) SE Runtime Environment (build 1.8.0_72-b15)
	Java HotSpot(TM) 64-Bit Server VM (build 25.72-b15, mixed mode)

#Versao Scala
	Scala code runner version 2.11.8 -- Copyright 2002-2016, LAMP/EPFL


#Passo 1 
Compilar o arquivo ReadCSV.scala
scalac ReadCSV.scala

#passo 2
Executar o arquivo, para executar o programa é necessário a passagem de 2 parametros
  p1 = Caminho do arquivo a ser processo no formato CSV
  p2 = Caminho do arquivo de destino no formato JSON
Obs: ambos os parametros devem incluir o nome do arquivo Ex: /tmp/dados.csv /tmp/resultado.json 

scala ReadCSV <entrada> <saida>
