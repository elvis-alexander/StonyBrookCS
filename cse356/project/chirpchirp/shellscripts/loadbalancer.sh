#!/usr/bin/env bash

# chmod +x loadbalancer.sh
# USAGE: ./loadbalancer.sh IP_ONE IP_TWO IP_THREE IP_FOUR

loadbalancer_conf="https://raw.githubusercontent.com/elvis-alexander/chirpchirp/master/nginxconfig/loadbalancer.conf"
seperator="---------------------"

# downloading nginx config
echo $seperator "Downloading nginx config" $seperator
sleep 2
wget $loadbalancer_conf

# setting nginx config file
echo $seperator "Setting up config file..." $seperator
sleep 2
sed -ie 's/IP_ONE/'$1'/g' loadbalancer.conf
sed -ie 's/IP_TWO/'$2'/g' loadbalancer.conf
sed -ie 's/IP_THREE/'$3'/g' loadbalancer.conf
sed -ie 's/IP_FOUR/'$4'/g' loadbalancer.conf

# install nginx
echo $seperator "Updating apt-get" $seperator
sleep 2
sudo apt-get update
echo $seperator "Installing Nginx" $seperator
sleep 2
sudo apt-get install nginx

# moving config to correct directory
echo $seperator "Connecting nginx to uwsgi" $seperator
sleep 2
sudo ln -s loadbalancer.conf /etc/nginx/sites-enabled/

# nginx status
# run nginx
echo $seperator "Restarting nginx" $seperator
sleep 2
sudo service nginx restart
echo $seperator "Nginx Status" $seperator
sleep 2
sudo service nginx status