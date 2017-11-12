#!/bin/bash

## if you have installed Cassadra properly, with all operations as a service
## you don't need to pass the installation directory as a Parammeter
## Check the below site if you want to pass your own configuartions to cassandra
## cqlsh command
## http://cassandra.apache.org/doc/latest/tools/cqlsh.html#cqlshrc

## cassandra installation directory
installation_cassandra_directory=$1

## key space name
KEYSPACETEST="dbtest"

## keyspace creation error
KEYSPACE_CREATION_ERROR="keyspace could not be created"

## table creation error
TABLE_CREATION_ERROR="testtable could not be created"

## pattern : echo "pattern QUERY; exit" | cqlsh CASSANDRA_HOST -u 'USER' -p 'PASS'

create_keyspace_with_testtable () {

## echo ${dd_create_keyspace} | bin/cqlsh
local dd_create_keyspace="CREATE KEYSPACE ${KEYSPACETEST} WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};"

## echo ${dd_create_table} | bin/cqlsh
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

if [ -z "$installation_cassandra_directory" ]; then
	## Cassandra could be installed as a service or in Path
	## Call function to create keyspace
	## echo ${dd_create_keyspace} | cqlsh

    local create_keyspace_operation=$(echo ${dd_create_keyspace} | cqlsh 2>&1 | more )

		if [ -z "$create_keyspace_operation" ]; then
        	## keyspace created, now it's time to create table
        	echo "kesyspace for test has been created"

        	## echo ${dd_create_table} | cqlsh
            local create_table_operation=$(echo ${dd_create_table} | cqlsh 2>&1 | more )

        		if [ -z "$create_table_operation" ]; then
        			echo "Test table has been created"
        		else
                    ## create table operation Error!!
                    echo $create_table_operation
        		fi
        else
            ## create keyspace operation error!!
        	echo $create_keyspace_operation
        fi
else
    # save current directory
	pushd $1
	# echo ${dd_create_keyspace} | bin/cqlsh

    local create_keyspace_operation=$(echo ${dd_create_keyspace} | bin/cqlsh 2>&1 | more )

		if [ -z "$create_keyspace_operation" ]; then
        	## keyspace created, now it's time to create table
        	echo "kesyspace for test has been created"

        	##echo ${dd_create_table} | bin/cqlsh
            local create_table_operation=$(echo ${dd_create_table} | bin/cqlsh 2>&1 | more )

        		if [ -z "$create_table_operation" ]; then
                    echo "Test table has been created"
        		else
                    ## create table operation Error!!
        			echo $create_table_operation
        		fi
        else
            ## create keysapce operation error!!
        	echo $create_keyspace_operation
        fi
     ## go back to the initial directory
     popd
fi
}

# cheking if cassandra service is started
# it doesn't matter if it started by a services in an standard installation
# or if it is started by a manual installation
ps -fe | grep [c]assandra > /dev/null
if [ $? -eq 0 ]; then
  echo "creating keyspace name ${KEYSPACETEST},Cassandra is running in directory: $1"
  create_keyspace_with_testtable ${installation_cassandra_directory}
else
  echo "Cassandra database is not started"
fi
