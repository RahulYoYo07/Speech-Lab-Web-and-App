from django.shortcuts import render, redirect
from django.http import HttpResponseRedirect, HttpResponse, HttpResponseNotFound

from django.urls import reverse
from django.contrib import messages

# import firebase_admin
# from firebase_admin import credentials
# from firebase_admin import firestore

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

import random

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
from home.authhelper import loginFLOW

def dashboard(request):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']

    if Designation == "Faculty":
        ProfCourseList = user_dict['ProfCourseList']
        CourseDetails = []
        for course in ProfCourseList:
            CourseDetails.append(course.get().to_dict())
            print(course.get())

        context['CourseDetails'] = CourseDetails

        return render(request, 'course/main_page.html', context)

    elif Designation == "Student":
        if not 'CourseList' in user_dict:
            StudCourseList = []
        else:
            StudCourseList = user_dict['CourseList']
        # print(StudCourseList)
        RegisteredCourses = []
        TotalCourses = []
        for course in StudCourseList:
            RegisteredCourses.append(course['CourseID'].get().to_dict())

        Courses = db.collection(u'Courses').get()
        for course in Courses:
            TotalCourses.append(course.to_dict())

        context['RegisteredCourses'] = RegisteredCourses
        context['TotalCourses'] = TotalCourses

        return render(request, 'course/main_page_stud.html', context)


def Enroll_CoursePage(request, cinfo):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']
    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']

    context['CourseInfo'] = cinfo

    if request.method == 'POST':
        postdata = request.POST.get("Enrollment")
        main = db.collection(u'Courses').document(cinfo).get().to_dict()

        try:
            copydata = db.collection(u'Users').document(
                username).get().to_dict()['CourseList']
        except:
            copydata = []

        datay = {
            u'CourseID':  db.collection(u'Courses').document(cinfo)
        }
        copydata.append(datay)

        atndata = {
            u'StudentID': db.collection(u'Users').document(username),
            u'TotalAttendance': 0
        }

        try:
            attendance_list = db.collection(u'Courses').document(
                cinfo).get().to_dict()['AttendanceList']
        except:
            attendance_list = []

        attendance_list.append(atndata)

        dbdata = main['EnrollmentKey']

        try:
            stn_list = db.collection(u'Courses').document(
                cinfo).get().to_dict()['StudentList']
        except:
            stn_list = []

        pushdata = {
            u'StudentID': db.collection(u'Users').document(username),
            u'Grade': 0
        }
        stn_list.append(pushdata)

        if dbdata == postdata:
            data = {
                u'CourseList': copydata
            }
            attndata = {
                u'AttendanceList': attendance_list
            }
            StnList = {
                u'StudentList': stn_list
            }
            db.collection(u'Users').document(username).update(data)
            db.collection(u'Courses').document(cinfo).update(attndata)
            db.collection(u'Courses').document(cinfo).update(StnList)
        return HttpResponseRedirect(reverse('course:dashboard'))
    else:
        if Designation == 'Student':
            return render(request, 'course/enrollcourse.html', context)
        elif Designation == 'Faculty':
            return HttpResponseNotFound('<h1>Page not found</h1>')


def Update_Attendance(request, cinfo, aid, gid):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    if request.method == 'POST':
        checkedstudent = request.POST.getlist('checks[]')
        for student in checkedstudent:
            userref = db.collection(u'Users').document(student)
            courseref = db.collection(u'Courses').document(cinfo).get()
            attendance_list = courseref.to_dict()['AttendanceList']
            index = 0
            check = 0
            while (check == 0):
                try:
                    stud_username = attendance_list[index]['StudentID'].get().to_dict()[
                        'username']
                    if stud_username == student:
                        attendance_list[index]['TotalAttendance'] = attendance_list[index]['TotalAttendance'] + 1
                        data = {
                            u'AttendanceList': attendance_list
                        }
                        db.collection(u'Courses').document(cinfo).update(data)
                        check = 1
                    index = index + 1
                except:
                    check = 1

        return HttpResponseRedirect(reverse('course:dashboard'))
    else:
        cinfo = "CS243"
        aid = "As_01"
        group_ref = db.collection(u'Courses').document(cinfo).collection(
            u'Assignments').document(aid).collection(u'Groups').document(gid).get()
        studentlist = group_ref.to_dict()['StudentList']
        index = 0
        studentinfo = []
        check = 0
        while(check == 0):
            try:
                studentinfo.append(
                    studentlist[index]['StudentID'].get().to_dict())
                index = index + 1
            except:
                check = 1

        print(studentinfo)
        context['cinfo'] = cinfo
        context['aid'] = aid
        context['gid'] = gid
        context['studentinfo'] = studentinfo
        # {
        #     'cinfo': cinfo,
        #     'aid': aid,
        #     'gid': gid,
        #     'studentinfo': studentinfo,
        # }
        return render(request, 'course/updateattendance.html', context)


