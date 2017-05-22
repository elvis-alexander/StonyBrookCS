import mysql.connector
from sys import argv

config = {
  'user': 'best_user',
  'password': 'password',
  'host': '127.0.0.1',
  'database': 'hw7',
  'raise_on_warnings': True,
  'use_pure': False,
}
connection = mysql.connector.connect(**config)
cursor = connection.cursor()
index = 0
data_file = open(argv[1])
lines = data_file.readlines()
for line in lines[1:len(lines)]:
    if line == '':
        continue
    line = line.replace("\r\n", "")
    tokens = line.split(',')
    added_index = 0
    if line.find("Duke Energy Carolinas, LLC") != -1:
        added_index = 1
    elif line.find("Duke Energy Florida, Inc") != -1:
        added_index = 1
    elif line.find("ALLETE, Inc.") != -1:
        added_index = 1
    elif line.find("UGI Utilities, Inc") != -1:
        added_index = 1
    elif line.find("UNS Electric, Inc") != -1:
        added_index = 1
    state = tokens[3 + added_index]
    service_type = tokens[4 + added_index]
    comm_rate = float(tokens[6 + added_index])
    ind_rate = float(tokens[7 + added_index])
    res_rate = float(tokens[8 + added_index])
    cursor.execute(
        """INSERT INTO electric (state, service_type, comm_rate, ind_rate, res_rate) VALUES (%s,%s,%s,%s,%s)""",
        (state, service_type, comm_rate, ind_rate, res_rate))
connection.commit()
connection.close()



