
Homework #7 (mysql, memcached)
Due: 4/11
Step 1: Install a mysql variant (mysql, maria, percona, …)
Step 2: Create a database called “hw7”
Step 3: Create a table called “electric” and import U.S. Electric Utility Companies and Rates: Look-up by Zipcode (2013) IOU rates into it (https://catalog.data.gov/dataset)
Step 4: Create a REST service to access the data and return the averages across all matching ZIP codes
/hw7 { state:, service_type: }
to get { status: “OK”, comm_rate_avg:, ind_rate_avg:, res_rate_avg: }
Step 5: Install memcached
Step 6: Integrate memcached caching to speed up the REST-based service
```
apt-get update
apt-get dist-upgrade
apt-get install mysql-server
```


```
pip install mysql-connector==2.1.4
pip install python-memcached
```


```
mysql -u root -p
create database hw7;
grant all on hw7.* to 'best_user' identified by 'password';
```

```
mysql -u best_user -p;
use hw7;
CREATE TABLE electric (state TEXT, service_type TEXT, comm_rate DOUBLE, ind_rate DOUBLE, res_rate DOUBLE, id INT AUTO_INCREMENT, PRIMARY KEY (id));
show tables;
select * from electric;
```


```
python loaddb.py <filepath>
```


```
SELECT * FROM electric WHERE state="PA"; => 4328
SELECT * FROM electric WHERE state="PA" AND service_type="Delivery"; => 2162
SELECT * FROM electric WHERE state="PA" AND service_type="Bundled"; => 2166
SELECT AVG(comm_rate), AVG(ind_rate), AVG(res_rate) FROM electric WHERE state="PA" AND service_type="Bundled";
+---------------------+--------------------+---------------------+
| AVG(comm_rate)      | AVG(ind_rate)      | AVG(res_rate)       |
+---------------------+--------------------+---------------------+
| 0.10699368946895754 | 0.0887677925284764 | 0.12306440880594818 |
+---------------------+--------------------+---------------------+
```


```

Run flask 
export FLASK_APP=app.py
flask run --host=0.0.0.0
```
