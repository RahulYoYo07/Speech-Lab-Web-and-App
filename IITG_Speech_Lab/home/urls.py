from django.contrib import admin
from django.urls import path
from . import views

app_name = 'home'

urlpatterns = [
    # Home page
    path('', views.home, name='home'),
    # Projects page
    path('projects/', views.projects, name='projects'),
]
