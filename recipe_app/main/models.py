from django.db import models


class Recipe(models.Model):
    name = models.CharField(max_length=250)
    rating = models.FloatField(default=0.0)
    url = models.TextField()
    reviews = models.IntegerField(default=0)
    #convert ingredients to Foreign Model
    ingredients = models.TextField()
    directions = models.TextField()
    #remove unnecessary nutritions field
    nutritions = models.TextField()

    def __str__(self):
        return self.name

class Nutrition(models.Model):
    calories = models.FloatField(default=0.0)
    fat = models.FloatField(default=0.0)
    carbohydrates = models.FloatField(default=0.0)
    protein = models.FloatField(default=0.0)
    cholesterol = models.FloatField(default=0.0)
    sodium = models.FloatField(default=0.0)
    fiber = models.FloatField(default=0.0)
    #name added for testing, will be removed
    name = models.CharField(max_length=250)

    def __str__(self):
        return self.name
