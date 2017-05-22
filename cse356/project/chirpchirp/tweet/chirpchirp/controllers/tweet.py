from .. utils import responses
from .. db.tweetdb import TweetDB
from .. models.tweetmodel import TweetModel
from .. models.searchmodel import SearchModel
from .. models.likemodel import LikeModel
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
from .. utils import auth

# @Todo implement retweet :)
# creates a new tweet {content}
@csrf_exempt
def additem(request):
    if not auth.auth_session(request):
        print 'uhoh not logged in ->', request.session
        return responses.err_response("Please login before adding item")
    uname = request.session.get("uname","")
    uid = request.session.get("uid","")
    t = TweetModel(uname, uid, request)
    db = TweetDB(tweet=t)
    tid = db.post_tweet()
    if tid == None:
        db.close()
        return responses.err_response("Cannot retweet a non-existent tweet.")
    print 'success tweet: tid=>', tid
    db.close()
    return responses.id_response(tid)


# /item/<id> retrieve/delete
@csrf_exempt
def item(request, id):
    db = TweetDB()
    if request.method == "DELETE":
        delete_response = db.delete_tweet(id)
        db.close()
        if delete_response == True:
            return HttpResponse(status=200)
        return HttpResponse(status=400)
    # insert tweet on POST request
    r = db.retrieve_tweet(id)
    db.close()
    return responses.returnresp(r)

# /item/<id>/like
@csrf_exempt
def like(request, id):
    # get current model
    lmodel = LikeModel(request)
    # get current user
    uid = request.session.get("uid", "")
    db = TweetDB(like=lmodel)
    # likes or unlike a given tweet
    db.like_tweet(id, uid)
    return responses.ok_response()


# {timestamp, limit}
@csrf_exempt
def search(request):
    if not auth.auth_session(request):
        return responses.err_response("Please log in before using search")
    uname = request.session.get("uname", "")
    tsearch = SearchModel(request)
    db = TweetDB(search=tsearch)
    r = db.tweetsearch(loggedin_username=uname)
    db.close()
    return responses.returnresp(r)
