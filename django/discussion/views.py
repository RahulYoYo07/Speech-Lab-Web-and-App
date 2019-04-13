from django.http import HttpResponse
from django.shortcuts import render
from django.utils.safestring import mark_safe
import json
from django.shortcuts import redirect

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from . import consumers

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

# db = firestore.client()

def course_group(request, CourseID, CourseGroupID):
    return render(request, 'discussion/group.html', {
        'CourseGroupID_json': mark_safe(json.dumps(CourseGroupID)),
        'CourseID_json': mark_safe(json.dumps(CourseID)),
    })

def group(request, CourseID, AssignmentID, GroupID):
    pass

def notice_board(request,CourseID):
    # CourseID = self.scope['url_route']['kwargs']['CourseID']
    doc_ref = db.collection(u'Courses').document(u'CS243').collection(u'Notices')
    # #
    all_notice=[]
    docs = list(doc_ref.get())
    for i in range(len(docs)):
        id = docs[i].id
        doc = docs[i]
        doc = doc.to_dict()
        temp={
            'NoticeHead' : doc['NoticeHead'],
            'NoticeBody': doc['NoticeBody'],
            # 'NoticeAuthor': doc['Author'],
            # 'NoticeTime': doc['NoticeTime'],
        }
        print(temp)
        all_notice.append(temp)
    print(CourseID)
    context={
        "all_notice":all_notice,
        "title":CourseID
    }
    return render(request,'discussion/notice.html',context)

def add_notice(request,CourseID):
    NoticeHead = request.POST['NoticeHead']
    if NoticeHead=="":
        NoticeHead="Notice"
    NoticeBody = request.POST['NoticeBody']
    print(NoticeHead)
    print(NoticeBody)
    doc_ref = db.collection(u'Courses').document(CourseID).collection(u'Notices').add({'Author' : 'Udbhav Chugh','NoticeHead' : NoticeHead, 'NoticeBody' : NoticeBody, 'NoticeTime':firestore.SERVER_TIMESTAMP})
    return redirect('/discussion/courses/'+CourseID+'/noticeboard')
