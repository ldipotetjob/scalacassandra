package com.ldg.dbmodule.datamanipulation

import com.datastax.driver.core.Row

import com.ldg.dbmodule.model.Insertable

/**
  * Created by ldipotet on 11/4/17.
  *
  *  This trait emulate DM Operations
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
    */
  def insertOP[A <:Insertable ](objectToInsert: A, insertIntoTable:String): String

  /**
    * To alter the behavior of creating a snapshot before truncate you can change your cassandra.yaml
    *
    * Whether or not a snapshot is taken of the data before keyspace truncation
    * or dropping of column families. The STRONGLY advised default of true
    * should be used to provide data safety. If you set this flag to false, you will
    * lose data on truncation or drop.
    * auto_snapshot: true
    *
    * BUT it is NOT recommended this flag is to make sure that after a drop or a truncate your data is not
    * lost without a second thought
    *
    * @param truncateTable
    * @return
    */

  def truncateOP(truncateTable:String): String

}
