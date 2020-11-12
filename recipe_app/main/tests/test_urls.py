from django.test import SimpleTestCase
from django.urls import reverse, resolve
from main.views import homepage, suggest, single_slug, recipes, RecipeList


class TestUrls(SimpleTestCase):

    def test_homepage_url_is_resolved(self):
        url = reverse('main:homepage')
        print(resolve(url))
        self.assertEquals(resolve(url).func, homepage)

    def test_recipes_url_is_resolved(self):
        url = reverse('main:recipes')
        print(resolve(url))
        self.assertEquals(resolve(url).func, recipes)

    def test_suggest_url_is_resolved(self):
        url = reverse('main:suggest')
        print(resolve(url))
        self.assertEquals(resolve(url).func, suggest)

    def test_slug_url_is_resolved(self):
        url = reverse('main:single_slug', args=['12345'])
        print(resolve(url))
        self.assertEquals(resolve(url).func, single_slug)

    def test_recipe_api_url_is_resolved(self):
        url = reverse('main:recipe_api')
        print(resolve(url))
        self.assertEquals(resolve(url).func.view_class, RecipeList)


