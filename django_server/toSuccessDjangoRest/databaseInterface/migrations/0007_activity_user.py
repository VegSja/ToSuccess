# Generated by Django 3.0.8 on 2020-07-14 20:01

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('databaseInterface', '0006_auto_20200709_1827'),
    ]

    operations = [
        migrations.AddField(
            model_name='activity',
            name='user',
            field=models.CharField(default=0, max_length=250),
            preserve_default=False,
        ),
    ]
