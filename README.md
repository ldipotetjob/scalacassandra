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
* Cassandra 3.3.x -> how check sbt version: bin/nodetool version 

note: if cassandra is installed as a service, it is NOT necessary to go to the installation directory of cassandra, it will be enough: **nodetool version** from anywhere in your system.

### Installation ###

* clone repository
* go to root project
* call the following scripts(for Linux \[Ubuntu - Centos] or OSX):



### Launching ###

You have several rest sevices to call:

This is [the route file](https://github.com/ldipotetjob/restfulinplay/blob/master/modules/apirest/conf/apirest.routes) and it has commented several examples on how call all services exposed in this project.
The commented examples has the following structure:

\# pattern: 

\# example: 

You can paste the \# example: in your **terminal** in case of **curl** o in your **browser** in case **http**


### Testing ###

* go to root project
* type in terminal: sbt test

video: [running test](https://youtu.be/s-jO1PFaUR4)

The basic information is [here on gitHub](https://github.com/ldipotetjob/restfulinplay/blob/master/package.txt) and contains the main project information.


**Each package in the source code has a file (package.txt) that explains the fundamentals of that specific package.**  
<br>
<br>
<br>
https://mojitoverdeintw.blogspot.com 




## Connecting to Cassandra in an Scala ecosystem 

For any project in this repository you will need to install the following softwares:

* jdk 1.7+
* scala 2.11.x
* sbt 0.13.11+
* Cassandra 3.3.x

Have been implemented the following dml commands:

* INSERT
* SELECT

All previous commands can be easily custumized to your own model.


**Every package in the source code have a file(package.txt) that explain the core of 
the specific package.**  

Enjoy :+1:



https://mojitoverdeintw.blogspot.com 

https://mojitoverde.blogspot.com


## Connecting to Cassandra in an Scala ecosystem 

For any project in this repository you will need to install the following softwares:

* jdk 1.7+
* scala 2.11.x
* sbt 0.13.11+
* Cassandra 3.3.x

Have been implemented the following dml commands:

* INSERT
* SELECT

All previous commands can be easily custumized to your own model.


**Every package in the source code have a file(package.txt) that explain the core of 
the specific package.**  

Enjoy :+1:



https://mojitoverdeintw.blogspot.com 

https://mojitoverde.blogspot.com
