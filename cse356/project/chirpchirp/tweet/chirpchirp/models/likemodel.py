import json

class LikeModel:
    def __init__(self, request):
        body = request.body
        params = json.loads(body)
        self.like = str(params.get("like", True)).lower() == 'true'