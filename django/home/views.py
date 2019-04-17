from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.urls import reverse
from home.authhelper import get_signin_url, get_token_from_code, get_access_token
from home.outlookservice import get_me
import time
from django.views.decorators.csrf import csrf_exempt
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from course import views
from django.contrib import auth
from urllib.parse import quote, urlencode
# from django.auth import logout
import os

#cred = credentials.Certificate('./iitg-speech-lab-firebase-adminsdk-ggn1f-2f757184a1.json')
cred = credentials.Certificate({
    "type": "service_account",
    "project_id": "iitg-speech-lab",
    "private_key_id": "2f757184a13945f894a474a1f7f0ed15547d0781",
    "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCnGJ6a0ncoPF0w\nilyrOHSMNuRM7XK620f3/euPvayvIxC/Yad9CHUbieQiwnIJTknJv5T5aXZ2Brq8\nxr0tkxkjNnhzn8VYS2QjrWkgVHP80WNs/S4kpZp7E4G+kgzCVxi5Mu0Qd1onyiL9\nvY4X5I9PrP06foAl18FPDRSxKi7mBJf4enQcp6fvSASDbAKMoOlU4u1NVMbi50Nx\nUqJC0Hq1TogyKgFgDbSOSEdFjRHk53+G5Ved4QlTIEJCws6NHcLaek6YPyz80vp3\nB61+u4IvDsGEcITbkq1ayKwhCNlTG4A5ksxETOKAOC55njRjrVtjinxXXjqkrioG\n+CxIatGrAgMBAAECggEAAK/nuxBBgC9bXL886VFWnVr+bliNn7oWHi1zoggwJRo6\nT+cpZqi5vo6/Gut8x5AEWqmIhcwKuiqF6w/QKFdSA6SOMz+FcrsAoursIz9lqLT9\nuS2DWpA5xebLIkr8dXIhPmW4ttgezUoWAcAdTPjaJAQ8mFh702wDNf2CR8Y6IiUB\n/OECTrCGI6ED+5xvE8JevRE8HLksOv5crwKBoEa/yb65uDmVnFgHfISS3TkdRmpW\nFAHpPZ/1zsbgU3v4cXSQJsj5xuEk/vv5tamuvJtfyJ9MdEd2WDjN1zw2fpl/Fhgy\nC9/hED02Lu/lmLRNoU9IyUFSNAWVToWR6waobmAZ8QKBgQDaSvjCJfd0jDbd6nHX\nmuLIYU5vwEqWDO8lZljH8JBX9DDvXEzqKdRQS6kqdYu3hsKpIlJzDvqShVemfAda\ntdXNQXrqt8pPH4IVZVYj4raAIT4nXqGJDl4Mnayk/eM6YVhJQMIr/yW5EPfssHjz\nxZ0avYn2I0YBfMupn4JB12FZEQKBgQDD9bPc/uw1JDnj+9i9/xdsDbpQ8e8pjbA7\nIwSlzs6PhjVg1oZUjIhlKleBshqG/DKeBZfhrKGo7HsX3yXpOZcs1iVT3a50TW6h\n+CX/H3nzVXRjdEFUcWX2qwKjGagOeNtNtAR0dkluovHbkxiWAHXNMnyG0+HEN6zj\nlAi3lwCe+wKBgFSBr5mhjxGccmUorJe2C1NdcDsM6xL5wN7upzIH7ClQjF0tk00X\nkmzfTYb1aHhNADDv65FFXDW6zzrRSxuPx0wlrEsPiY9l+DsGNvm/e71QoTomhUyE\ntl4V8E8TRpNEOiRpoIHdzaG+cuw7SSe9+drvQ2h5MVHEGSf6azfIBJSxAoGBALrt\nQmnpcwEuUVq8/wAeugUFA1n7rxyAYD/JI8HXCQu4BmsduH4moGWAgoDhmJRzNwWu\naDeKKZuuGa2n284idab7kBf0O1oOEx7GS9iV+gq41ZGZcEhQ8+bdMmLLMpi7iNcS\nhb1iqKG1JelC5A0S20ymgEtNCuvWAEIHEFmw3ZLJAoGAVfK3ovsR3e0PptoJ27WS\neoFm1Fh2iXovrtOE23EONuakCy9aLmc9QfBq3TG+OJdKZXD+pe4xtRVvtipnbryU\nAzYz9k4p3cJHnnQwtHDakeaXvQXO960uM1C3VHn+XAVNd8tO3sUNyenpZzJPwcX2\no4Lsy7nKljoEGxCgS2I94mI=\n-----END PRIVATE KEY-----\n",
    "client_email": "firebase-adminsdk-ggn1f@iitg-speech-lab.iam.gserviceaccount.com",
    "client_id": "110868864917664596078",
    "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    "token_uri": "https://oauth2.googleapis.com/token",
    "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
    "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ggn1f%40iitg-speech-lab.iam.gserviceaccount.com"
})
# firebase_admin.initialize_app(cred)
db = firestore.client()

