from django.contrib import admin
from django.urls import path
from . import views

app_name = 'discussion'

urlpatterns = [
    # CourseGroup Discussion Room
    path('courses/<slug:CourseID>/coursegroup/<slug:CourseGroupID>', views.course_group, name='course_group'),
    # Course individual groups
    path('courses/<slug:CourseID>/assignments/<slug:AssignmentID>/groups/<slug:GroupID>', views.group, name='group'),
]
