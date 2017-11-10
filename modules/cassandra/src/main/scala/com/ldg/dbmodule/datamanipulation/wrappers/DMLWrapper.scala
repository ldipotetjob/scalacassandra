package com.ldg.dbmodule.datamanipulation.wrappers

//import com.datastax.driver.core.Row
//import com.ldg.datamanipulation.RowResult
import com.ldg.dbmodule.datamanipulation.{Connection, DataBaseDataManipulations}
import com.ldg.dbmodule.model.{Insertable, Match}

import scala.reflect.runtime.universe._

/**
  * Created by ldipotet on 11/4/17.
  *
  * Our Generic [A] indicate the type to which we want to convert the result of our selection
  *
  */
case class SelectWrapper[A: TypeTag](pattern:String) extends  DataBaseDataManipulations[A]{

  override type T[X] = List[A]

  override def unapply(obj: Any): T[A] = obj.asInstanceOf[T[A]]

  override def executeDml: (Connection) => Any = connnection=>connnection.selectOP(pattern)

}

case class InsertWrapper[A<:Insertable](objectToInsert:A,insertIntoTable:String) extends  DataBaseDataManipulations[A]{

  override type T[X] = A

  override def executeDml: (Connection) => Any = {connnection=>connnection.insertOP[A](objectToInsert,insertIntoTable)}

  /**
  * Extractor of the result of operations
  *
  * j is the object that is being extracted
  *            //@tparam S  object's type to extract
  * @return if object can be extracted return Option(Extracted object) if Don't return None. Implement
  *         in the future : Try[] and in case of Failure return the exception
  */
  override def unapply(obj: Any): A = obj.asInstanceOf[A]

}

case class TruncateWrapper[A<:String](pattern:String) extends  DataBaseDataManipulations[A]{

  override type T[X] = List[A]

  override def unapply(obj: Any): T[A] = obj.asInstanceOf[T[A]]

  override def executeDml: (Connection) => Any = connnection=>connnection.selectOP(pattern)

}