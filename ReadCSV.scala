/**
  * @author Rogerio Souza - rogerioanderson@gmail.com
  */

import java.io.{BufferedWriter, File, FileWriter}
import java.text.{ParseException, SimpleDateFormat}
import java.util.Calendar
import java.util.logging.{Level, Logger}

import scala.util.parsing.json._

object ReadCSV {

  /**
    *
    * @param args
    */
  def main(args: Array[String]){
    val log = Logger.getAnonymousLogger();

    if(args.length!=2){
      throw new RuntimeException("O progama deve receber 2 parametros: {caminho do arquivo de entrada(CSV, caminho do arquivo de saida (JSON)}");
    }
    val csvpath = args(0)
    val destinationPath = args(1)

    log.info("Iniciando Leitura de arquivo")

    val lines: Iterator[String] = scala.io.Source.fromFile(csvpath).getLines


    //recupera a primeira linha
    if(lines.hasNext) {
      //le a primeira linha recuperando o cabecalho do arquivo
      log.info("Recuperando colunas para cabecalho do arquivo")
      val header = lines.next().split(",")

      log.info("Iniciando processamento dos registros")
      val values = lines.toArray.map(readLine). //le os registros do arquivo
        filter(filterValids). //filtra os registros com os campos preenchidos
        filter(element=>filterByDate("yyyy-MM-dd",element,6)). //filtra os registros baseado no mes de corte
        map(element => jsonConverter(element,header)). //cria o mapa com os dados para a criacao do json
        toList

      //converte os mapas para JSON
      log.info("Convertendo resultados para JSON")
      val result = JSONArray(values)

      log.info("Gravando arquivo no disco")
      writeFile(destinationPath, result)

    }
  }


  def writeFile(destinationPath: String, result: JSONArray): Unit = {
    //grava o arquivo em disco
    val log = Logger.getAnonymousLogger
    val file = new File(destinationPath)
    val writer = new BufferedWriter(new FileWriter(file))
    writer.write(result.toString())
    writer.flush()
    writer.close()
  }

  /**
    *
    *
    * @param element colunas contendo dados
    * @param header  colunas de cabecalho utilizada
    * @return mapa com os dados para criacao de arquivo json
    */
  def jsonConverter(element: Array[String], header: Array[String]): JSONObject = {
    val log = Logger.getAnonymousLogger
    val map = Map((header(0),element(0)),(header(1),element(1)),
      (header(2),element(2)),(header(3),element(3)))
    JSONObject(map);
  }

  /**
    *
    * @param pattern formato da data
    * @param element   colunas com conteudo
    * @param deadLineMonth mes limite para o filtro
    * @return
    */
  def filterByDate(pattern:String ,element: Array[String], deadLineMonth: Int):Boolean ={
    val log = Logger.getLogger(getClass.getName)
    try{
        val sdf = new SimpleDateFormat(pattern){}
        sdf.setLenient(false) //nao permite o parse de datas invalidas

        val birthday = Calendar.getInstance()
        birthday.setTime(sdf.parse(element(3)));
        val month = birthday.get(Calendar.MONTH)

        if((month+1)>deadLineMonth){
          log.log(Level.FINE,"Nao faz aniversario no 1 semestre: "+element(1)+" data "+element(3))
          return false
        }

        true
    }catch{
      case e: ParseException => false
    }
  }

  /**
    *
    * @param element
    * @return
    */
  def readLine(element:String):Array[String]={
    element.split(",");
  }

  /**
    * Verifca se a coluna possui todos os valores
    * @param element coluna de dados
    * @return
    */
  def filterValids(element:Array[String]):Boolean={
    //valida se o registro possui todos os campos
    if(element.length<4){
      return false;
    }
    true;
  }

}