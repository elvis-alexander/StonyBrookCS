# chirpchirp

#dependencies before running:
memcached
mongodb
nginx(production)

# PRODUCTION vs DEV
In settings tweet folder /tweet/settings.py
In application chirpchirp folder /chirpchirp/config/settings.py
Change the variable PRODUCTION = True or False for both files.


uname -n | sudo tee /usr/share/nginx/html/index.html

ps -ax | grep uwsgi<br>
#####################################<br>
loadbalancer ip: {130.245.169.41} {mongos: port: {27017}, memcached: port: {11211}}

config ip: {192.168.1.49} port: {27030}

shard1 ip: {192.168.1.45} port: {27040}
shard2 ip: {192.168.1.46} port: {27040}
shard3 ip: {192.168.1.47} port: {27040}
shard4 ip: {192.168.1.48} port: {27040}

appserver1 ip: {192.168.1.55}
appserver2 ip: {192.168.1.51}
appserver3 ip: {192.168.1.52}
appserver4 ip: {192.168.1.53}

sudo killall uwsgi

@TODO shouldnt return media array on /item if no media loaded in the first place


<!---->
Things to question:<br>
If i delete a retweet should the number of retweets for that tweet go down by one


/etc/sysctl.conf
fs.file-max = 500000
sudo sysctl --system


ulimit -f unlimited -t unlimited -v unlimited -n 64000 -m unlimited -u 64000
ulimit -f
sysctl -a

http://stackoverflow.com/questions/22697584/nginx-uwsgi-104-connection-reset-by-peer-while-reading-response-header-from-u
