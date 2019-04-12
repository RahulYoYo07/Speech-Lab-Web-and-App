from django.shortcuts import render
from django.http import HttpResponseRedirect
from django.urls import reverse
# import firebase_admin
# from firebase_admin import credentials
# from firebase_admin import firestore

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

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
firebase_admin.initialize_app(cred)
db = firestore.client()

# Create your views here.


def dashboard(request):
    username = "gulat170123030"
    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']

    if Designation == "Faculty" :
        ProfCourseList = user_dict['ProfCourseList']
        CourseDetails = []
        for course in ProfCourseList:
            CourseDetails.append(course.get().to_dict())
            print(course.get())

        context = {
            'CourseDetails' : CourseDetails
        }

        return render(request,'course/main_page.html',context)

    elif Designation == "Student" :
        StudCourseList = user_dict['CourseList']
        #print(StudCourseList)
        RegisteredCourses = []
        TotalCourses = []
        for course in StudCourseList:
            RegisteredCourses.append(course['CourseID'].get().to_dict())

        Courses = db.collection(u'Courses').get()
        for course in Courses :
            TotalCourses.append(course.to_dict())

        context = {
            'RegisteredCourses' : RegisteredCourses,
            'TotalCourses' : TotalCourses
        }

        return render(request,'course/main_page_stud.html',context)

def Enroll_CoursePage(request, cinfo):
    username = "avira170101014"
    if request.method == 'POST':
        postdata = request.POST.get("Enrollment")
        main = db.collection(u'Courses').document(cinfo).get().to_dict()
        dbdata = main['EnrollmentKey']
        if dbdata == postdata :
            data = {
                u'CourseList' : {
                    u'CourseID' :  db.collection(u'Courses').document(cinfo)
                }
            }
            # db.collection(u'Users').document(username).add(data)
        return HttpResponseRedirect(reverse('course:dashboard'))
    else :
        return render(request,'course/enrollcourse.html')


def AddCourse(request):
    username = "pradip"
    if request.method == 'POST':
        ref_prof = db.collection(u'Users').document(username)
        data = {
            u'AboutCourse': request.POST.get("AboutCourse", ""),
            u'CourseID': request.POST.get("CourseID", ""),
            u'CourseName': request.POST.get("CourseName", ""),
            u'Weightage': int(request.POST.get("Weightage", "")),
            u'EnrollmentKey': request.POST.get("EnrollmentKey", ""),
            u'EndSemester': {
                u'SemesterType':  request.POST.get("EndSemesterType", ""),
                u'Session': int(request.POST.get("EndSemesterSession", ""))
            },
            u'StartSemester': {
                u'SemesterType':  request.POST.get("StartSemesterType", ""),
                u'Session': int(request.POST.get("StartSemesterSession", ""))
            },
            u'FacultyList': [ref_prof],
            u'CourseInfo': request.POST.get("CourseID", "") + "_" + username + "_" + request.POST.get("StartSemesterSession", "")
        }
        cid = request.POST.get("CourseID", "")

        cinfo = cid + "_" + username + "_" + \
            request.POST.get("StartSemesterSession", "")

        db.collection(u'Courses').document(cinfo).set(data)

        course_ref = db.collection(u'Courses').document(cinfo)

        prof_data = db.collection(u'Users').document(username).get().to_dict()
        CoursesList = prof_data['ProfCourseList']
        CoursesList.append(course_ref)

        db.collection(u'Users').document(username).update({
            u'ProfCourseList': CoursesList
        })

        # return render(request,'course/main_page.html')
        return HttpResponseRedirect(reverse('course:dashboard'))

    return render(request, 'course/addcourseform.html')

def ViewCourse(request, cinfo):

    username = "pradip"
    #cid += "_" + username + " _" + cyear
    assgn_ref = db.collection(u'Courses').document(
        cinfo).collection(u'Assignments').get()
    AssgnDetails = []
    for assgn in assgn_ref:
        AssgnDetails.append(assgn.to_dict())
    context = {
        'AssgnDetails': AssgnDetails,
        'CourseInfo': cinfo
    }
    return render(request, 'course/viewcourse.html', context)

def AddAssgn(request, cinfo):
    username = "pradip"
    if request.method == 'POST':
        ref_prof = db.collection(u'Users').document(username)
        data = {
            u'About' : request.POST.get("About",""),
            u'AssignmentID' : request.POST.get("AssignmentID",""),
            u'Deadline' : request.POST.get("Deadline",""),
            u'Name' : request.POST.get("Name",""),
        }
        aid = request.POST.get("AssignmentID","")
        db.collection(u'Courses').document(cinfo).collection(u'Assignments').document(aid).set(data)

        return render(request,'course/main_page.html')
        #return HttpResponseRedirect(reverse('course:dashboard'))

    return render(request, 'course/addassgnform.html')



def ViewAssgn(request, cinfo, aid ):
    username = "pradip"
    #cid += "_" + username + " _" + cyear
    group_ref = db.collection(u'Courses').document(cinfo).collection(
        u'Assignments').document(aid).collection(u'Groups').get()
    GroupDetails = []
    for group in group_ref:
        GroupDetails.append(group.to_dict())
    context = {
        'GroupDetails': GroupDetails,
    }
    return render(request, 'course/viewassgn.html', context)


def AddTA(request, cinfo):
    return render(request, 'course/AddTA.html')
