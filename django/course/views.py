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
    username = "pradip"
    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']

    if Designation == "Faculty":
        ProfCourseList = user_dict['ProfCourseList']
        CourseDetails = []
        for course in ProfCourseList:
            CourseDetails.append(course.get().to_dict())
            print(course.get())

        context = {
            'CourseDetails': CourseDetails
        }

        return render(request, 'course/main_page.html', context)

    elif Designation == "Student":
        StudCourseList = user_dict['CourseList']
        # print(StudCourseList)
        RegisteredCourses = []
        TotalCourses = []
        for course in StudCourseList:
            RegisteredCourses.append(course['CourseID'].get().to_dict())

        Courses = db.collection(u'Courses').get()
        for course in Courses:
            TotalCourses.append(course.to_dict())

        context = {
            'RegisteredCourses': RegisteredCourses,
            'TotalCourses': TotalCourses
        }

        return render(request, 'course/main_page_stud.html', context)


def Enroll_CoursePage(request, cinfo):
    username = "ravi170101053"
    if request.method == 'POST':
        postdata = request.POST.get("Enrollment")
        main = db.collection(u'Courses').document(cinfo).get().to_dict()
        copydata = db.collection(u'Users').document(
            username).get().to_dict()['couseList']
        print(copydata)
        datay = {
            u'CourseID':  db.collection(u'Courses').document(cinfo)
        }
        copydata.append(datay)
        print(copydata)
        dbdata = main['EnrollmentKey']
        if dbdata == postdata:
            data = {
                u'couseList': copydata
            }
            updateit = db.collection(u'Users').document(username)
            updateit.update(data)

        return HttpResponseRedirect(reverse('course:dashboard'))
    else:
        return render(request, 'course/enrollcourse.html')


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
            u'About': request.POST.get("About", ""),
            u'AssignmentID': request.POST.get("AssignmentID", ""),
            u'Deadline': request.POST.get("Deadline", ""),
            u'Name': request.POST.get("Name", ""),
        }
        aid = request.POST.get("AssignmentID", "")
        db.collection(u'Courses').document(cinfo).collection(
            u'Assignments').document(aid).set(data)

        return render(request, 'course/main_page.html')
        # return HttpResponseRedirect(reverse('course:dashboard'))

    return render(request, 'course/addassgnform.html')


def viewTA(request, cinfo):
    username = 'pradip'
    TAList = db.collection(u'Courses').document(
        cinfo).get().to_dict()["TAList"]

    talist = list()

    for TA in TAList:
        talist.append(TA.get().to_dict())

    context = {
        'TA': talist,
        'CourseInfo': cinfo
    }

    return render(request, 'course/viewTA.html', context)


def ViewAssgn(request, cinfo, aid):
    username = "pradip"
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
    username = "pradip"
    BTech, MTech, Phd = getStudents()
    context = {
        'BTech': BTech,
        'MTech': MTech,
        'Phd': Phd,

    }

    if request.method == 'POST':
        ref_course = db.collection(u'Courses').document(cinfo)
        stuType = request.POST.get("TA_BRANCH", "")
        stuID = request.POST.get(stuType+"TA", "")
        stuID = stuID.split('-')[1]
        ref_TA = db.collection(u'Users').document(stuID)
        currTA = ref_course.get().to_dict()['TAList']
        if ref_TA not in currTA:
            currTA.append(ref_TA)
        else:
            # show error message or resolve before hand
            pass

        # aid = request.POST.get("AssignmentID", "")
        data = {
            u'TAList': currTA
        }
        db.collection(u'Courses').document(cinfo).update(data)

        return render(request, 'course/AddTA.html', context)

    return render(request, 'course/AddTA.html', context)


def getStudents():

    users_ref = db.collection(u'Users').get()
    BTech = []
    MTech = []
    Phd = []

    for user in users_ref:
        userdict = user.to_dict()

        # Remove the first if condition
        if "Designation" in userdict.keys() and userdict["Designation"] == "Student":

            if userdict["Program"] == "Btech":
                BTech.append(userdict)

            if userdict["Program"] == "Mtech":
                MTech.append(userdict)

            if userdict["Program"] == "Phd":
                Phd.append(userdict)

    return BTech, MTech, Phd

def AddCourseMaterial(request, cinfo):
    context = {
        'CourseInfo': cinfo,
    }
    return render(request, 'course/addcoursematerial.html', context)

def ViewCourseMaterial(request , cinfo):
    course_data = db.collection(u'Courses').document(cinfo).get().to_dict()

    CMaterials = course_data['CourseMaterial']

    context = {
        'CMaterials': CMaterials,
        'CourseInfo': cinfo
    }
    return render(request, 'course/viewcoursematerial.html', context)

def StoreCMinDb(request, cinfo):
    if request.method == 'POST':
        course_data = db.collection(u'Courses').document(cinfo).get().to_dict()
        newcm={}
        newcm['Name'] = request.POST.get('filename')
        newcm['Url'] = request.POST.get('cmurl')

        if not 'CourseMaterial' in course_data:
            cm_array=[ newcm ]
            db.collection(u'Courses').document(cinfo).update({
                u'CourseMaterial' : cm_array
            })
        else:
            cm_array = course_data['CourseMaterial']
            cm_array.append(newcm)
            db.collection(u'Courses').document(cinfo).update({
                u'CourseMaterial': cm_array
            })
        # print(newcm)
        # print(cm_array)


        return HttpResponseRedirect(reverse('course:view_course_material', kwargs={'cinfo':cinfo}))
    else:
        return HttpResponseRedirect(reverse('course:view_course_material', kwargs={'cinfo':cinfo}))
