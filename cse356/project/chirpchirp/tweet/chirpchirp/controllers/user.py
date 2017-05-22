from .. utils import responses
from .. db.tweetdb import TweetDB
from .. models.usermodel import UserModel
from django.views.decorators.csrf import csrf_exempt


# /adduser {username, email, password}
# no bad result maybe if username already exists
@csrf_exempt
def adduser(request):
    u = UserModel(request)
    db = TweetDB(user=u)
    # actually add user here
    success = db.insert_disable()
    db.close()
    if(success):
        return responses.ok_response()
    else:
        return responses.err_response("username or email already exists")


# /verify {email, key}
# no check to see if a user with this email
@csrf_exempt
def verify(request):
    u = UserModel(request)
    db = TweetDB(user=u)
    success = db.verify_user()
    db.close()
    if success:
        return responses.ok_response()
    else:
        return responses.err_response("User does not exist")


# login resource (username, password)
@csrf_exempt
def login(request):
    # used to login
    u = UserModel(request)
    db = TweetDB(user=u)
    # verify user and account details
    if db.is_verified() == False:
        db.close()
        return responses.err_response("Not Verified")
    # store cookie/session
    request.session["uid"] = db.get_uid()
    request.session["uname"] = u.username
    db.close()
    return responses.redirect_ok_response("/homepage")


# logout {}
@csrf_exempt
def logout(request):
    try:
        del request.session['uname']
        del request.session['uid']
        return responses.redirect_ok_response('/')
    except KeyError:
        return responses.err_response("Please login, before logging out")
