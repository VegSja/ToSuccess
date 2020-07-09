from django.urls import path
from databaseInterface import views

urlpatterns = [
    path('activities/', views.activity_list),
]