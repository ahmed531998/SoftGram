import pandas as pd
import json
import math
from numpy import random

relations = pd.read_csv('relations.csv')

develop = None
follow = None

for relation, df_type in relations.groupby('type'):
    if relation == 'develops':
        develop = df_type
    else:
        follow = df_type
        
userfollows = pd.DataFrame(columns=['user1', 'user2', 'type'])

for index, record in follow.iterrows():
    developer = develop[develop['appId'] == record['appId']]
    for _, d in developer.iterrows():
        if d['username'] != record['username']:
            f = {'user1': [record['username']], 'user2': [d['username']], 'type': ['follows']}
            df = pd.DataFrame(data=f)
            userfollows = userfollows.append(df)

    
for followedUser, df in userfollows.groupby('user2'):
    numFollows = random.randint(df.shape[0])+1
    for i in range(0, numFollows):
        user1Index = random.randint(df.shape[0])
        if user1Index >= df.shape[0]:
            user1Index -= 1
        user2Index = random.randint(df.shape[0])
        while user2Index >= df.shape[0] or user2Index == user1Index:
            user2Index = random.randint(df.shape[0])
        
        f = {'user1': [df.iloc[user1Index]['user1']], 'user2': [df.iloc[user2Index]['user1']], 'type': ['follows']}
        d = pd.DataFrame(data=f)
        userfollows = userfollows.append(d)


userfollows.drop_duplicates(keep='first',inplace=True)
userfollows.to_csv('userfollows.csv')

    
