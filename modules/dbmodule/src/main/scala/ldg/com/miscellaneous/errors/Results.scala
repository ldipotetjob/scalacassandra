package ldg.com.miscellaneous.errors

//import com.typesafe.config.ConfigException.Null

/**
  * Created by ldipotet on 7/4/17.
  */

sealed trait FailureTrait extends {
  val failuresTags: String
}

//for wrapped exception implemented in funcional way
case class ConnectionError(failuresTags: String="",message:String="",e: Throwable=null) extends FailureTrait

//exception that will down teh API
case class ConnException(message: String="", e: Throwable=null) extends Exception(message: String, e: Throwable)