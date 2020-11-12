from django.urls import path

from main.api.views import api_detail_recipe_view, ApiRecipeListView

app_name = 'main'

urlpatterns = [
    path('<slug>/', api_detail_recipe_view, name='detail'),
    path('list', ApiRecipeListView.as_view(), name='list'),
]