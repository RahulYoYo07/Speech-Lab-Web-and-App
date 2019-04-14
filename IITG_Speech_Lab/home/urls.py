from django.urls import path
from home import views

app_name = 'home'
urlpatterns = [
    # The home view ('/home/')

    # Explicit home ('/home/home/')
    path('', views.home, name='home'),
    # Redirect to get token ('/home/gettoken/')
    path('gettoken/', views.gettoken, name='gettoken'),
    path('student/', views.student, name='student'),
    path('faculty/', views.faculty, name='faculty'),
    path('login/', views.login, name='login'),
    path('projects/', views.projects, name='projects'),
    path('login/login_add/', views.login_add, name='login_add'),
    path('logout/', views.logout, name='logout'),
    path('projects/<slug:pinfo>/', views.projectView, name='projectView'),
]