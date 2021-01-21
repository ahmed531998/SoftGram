import pandas as pd
import json
import math
import types

users = pd.read_json('user.json')
apps = pd.read_json('app.json')

reviews = pd.DataFrame(columns=['username', 'appId', 'appName', 'category', 'date', 'score', 'content'])


for index, record in users.iterrows():
    if isinstance(record['reviews'], list):
        for review in record['reviews']:
            appDF = apps[apps['_id'] == review['appId']]
            d = {'username': [record['_id']],
                 'appId': [review['appId']],
                 'appName': [appDF.iloc[0]['name']],
                 'category': [appDF.iloc[0]['category']],
                 'date': [review['review']['date']],
                 'score': [review['score']],
                 'content': [review['review']['content']]}
            df = pd.DataFrame(data=d)
            reviews = reviews.append(df)
    
reviews.to_json('review.json', orient='records', date_format='iso')
