<img width="928" alt="captura de pantalla 2017-10-07 a las 15 09 49" src="https://user-images.githubusercontent.com/8100363/31313078-665da9a6-abcf-11e7-9266-932880ea6ed2.png">


# Implementation of DML in an Scala ecosystem connecting to Cassandra #

This project is a template for a DML statements. Specifically It implements Data Manipulation Statement \[INSERT - SELECT ]

## What will you find here? ##

* A basic template that implement INSERT and QUERY statements. It contain implementation an test module.
    
* How implement TDD in our development process:
    * We have create the base of Spec with FlatSpec style: We have NOT covered the whole lines of code but we have created enoguh test suites for give you an idea of how to make with the rest. In our case case we connect to an specific Keyspace - table  
    
## What you will not find here, but you should ##

* ErrorHandler implementation as indicate play framework specification. 
* Internationalization of messages.
* LogHandler: 
    * Although we register some log, must exist a log handler, which could be a trait. This trait could be implemented by all those classes that, at some point, can generate a log.
   
## Requirements, Installation, Launching, Testing ##

### Requirements ###

* jdk 1.7+ -> how check java version: java -version
* scala 2.11.x -> how check scala version: scala -version
* sbt 0.13.11+ -> how check sbt version: sbt about
* Cassandra 3.3.x+ -> how check sbt version: bin/nodetool version 

note: *If cassandra is installed as a service, it is NOT necessary to go to the installation directory of cassandra, it will be enough: **nodetool version** from anywhere in your system.*

### Installation ###

* clone repository
* go to root project
* call the following scripts(for Linux \[Ubuntu - Centos] or OSX):

Script files are more complex because we need to make several verificacion about process that are running under OS before execute any process. We need to check afer every execution what exactly happened before and so on. But if you feel confortable in OSX/Linux terminal you only need to execute in your terminal following lines of code: 

You must go to Cassadra installation directory: 

* Create keyspace for our test:

```bash
## keyspace name
KEYSPACETEST=keyspacetest 

local dd_create_keyspace="CREATE KEYSPACE ${KEYSPACETEST} WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};" 

echo ${dd_create_keyspace} | bin/cqlsh
```

if the previous process was success:

* Create table for our test:

```bash
## table 
local dd_create_table="CREATE TABLE dbtest.footballtest (
    league int PRIMARY KEY,
    awaygoals int,
    awaygoalsplayer map<text, text>,
    awayteam text,
    dategame timestamp,
    homegoal int,
    homegoalsplayer map<text, text>,
    hometeam text,
    matchweek text) WITH comment='Contains stats for europe football leagues';"
    
echo ${dd_create_table} | bin/cqlsh    
```
note: *If cassandra **is installed as a service** in your OS, it is NOT necessary to go to the installation directory of cassandra, so you can replace a piece in the previous lines and instead of **bin/cqlsh** can use **bin/cqlsh**. So you can execute the previous line from every where.*

For windows users is mandatory and for OSX or Linux users who wish to use the cqlsh console, these are the two commands to be executed from the Cassandra console:

```sql
CREATE KEYSPACE keyspacetest WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE dbtest.footballtest (
    league int PRIMARY KEY,
    awaygoals int,
    awaygoalsplayer map<text, text>,
    awayteam text,
    dategame timestamp,
    homegoal int,
    homegoalsplayer map<text, text>,
    hometeam text,
    matchweek text) WITH comment='Contains test stats ';
```
Now you are ready to work.

### Testing ###

This Test suite basically **insert** a record and then execute a **query** for get the record inserted previously. You can use this platform for you own objects, only need the appropriate implicit conversions.

* go to root project
* type in terminal: sbt compile test

**Each package in the source code has a file (package.txt) that explains the fundamentals of that specific package.**  
<br>
<br>
<br>
https://mojitoverdeintw.blogspot.com 
