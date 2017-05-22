# responsible for creating and verifying users
from util import *
from django.template import loader
from eliza.db.elizaDB import elizaDB
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt


@csrf_exempt
def adduser(request):
    # @Todo send email verify here
    db = elizaDB()
    db.insert_disabled(request.POST)
    return ok_response()

@csrf_exempt
def verify(request):
    email = ''
    key = ''
    # verified using a post request, backdoor most likely
    if request.method == 'POST':
        email = request.POST.get('email', '')
        key = request.POST.get('key', '')
    else:
        # most likely for email
        email = request.GET.get('email', '')
        key = request.GET.get('key', '')
    db = elizaDB()
    valid = db.verify_user(email, key)
    if valid:
        return ok_response()
    return err_response()

