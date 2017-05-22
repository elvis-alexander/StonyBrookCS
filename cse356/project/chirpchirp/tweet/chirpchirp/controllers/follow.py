from .. utils import responses
from .. db.tweetdb import TweetDB
from .. models.followmodel import FollowModel
from django.views.decorators.csrf import csrf_exempt
from .. utils import auth


# /follow {username, follow=True}
@csrf_exempt
def follow(request):
    if(not auth.auth_session(request)):
        return responses.err_response("Please login before following");
    uid = request.session.get("uid", None)
    uname = request.session.get("uname", None)
    # get request model
    f_model = FollowModel(request=request)
    # connect to mongo
    db = TweetDB(follow=f_model)
    # perform database transaction
    transaction_status = db.follow_or_unfollow(uname)
    # close connection
    db.close()
    if(transaction_status):
        return responses.ok_response()
    return responses.err_response("follow or unfollow failed")


# /user/<username>
@csrf_exempt
def user(request, username):
    print 'follow.py(30)', username
    db = TweetDB()
    r = db.retrieve_user(username)
    db.close()
    if(r==None):
        return responses.err_response("User Not Found:"+username)
    return responses.returnresp(r)
#


# /user/<username>/followers @Change
@csrf_exempt
def followers(request, username):
    limit = int(request.GET.get('limit', 50))
    if limit > 200:
        limit = 200
    db = TweetDB()
    r = db.get_followers(username, limit)
    db.close()
    return responses.returnresp(r)


# /user/<username>/following
@csrf_exempt
def following(request, username):
    limit = int(request.GET.get('limit', 50))
    if limit > 200:
        limit = 200
    db = TweetDB()
    r = db.get_following(username, limit)
    db.close()
    return responses.returnresp(r)
