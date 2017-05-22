import json

# used for loading a User object on a request
class UserModel:
    def __init__(self, request):
        body = request.body # expect stringified json
        params = json.loads(body)
        self.username = params.get("username", "")
        self.email = params.get("email", "")
        self.password = params.get("password", "")
        self.key = params.get("key", "")
        print self.email, self.password
