from django.contrib import admin
from django.urls import path
from . import views

app_name = 'course'

urlpatterns = [
    path('dashboard', views.dashboard, name='course'),
    path('add_course', views.AddCourse, name='add_course'),
]
