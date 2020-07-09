from django.db import models

# Create your models here.
class Activity(models.Model):
    activity_name = models.CharField(max_length=250)
    minutes_after_midnight = models.IntegerField()
    date = models.IntegerField()

    class meta:
        ordering = ['created']