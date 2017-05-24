package ldg.com.dbmodule.model

/**
  * Created by ldipotet on 5/4/17.
  */


import java.math.BigInteger
import java.sql.Date

import com.datastax.driver.core.querybuilder.{Insert, QueryBuilder}
import com.datastax.driver.core.{Cluster, ResultSet, ResultSetFuture}
//import ldg.com.dbmodule.model.Prueba
import scala.util.{Failure, Success, Try}
import java.util.concurrent.TimeUnit
import scala.collection.JavaConversions._


//We must create our OWN execution CONTEXT!!!!!
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.concurrent.duration._


import java.lang.String._


object CassandraDataStaxClient {

  /**  This Implicit is if you use com.google.guava
    * implicit def resultSetFutureToScala(f: ResultSetFuture): Future[ResultSet] = {
    * val p = Promise[ResultSet]()
    * Futures.addCallback(f,
    * new FutureCallback[ResultSet] {
    * def onSuccess(r: ResultSet) = p success r
    * def onFailure(t: Throwable) = p failure t
    * })
    * p.future
    * }
    */

  //We create here a CallBack in Scala with the DataStax api

  implicit def resultSetFutureToScala(rf: ResultSetFuture): Future[ResultSet] = {

    val p = Promise[ResultSet]()
    val producer = Future {

      val r = rf.getUninterruptibly

      getResultSet(rf) match {

        //we write a promise with an specific value
        case Success(resultset) => p success resultset
        case Failure(e) => p failure (new IllegalStateException)

      }
    }
    p.future
  }

  def getResultSet: ResultSetFuture => Try[ResultSet] = rsetFuture => {
    Try(
      // Other choice can be:
      // getUninterruptibly(long timeout, TimeUnit unit) throws TimeoutException
      // for an specific time

      //can deal an IOException
      rsetFuture.getUninterruptibly
    )

  }
  def runInsert():Insert = {

    val qb:Insert = QueryBuilder.insertInto("footbastatsleaguetest")

    val mapHomeplayer:java.util.Map[String,String] = scala.collection.mutable.Map("Ianx1 Crook"->"  80'  ","David Phillips"->"  31'  ")
    val mapAwayPlayer:java.util.Map[String,String] = scala.collection.mutable.Map("Nigelx Clough"->"  31'  ")

    val names:List[String]= List("league","awaygoals","awaygoalsplayer","awayteam","dategame","homegoal","homegoalsplayer","hometeam","matchweek")
    val values:List[AnyRef] = List(new Integer(777),new Integer(1),mapAwayPlayer,"Nottingham Forest",new Date("1495502158120".toLong),new Integer(3),mapHomeplayer,"Norwich City","MW 3")

//    val values:List[AnyRef] = List(new Integer(575),new Integer(1),"{'Nigel Clough':' 31'' '}","Nottingham Forest","715286700000",new Integer(3),"{'Ian Crook':' 2'' ','Lee Power':' 76'' ','David Phillips':' 89'' '}","Norwich City","MW 3")

    qb.values(names,values)
    //qb.ifNotExists().values(names,values)

    /**
      * As should know => type String = java.lang.String that is a subclass of JavaObject
      * and JavaObject is a Subclass of AnyRef
      *
      * But Int is SubClass of AnyVal thay is a subclass of Any and we need an AnyRef type so:
      *  Int => new Integer(our_value )
      *
      */

    // insert.ifNotExists().values(names,values)

  }



  def main1(args: Array[String]) {

    def defaultFutureUnit() = TimeUnit.SECONDS
    val time = 20 seconds
    //Better use addContactPoints and adds more tha one contact point
    // ref: http://docs.datastax.com/en/drivers/java/2.0/com/datastax/driver/core/Cluster.Builder.html#addContactPoint-java.lang.String-
    // https://github.com/thiagoandrade6
    // https://dzone.com/articles/java-ee6-cdi-named-components


    val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
    val session = cluster.connect("footballstatistics")

    //session.executeAsync es asincrono y tendremos aqui un ResultSetFuture
    //convertido a resulset debido a Impliciconversion
    // val future: Future[ResultSet] = session.executeAsync("SELECT * FROM music2 ")

    val future: Future[ResultSet] = session.executeAsync("SELECT * FROM footbastatsleaguetest;")

    //blocking on a future is strongly discouraged!! next is an specific case
    //to make sure that all of the futures have been completed
    val results = Await.result(future,time).all()
    val resultsize = results.size()

    /*
    results.foreach(row=>println(row.getInt("league")))
    3143
    1145
    6538

    =================

    row.getString("hometeam")

    Liverpool
    Manchester United
    Bradford City
    Swansea City
    West Bromwich Albion

    =================

    row.getInt("homegoal"))

    1
    0
    1
    3

    ================

     row.getMap("homegoalsplayer", classOf[String], classOf[String]))

    {Cristiano Ronaldo= 23' , Jason Euell= 34' (og) , Kieran Richardson= 58' , Louis Saha= 19' }
    {Craig Gardner= 66' , Sebastian Larsson= 41' }
    {Bobby Zamora= 16' , Djibril Cissé= 34' }
    {Luis Boa Morte= 17' }
    {Mark Noble= 79' (pen) }
    {Carlton Cole= 67' }
    {David Weir= 33' }
    {Joleon Lescott= 40' }
    {Bryan Ruiz= 67' }
    {}
    {}
    {Vladimir Smicer= 90' }
    {}
    {Paul Ince= 67' }
    {Garry Parker= 32' (pen) }
    {Jason Roberts= 47' , Roque Santa Cruz= 45 +1', 77' }
    {}

    ===================

    results.foreach(row=>println(row.getTimestamp("dategame")))

    Sat Mar 22 15:00:00 GMT 1997
    Sat Feb 28 15:00:00 GMT 1998
    Sun Mar 01 12:30:00 GMT 2009
    Mon Dec 28 15:00:00 GMT 1992
    Sat Sep 26 15:00:00 BST 1992
    Mon Dec 03 20:00:00 GMT 2001
    Mon Apr 13 15:00:00 BST 1998
    Sat Jan 10 15:00:00 GMT 1998
    Tue Apr 17 20:00:00 BST 2007
    Sat Oct 02 15:00:00 BST 2010


    */

    //homegoalsplayer
    println(s" Este es el tamaño $resultsize")
    //results.foreach(row=>println(row.getString("user_id")) )

    results.foreach(row=>println(row.getTimestamp("dategame")))
    //ResulSet Object and the info
    val t = session.execute(runInsert())
    t.getExecutionInfo.getStatement
    session.close()
    cluster.close()
  }
}
