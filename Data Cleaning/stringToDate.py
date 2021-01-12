import pandas as pd
import json
import math
from datetime import datetime


def stringTOdate(x, dateFormat):
    return datetime.strptime(x, dateFormat)

apps = pd.read_json('app.json')
users = pd.read_json('user.json')

for index, row in users.iterrows():
    if row['reviews']:
        reviews = []
        for el in row['reviews']:
            reviews.append(el)
        for i in range (0, len(reviews)):
            reviews[i]['review']['date']= pd.to_datetime(reviews[i]['review']['date'], format='%Y-%m-%d %H:%M:%S')#stringTOdate(reviews[i]['review']['date'], '%Y-%m-%d %H:%M:%S')
            reviews[i]['scoreDate'] = pd.to_datetime(reviews[i]['scoreDate'], format='%Y-%m-%d %H:%M:%S')#stringTOdate(reviews[i]['scoreDate'], '%Y-%m-%d %H:%M:%S')
        users.at[index,'reviews'] = reviews

for index, row in apps.iterrows():
    if(row['released']):
        apps.at[index,'released'] = pd.to_datetime(apps.at[index,'released'], format='%b %d, %Y')#stringTOdate(apps.at[index,'released'], '%b %d, %Y')
    if(row['lastUpdated']):
       apps.at[index,'lastUpdated'] = pd.to_datetime(apps.at[index,'lastUpdated'], format='%b %d, %Y')#stringTOdate(apps.at[index,'lastUpdated'], '%b %d, %Y')
    if row['reviews']:
        reviews = []
        for el in row['reviews']:
            reviews.append(el)
        for i in range (0, len(reviews)):
            reviews[i]['review']['date']=pd.to_datetime(reviews[i]['review']['date'], format='%Y-%m-%d %H:%M:%S')#stringTOdate(reviews[i]['review']['date'], '%Y-%m-%d %H:%M:%S')
            reviews[i]['scoreDate'] = pd.to_datetime(reviews[i]['scoreDate'], format='%Y-%m-%d %H:%M:%S')#stringTOdate(reviews[i]['scoreDate'], '%Y-%m-%d %H:%M:%S')
        apps.at[index,'reviews'] = reviews
          

apps.to_json('appWithDates.json', orient='records', date_format='iso')
users.to_json('userWithDates.json', orient='records', date_format='iso')




##javascript script to be run in mongoshell after
'''
db.app.find().forEach(function(el){el.released = new Date(el.released);
                                   el.lastUpdated = new Date(el.lastUpdated);
                                   db.app.save(el)});
                                   
db.app.find({reviews: {$ne: null}}).forEach(function(el){for (var i in el.reviews){
                                                             el.reviews[i].scoreDate = new Date(el.reviews[i].scoreDate);
                                                             el.reviews[i].review.date = new Date(el.reviews[i].review.date);
                                                         } db.app.save(el)});

db.user.find({reviews: {$ne: null}}).forEach(function(el){for (var i in el.reviews){
                                                             el.reviews[i].scoreDate = new Date(el.reviews[i].scoreDate);
                                                             el.reviews[i].review.date = new Date(el.reviews[i].review.date);
                                                         } db.user.save(el)});
'''


