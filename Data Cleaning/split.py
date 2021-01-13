import pandas as pd
import json
import math

df = pd.read_csv('relations.csv')
df1 = None
df2 = None

for relation, df_type in df.groupby('type'):
    if relation == 'develops':
        df1 = df_type
    else:
        df2 = df_type

df1.to_csv('developrelation.csv')
df2.to_csv('followrelation.csv')

