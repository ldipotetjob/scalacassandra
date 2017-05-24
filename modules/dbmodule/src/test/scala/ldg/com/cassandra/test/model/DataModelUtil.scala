package ldg.com.cassandra.test.model

import ldg.com.dbmodule.model.Match
import ldg.com.dbmodule.model.Teams.{AwayTeam, HomeTeam}

/**
  * Created by ldipotet on 23/5/17.
  * This trait will be used as 
  *
  */
trait DataModelUtil {

//ref: http://www.freeformatter.com/epoch-timestamp-to-date-converter.html

//1485010800000 => 21 de enero de 2017, 15:00:00 GMT

  val awayTeam = AwayTeam("Manchester United",1,Map("Wayne Rooney"->List(" 90 +4'' ")))
  val homeTeam = HomeTeam("Stoke City",1,Map("Juan Mata"->List(" 19'' (og) ")))
  val matchGame = Match(877,homeTeam,awayTeam,"1495502158120","MW 22")


}
