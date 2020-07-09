from databaseInterface.models import Activity
from rest_framework import serializers

class ActivitySerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Activity
        fields = ['url', 'activity_name', 'seconds_after_midnight']
