package ldg.com.wrapper.results

import ldg.com.dbmodule.model._
import ldg.com.miscellaneous.errors.FailureTrait

/**
  * Created by ldipotet on 5/11/16.
  */
object WrapperResult {
  case class Result[A](failures:Option[FailureTrait],matches:Option[A])
  case class Times(currentHour:String,currentMinute:String,amOrPm:String)
}