# Create your views here.


# Create your views here.


def home(request):
    redirect_uri = request.build_absolute_uri(reverse('home:gettoken'))
    sign_in_url = get_signin_url(redirect_uri)
    data = db.collection(u'Homepage').document("homepage").get().to_dict()
    context = {'sign_in_url': sign_in_url,
                'data' : data}
    context = loginFLOW(request, context)
    return render(request, 'home/home.html', context)

def faq(request):
    data = db.collection(u'Homepage').document("faq").get().to_dict()
    temp = [item for item in enumerate(data['qa'])]
    context = {'stuff' : temp}
    context = loginFLOW(request, context)
    return render(request, 'home/faq.html', context)

def contactus(request):
    data = db.collection(u'Homepage').document("contactUs").get().to_dict()
    context = loginFLOW(request, data)
    return render(request, 'home/contactus.html', context)

def addProject(request, uinfo):
    context = {}
    context = loginFLOW(request, context)
    return render(request, 'home/addproject.html', context)

def add(request, uinfo):
    context = {}
    if request.method == 'POST':
        context = loginFLOW(request, context)
        if context['username'] != uinfo:
            return HttpResponse("Error")
        
        username = context['username']
        project = {}
        project['Title'] = request.POST.get('title')
        project['AboutProject'] = request.POST.get('about')
        project['Creator'] = db.collection(u'Users').document(username)
        project['Media'] = request.POST.get('cmurl')
        project['Mentor'] = request.POST.get('mentors')
        project['People'] = request.POST.get('people')
        
        db.collection(u'Projects').add(project)

        return HttpResponseRedirect("/users/" + username)


def gettoken(request):
    # return HttpResponse('gettoken view')
    auth_code = request.GET['code']
    redirect_uri = request.build_absolute_uri(reverse('home:gettoken'))
    token = get_token_from_code(auth_code, redirect_uri)
    access_token = token['access_token']
    user = get_me(access_token)
    refresh_token = token['refresh_token']
    expires_in = token['expires_in']

    username = user['mail']
    username = username.replace("@iitg.ac.in", "")
    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()

    if user_dict is None:
        if user['jobTitle'].upper() is "BTECH" or "MTECH" or "PHD" or "BDES" or "MDES":
            des = "Student"
            data = {
                u'FullName': user['displayName'],
                u'Program': user['jobTitle'],
                u'Designation': des,
                u'About': "",
                u'Contact': "",
                u'Department': user["department"],
                u'ProfilePic': "https://firebasestorage.googleapis.com/v0/b/iitg-speech-lab.appspot.com/o/ProfileImages%2Fprofilepic.png?alt=media&token=bed0c911-cff9-4674-88db-defdca5942a4",
                u'RollNumber': user["surname"],
                u'URL': {
                    'Github': '',
                    'Homepage': '',
                    'Linkedin': ''
                },
                u'Email': user['mail']
            }
            db.collection(u'Users').document(username).set(data)
        else:
            des = "Faculty"
            data = {
                u'Name': user['displayName'],
                u'CollegeDesignation': user['jobTitle'],
                u'Designation': des,
                u'About': "",
                u'Contact': "",
                u'Department': user["department"],
                u'ProfilePic': "",
                u'RoomNumber': "",
                u'URL': {
                    'Github': '',
                    'Homepage': '',
                    'Linkedin': ''
                },
                u'Email': user['mail']
            }
            db.collection(u'Users').document(username).set(data)



    # expires_in is in seconds
    # Get current timestamp (seconds since Unix Epoch) and
    # add expires_in to get expiration time
    # Subtract 5 minutes to allow for clock differences
    expiration = int(time.time()) + expires_in - 300


    # Save the token in the session
    request.session['access_token'] = access_token
    request.session['refresh_token'] = refresh_token
    request.session['token_expires'] = expiration
    # request.session.flush()
    # auth.logout(request)
    # logout(request)

    # return HttpResponseRedirect("https://login.microsoftonline.com/common/oauth2/logout?post_logout_redirect_uri =http://localhost:8000/home/")
    # return HttpResponse('Roll {0}'.format(user['jobTitle']))
    return HttpResponseRedirect("/users/" + username)


def login(request):
    return render(request, 'home/home.html')

@csrf_exempt
def login_add(request):
    if request.method == 'POST':
        username = request.POST.get('uname')
        request.POST.get('psw')
        user_ref = db.collection(u'Users').document(username).get()
        user_dict = user_ref.to_dict()

        if user_dict is None:
            redirect_uri = request.build_absolute_uri(reverse('home:gettoken'))
            sign_in_url = get_signin_url(redirect_uri)
            return HttpResponseRedirect(sign_in_url)

    return HttpResponseRedirect('/home/')


def logout(request):
    request.session['access_token'] = None
    return HttpResponseRedirect(reverse('home:home'))


