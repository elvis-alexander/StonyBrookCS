import smtplib
from email.mime.text import MIMEText

from django.http import JsonResponse
from pymongo import MongoClient
from datetime import datetime
from bson.objectid import ObjectId
from django.utils import timezone
from django.core.mail import send_mail


class elizaDB:

    def __init__(self):
        self.client = MongoClient()
        self.db = self.client.eliza
        self.user_collection = self.db.User
        self.convo_collection = self.db.convo
        self.chat_collection = self.db.chat

    # insert disabled user
    def insert_disabled(self, post):
        key = self.user_collection.insert({
            'username': post.get('username', ''),
            'password': post.get('password', ''),
            'email': post.get('email', ''),
            'disabled': True
        })
        link = 'http://130.245.168.162/verify/?email=' + post.get('email', '') + '&key=' + str(key)
        # link = 'http://localhost:8000/verify/?email=' + post.get('email', '') + '&key=' + str(key)
        # msg = MIMEText(u'<a href="' + link + '">' + link + '</a>', 'html')
        # s = smtplib.SMTP("smtp.gmail.com", 25)
        # s.ehlo()
        # s.starttls()
        # s.login("fernandez.elvis.0819@gmail.com", "oct81995")
        # s.sendmail('fernandez.elvis.0819@gmail.com', 'eaf207@gmail.com', msg.as_string())
        send_mail(
            'Verify your account here',
            link,
            'fernandez.elvis.0819@gmail.com',
            ['eaf207@gmail.com'],
            fail_silently=False,
        )
        self.close()


    # verify user
    def verify_user(self, email, key):
        # make sure user is actually in the db
        if self.user_collection.find({'email': email}).count() == 0:
            self.close()
            return False
        # check for backdoor
        if key == 'abracadabra':
            # i guess make a check that this user actually exists
            self.user_collection.update_one({'email': email}, {"$set": {'disabled': False}}, upsert=False)
            self.close()
            return True
        exists = self.user_collection.find({'_id': ObjectId(key)}).count() > 0
        if exists:
            self.user_collection.update_one({'email': email, '_id': ObjectId(key)}, {"$set": {'disabled': False}}, upsert=False)
            self.close()
            return True
        self.close()
        return False

    # verify user username and password
    def success_login(self, post):
        username = post.get('username', '')
        password = post.get('password', '')
        # user can login if username and password matches and is not disabled
        exists = self.user_collection.find({'username': username, 'password': password, 'disabled': False}).count() > 0
        self.close()
        return exists

    # creates a conversation
    def createconvo(self, username):
        id = self.convo_collection.insert({
            'start_date': datetime.now(),
            'username': username
        })
        self.close()
        return str(id)

    # log's human response
    def log_talk(self, convo_id, username, eliza, human):
        d = datetime.now()
        self.chat_collection.insert({
            'username': username,
            'name': 'eliza',
            'text': eliza,
            'timestamp': d,
            'convo_id': convo_id
        })
        self.chat_collection.insert({
            'username': username,
            'name': 'human',
            'text': human,
            'timestamp': d,
            'convo_id': convo_id
        })
        self.close()

    #
    def listconv(self, username):
        convos = {'status': 'OK', 'conversations': []}
        # query all conversation
        for doc in self.convo_collection.find({'username': username}):
            # print doc
            convos['conversations'].append({
                'id': str(doc.get('_id')),
                'start_date': doc['start_date']
            })
        self.close()
        return convos

    def getconv(self, id):
        chats = {'status': 'OK', 'conversation': []}
        # return error if
        if self.chat_collection.find({'convo_id': id}).count() == 0:
            return {'status': 'ERROR'}

        for chat in self.chat_collection.find({'convo_id': id}):
            new_chat = {
                'timestamp': chat['timestamp'],
                'name': chat['name'],
                'text': chat['text']
            }
            chats['conversation'].append(new_chat)
        self.close()
        return chats

    # close connection with client
    def close(self):
        self.client.close()