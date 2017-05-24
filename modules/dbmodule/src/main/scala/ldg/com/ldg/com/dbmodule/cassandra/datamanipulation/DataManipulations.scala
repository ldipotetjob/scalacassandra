package ldg.com.dbmodule.cassandra.datamanipulation

import com.datastax.driver.core.Row
import ldg.com.dbmodule.model.{Insertable}

/**
  * Created by ldipotet on 11/4/17.
  *
  * Refers to this Cassandra Data Manipulations:
  * http://cassandra.apache.org/doc/latest/cql/dml.html
  */
trait DataManipulations {

  /**
    * Emulate SELECT
    */
  def selectOP(pattern:String): List[Row]

  /**
    * Emulate INSERT
    *
    *
    *
    */
  def insertOP[A <:Insertable ](objectToInsert: A): String

}
