package com.ldg.dbconnection

class RecipesConnection(override val configfactory:Config) extends ConnConf[RecipesConnection](configfactory)

object RecipesConnection extends
  RecipesConnection(Config(List("127.0.0.1"),"footballstatistics","cassandra","cassandra"))

