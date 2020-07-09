from databaseInterface.models import Activity
from rest_framework import viewsets
from databaseInterface.serializers import ActivitySerializer

class ActivityViewSet(viewsets.ModelViewSet):
    #API endpoint that allows users to be viewed or edited
    queryset = Activity.objects.all().order_by('seconds_after_midnight')
    serializer_class = ActivitySerializer

