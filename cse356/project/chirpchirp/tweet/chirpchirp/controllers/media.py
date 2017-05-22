from .. utils import responses
from django.views.decorators.csrf import csrf_exempt
from .. db.tweetdb import TweetDB
from django.http import HttpResponse

@csrf_exempt
def addmedia(request):
    f = request.FILES['content']
    db = TweetDB()
    mid = db.save_media(f)
    db.close()
    return responses.id_response(mid)

@csrf_exempt
def retrieve(request, id):
    db = TweetDB()
    content = db.get_media(id)
    db.close()
    if content == None:
        r = HttpResponse("Image does not exist")
        r["Content-Type"] = "application/json"
        return r
    r = HttpResponse(content)
    r["Content-Type"] = "image/png"
    return r
