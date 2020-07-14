from databaseInterface.models import Activity
from databaseInterface.serializers import ActivitySerializer
from databaseInterface.token_validation import token_validation

from django.http import HttpResponse, JsonResponse
from rest_framework.parsers import JSONParser
from django.views.decorators.csrf import csrf_exempt

@csrf_exempt
def activity_list(request):
    #List all activities or create a new one

    if request.method == 'GET':
        activities = Activity.objects.all()
        serializer = ActivitySerializer(activities, many=True)
        print("GET Method called")
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = ActivitySerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)


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