def Show_Attendance(request, cinfo):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    course_ref = db.collection(u'Courses').document(cinfo).get()
    course_arr = course_ref.to_dict()['AttendanceList']
    list = []
    for user in course_arr:
        grade = 0
        for users in course_ref.to_dict()['StudentList']:
            if users['StudentID'] == user['StudentID']:
                grade = users['Grade']

        temp_dict = {
            "StudentName": user['StudentID'].get().to_dict()['FullName'],
            "Attendance": user['TotalAttendance'],
            "grade": grade
        }
        list.append(temp_dict)

    context['list'] = list
    context['CourseInfo'] = cinfo
    # {
    #     'list': list,
    #     'CourseInfo': cinfo,
    # }

    return render(request, 'course/show_attendance.html', context)


def Add_Grade(request, cinfo, aid, gid):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    group_ref = db.collection(u'Courses').document(cinfo).collection(
        u'Assignments').document(aid).collection(u'Groups').document(gid)
    group_data = group_ref.get().to_dict()['StudentList']
    if request.method == "POST":
        for user in group_data:
            Igrade = user['Grade']
            user['Grade'] = int(request.POST.get(
                user['StudentID'].get().to_dict()['username']))
            user_ref = user['StudentID']
            Course_Data = db.collection(u'Courses').document(
                cinfo).get().to_dict()['StudentList']
            for users in Course_Data:
                if users['StudentID'] == user['StudentID']:
                    users['Grade'] = users['Grade'] + user['Grade'] - Igrade
            data = {
                u'StudentList': Course_Data
            }
            db.collection(u'Courses').document(cinfo).update(data)
        data_main = {
            u'StudentList': group_data
        }
        db.collection(u'Courses').document(cinfo).collection(u'Assignments').document(
            aid).collection(u'Groups').document(gid).update(data_main)
        return HttpResponseRedirect(reverse('course:dashboard'))

    else:
        user_list = []
        for user in group_data:
            user_list.append(user['StudentID'].get().to_dict())

        context['user_list'] = user_list
        context['cinfo'] = cinfo
        context['aid'] = aid
        context['gid'] = gid
        # {
        #     'user_list': user_list,
        #     'cinfo': cinfo,
        #     'aid': aid,
        #     'gid': gid
        # }
        return render(request, 'course/addgrade.html', context)


def AddCourse(request):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    if request.method == 'POST':
        CourseID = request.POST.get("CourseID", "")

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

    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']
    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']
    #cid += "_" + username + " _" + cyear
    assgn_ref = db.collection(u'Courses').document(
        cinfo).collection(u'Assignments').get()
    AssgnDetails = []
    for assgn in assgn_ref:
        AssgnDetails.append(assgn.to_dict())
    context['AssgnDetails'] = AssgnDetails
    context['CourseInfo'] = cinfo
    context['Designation'] = Designation
    # {
    #     'AssgnDetails': AssgnDetails,
    #     'CourseInfo': cinfo
    # }
    return render(request, 'course/viewcourse.html', context)


def AddAssgn(request, cinfo):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    if request.method == 'POST':
        ref_prof = db.collection(u'Users').document(username)
        nxtID = len(list(db.collection(u'Courses').document(
            cinfo).collection(u'Assignments').get()))+1
        nextAssID = "AS{}".format(nxtID)
        data = {
            u'About': request.POST.get("About", ""),
            u'AssignmentID': nextAssID,
            u'Deadline': request.POST.get("Deadline", ""),
            u'Name': request.POST.get("Name", ""),
        }
        # aid = request.POST.get("AssignmentID", "")
        db.collection(u'Courses').document(cinfo).collection(
            u'Assignments').document(nextAssID).set(data)

        return HttpResponseRedirect(reverse('course:view_course', kwargs={'cinfo': cinfo}))

    return render(request, 'course/addassgnform.html')


def UpAssgn(request, cinfo, aid):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    AssgnDetails = db.collection(u'Courses').document(cinfo).collection(
        u'Assignments').document(aid).get().to_dict()
    context = {
        'CourseInfo': cinfo,
        'AssgnDetails': AssgnDetails,
    }

    if request.method == 'POST':
        ref_prof = db.collection(u'Users').document(username)
        data = {
            u'About': request.POST.get("About", ""),
            u'AssignmentID': aid,
            u'Deadline': request.POST.get("Deadline", ""),
            u'Name': request.POST.get("Name", ""),
        }
        # aid = request.POST.get("AssignmentID", "")
        db.collection(u'Courses').document(cinfo).collection(
            u'Assignments').document(aid).set(data)

        return HttpResponseRedirect(reverse('course:view_course', kwargs={'cinfo': cinfo}))

    else:
        return render(request, 'course/upassgn.html', context)


