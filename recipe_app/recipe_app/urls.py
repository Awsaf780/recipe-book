
from django.contrib import admin
from django.urls import path, include
from rest_framework.urlpatterns import format_suffix_patterns
from main import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path("", include("main.urls")),

    # path("recipejson", views.RecipeList.as_view()),
    path("nutritionjson", views.NutritionList.as_view()),

    # REST FRAMEWORK URLS
    path('api/recipes/', include('main.api.urls', 'recipe_api')),

]
