from databaseInterface.models import Activity
from databaseInterface.serializers import ActivitySerializer
from databaseInterface.token_validation import token_validation

from rest_framework.parsers import JSONParser
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.utils import json

from django.contrib.auth.models import User
from django.contrib.auth.base_user import BaseUserManager
from django.contrib.auth.hashers import make_password

from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
import requests

class activity_list_view(APIView):
    permission_classes = (IsAuthenticated,)

    def get(self, request):
        activities = Activity.objects.all()
        serializer = ActivitySerializer(activities, many=True)
        return JsonResponse(serializer.data, safe=False)
    
    def post(self, request):
        data = JSONParser().parse(request)
        serializer = ActivitySerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)


class GoogleView(APIView):
    def post(self, request):
        payload = request.data.get("token") # Make readable
        token = token_validation(payload)

        #payload = {'accces_token': request.data.get("token")} # validate the token
        # r = requests.get('https://www.googleapis.com/oauth2/v2/userinfo', params=payload)
        # data = json.loads(r.text)

        # if 'error' in data:
        #     content = {'message': 'wrong google token / this google token is already expired.'}
        #     return Response(content)

        # Create a user if no exist
        try:
            print("Checking if user exists....")
            user = User.objects.get(username=token.email)
        except User.DoesNotExist:
            print("Creating new user...")
            user = User()
            user.username = token.email
            # provider random default password
            user.password = make_password(BaseUserManager().make_random_password())
            user.email = token.email
            user.save()
            print("Successfully created new user...")

        #Genereate token without username and password
        token = RefreshToken.for_user(user)
        response = {}
        response['username'] = user.username
        response['access_token'] = str(token.access_token)
        response['refreash_token'] = str(token)
        return Response(response)

#This method is mainly used for deleting activities
@csrf_exempt   
def activity_detail(request, name):
    print("Activity detail called METHOD: " + request.method)
    try:
        activity = Activity.objects.get(activity_name=name)
    except Activity.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = ActivitySerializer(activity)
        return JsonResponse(serializer.data)
    
    if request.method == 'DELETE':
        activity.delete()
        return HttpResponse(status=status.HTTP_204_NO_CONTENT)

@csrf_exempt
def sign_in(request, idToken):

    if request.method == 'POST':
        token_validation(idToken)