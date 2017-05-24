package ldg.com.cassandra.appoperations

import ldg.com.dbmodule.model._
/**
  * Created by ldipotet on 7/4/17.
  */
trait CassandraOperations {

  /**
    * This version of getAllMatch() does Not include Implicit Conversion
    * so if for any reason you need to serialize de result for any request for example
    * a good solutions could be Implicits Conversions
    *
    * @return Match Sequence with all match Stored at database
    */
  def getAllMatch(): Option[List[Match]]

  /**
    * This version insertMatch  does Not include Implicit Conversion so if
    * for any reason you want to pass a Match via a POST a good solution could be
    * Implicits Conversions
    *
    * @param matchObj Match to Insert In cassandra
    * @return
    */
  def insertMatch(matchObj: Match): Boolean

}