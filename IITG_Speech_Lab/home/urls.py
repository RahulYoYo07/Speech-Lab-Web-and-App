from django.urls import path
from home import views

app_name = 'home'
urlpatterns = [
  # The home view ('/home/')
  path('', views.home, name='home'),
  # Explicit home ('/home/home/')
  path('home/', views.home, name='home'),
  # Redirect to get token ('/home/gettoken/')
  path('gettoken/', views.gettoken, name='gettoken'),
]