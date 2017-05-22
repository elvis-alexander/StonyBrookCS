from flask import Flask, request
from os import urandom
from json import loads, dumps
import mysql.connector
import memcache

# set up flask
app = Flask(__name__)
app.secret_key = urandom(24)

config = {
    'user': 'best_user',
    'password': 'password',
    'host': '127.0.0.1',
    'database': 'hw7',
    'raise_on_warnings': True,
    'use_pure': False,
}


@app.route("/hw7", methods=["POST"])
def hw7():
    params = loads(request.data)
    state = params.get("state", "")
    service_type = params.get("service_type", "")
    mc = memcache.Client(['127.0.0.1:11211'], debug=0)
    key = state + "|" + service_type
    value = mc.get(key)
    # if cache miss go to mysql
    if not value:
        connection = mysql.connector.connect(**config)
        cursor = connection.cursor()
        query = "SELECT AVG(comm_rate), AVG(ind_rate), AVG(res_rate) FROM electric WHERE state=%s AND service_type=%s"
        cursor.execute(query, (state, service_type))
        results = cursor.fetchone()
        avg_comm_rate = results[0]
        avg_ind_rate = results[1]
        avg_res_rate = results[2]
        connection.close()
    else:
        tokens = value.split("|")
        avg_comm_rate = float(tokens[0])
        avg_ind_rate = float(tokens[1])
        avg_res_rate = float(tokens[2])
    output = {
        "status": "OK",
        "comm_rate_avg": avg_comm_rate,
        "ind_rate_avg": avg_ind_rate,
        "res_rate_avg": avg_res_rate
    }
    return app.response_class(
        response=dumps(output),
        status=200,
        mimetype="application/json"
    )
