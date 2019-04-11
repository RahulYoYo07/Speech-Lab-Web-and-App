from django.urls import path
from home import views

app_name = 'home'
urlpatterns = [

    path('<slug:uinfo>/', views.ViewUser, name='view_user'),
]