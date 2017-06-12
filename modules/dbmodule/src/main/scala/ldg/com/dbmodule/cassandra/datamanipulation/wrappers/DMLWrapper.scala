package ldg.com.dbmodule.cassandra.datamanipulation.wrappers

import com.datastax.driver.core.Row
import ldg.com.dbmodule.cassandra.datamanipulation.{RowResult, Connection, DataBaseDataManipulations}

import ldg.com.dbmodule.model.{Match,Insertable}

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

case class InsertWrapper[A<:Insertable](objectToInsert:A) extends  DataBaseDataManipulations[A]{

  override type T[X] = A

  override def executeDml: (Connection) => Any = {connnection=>connnection.insertOP[A](objectToInsert)}

  /**
    * Extractor the result of an operation of Type A
    * @return if object can be extracted return Option(Extracted object) 
    */
  override def unapply(obj: Any): A = obj.asInstanceOf[A]

}
