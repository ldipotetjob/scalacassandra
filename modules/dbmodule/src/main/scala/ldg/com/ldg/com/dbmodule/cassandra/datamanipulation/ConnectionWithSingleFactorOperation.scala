package ldg.com.dbmodule.cassandra.datamanipulation

import java.sql.Date

import com.datastax.driver.core.querybuilder.{QueryBuilder, Insert}
import com.datastax.driver.core.{Row, ResultSet, Session}
import scala.concurrent.{Await,Future}

import ldg.com.implicitdb.conversions.ImplicitCassandra._
import scala.concurrent.duration._
import scala.collection.JavaConversions._
import ldg.com.dbmodule.model.{Insertable, ConvertVal, Match}
/**
  * Created by ldipotet on 11/4/17.
  *
  * This connection do NOT CONSIDER ATOMICITY because deal only Single
  * Operation in ONE NODE(a replication factor == 1)
  *
  */
case class ConnectionWithSingleFactorOperation(session:Session,contactPoint: String = "127.0.0.1") extends Connection{
  /**
    * Emulate SELECT
    *
    * @return
    */
  val timeOfWaiting = 20 second

  override def selectOP(pattern:String): List[Row] = {

    try {

      //session.executeAsync es asyncronous so we'll have here a ResultSetFuture
      //converted to a resultset due to Implicitconversion @see ldg.com.implicitdb.conversions.ImplicitCassandra
      val future: Future[ResultSet] = session.executeAsync(pattern)

      /**
        * blocking on a future is strongly discouraged!! next is an specific case
        * to make sure that all of the futures have been completed
        * .all() is for return all rows of our Select
        *
        */

      val results = Await.result(future, timeOfWaiting).all

      results.toList

    }catch{

      case ex: Exception => {
        /**
          * Deal the following exceptions:
          * - NoHostAvailableException, QueryExecutionException, QueryValidationException
          * - UnsupportedFeatureException,DriverException
          * - all extend  which is a RuntimeException
          */
        throw new Exception("Error processing the session", ex)
      }
    }
  }

  /**
    *
    * Emulate INSERT
    *
    */
override def insertOP[A <:Insertable](objectToInsert: A): String = {

    type T[X] = A

    def getObjectToInsert(objtoIns:Insertable):ConvertVal = {
      objtoIns.getConvertVal(objtoIns)
    }

    try {
      val qb: Insert = QueryBuilder.insertInto("footbastatsleaguetest")

      /**
        * converted Match => TupleToInsert @see ldg.com.implicitdb.conversions.ImplicitCassandra.matchToRow
        * Insert.values(fiellds_in_which_insert_values,values_to_insert_in_each_field)
        *
        * Insert ref.:https://docs.datastax.com/en/drivers/java/2.0/com/datastax/driver/core/querybuilder/Insert.html
        *
        */

      val obj = getObjectToInsert(objectToInsert)

     val (names:List[String],values:List[AnyRef])= (obj.names,obj.values)

      qb.values(names, values)

      //See ref. about the performance of async queries:
      //http://www.datastax.com/dev/blog/java-driver-async-queries

      val future: Future[ResultSet] = session.executeAsync(qb)

      //blocking on a future is strongly discouraged!! next is an specific case
      //to make sure that all of the futures have been completed
      val results = Await.result(future, timeOfWaiting)

      // results NOT contain .all because only need the statement, we have made an insertions
      results.getExecutionInfo.getStatement.toString
    }catch{

      case ex: Exception => {
        /**
          * Deal the following exceptions:
          * - NoHostAvailableException, QueryExecutionException, QueryValidationException
          * - UnsupportedFeatureException,DriverException
          * - all extend  which is a RuntimeException
          */
        throw new Exception("Error processing the session", ex)
      }
    }

  }

  /**
    * Check if our connection is valid, we have here a blocking exception
    * We can wait an try later
    *
    */
  private def isValidConnection(): Unit ={
  //TODO ckeck if connection is valid

  }
}
