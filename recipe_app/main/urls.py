from django.contrib import admin
from django.urls import path
from . import views
# from .views import (
#     RecipeListView,
#     )

app_name = 'main'

urlpatterns = [
    path('', views.homepage, name='homepage'),
    path('suggest', views.suggest, name='suggest'),
    # path('recommend', views.recommend),
    # path('recipes', RecipeListView.as_view()),
    path('recipes', views.recipes, name="recipes"),
    path("recipes/<single_slug>", views.single_slug, name="single_slug"),
    path("recipejson", views.RecipeList.as_view(), name="recipe_api"),
]
