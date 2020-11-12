
import pandas as pd
from scipy.spatial.distance import cosine, euclidean, hamming
from sklearn.preprocessing import normalize
import sys

df_normalized_with_title = pd.read_csv('normalized_data.csv', index_col='recipe_id')
df_normalized = df_normalized_with_title.drop(['recipe_name'], axis=1)


def selected_recipe(recipe_id):
    recipe_df = df_normalized_with_title
    # x = "{}  {},".format(recipe_id, recipe_df.at[recipe_id, 'recipe_name'])
    x = int(recipe_id)
    print(x)

def nutrition_recommender(distance_method, recipe_id, N):

    allRecipes = pd.DataFrame(df_normalized.index)
    allRecipes = allRecipes[allRecipes.recipe_id != recipe_id]
    allRecipes["distance"] = allRecipes["recipe_id"].apply(lambda x: distance_method(df_normalized.loc[recipe_id], df_normalized.loc[x]))
    TopNRecommendation = allRecipes.sort_values(["distance"]).head(N).sort_values(by=['distance', 'recipe_id'])

    # sort by distance then recipe id, the smaller value of recipe id will be picked.
    recipe_df = df_normalized_with_title
    recipe_id = [recipe_id]
    recipe_list = []

    for recipeid in TopNRecommendation.recipe_id:
        recipe_id.append(recipeid)   # list of recipe id of selected recipe and recommended recipe(s)
        recipe_list.append("{}  {}".format(recipeid, recipe_df.at[recipeid, 'recipe_name']))

    return df_normalized.loc[recipe_id, :]



formula = cosine
count = 5

output = "%s" % (sys.argv[1])
output = int(output)
# selected_recipe(output)

result=nutrition_recommender(formula,output,count)
recommendation = result.index.values.tolist()
recommendation = recommendation[1:]
# print(recommendation)

suggest = []

for i in reversed(recommendation):
    suggest.append(int(i))

print(suggest)