def ViewUser(request, uinfo):
    context = {}
    context = loginFLOW(request, context)

    if context['username'] != uinfo:
        return HttpResponse("Error")

    profile = db.collection(u'Users').document(context['username'])
    profile_dict = profile.get().to_dict()
    
    context['profile'] = profile_dict

    
    all_projects = db.collection(u'Projects').where(u'Creator', u'==', profile).get()
    project_list = []
    for x in all_projects:
        project = x.to_dict()
        project['id'] = x.id
        project_list.append(project)
    if 'isAdmin' in profile_dict:
        context["isAdmin"] = "True"
    else:
        context["isAdmin"] = "False"
    context['projects'] = project_list
    return render(request, 'home/profile.html', context)


def student(request):
    user_ref = db.collection(u'Users').get()
    user_list = []
    count = 0
    counter_list = []
    for user in user_ref:
        user_dict = user.to_dict()
        # print(user_dict["Designation"])
        if user_dict["Designation"] == 'Student':
            user_list.append(user_dict)

            counter_list.append(count % 3)
            count += 1
    uc_list = zip(user_list, counter_list)
    context = {'uc_list': uc_list}
    context = loginFLOW(request, context)
    return render(request, 'home/people.html', context)


def faculty(request):
    user_ref = db.collection(u'Users').get()
    user_list = []
    count = 0
    counter_list = []
    for user in user_ref:
        user_dict = user.to_dict()
        if user_dict["Designation"] == 'Faculty':
            user_list.append(user.to_dict())
            counter_list.append(count % 3)
            count += 1
    uc_list = zip(user_list, counter_list)
    context = {'uc_list': uc_list}
    context = loginFLOW(request, context)
    return render(request, 'home/people.html', context)


def projects(request):
    context = {}
    projects = db.collection(u'Projects').get()
    project_list = []
    for x in projects:
        project = x.to_dict()
        project['id'] = x.id
        project_list.append(project)

    context['projects'] = project_list
    context = loginFLOW(request, context)
    return render(request, 'home/projects.html', context)


def projectView(request, pinfo):
    project_ref = db.collection(u'Projects').document(pinfo).get()
    project_dict = project_ref.to_dict()
    
    context = {'Project': project_dict}
    context = loginFLOW(request, context)
    return render(request, 'home/viewProject.html', context)


def editProfile(request, uinfo):
    access_token = get_access_token(request, request.build_absolute_uri(reverse('home:gettoken')))
    if not access_token:
        return HttpResponse("You are not logged in")
    user = get_me(access_token)
    username = user['mail'].replace("@iitg.ac.in", "")
    if username != uinfo:
        return HttpResponse("Error")
    user = db.collection(u'Users').document(username).get()
    user_dict = user.to_dict()
    user_dict["id"] = user.id
    t = 0
    if user_dict["Designation"] == "Student":
        t = 1
    if user_dict["Designation"] == "Faculty":
        t = 0
    context = {'isStudent': t, 'user': user_dict}
    context = loginFLOW(request, context)
    if 'isAdmin' in user_dict:
        context["isAdmin"] = "True"
    else:
        context["isAdmin"] = "False"
    return render(request, 'home/editProfile.html', context)

#def editContact(request, uinfo):


#def editHome(request, uinfo):

#def editFaq(request, uinfo):



def edit(request, uinfo):
    if request.method == 'POST':
        access_token = get_access_token(request, request.build_absolute_uri(reverse('home:gettoken')))
        if not access_token:
            return HttpResponse("You are not logged in")
        user = get_me(access_token)
        username = user['mail'].replace("@iitg.ac.in", "")
        if username != uinfo:
            return HttpResponse("Error")
        user = db.collection(u'Users').document(username).get()
        user_dict = user.to_dict()
        user_dict["ProfilePic"] = request.POST.get('cmurl')
        user_dict["About"] = request.POST.get('about_me')
        user_dict["Contact"] = request.POST.get('contact')
        user_dict["URL"]["Github"] = request.POST.get('github')
        user_dict["URL"]["Linkedin"] = request.POST.get('linkedin')
        user_dict["URL"]["Homepage"] = request.POST.get('homepage')
        db.collection(u'Users').document(username).set(user_dict)
        return HttpResponseRedirect("/users/" + username)

def redirect_user(request):
    access_token = get_access_token(request, request.build_absolute_uri(reverse('home:gettoken')))
    if not access_token:
        return HttpResponse("You are not logged in")
    user = get_me(access_token)
    username = user['mail'].replace("@iitg.ac.in", "")
    return HttpResponseRedirect("/users/" + username)

def loginFLOW(request, context):
    redirect_uri = request.build_absolute_uri(reverse('home:gettoken'))
    sign_in_url = get_signin_url(redirect_uri)
    access_token = get_access_token(request, request.build_absolute_uri(reverse('home:gettoken')))
    if not access_token:
        context['username'] = ''
    else:
        user = get_me(access_token)
        username = user['mail'].replace("@iitg.ac.in", "")
        context['username'] = username

    context['sign_in_url'] = sign_in_url

    return context