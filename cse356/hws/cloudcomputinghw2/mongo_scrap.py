from os import path, listdir
from shutil import rmtree
from git import Repo
from pymongo import MongoClient
import json


# remove directory if exists
dir = './factbook'
git_dir = '.git'
meta_dir = 'meta'
git_url = 'https://github.com/opendatajson/factbook.json'
if path.exists(dir):
    rmtree(dir)
# clone repo
Repo.clone_from(git_url, dir)
# get mongo client
client = MongoClient()
db = client.hw2
# find json directories
for d in listdir(dir):
    if d == git_dir or d == meta_dir:
        continue
    dir_path = "{}/{}".format(dir, d)
    if path.isdir(dir_path):
        # insert into db
        for json_file in listdir(dir_path):
            json_dir = "{}/{}".format(dir_path, json_file)
            # print json_dir
            s = open(json_dir).read()
            if json_file == 'nh.json':
                s = s.replace('.highest court(s)', 'highest court(s)')
            parsed = json.loads(s)
            try:
                db.factbook.insert(parsed)
            except Exception:
                print(json_dir)