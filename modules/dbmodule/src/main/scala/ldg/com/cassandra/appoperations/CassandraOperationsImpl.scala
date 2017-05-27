package ldg.com.cassandra.appoperations

import com.datastax.driver.core.Row
import ldg.com.dbmodule.cassandra.datamanipulation.ConnConf._
import ldg.com.dbmodule.cassandra.datamanipulation.{ConnConf, RuntimeDml, ConnectionWithSingleFactorOperation}
import ldg.com.dbmodule.cassandra.datamanipulation.wrappers.{InsertWrapper, SelectWrapper}
import ldg.com.dbmodule.model.Match
import ldg.com.implicitdb.conversions.ImplicitCassandra.{rowToMatch,ListOf}

/**
  * Created by ldipotet on 10/4/17.
  *
  * A good idea should be intead Object Connection a Class Connection because then we can
  * //@Inject()Coonection class But we can't inject connection objects
  *
  */
class CassandraOperationsImpl extends CassandraOperations{
  /**
    * This version of getAllMatch() does Not include Implicit Conversion
    * so if for any reason you need to serialize de result for any request for example
    * a good solutions could be Implicits Conversions
    *
    * @return Match Sequence with all match Stored at database
    */
  override def getAllMatch(): Option[List[Match]] = {
    val selectAllGamesQuery = SelectWrapper[Row]("SELECT * FROM footbastatsleaguetest;")

    try {
      //TODO Check if there is connection and if it a valid connection

      ConnConf.getSession match {

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
        case CassandraSession(Some(fail), None) => Option.empty[List[Match]]

          /**
            * Something fail, the especifications errors are in fail so if you are using the implementation you
            * can deal this exception and write a log
            *
            */
      }

    } catch {

      case e: Exception =>{
        e.printStackTrace()
        None
      }
    }
  }

  /**
    * This version insertMatch  does Not include Implicit Conversion so if
    * for any reason you want to pass a Match via a POST a good solution could be
    * Implicits Conversions
    *
    * matchObj Match to Insert In cassandra
    *
    */
  override def insertMatch(matchObj: Match): Boolean = {
    val insertMatchGamesQuery = InsertWrapper[Match](matchObj)

    try {
      //TODO Check if there is connection and if it a valid connection

      ConnConf.getSession match {

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
          * Something fail, the especifications errors are in fail so if you are using the implementation you
          * can deal this exception and write a log
          *
          */
      }

    } catch {

      case e: Exception =>{
        e.printStackTrace()
        false
      }
    }
  }
}
