from django.contrib import admin
from django.urls import path
from . import views

app_name = 'course'

urlpatterns = [
    path('dashboard', views.dashboard, name='course'),
]
