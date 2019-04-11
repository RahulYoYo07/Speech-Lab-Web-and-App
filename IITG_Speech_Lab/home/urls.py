from django.urls import path
from home import views

app_name = 'home'
urlpatterns = [
    # The home view ('/home/')

    # Explicit home ('/home/home/')
    path('', views.home, name='home'),
    # Redirect to get token ('/home/gettoken/')
    path('gettoken/', views.gettoken, name='gettoken'),
    path('login/', views.login, name='login'),
    path('login/login_add/', views.login_add, name='login_add'),
    path('logout/', views.logout, name='logout')
]