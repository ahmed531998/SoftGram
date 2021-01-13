import pandas as pd
import json
import math

users = pd.read_json('user.json')
users = users.astype({"password": str})

for index, record in users.iterrows():
    users.at[index,'password'] = record['_id']+'0'
    
users.to_json('userWithPass.json', orient='records', date_format='iso')
