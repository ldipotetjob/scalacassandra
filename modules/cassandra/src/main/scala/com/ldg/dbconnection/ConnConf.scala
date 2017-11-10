package com.ldg.dbconnection

import com.datastax.driver.core.{Session, Cluster}
import com.typesafe.config.ConfigFactory
import com.ldg.miscellaneous.errors.{ConnException, FailureTrait, ConnectionError}
import scala.collection.JavaConversions._
import java.io.Closeable
/**
  * This Object must be created at the beginning of the lifecycle app
  * Initialize cluster
  * Initialize session
  *
  * Share this session during lifetime of application
  *
  * ref. http://www.datastax.com/dev/blog/4-simple-rules-when-using-the-datastax-drivers-for-cassandra
  * better solution can be implements if we are using RESTfull with playframework. In that case you can 
  * create class ConnConf with @Singleton instead of object ConnConf. And then we can inject the class 
  * when it be necessary.
  *
  *
  * Other practice if you're in a PlayRestFull Environment could be:
  *
  *  When the application starts, register the session Object in the
  *  ApplicationLifecycle object.
  *
  */

// Point.scala
case class Config(host:List[String],keyspace:String,usr:String,password:String)
case class CassandraSession(failures:Option[FailureTrait],session:Option[Session])
case class CassandraCluster(failures:Option[FailureTrait],session:Option[Cluster])

//class ConnConf(val configfactory:Config= Config(List("127.0.0.1"),"footballstatistics","","")) {
abstract class ConnConf[A<:ConnConf[A]](val configfactory:Config) {

  import scala.util.{Try, Success, Failure}



  //TODO: Create Clusster with timeOUT solutions

  /**
    * We can read a Configuration file but ByDefault add Localhosts and Ports
    *
    * private val config = ConfigFactory.load()
    *
    * private val hosts = config.getStringList("cassandra.host").toList
    * private val keyspace = config.getString("cassandra.keyspace")
    * private val username = config.getString("cassandra.username")
    * private val password = config.getString("cassandra.password")
    *
    */

 // private val hosts = List("127.0.0.1")
  //private val keyspace = "footballstatistics"


  // Better use addContactPoints and adds more than one contact point
  // ref: http://docs.datastax.com/en/drivers/java/2.0/com/datastax/driver/core/Cluster.Builder.html#addContactPoint-java.lang.String-

  private val tmpCluster: Try[Cluster] = Try(Cluster.builder().addContactPoints(configfactory.host.toArray: _*)
                                                              .withCredentials(configfactory.usr, configfactory.password).build())

  private val cluster:Either[FailureTrait,Cluster]= eitherFunc[Cluster]{tmpCluster}{ConnectionError()}

  def getCluster:Option[Cluster] = {cluster.fold(clust=> Option.empty[Cluster],clust=> Option(clust))}

  private val session: Try[Session] = Try {

    val workCluster:Cluster = cluster match {
      case Right(cluster) => cluster
      case Left(fail) => throw new ConnException("Error creating the Cassandra Cluster",fail.asInstanceOf[ConnectionError].e)
    }

    workCluster.connect(configfactory.keyspace)
  }

  private val tmpSession:Either[FailureTrait,Session]= eitherFunc[Session]{session}{ConnectionError()}


  def getSession: CassandraSession = {
    tmpSession match {
      case Right(session) => CassandraSession(None,Some(session))
      case Left(fail) => CassandraSession(Some(fail),None)
    }
  }

  def eitherFunc[S](tryResult: => Try[S])(fail: => ConnectionError): Either[ConnectionError, S] = tryResult match {
    case Success(pattern) => Right(pattern)
    case Failure(error) => Left(fail.copy(failuresTags=error.getClass.getName,message=error.getMessage,e=error))
  }

  def fClose (fsess:Session):Unit={
    fsess.close()
  }

  def fCloseC (fcluss:Cluster):Unit={
    fcluss.close()
  }

  /**
    * We invoke this method in the following 	circumstances :
    *
    *  - When the API shutDown
    *  - THe connection is failing for any reason
    *
    */
  def shutdown() {

       getSession.session.fold{println("we don't have session")}{fClose(_)}
       getCluster.fold{println("we don't have cluster")}{fCloseC(_)}

  }
  // TODO: CREATE METHOD to monitorize Cassandra :
  //  http://docs.datastax.com/en/developer/java-driver/2.1/manual/pooling/

}
/**
object ConnConf{

  def apply(configfactory: Config): ConnConf = new ConnConf(configfactory)
  def apply(): ConnConf = new ConnConf()
}
**/