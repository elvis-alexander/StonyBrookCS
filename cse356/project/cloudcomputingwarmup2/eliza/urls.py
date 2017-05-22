from django.conf.urls import url
from eliza import loginview
from eliza import registrationview
from eliza import chatview

# verify sessions are appropriate and send err_msgs otherwise (logout,
# do I really wanna prompt error for basically nothing, ('not being logged on'))
urlpatterns = [
    # verified and tested
    url('^login', loginview.login, name='login'),
    url('^logout', loginview.logout, name='logout'),
    # verified and tested
    url('^adduser', registrationview.adduser, name='adduser'),
    url('^verify', registrationview.verify, name='verify'),
    # verified and tested
    url('^DOCTOR', chatview.doctor, name='doctor'),
    # verified and tested
    url('^listconv', chatview.listconv, name='listconv'),
    #
    url('^getconv', chatview.getconv, name='getconv'),

]


