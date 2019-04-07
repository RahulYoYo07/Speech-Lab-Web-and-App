from django.shortcuts import render
# import firebase_admin
# from firebase_admin import credentials
# from firebase_admin import firestore

# Use a service account
# cred = credentials.Certificate('iitg-speech-lab-firebase-adminsdk-ggn1f-2f757184a1.json')
# firebase_admin.initialize_app(cred)
#
# db = firestore.client()
# Create your views here.

def dashboard(request):
    return render(request,'course/main_page.html')

def AddCourse(request):

    if request.method == 'POST':
        return render(request, 'course/main_page.html')

    return render(request, 'course/addcourseform.html')
