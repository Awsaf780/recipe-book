# from django.test import TestCase, Client
# from django.urls import reverse
# from main.models import Recipe
# import json
#
#
# class TestViews(TestCase):
#
#     def setUp(self):
#         self.client = Client()
#         self.single_slug = reverse('main:single_slug', args=['999999'])
#         self.recipes = reverse('main:recipes')
#
#         self.recipe1 = Recipe.objects.create(
#             name = "Recipe Test Case",
#             rating = 4.0,
#             url = "",
#             reviews = 12,
#             ingredients = "Ingredient1^Ingredient2",
#             directions = "Prep\n12m\nDo this\nDo that",
#             nutritions = "Nothing"
#         )
#
#     def test_recipe_info(self):
#         response = self.client.get(self.single_slug)
#
#         self.assertEquals(response.status_code, 200)
#         self.assertTemplateUsed(response, "main/tutorial.html")
#
#     def test_recipe_api(self):
#         response = self.client.get(self.recipes)
#
#         self.assertEquals(response.status_code, 200)
#         self.assertTemplateUsed(response, "main/recipes.html")
