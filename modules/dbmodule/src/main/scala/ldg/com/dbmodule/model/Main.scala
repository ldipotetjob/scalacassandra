package ldg.com.dbmodule.model

import ldg.com.cassandra.appoperations.CassandraOperationsImpl
import ldg.com.dbmodule.model.Teams.{HomeTeam, AwayTeam}

/**
  * Created by ldipotet on 23/5/17.
  */
object Main extends App{

  //val homeTeam = HomeTeam("Stoke City",1,Map("Juan Mata"->List(" 19'' (og) ")))

  val awayTeam = AwayTeam("Manchester United",1,Map("Wayne Rooney"->List(" 90 +4'' ")))
  val homeTeam = HomeTeam("Stoke City",1,Map("Juan Mata"->List(" 19'' ")))

  /**
    * Object to insert: Match Game
    */
  val matchGame = Match(1877,homeTeam,awayTeam,"1495502158120","MW 22")

  val cassaOP = new CassandraOperationsImpl

 // val matches:Option[List[Match]] = cassaOP.getAllMatch()

  val insertval= cassaOP.insertMatch(matchGame)

//  matches.fold(println("No Items"))(matchItems=>matchItems.foreach(matchItem=>println(s"number of game: $matchItem")))

  if (insertval) println("Sussess") else println("Errorrr!!!")

}
