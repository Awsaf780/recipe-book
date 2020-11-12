from django.shortcuts import render, redirect
get_recommendatiom=['18073', '20156', '215379', '220994', '233652']
from django.http import HttpResponse, request
from .models import Recipe

from subprocess import run, PIPE
import sys
from django.views.generic import (
    ListView,
)
import django_filters
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger


def homepage(request):
    return render(request, "main/home.html")


def single_slug(request, single_slug):
    get_recommendation = suggest(request, single_slug)
    print(get_recommendation)


    get_recommendation = str(get_recommendation)
    get_recommendation = get_recommendation.replace("['", '')
    get_recommendation = get_recommendation.replace("\\r\\n']", '')
    get_recommendation = get_recommendation.replace("[", '')
    get_recommendation = get_recommendation.replace("'", '')
    get_recommendation = get_recommendation.replace("]", '')
    get_recommendation = get_recommendation.replace("\\n", '')
    get_recommendation = get_recommendation.replace(" ", '')

    # get_recommendation = get_recommendation.split(', ')

    get_recommendation = get_recommendation.split(',')

    try:
        recommended_recipe = Recipe.objects.filter(id__in=get_recommendation)
        print(recommended_recipe)
    except:

        recommended_recipe = Recipe.objects.filter(id__in=get_recommendatiom)

    recipes = [r.id for r in Recipe.objects.all()]
    single_slug = int(single_slug)

    if single_slug in recipes:
        this_recipe = Recipe.objects.get(id = single_slug)

        ingredients = this_recipe.ingredients
        ingredients = ingredients.split('^')

        directions = this_recipe.directions
        directions = directions.replace("{'directions': u'", "")
        directions = directions.replace("'}", "")
        directions = directions.replace('{\'directions\': u"', '')
        directions = directions.replace('}"', '')
        directions = directions.replace("\\n", ";")

        directions = directions.split(";")
        if ((directions[0] == 'Prep' and directions[2] == 'Ready In') or (directions[0] == 'Prep' and directions[2] == 'Cook' and directions[4] != 'Ready In') or (directions[0] == 'Cook' and directions[2] == 'Ready In')):
            directions_details = directions[4:]
            directions_info = {
                directions[0]: directions[1],
                directions[2]: directions[3],

            }
        elif(directions[0] == 'Prep' and directions[2] == 'Cook' and directions[4] == 'Ready In'):
            directions_details = directions[6:]
            directions_info = {
                directions[0]: directions[1],
                directions[2]: directions[3],
                directions[4]: directions[5],

            }
        else:
            directions_details = directions
            directions_info = {
                'Prep': 'Not Determined',
                'Cook': 'Not determined',
                'Ready In': 'Not Determined',

            }

        print(type(directions))
        return render(request, "main/tutorial.html", {'recipe': this_recipe,
                                                      'ingredients': ingredients,
                                                      'info': directions_info,
                                                      'directions': directions_details,
                                                      'instructions': directions,
                                                      'recommended': recommended_recipe})
    else:
        return HttpResponse("Does not exist")

# def suggest(request):
#     inp = request.POST.get('param')
#
#     out = run([sys.executable, 'suggest.py', inp], shell=False, stdout=PIPE)
#     print(out)
#     out = out.stdout.decode('utf=8')
#     # out = out[1:len(out)]
#     out = list(out.split(","))
#
#     return render(request, 'main/suggest.html', {'data': out})

def suggest(request, index):
    inp = index

    out = run([sys.executable, 'suggest.py', inp], shell=False, stdout=PIPE)
    print(out)
    out = out.stdout.decode('utf=8')
    # out = out[1:len(out)]
    out = list(out.split(","))

    return out

# def recommend(request):
#     return render(request, "main/suggest.html")


# def recipes(request):
#     context = {
#         'recipes': Recipe.objects.all()
#     }
#     return render(request, "main/recipes.html", {'recipes': Recipe.objects.all})


class RecipeFilter(django_filters.FilterSet):
    class Meta:
        model = Recipe
        fields = {'name': ['icontains']}


# class RecipeListView(ListView):
#
#
# 	model = Recipe
#     template_name = 'main/recipes.html'
#     context_object_name = 'recipes'
#     ordering = ['id']
#     paginate_by = 50
#
#     def get_context_data(self, **kwargs):
#     	context = super().get_context_data(**kwargs)
#     	context['filter'] = RecipeFilter(self.request.GET, queryset=self.get_queryset())
#     	return context
#

# def recipes(request):
#     filtered_qs = RecipeFilter(request.GET, queryset=Recipe.objects.all()).qs
#     paginator = Paginator(filtered_qs, 20)
#     page = request.GET.get('page')
#     try:
#         response = paginator.page(page)
#     except PageNotAnInteger:
#         response = paginator.page(1)
#     except EmptyPage:
#         response = paginator.page(paginator.num_pages)
#
#     return render(request, 'main/recipes.html', {'response': response, 'page': page})

def recipes(request):
    recipe_list = Recipe.objects.all().order_by('-reviews')
    # recipe_list = Recipe.objects.all()

    recipe_filter = RecipeFilter(request.GET, queryset=recipe_list)

    paginator = Paginator(recipe_filter.qs, 20)
    page = request.GET.get('page', 1)

    try:
        recipes = paginator.page(page)
    except PageNotAnInteger:
        recipes = paginator.page(1)
    except EmptyPage:
        recipes = paginator.page(paginator.num_pages)

    return render(request, 'main/recipes.html', {
        'title': 'All Recipes',
        'recipes': recipes,
        'page': page,
        'recipe_filter': recipe_filter,
    })



############# NEW REST CODE #######################

# new imports
from django.shortcuts import get_object_or_404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from . serializers import RecipeSerializer


class RecipeList(APIView):
    def get(self, request):
        recipe_json = Recipe.objects.all()
        serializer = RecipeSerializer(recipe_json, many=True)

        return Response(serializer.data)

    def post(self):
        pass


########## NEW MODEL CODE ###########################
class NutritionList(APIView):
    def get(self, request):
        nutrition_json = Nutrition.objects.all()
        serializer = NutritionSerializer(nutrition_json, many=True)

        return Response(serializer.data)

    def post(self):
        pass
