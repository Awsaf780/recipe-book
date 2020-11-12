from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view
from rest_framework.pagination import PageNumberPagination
from rest_framework.generics import ListAPIView
from rest_framework.filters import SearchFilter, OrderingFilter
from main.models import Recipe

from main.api.serializers import RecipeSerializer


@api_view(['GET', ])
def api_detail_recipe_view(request, slug):
    try:
        recipe = Recipe.objects.get(id=slug)
    except Recipe.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == "GET":
        serializer = RecipeSerializer(recipe)
        return Response(serializer.data)


class ApiRecipeListView(ListAPIView):
    queryset = Recipe.objects.all()
    serializer_class = RecipeSerializer
    pagination_class = PageNumberPagination
    filter_backends = (SearchFilter, OrderingFilter)
    search_fields = ('name', 'ingredients', 'directions')