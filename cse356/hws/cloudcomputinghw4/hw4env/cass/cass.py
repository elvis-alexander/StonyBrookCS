from cassandra.cluster import Cluster

cluster = Cluster()
session = cluster.connect('hw4_')
#session.execute("INSERT INTO imgs_ (filename_, contents_) VALUES (%s, %s)", ("f1.txt","image_bytes",))
#session.execute("INSERT INTO imgs_ (filename_) VALUES (%s)", ("f2.txt",))

# retrieve blob
fname_ = "f2.txt"
r = session.execute("SELECT * FROM imgs_ WHERE filename_=%s", (fname_,))

try:
	print r[0].filename_
except:
	print 'not found'
