package com.ldg.appoperations

import com.datastax.driver.core.Row
import com.ldg.dbconnection._
import com.ldg.dbmodule.datamanipulation.{ConnectionWithSingleFactorOperation, RuntimeDml}
import com.ldg.dbmodule.datamanipulation.wrappers.{InsertWrapper, SelectWrapper, TruncateWrapper}
import com.ldg.dbmodule.model.Match
import com.ldg.implicitdb.conversions.ImplicitCassandra.{ListOf, rowToMatch}
import com.ldg.miscellaneous.errors.ConnectionError


/**
  * Created by ldipotet on 10/4/17.
  *
  * A good idea should be intead Object Connection a Class Connection because then we can
  * //@Inject()Coonection class But we can't inject connection objects
  *
  */

class CassandraOperationsImpl(cassSession:CassandraSession) extends CassandraOperations{
  /**
    * This version of getAllMatch() does Not include Implicit Conversion
    * so if for any reason you need to serialize de result for any request for example
    * a good solutions could be Implicits Conversions
    *
    * pattern: "SELECT * FROM footbastatsleague;"
    *
    * @return Match Sequence with all match Stored at database
    */
  override def getAllMatch(pattern:String): Option[List[Match]] = {

    val selectAllGamesQuery = SelectWrapper[Row](pattern)


    try {
      //TODO Check if there is connection and if it a valid connection

      cassSession match {

        case CassandraSession(None, Some(session)) => {
          val runtime = RuntimeDml[Row](session, ConnectionWithSingleFactorOperation(session), selectAllGamesQuery)
          val rowsOfMatch:List[Row] = selectAllGamesQuery.unapply(runtime.runtimeDml)

          /**
            * we use 2 implicit conversions :
            *
            * - an implicit conversion that convert Row => Match
            * - an Implict conversion that convert List[x] => List[Y]
            */

          Option(rowsOfMatch.of[Match])

        }
        case CassandraSession(Some(fail), None) => {

          System.out.println(fail.asInstanceOf[ConnectionError].message)

          Option.empty[List[Match]]

          /**
            * Something fail, the especifications errors are in fail so if you are using the implementation you
            * can deal this exception and write a log
            *
            */
        }
      }

    } catch {

      case e: Exception =>{
        e.printStackTrace()
        None
      }
    }
  }

  /**
    * This version insertMatch  does Not include Implicit Conversion so if for any reason you want
    * to pass a Match via a POST in a Rest services a good solution could be Implicits Conversions
    *
    * matchObj Match to Insert In cassandra
    *
    */
  override def insertMatch(matchObj: Match,tableIntoInsert: String ): Boolean = {
    val insertMatchGamesQuery = InsertWrapper[Match](matchObj,tableIntoInsert)

    try {
      //TODO Check if there is connection and if it a valid connection

      cassSession match {

        case CassandraSession(None, Some(session)) => {
          val runtime = RuntimeDml[Match](session, ConnectionWithSingleFactorOperation(session), insertMatchGamesQuery)
          runtime.runtimeDml

          /**
            * @see package.txt
            */
          true
        }
        case CassandraSession(Some(fail), None) => false//Option.empty[List[Match]]

        /**
          * Something fail getting the Session Object, the specifications errors are in fail so if you are using
          * the implementation you can deal this exception and write a log
          *
          */
      }

    } catch {

      case e: Exception =>{

        false
      }
    }
  }


  override def truncateTable(tableToTruncate: String): Boolean = {

    val TruncateTableQuery = TruncateWrapper[String](tableToTruncate)



    try {
      //TODO Check if there is connection and if it a valid connection

      cassSession match {

        case CassandraSession(None, Some(session)) => {
          val runtime = RuntimeDml[String](session, ConnectionWithSingleFactorOperation(session), TruncateTableQuery)
          runtime.runtimeDml

          /**
            * @see package.txt
            */
          true
        }
        case CassandraSession(Some(fail), None) => false//Option.empty[List[Match]]

        /**
          * Something fail getting the Session Object, the specifications errors are in fail so if you are using
          * the implementation you can deal this exception and write a log
          *
          */
      }

    } catch {

      case e: Exception =>{

        false
      }
    }









  }





}





object CassandraOperationsImpl {
  def apply(cassSession:CassandraSession): CassandraOperationsImpl = new CassandraOperationsImpl(cassSession)
}