def viewTA(request, cinfo):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']

    TAList = db.collection(u'Courses').document(
        cinfo).get().to_dict()["TAList"]

    talist = list()

    for TA in TAList:
        talist.append(TA.get().to_dict())

    context['TA'] = talist
    context['CourseInfo'] = cinfo
    context['Designation'] = Designation
    # {
    #     'TA': talist,
    #     'CourseInfo': cinfo
    # }

    return render(request, 'course/viewTA.html', context)


def ViewAssgn(request, cinfo, aid):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']

    group_ref = db.collection(u'Courses').document(cinfo).collection(
        u'Assignments').document(aid).collection(u'Groups').get()
    GroupDetails = []
    for group in group_ref:
        GroupDetails.append(group.to_dict())

    context['GroupDetails'] = GroupDetails
    context['cinfo'] = cinfo
    context['aid'] = aid
    context['Designation'] = Designation
    # {
    #     'GroupDetails': GroupDetails,
    #     'cinfo': cinfo,
    #     'aid': aid,
    # }
    return render(request, 'course/viewassgn.html', context)


def AddTA(request, cinfo):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    BTech, MTech, Phd = getStudents()
    context['BTech'] = BTech
    context['MTech'] = MTech
    context['Phd'] = Phd
    # {
    #     'BTech': BTech,
    #     'MTech': MTech,
    #     'Phd': Phd,
    #
    # }

    if request.method == 'POST':
        ref_course = db.collection(u'Courses').document(cinfo)
        stuType = request.POST.get("TA_BRANCH", "")
        stuID = request.POST.get(stuType+"TA", "")
        stuID = stuID.split('-')[1]
        ref_TA = db.collection(u'Users').document(stuID)

        if 'TAList' not in ref_course.get().to_dict():
            currTA = []
        else :
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

        if not 'CoursesListAsTA' in ref_TA.get().to_dict():
            coursesAsTA = []
        else:
            coursesAsTA = ref_TA.get().to_dict()['CoursesListAsTA']

        if not ref_course in coursesAsTA:
            coursesAsTA.append(ref_course)
        else:
            pass

        ref_TA.update({
            u'CoursesListAsTA' : coursesAsTA
        })

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

            if userdict["Program"].upper() == "BTECH":
                BTech.append(userdict)

            if userdict["Program"].upper() == "MTECH":
                MTech.append(userdict)

            if userdict["Program"].upper() == "PHD":
                Phd.append(userdict)

    return BTech, MTech, Phd


def AddCourseMaterial(request, cinfo):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    context = {
        'CourseInfo': cinfo,
    }
    return render(request, 'course/addcoursematerial.html', context)


def ViewCourseMaterial(request, cinfo):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']

    course_data = db.collection(u'Courses').document(cinfo).get().to_dict()

    course_data = db.collection(u'Courses').document(cinfo).get().to_dict()
    if not 'CourseMaterial' in course_data:
        CMaterials = []
    else:
        CMaterials = course_data['CourseMaterial']

    context['CMaterials'] = CMaterials
    context['CourseInfo'] = cinfo
    context['Designation'] = Designation
    # {
    #     'CMaterials': CMaterials,
    #     'CourseInfo': cinfo
    # }
    return render(request, 'course/viewcoursematerial.html', context)


def StoreCMinDb(request, cinfo):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    if request.method == 'POST':
        course_data = db.collection(u'Courses').document(cinfo).get().to_dict()
        newcm = {}
        newcm['Name'] = request.POST.get('filename')
        newcm['Url'] = request.POST.get('cmurl')

        if not 'CourseMaterial' in course_data:
            cm_array = [newcm]
            db.collection(u'Courses').document(cinfo).update({
                u'CourseMaterial': cm_array
            })
        else:
            cm_array = course_data['CourseMaterial']
            cm_array.append(newcm)
            db.collection(u'Courses').document(cinfo).update({
                u'CourseMaterial': cm_array
            })
        # print(newcm)
        # print(cm_array)

        return HttpResponseRedirect(reverse('course:view_course_material', kwargs={'cinfo': cinfo}))
    else:
        return HttpResponseRedirect(reverse('course:view_course_material', kwargs={'cinfo': cinfo}))


