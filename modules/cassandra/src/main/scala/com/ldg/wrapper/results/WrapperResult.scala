package com.ldg.wrapper.results

import com.ldg.miscellaneous.errors.FailureTrait

/**
  * Created by ldipotet on 5/11/16.
  */
object WrapperResult {
  case class Result[A](failures:Option[FailureTrait],matches:Option[A])
  case class Times(currentHour:String,currentMinute:String,amOrPm:String)
}
