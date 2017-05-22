# cloudcomputinghw4


Homework #4 (cassandra)<br/>
Step 1: Install Cassandra<br/>
Step 2: Create “hw4” keyspace (replication factor 1)<br/>
Step 3: Create a table “imgs” that includes a filename (string) and contents (blob) columns<br/>
Step 4: Create a POST form target<br/>
/deposit { filename: (type=text), contents: (type=file) }<br/>
Uploaded files should be deposited into hw4/imgs in Cassandra<br/>
Step 5: Create a GET service<br/>
/retrieve { filename: }<br/>
to get the previously uploaded image (make sure to respond with the appropriate image/… content type)
(note: use Cassandra 2.2 (22x) for this homework)<br/>

Install <a href="http://cassandra.apache.org/download/">Cassandra</a>
```
echo "deb http://www.apache.org/dist/cassandra/debian 22x main" | sudo tee -a /etc/apt/sources.list.d/cassandra.sources.list
curl https://www.apache.org/dist/cassandra/KEYS | sudo apt-key add -
sudo apt-get update
sudo apt-get install cassandra
```

Use the cassandra shell
```
> cqlsh
> CREATE KEYSPACE hw4 WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;
> DESCRIBE hw4
> SELECT * FROM system.schema_keyspaces
> USE hw4
> CREATE TABLE imgs_(filename_ text PRIMARY KEY, contents blob);
> SELECT * FROM imgs;
```


