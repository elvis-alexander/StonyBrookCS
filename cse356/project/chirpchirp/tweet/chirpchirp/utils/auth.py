# inspect request object's session, if previously signed in.
def auth_session(req):
    if('uid' in req.session and 'uname' in req.session):
        return True
    return False
