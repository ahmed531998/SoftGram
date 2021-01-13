import pandas as pd
import json
import math
from datetime import datetime


def stringTOdate(x, dateFormat):
    return datetime.strptime(x, dateFormat)

apps = pd.read_json('app.json')
users = pd.read_json('user.json')


for index, row in users.iterrows():
    if isinstance(row['reviews'], list):
        reviews = []
        for el in row['reviews']:
            reviews.append(el)
        for i in range (0, len(reviews)):
            reviews[i]['review']['date']= pd.to_datetime(reviews[i]['review']['date'], format='%Y-%m-%d %H:%M:%S')#stringTOdate(reviews[i]['review']['date'], '%Y-%m-%d %H:%M:%S')
            reviews[i]['scoreDate'] = pd.to_datetime(reviews[i]['scoreDate'], format='%Y-%m-%d %H:%M:%S')#stringTOdate(reviews[i]['scoreDate'], '%Y-%m-%d %H:%M:%S')
        users.at[index,'reviews'] = reviews

for index, row in apps.iterrows():
    if isinstance(row['released'], str):
        apps.at[index,'released'] = pd.to_datetime(apps.at[index,'released'], format='%b %d, %Y')#stringTOdate(apps.at[index,'released'], '%b %d, %Y')
    if isinstance(row['lastUpdated'], str):
       apps.at[index,'lastUpdated'] = pd.to_datetime(apps.at[index,'lastUpdated'], format='%b %d, %Y')#stringTOdate(apps.at[index,'lastUpdated'], '%b %d, %Y')
    if isinstance(row['reviews'], list):
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
1- add passwords using the setPassword.py script
2- convert installCounts into int using this:
    db.app.updateMany({"installCount": {$exists : false}}, {$set: {"installCount": "0"}})
    db.app.find().forEach(function(doc) {doc.installCount = doc.installCount.replace(/[^0-9]/g, '');db.app.save(doc);});
    db.app.find().forEach(function(doc) {doc.installCount = parseInt(doc.installCount);db.app.save(doc);});

3- run stringToDate.py script
4- run the following on mongo:
    db.app.find({released: {$ne: null}}).forEach(function(el){el.released = new Date(el.released); db.app.save(el)});
    db.app.find({lastUpdated: {$ne: null}}).forEach(function(el){el.lastUpdated = new Date(el.lastUpdated); db.app.save(el)});                              
    db.app.find({reviews: {$ne: null}}).forEach(function(el){for (var i in el.reviews){
                                                             el.reviews[i].scoreDate = new Date(el.reviews[i].scoreDate);
                                                             el.reviews[i].review.date = new Date(el.reviews[i].review.date);
                                                         } db.app.save(el)});

    db.user.find({reviews: {$ne: null}}).forEach(function(el){for (var i in el.reviews){
                                                             el.reviews[i].scoreDate = new Date(el.reviews[i].scoreDate);
                                                             el.reviews[i].review.date = new Date(el.reviews[i].review.date);
                                                         } db.user.save(el)});

5- remove null values:
    var coll = db.getCollection("user");
    var cursor = coll.find();
    while (cursor.hasNext()) {
        var doc = cursor.next();
        var keys = {};
        var hasNull = false;
        for ( var x in doc) {
            if (x != "_id" && doc[x] == null) {
                keys[x] = 1;
                hasNull = true;
            }
        }
        if (hasNull) {
            coll.update({_id: doc._id}, {$unset:keys});
        }
    }
    var coll = db.getCollection("app");
    var cursor = coll.find();
    while (cursor.hasNext()) {
        var doc = cursor.next();
        var keys = {};
        var hasNull = false;
        for ( var x in doc) {
            if (x != "_id" && doc[x] == null) {
                keys[x] = 1;
                hasNull = true;
            }
        }
        if (hasNull) {
            coll.update({_id: doc._id}, {$unset:keys});
        }
    }
    db.app.updateMany({"developer.developerWebsite": null}, { $unset : { "developer.developerWebsite" : 1 }})
    db.app.updateMany({"developer.developerEmail": null}, { $unset : { "developer.developerEmail" : 1 }})

6- export json files
'''


