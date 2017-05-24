package ldg.com.implicitdb.conversions

import java.sql.Date

import com.datastax.driver.core.{Row, ResultSet, ResultSetFuture}
import ldg.com.dbmodule.model._
import ldg.com.dbmodule.model.Teams._
import ldg.com.wrapper.results.StatsTypes._

import scala.concurrent.{Promise, Future}
import scala.util.{Try, Failure, Success}

//Use Implicit Global Context is strongly discouraged! we must create our OWN execution CONTEXT !
import scala.concurrent.ExecutionContext.Implicits.global

import scala.collection.JavaConversions._


/**
  * https://stackoverflow.com/questions/19776508/scala-implicit-conversion-from-generic-type-to-second-generic-type
  * Created by ldipotet on 3/5/17.
  */


trait Converter[A <: Insertable, B <: ConvertedInsertable] {
  def convert(a : A) : B
}

object ImplicitCassandra {

  /**
    *
    * We create here a CallBack in Scala with the DataStax api
    * rowsPromise: successfully complete a future with rsetFuture.getUninterruptibly value
    *              unsuccessfully failing the promise, rise an IllegalStateException
    *
    */

  implicit def resultSetFutureToScala(rf: ResultSetFuture): Future[ResultSet] = {

    val rowsPromise = Promise[ResultSet]()
    val producer = Future {

      getResultSet(rf) match {

        case Success(rowResultset) => rowsPromise success rowResultset
        case Failure(e) => rowsPromise failure (new IllegalStateException)

      }

    }
    rowsPromise.future
  }

  implicit def rowToMatch(rowMatch:Row): Match = {

    def goalsPlayer(key:String):Map[playerName,List[minuteGoal]]={

      //the scala.collection.mutable.Map[String,String]
      val tmpMapGoalPlayer= rowMatch.getMap(key, classOf[String], classOf[String])
      tmpMapGoalPlayer.map {case (playername, minutesgoals) => (playername -> minutesgoals.split(",").toList)}.toMap
    }

    // Key for get Goal Per Player for HOMETEAM: "homegoalsplayer"
    val homeTeam=HomeTeam(rowMatch.getString("hometeam"),rowMatch.getInt("homegoal"),goalsPlayer("homegoalsplayer"))
    // Key for get Goal Per Player for AWAYTEAM: "awaygoalsplayer"
    val awayTeam=AwayTeam(rowMatch.getString("awayteam"),rowMatch.getInt("awaygoals"),goalsPlayer("awaygoalsplayer"))

    val dateGame = rowMatch.getTimestamp("dategame").toString

    Match(rowMatch.getInt("league"),homeTeam,awayTeam,dateGame,rowMatch.getString("matchweek"))

  }

  /**
    * Implicit Class that convert a List[A] => List[B]
    * An implicit funtion f: A => B is needed
    */
  implicit class ListOf[A](val list: List[A]) {
    def of[B](implicit f: A => B): List[B] = list map f
  }

  def getResultSet: ResultSetFuture => Try[ResultSet] = rsetFuture => {
    Try(
      //can throws TimeoutException IOException
      rsetFuture.getUninterruptibly
    )
  }

  implicit object matchToRowConverter extends Converter[Insertable,ConvertVal]{
    def convert(matchGameInsertable : Insertable) : ConvertVal = {

      val matchGame:Match = matchGameInsertable.asInstanceOf[Match]

      val inmutableMapHomePlayer= matchGame.homeTeam.goalsPlayer.map{case (players, goallist) =>(players,goallist.mkString(","))}

      println(inmutableMapHomePlayer)

      val mapHomeplayer:java.util.Map[String,String]  = scala.collection.mutable.Map(inmutableMapHomePlayer.toSeq:_*)

      println(mapHomeplayer)

      val inmutableMapAwayPlayer= matchGame.awayTeam.goalsPlayer.map{case (players, goallist) =>(players,goallist.mkString(","))}

      val mapAwayplayer:java.util.Map[String,String]  = scala.collection.mutable.Map(inmutableMapAwayPlayer.toSeq:_*)

      val listOfFieldsValuesToInsert: List[(String,AnyRef)] = List(("league",new Integer(matchGame.matchNunmber)),
        ("dategame",new Date(matchGame.date.toLong)),
        ("matchweek",matchGame.season),
        ("awaygoals",new Integer(matchGame.awayTeam.goals)),
        ("awayteam",matchGame.awayTeam.name),
        ("awaygoalsplayer",mapAwayplayer),
        ("homegoal",new Integer(matchGame.awayTeam.goals)),
        ("hometeam",matchGame.awayTeam.name),
        ("homegoalsplayer",mapHomeplayer)
      )

      val (names:List[String],values:List[AnyRef])= listOfFieldsValuesToInsert.unzip

      ConvertVal(names,values)
    }
  }
  implicit def InsertableToConvertedInsertable[A <: Insertable, B <: ConvertedInsertable](a : A)(implicit converter : Converter[A, B]) : B = converter.convert(a)

}


