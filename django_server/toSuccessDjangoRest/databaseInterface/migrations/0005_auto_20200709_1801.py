# Generated by Django 3.0.8 on 2020-07-09 18:01

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('databaseInterface', '0004_auto_20200709_1649'),
    ]

    operations = [
        migrations.AlterField(
            model_name='activity',
            name='date',
            field=models.CharField(max_length=4),
        ),
        migrations.AlterField(
            model_name='activity',
            name='minutes_after_midnight',
            field=models.CharField(max_length=4),
        ),
    ]