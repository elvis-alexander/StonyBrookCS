#!/usr/bin/env bash

# chmod +x mongoshard.sh
# USAGE: ./mongoshard.sh {host_ip} {host_port}
# ./mongoshard.sh 192.168.1.45 27040
# ./mongoshard.sh 192.168.1.46 27040
# ./mongoshard.sh 192.168.1.47 27040
# ./mongoshard.sh 192.168.1.48 27040
# ./mongoshard.sh 192.168.1.56 27040
# ./mongoshard.sh 192.168.1.57 27040


# script variables
host_ip=$1
host_port=$2
seperator="---------------------"
sleep_limit=5
mongoshard_js="https://raw.githubusercontent.com/elvis-alexander/chirpchirp/master/shellscripts/js/mongoshard.js"

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
mkdir mongo/shard

# running shard
echo $seperator "Running Shard" $seperator
sleep $sleep_limit
mongod --shardsvr --replSet shard1 --dbpath /home/ubuntu/mongo/shard/ --bind_ip $host_ip --port $host_port --logpath /home/ubuntu/mongo/mongo_logs/shard.log --logappend --fork
#mongod --shardsvr --replSet shard1 --dbpath /home/ubuntu/mongo/shard/ --bind_ip 192.168.1.45 --port 27040 --logpath /home/ubuntu/mongo/mongo_logs/shard.log --logappend --fork
#mongod --shardsvr --replSet shard2 --dbpath /home/ubuntu/mongo/shard/ --bind_ip 192.168.1.46 --port 27040 --logpath /home/ubuntu/mongo/mongo_logs/shard.log --logappend --fork
#mongod --shardsvr --replSet shard3 --dbpath /home/ubuntu/mongo/shard/ --bind_ip 192.168.1.47 --port 27040 --logpath /home/ubuntu/mongo/mongo_logs/shard.log --logappend --fork
#mongod --shardsvr --replSet shard4 --dbpath /home/ubuntu/mongo/shard/ --bind_ip 192.168.1.48 --port 27040 --logpath /home/ubuntu/mongo/mongo_logs/shard.log --logappend --fork
#mongod --shardsvr --replSet shard5 --dbpath /home/ubuntu/mongo/shard/ --bind_ip 192.168.1.56 --port 27040 --logpath /home/ubuntu/mongo/mongo_logs/shard.log --logappend --fork
#mongod --shardsvr --replSet shard6 --dbpath /home/ubuntu/mongo/shard/ --bind_ip 192.168.1.57 --port 27040 --logpath /home/ubuntu/mongo/mongo_logs/shard.log --logappend --fork
#(Ignore) mongod --bind_ip 192.168.1.37 --port 27017 --dbpath /home/ubuntu/db --logpath /home/ubuntu/log/user.log --logappend --fork

# retrieving js file
echo $seperator "Wget js file" $seperator
sleep $sleep_limit
wget $mongoshard_js

# feeding js input
echo $seperator "Feeding js input" $seperator
sleep $sleep_limit
mongo --host $host_ip --port $host_port < mongoshard.js
sleep 20

# mongo ps results
echo $seperator "ps results" $seperator
sleep $sleep_limit
ps -ax | grep mongo