from django.contrib import admin
from django.urls import path
from . import views

app_name = 'course'

urlpatterns = [
    path('dashboard', views.dashboard, name='dashboard'),
    path('add_course', views.AddCourse, name='add_course'),
    path('<slug:cinfo>/assignments/add_assgn', views.AddAssgn, name='add_assgn'),
    path('<slug:cinfo>/', views.ViewCourse, name='view_course'),
    path('<slug:cinfo>/enrollcoursepage', views.Enroll_CoursePage, name='enroll_course_page'),
    path('<slug:cinfo>/assignments/<slug:aid>/', views.ViewAssgn, name='view_assgn'),
    path('<slug:cinfo>/assignments/<slug:aid>/',
         views.ViewAssgn, name='view_assgn'),
    path('<slug:cinfo>/addta', views.AddTA, name='add_TA'),
    path('<slug:cinfo>/talist', views.viewTA, name='view_TA'),


]
