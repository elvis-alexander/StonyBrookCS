from flask import Flask, current_app, request, render_template, url_for, make_response
from flask_restful import Resource, Api
from werkzeug.utils import secure_filename
from cassandra.cluster import Cluster
import os

UPLOAD_FOLDER = '/root/hw4/hw4env/restapi/deposits'
app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = 735344
api = Api(app)


#/deposit { filename: (type=text), contents: (type=file) }
class Deposit(Resource):
	# save image on cassandra
	def post(self):
		try:
			filename = request.form['filename']
			file = request.files['contents']
			# save locally
			file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
			fpath = UPLOAD_FOLDER + "/" + filename
			# open for bytes
			f = open(fpath, "rb")
			fbytes = bytearray(f.read())
			f.close()
			# close for bytes
			cluster = Cluster()
			session = cluster.connect("hw4")
			session.execute("INSERT INTO imgs (filename, contents) VALUES (%s, %s)",
				(filename, fbytes,)
			)
			return {"status": "success"}
		except Exception as e:
			return {"status": "failure", "errmsg": e}
	# render static page (allow users to submit images)
	def get(self):
		return current_app.send_static_file('index.html')


# /retrieve { filename: }
class Retrieve(Resource):
	def get(self):
		try:
			fname_ = request.form['filename']
			t = "image/jpg"
			if fname_.endswith("png"):
				t = "image/png"
			if fname_.endswith("jpeg"):
				t = "image/jpeg"
			cluster = Cluster()
			session = cluster.connect("hw4")
			r = session.execute("SELECT * FROM imgs WHERE filename=%s", (fname_,))
			image_binary = r[0].contents
			response = make_response(image_binary)
			response.headers['Content-Type'] = t
			return response
		except Exception as e:
			return {"status": "failure", "errmsg":e}
			

api.add_resource(Deposit, '/deposit')
api.add_resource(Retrieve, '/retrieve')
if __name__ == '__main__':
    app.run(debug=True)
#/retrieve { filename:}


