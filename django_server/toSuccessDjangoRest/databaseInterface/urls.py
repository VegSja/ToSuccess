from django.urls import path
from databaseInterface import views
from rest_framework.urlpatterns import format_suffix_patterns

urlpatterns = [
    path('activities/', views.activity_list),
    path('activities/<str:name>/', views.activity_detail),
    path('tokensignin/<str:idToken>/', views.sign_in),
]

urlpatterns = format_suffix_patterns(urlpatterns)