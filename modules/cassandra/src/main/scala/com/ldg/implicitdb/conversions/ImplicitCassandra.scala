package com.ldg.implicitdb.conversions

import java.sql.Date

import com.datastax.driver.core.{ResultSet, ResultSetFuture, Row}
import com.ldg.dbmodule.model._
import com.ldg.dbmodule.model.Teams._
import com.ldg.wrapper.results.StatsTypes.{minuteGoal, playerName}

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConversions._


/**
  * Use Implicit Global Context is strongly discouraged! we must create our OWN execution CONTEXT !
  *
  * We import the default global execution context for execute tasks submitted to them.It's essential for
  * the Future.apply method. We encourage You to define your own execution contexts and use them with Future, for
  * now it is sufficient to know that you can import the default execution context as shown above.
  *
  */


/**
  *
  * Created by ldipotet on 3/5/17.
  *
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

    //TODO :!!!!!! IMPORTANT CHECK if DATA IS NULL
    val dateGame = rowMatch.getTimestamp("dategame")+""

    Match(rowMatch.getInt("league"),homeTeam,awayTeam,dateGame,rowMatch.getString("matchweek"))

  }


  /**
    * we use 2 implicit conversions :
    *
    * - an implicit conversion that convert Row => Match
    * - an Implict conversion that convert List[x] => List[Y]
    */



  /**
    *
    * Implicit Class that convert a List[A] => List[B]
    *
    * implicit conversion that convert Row => Match
    *
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

  /**
    * Implicit conversion used for convert every field to insert in the require format
    * needed for insert command in DataStax api
    *
    */

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
        ("homegoal",new Integer(matchGame.homeTeam.goals)),
        ("hometeam",matchGame.homeTeam.name),
        ("homegoalsplayer",mapHomeplayer)
      )

      val (names:List[String],values:List[AnyRef])= listOfFieldsValuesToInsert.unzip

      ConvertVal(names,values)
    }
  }
  implicit def InsertableToConvertedInsertable[A <: Insertable, B <: ConvertedInsertable](a : A)(implicit converter : Converter[A, B]) : B = converter.convert(a)

}


