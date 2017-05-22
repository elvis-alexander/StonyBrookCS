# cloudcomputinghw2
Step 1: install mongodb<br>
Step 2: create a database called “hw2”<br>
Step 3: create a collection called “factbook”<br>
Step 4: populate the collection with data from https://github.com/opendatajson/factbook.json <br>

Set up for MongoDB
```
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 0C49F3730359A14518585931BC711F9BA15703C6
sudo echo "deb [ arch=amd64 ] http://repo.mongodb.org/apt/ubuntu trusty/mongodb-org/3.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.4.list
sudo apt-get update
sudo apt-get install -y mongodb-org
```

Start Mongo Database Service
```
service mongod start
```


Start mongo shell and create db
```
$ mongo
use hw2
hw2.createCollection('factbook')
```

Set up for Python script

```
Python 2.x
apt-get update
apt-get install python-pip
pip install pymango
pip install gitpython
```

Run
```
python mongo_scrap.py
```
