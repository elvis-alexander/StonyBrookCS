# responsible for logging users in and out
from util import *
from django.template import loader
from django.http import JsonResponse
from eliza.db.elizaDB import elizaDB
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt


@csrf_exempt
def login(request):
    # log user in on a post request
    if request.method == 'POST':
        db = elizaDB()
        if db.success_login(request.POST):
            # makes sure user is not logging in again
            if request.session.get('username', '') == '':
                # log user in and log new convo
                request.session['username'] = request.POST.get('username', '')
                request.session['convo_id'] = elizaDB().createconvo(request.POST.get('username', ''))
                return ok_response()
    return err_response()


@csrf_exempt
def logout(request):
    try:
        del request.session['username']
        del request.session['convo_id']
    except KeyError:
        return ok_response()
    return ok_response()