package ldg.com.dbmodule.model

import ldg.com.dbmodule.model.Teams.{HomeTeam, AwayTeam}
import ldg.com.implicitdb.conversions.ImplicitCassandra._

sealed trait Insertable{def getConvertVal(m:Insertable):ConvertVal}
sealed trait ConvertedInsertable{}

//This case class model de game
case class Match(matchNunmber: Int, homeTeam: HomeTeam, awayTeam: AwayTeam, date: String, season : String) extends Insertable{
  // construction reference =>
  // this.result = Integer.toString(homeTeam.getGoals())+"-"+Integer.toString(awayTeam.getGoals());
  def result: Int = homeTeam.goals + awayTeam.goals
  def getConvertVal(m:Insertable)= this.asInstanceOf[Insertable]
}

case class ConvertVal(names:List[String],values:List[AnyRef])  extends ConvertedInsertable