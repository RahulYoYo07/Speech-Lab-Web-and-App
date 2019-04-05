from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.urls import reverse
from home.authhelper import get_signin_url, get_token_from_code, get_access_token
from home.outlookservice import get_me
import time

# Create your views here.


def home(request):
    redirect_uri = request.build_absolute_uri(reverse('home:gettoken'))
    sign_in_url = get_signin_url(redirect_uri)
    return HttpResponse('<a href="' + sign_in_url +'">Click here to sign in</a>')


def gettoken(request):
    # return HttpResponse('gettoken view')
    auth_code = request.GET['code']
    redirect_uri = request.build_absolute_uri(reverse('home:gettoken'))
    token = get_token_from_code(auth_code, redirect_uri)
    access_token = token['access_token']
    user = get_me(access_token)
    refresh_token = token['refresh_token']
    expires_in = token['expires_in']

    # expires_in is in seconds
    # Get current timestamp (seconds since Unix Epoch) and
    # add expires_in to get expiration time
    # Subtract 5 minutes to allow for clock differences
    expiration = int(time.time()) + expires_in - 300

    # Save the token in the session
    request.session['access_token'] = access_token
    request.session['refresh_token'] = refresh_token
    request.session['token_expires'] = expiration
    return HttpResponse('User: {0}, Access token: {1}'.format(user['displayName'], access_token))
