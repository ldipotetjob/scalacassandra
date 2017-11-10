package ldg.com.cassandra.test.dml

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.BeforeAndAfter
import com.ldg.appoperations.CassandraOperationsImpl
import com.ldg.dbconnection.{Config, RecipesConnection}
import ldg.com.cassandra.test.model.DataModelUtil
import com.ldg.dbmodule.model.{Match, Teams}

/**
  *
  * For this test suppose that Cassandra is started BUT for production projects
  * you will need a Test DB as a mirror in ddl as a production DB for Test de api
  *
  */
class DMLCassandraOperationsSpec  extends FlatSpec with Matchers with BeforeAndAfter  {

  object RecipesTestConnection extends RecipesConnection(Config(List("127.0.0.1"),"dbtest","cassandra","cassandra"))
  object datatest extends DataModelUtil

  val cassaOP = CassandraOperationsImpl(RecipesTestConnection.getSession)

  before {
    cassaOP.truncateTable("footballtest")
  }


  "Insert operation if was success " should "return a boolean true value" in {

    val insertval= cassaOP.insertMatch(datatest.matchGame,"footballtest")

    insertval shouldBe true

  }

  "Select Operation " should " return a proper Record inserted in other test" in {

    val matches:Option[List[Match]] = cassaOP.getAllMatch("SELECT * FROM footballtest;")

    matches shouldBe defined

    //Should NOT use get getOrElse!!

    val listOfMatches:List[Match] = matches.get

    listOfMatches should have size 1

    assert (listOfMatches(0).homeTeam.name == datatest.matchGame.homeTeam.name)

    assert (listOfMatches(0).awayTeam.name == datatest.matchGame.awayTeam.name)

    assert (listOfMatches(0).homeTeam.goals == datatest.matchGame.homeTeam.goals)

    assert (listOfMatches(0).awayTeam.goals == datatest.matchGame.awayTeam.goals)

  }

}