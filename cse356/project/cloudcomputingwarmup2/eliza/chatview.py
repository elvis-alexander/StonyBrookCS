from util import *
from chat import chat_response
from eliza.db.elizaDB import elizaDB
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt


@csrf_exempt
def doctor(request):
    convo_id = request.session.get('convo_id', '')
    # trying to talk to doctor w/o logging in first
    if convo_id == '':
        return err_response()
    username = request.session.get('username', '')
    human = request.POST.get('human', '')
    eliza_response = chat_response(human)
    # log human and eliza response
    db = elizaDB()
    db.log_talk(convo_id, username, eliza_response, human)
    return ok_response()


@csrf_exempt
def listconv(request):
    username = request.session.get('username', '')
    if username == '':
        return err_response()
    db = elizaDB()
    return JsonResponse(db.listconv(request.session.get('username', '')))


# @Todo should be marked private i think
@csrf_exempt
def getconv(request):
    # / getconv, {id:}
    convo_id = request.POST.get('id', '')
    # no post parameter
    if convo_id == '':
        return err_response()
    db = elizaDB()
    return JsonResponse(db.getconv(convo_id))