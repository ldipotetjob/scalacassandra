package ldg.com.dbmodule.cassandra.datamanipulation

import com.datastax.driver.core.{ResultSet, Row, Session}
import ldg.com.dbmodule.cassandra.datamanipulation.wrappers.SelectWrapper
import scala.concurrent.{Await, Future}

//import ldg.com.implicitdb.conversions.ImplicitCassandra.{resultSetFutureToScala,rowToMatch}
import ldg.com.implicitdb.conversions.ImplicitCassandra.{resultSetFutureToScala,rowToMatch,ListOf}
import scala.concurrent.duration._

import ldg.com.dbmodule.model.Match

import scala.collection.JavaConversions._
//import scala.reflect.runtime.universe._
//import scala.tools.reflect.ToolBox
import scala.reflect.runtime.currentMirror

/**
  * Created by ldipotet on 10/4/17.
  *
  * This is a base connection than can be inherited for two types of connections:
  *
  *  1. A connection to a Cassandra DB with a replication Factor == 1
  *
  *  2. A connection to a Cassandra DB with a replication Factor == 2
  *
  *  In 2. should be considered operation like INSERT,DELETE,UPDATE or BATCH operations.
  *
  *  ref:http://cassandra.apache.org/doc/latest/cql/dml.html?highlight=transactions#
  *
  *  Because for example, if using a write consistency level with a replication factor of 2 or > , Cassandra will replicate the write
  *
  *  to all nodes in the cluster and wait for acknowledgement from two nodes. If the write fails on one of the nodes but succeeds on the other,
  *
  *  Cassandra reports a failure to replicate the write on that node.
  *
  *  However, the replicated write that succeeds on the other node is not automatically rolled back.
  *
  *  ref: https://docs.datastax.com/en/cassandra/3.0/cassandra/dml/dmlTransactionsDiffer.html
  *
  */

trait Connection extends DataManipulations{


}

/**
  * Named from: Cassandra Data Manipulations Operations
  * This trait establishes a contract for the following operations in Cassandra :
  *
  * - SELECT
  * - INSERT
  * =========================
  * Pending:
  * - UPDATE
  * - DELETE
  * - BATCH
  * =========================
  *
  * Pending:
  * Logs Operation for trace any errors or for know every moment what operation
  * have been done. For now we'll inspect Cassandra Log.
  *
  */

trait DataBaseDataManipulations[A] {

  type T[B]

  /**
    * Extractor of the result of operations
    *
    * @param obj is the object that must be extracted
    * @return if object can be extracted return Option(Extracted object) if Don't return None. Implement
    *         in the future : Try[] and in case of Failure return the exception
    */
  def unapply(obj: Any): T[A]

  /// def _pattern: String

  /**
    * Execute specific operation[SELECT,INSERT ect]
    *
    * @return Can return Any[A collection of Row in case of SELECT or for example INSERT command return an statement]
    */
  def executeDml: Connection => Any

}

case class RowResult(result: (List[Match]))
case class RuntimeDml[T](session:Session,connection:Connection,wrapperOp:DataBaseDataManipulations[T]) {

  def runtimeDml: Any = {
    wrapperOp.executeDml(connection)
  }
}



