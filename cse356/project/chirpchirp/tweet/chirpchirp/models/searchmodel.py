import json
import time

# helper for a search query
class SearchModel:
    def __init__(self, request):
        body = request.body
        params = json.loads(body) # expects stringified json
        try:
            self.tweetstamp = int(params.get("timestamp"))
        except:
            self.tweetstamp = time.time()
        limit = int(params.get("limit", 25))
        # set default
        if limit > 100:
            limit = 100
        self.limit = int(limit)
        # search query, default to any string
        self.q = params.get("q", ".*")
        self.q = self.q.split(" ")
        # username
        self.username = params.get("username", None)
        # following param
        self.following = str(params.get("following", True)).lower()== 'true'
        # rank parameter
        self.rank = params.get("rank", "interest")
        # get optional parent parameter, default to None
        try:
            self.parent = params.get("parent")
        except:
            self.parent = None
        self.replies = params.get("replies",True)
        # cast to boolean, by checking if string is "false." If so, it sets a boolean False.
        if(not isinstance(self.replies, bool)):
            self.replies = not (str(self.replies).strip().lower() == "false")
        # print '=>model:', self.tweetstamp, self.limit, self.q, self.username, self.following