def Update_Submission(request, cinfo, aid, gid):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    group = db.collection(u'Courses').document(cinfo).collection(
        u'Assignments').document(aid).collection(u'Groups').document(gid)

    print(group.get().to_dict())

    context['sub'] = ''
    context['CourseInfo'] = cinfo
    context['aid'] = aid
    context['gid'] = gid
    # {
    #     'sub': '',
    #     'CourseInfo': cinfo,
    #     'aid': aid,
    #     'gid': gid
    # }

    if request.method == 'POST':
        # get submission file from the request and store it in the database
        newcm = {
            'Name': request.POST.get('filename'),
            'Url': request.POST.get('cmurl')
        }
        print(context)
        group.update({u'SubmissionFile': newcm})

        return HttpResponseRedirect(reverse('course:up_submission', kwargs={
            'cinfo': cinfo,
            'aid': aid,
            'gid': gid
        }))

    else:
        # display submission file to the users
        try:
            context['sub'] = group.get().to_dict()['SubmissionFile']
        except:
            pass
        return render(request, 'course/addSubmission.html', context)


def ViewGroup(request, cinfo, aid, gid):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']
    #cid += "_" + username + " _" + cyear

    user_ref = db.collection(u'Users').document(username).get()
    user_dict = user_ref.to_dict()
    Designation = user_dict['Designation']

    group_ref = db.collection(u'Courses').document(cinfo).collection(
        u'Assignments').document(aid).collection(u'Groups').document(gid).get()
    GroupDetails = group_ref.to_dict()
    studs = GroupDetails['StudentList']
    StudentDetails = []
    for stud in studs:
        StudentDetails.append(stud['StudentID'].get().to_dict())
    context['cinfo'] = cinfo
    context['aid'] = aid
    context['GroupDetails'] = GroupDetails
    context['StudentDetails'] = StudentDetails
    # {
    #     'cinfo' : cinfo,
    #     'aid' : aid,
    #     'GroupDetails' : GroupDetails,
    #     'StudentDetails' : StudentDetails,
    # }
    print(Designation)
    return render(request, 'course/viewgroup.html', context)


def UpdateGroup(request, cinfo, aid, gid):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    if request.method == 'POST':
        #ref_prof = db.collection(u'Users').document(username)
        data = {
            u'ProjectTitle': request.POST.get("ProjectTitle", ""),
            u'ProblemStatement': request.POST.get("ProblemStatement", "")
        }
        print(data)
        db.collection(u'Courses').document(cinfo).collection(u'Assignments').document(
            aid).collection(u'Groups').document(gid).update(data)
        group_ref = db.collection(u'Courses').document(cinfo).collection(
            u'Assignments').document(aid).collection(u'Groups').document(gid).get()
        GroupDetails = group_ref.to_dict()
        studs = GroupDetails['StudentList']
        StudentDetails = []
        for stud in studs:
            StudentDetails.append(stud['StudentID'].get().to_dict())
        context['cinfo'] = cinfo
        context['aid'] = aid
        context['GroupDetails'] = GroupDetails
        context['StudentDetails'] = StudentDetails
        # {
        #     'cinfo' : cinfo,
        #     'aid' : aid,
        #     'GroupDetails' : GroupDetails,
        #     'StudentDetails' : StudentDetails,
        # }

        return render(request, 'course/viewgroup.html', context)

    return render(request, 'course/updategroupform.html')

def RandomGroups(request, cinfo, aid):
    context = {}
    context = loginFLOW(request, context)
    if context['username'] == '':
        return HttpResponseRedirect(reverse('home:home'))

    username = context['username']

    if request.method == 'GET':
        return render(request, 'course/random_groups.html')
    elif request.method == 'POST':
        NumGroups = request.POST['NumGroups']
        NumGroups = int(NumGroups)

        StudList = db.collection(u'Courses').document(cinfo).get().to_dict()['StudentList']
        random.shuffle(StudList)

        GroupList = []
        for i in range(NumGroups):
            GroupList.append([])
        for i in range(len(StudList)):
            GroupList[i%NumGroups].append(StudList[i])

        print(GroupList)

        for i in range(len(GroupList)):
            if len(GroupList[i]) > 0:
                data = {
                    u'GroupID': 'GID_'+str(i+1),
                    u'ProblemStatement': '',
                    u'ProjectTitle': '',
                    u'StudentList': GroupList[i],
                }

                db.collection(u'Courses').document(cinfo).collection(u'Assignments').document(aid).collection(u'Groups').document('GID_'+str(i+1)).set(data)

        return redirect('/courses/'+cinfo+'/assignments/'+aid)
