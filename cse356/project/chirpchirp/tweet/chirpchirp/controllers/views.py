from django.shortcuts import render
from .. utils import auth
from .. db.tweetdb import TweetDB

# For unauthenticated users, redirect to standard index page.
def defaultpage(request):
    return render(request, "chirpchirp/index.html")


# Page used to render profile cards of other users to follow.
def userpage(request):
    if not auth.auth_session(request):
        return defaultpage(request)
    username = request.session.get("uname","")

    return render(request,"chirpchirp/userpage.html",{"username":username})



# Page used to render profile cards of other users to follow.
def followpage(request):
    if not auth.auth_session(request):
        return defaultpage(request)
    return render(request,"chirpchirp/followpage.html")


# Page used to search for a specific tweet
def tweetsearchpage(request):
    if not auth.auth_session(request):
        return defaultpage(request)
    return render(request,"chirpchirp/tweetsearchpage.html")


# search user profiles from this page, anyone can access.
# /profile
def searchprofilepage(request):
    return render(request,"chirpchirp/profilepage.html")


def homepage(request):
    if not auth.auth_session(request):
        return defaultpage(request)
    # render template based on user's data
    username = request.session.get("uname", "")
    db = TweetDB()
    results = db.retrieve_user(username)
    follower_count, following_count = 0,0
    if(results):
        follower_count = results.get("user").get("followers")
        following_count = results.get("user").get("following")
    context = {
        'username': username,
        'follower_count':follower_count,
        'following_count':following_count
    }
    return render(request, "chirpchirp/homepage.html", context)

#send user to login page
def index(request):
    if(not auth.auth_session(request)):
        return defaultpage(request)
    # foreward to homepage rendering.
    return homepage(request)
