#!/usr/bin/env bash

# chmod +x mongoconf.sh
# USAGE: ./mongoconf.sh {host_ip} {host_port}
# ./mongoconf.sh 192.168.1.49 27030

# script variables
seperator="---------------------"
host_ip=$1
# 27030
host_port=$2
sleep_limit=5
mongoconf_js="https://raw.githubusercontent.com/elvis-alexander/chirpchirp/master/shellscripts/js/mongoconf.js"

# installing mongodb
echo $seperator "Installing MongoDB" $seperator
sleep $sleep_limit
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6
sudo echo "deb [ arch=amd64 ] http://repo.mongodb.org/apt/ubuntu trusty/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list
sudo apt-get update
sudo apt-get install -y mongodb-org

# creating directories
echo $seperator "Creating config directories" $seperator
sleep $sleep_limit
mkdir mongo
mkdir mongo/mongo_logs
mkdir mongo/config
mkdir mongo/config/conf

# running mongo config server
echo $seperator "Running config server" $seperator
sleep $sleep_limit
mongod --configsvr --replSet conf --dbpath ~/mongo/config/conf --bind_ip $host_ip --port $host_port --logpath ~/mongo/mongo_logs/conf.log --fork
#mongod --configsvr --replSet conf --dbpath ~/mongo/config/conf --bind_ip 192.168.1.49 --port 27030 --logpath ~/mongo/mongo_logs/conf.log --fork

# retrieving js file
echo $seperator "Wget js file" $seperator
sleep $sleep_limit
wget $mongoconf_js

# feeding js input
echo $seperator "Feeding js input" $seperator
sleep $sleep_limit
mongo --host $host_ip --port $host_port < mongoconf.js
sleep 20

# mongo ps results
echo $seperator "ps results" $seperator
sleep $sleep_limit
ps -ax | grep mongo