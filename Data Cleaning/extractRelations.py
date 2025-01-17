import pandas as pd
import json
import math

apps = pd.read_json('app.json')
relations = pd.DataFrame(columns=['username', 'appId', 'type'])

for _, record in apps.iterrows():
    if record['reviews']:
        for review in record['reviews']:
            d = {'username': [review['username']],
                 'appId': [record['_id']],
                 'type': ['follows']}
            
            df = pd.DataFrame(data=d)
            relations = relations.append(df)
    if record['developer']['developerId']:
        d = {'username': [record['developer']['developerId']],
             'appId': [record['_id']],
             'type': ['develops']}
        df = pd.DataFrame(data=d)
        relations = relations.append(df)

relations.drop_duplicates(keep='first',inplace=True)

relations.to_csv('relations.csv')


