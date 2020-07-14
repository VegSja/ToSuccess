from django.urls import path
from databaseInterface import views
from rest_framework.urlpatterns import format_suffix_patterns

urlpatterns = [
    path('activities/', views.activity_list_view.as_view(), name='activities'),
    path('activities/<str:name>/', views.activity_detail),
    path('tokensignin/<str:idToken>/', views.sign_in),
    path('google/', views.GoogleView.as_view(), name='google')
]

urlpatterns = format_suffix_patterns(urlpatterns)