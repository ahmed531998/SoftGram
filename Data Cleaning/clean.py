import pandas
import json
import math


def getDuplicates(df, column):
    dups = df.pivot_table(index=[column], aggfunc='size')
    dup = []
    for index, value in dups.items():
        if value > 1:
            dup.append(index)
    return dup

def removeCols(df, cols):
    modDF = df.drop(cols, axis=1)
    return modDF


def removeDups(df, col, keepVal):
    newDF = df.drop_duplicates(col, keep=keepVal)
    return newDF

def removeRowsWithValue(df, col, val):
    newDF = df[df[col] != val]
    return newDF

def mergeApps (dfSmall, dfBig, mergeKey):
    for index, row in dfSmall.iterrows():
        exists = row[mergeKey] in dfBig[mergeKey].values

        
        if exists:
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Ad Supported'] = row['Ad Supported']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Released'] = row['Released']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'App Name'] = row['App Name']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Rating'] = row['Rating']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Price'] = row['Price']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Category'] = row['Category']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Rating Count'] = row['Rating Count']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Installs'] = row['Installs']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Size'] = row['Size']
            dfBig.loc[dfBig[mergeKey] == row[mergeKey], 'Content Rating'] = row['Content Rating'] 
        else:
            d = {'App Id': [row['App Id']], 'Ad Supported': [row['Ad Supported']],
                 'Released': [row['Released']], 'App Name': [row['App Name']],
                 'Rating': [row['Rating']], 'Price': [row['Price']],
                 'Category': [row['Category']], 'Rating Count': [row['Rating Count']],
                 'Installs': [row['Installs']], 'Size': [row['Size']],
                 'Content Rating': [row['Content Rating']], 'Currency': [None],
                 'Developer Id': [None], 'Developer Email': [None], 'Developer Website': [None], 'Last Updated': [None],
                 'In App Purchases': [None]}
            df = pandas.DataFrame(data=d)
            dfBig = dfBig.append(df)
    return dfBig
        
def mergeUsers(developers, users):
    for index, value in users.items():
        exists = value in developers['Developer Id'].values
        if not exists:
            d = {'Developer Id': value, 'Developer Email': [None],
                 'Developer Website': [None], 'Role': ['Normal User']}
            df = pandas.DataFrame(data=d)
            developers = developers.append(df)
    return developers


def get_entity_dict(df, dictCreator, user=False, revDF=[]):
    records = []    
    for index, row in df.iterrows():
        Reviews = []
        if not user:
            Reviews = getReviews(row['App Id'], revDF, 'app_Id')
        else:
            if 'Developer Id' not in row:
                Reviews = getReviews(row['userName'], revDF, 'userName')
        rec = dictCreator(index, row, Reviews)
        records.append(rec)
    return records

def getReviews(idValue, revDF, colID):
    Reviews = revDF[revDF[colID] == idValue]
    return Reviews


def createReviewDoc(row, app=False):
    rec = {}
    rec['review'] = {'content': None if pandas.isnull(row['content']) else row['content'],
                     'date': None if pandas.isnull(row['at']) else row['at']}
    if app:
        rec['username'] = None if 'userName' not in row or ('userName' in row and pandas.isnull(row['userName'])) else row ['userName']
    else:
        rec['appId'] = None if 'app_Id' not in row or ('app_Id' in row and pandas.isnull(row['app_Id'])) else row ['app_Id']
    rec['score'] = None if 'score' not in row or ('score' in row and pandas.isnull(row['score'])) else row ['score']
    rec['scoreDate'] = None if 'at' not in row or ('at' in row and pandas.isnull(row['at'])) else row ['at']
    return rec

    
def createAppDict(index, row, appReviews=[]):
    rec = {}
    rec['_id'] = row['App Id']
    rec['adSupported'] = None if pandas.isnull(row['Ad Supported']) else row['Ad Supported']
    rec['released'] = None if pandas.isnull(row['Released']) else row['Released']
    rec['name'] = None if pandas.isnull(row['App Name']) else row['App Name']
    rec['price'] = None if pandas.isnull(row['Price']) else row['Price']
    rec['category'] = None if pandas.isnull(row['Category']) else row['Category']
    rec['ratingCount'] = None if pandas.isnull(row['Rating Count']) else row['Rating Count']
    rec['installCount'] = None if pandas.isnull(row['Installs']) else row['Installs']
    rec['size'] = None if pandas.isnull(row['Size']) else row['Size']
    rec['ageGroup'] = None if pandas.isnull(row['Age Group']) else row['Age Group']
    rec['currency'] = None if pandas.isnull(row['Currency']) else row['Currency']
    rec['lastUpdated'] = None if pandas.isnull(row['Last Updated']) else row['Last Updated']
    rec['inAppPurchase'] = None if pandas.isnull(row['In App Purchases']) else row['In App Purchases']
    rec['developer'] = {'developerId': None if pandas.isnull(row['Developer Id']) else row['Developer Id'],
                        'developerEmail': None if pandas.isnull(row['Developer Email']) else row['Developer Email'],
                        'developerWebsite': None if pandas.isnull(row['Developer Website']) else row['Developer Website']}

    reviewList = []
    for _, review in appReviews.iterrows():
        reviewList.append(createReviewDoc(review, app=True))

    rec['reviews'] = None if not reviewList else reviewList

    
    return rec

    
def createUserDict(index, row, userReviews=[]):
    rec = {}
    rec['_id'] = row['Developer Id'] if 'Developer Id' in row else row['userName']
    
    rec['birthday'] = None if 'Birthday' not in row or ('Birthday' in row and pandas.isnull(row['Birthday'])) else row ['Birthday']
    rec['email'] = None if 'Developer Email' not in row or ('Developer Email' in row and pandas.isnull(row['Developer Email'])) else row ['Developer Email']
    rec['website'] = None if 'Developer Website' not in row or ('Developer Website' in row and pandas.isnull(row['Developer Website'])) else row ['Developer Website']
    rec['role'] = 'Developer' if 'Developer Id' in row else 'Normal User'
    rec['password'] = None if 'Password' not in row or ('Password' in row and pandas.isnull(row['Password'])) else row ['Password']
    rec['Country'] = None if 'Country' not in row or ('Country' in row and pandas.isnull(row['Country'])) else row ['Country']

    reviewList = []
    if 'Developer Id' not in row:
        for _, review in userReviews.iterrows():
            reviewList.append(createReviewDoc(review, app=False))

    rec['reviews'] = None if not reviewList else reviewList

    return rec



def writeToJson(filename, dataList):
    with open(filename, 'w') as fp:
        json.dump(dataList, fp)




bigApps = pandas.read_csv('appInfo.csv')
smallApps = pandas.read_csv('Apps.csv')
reviews = pandas.read_csv('Reviews.csv')

#Removing the useless attributes
bigAppsWithoutUselessCols = removeCols(bigApps, ['Minimum Installs', 'Maximum Installs', 'Minimum Android', 'Privacy Policy', 'Editors Choice', 'Free'])
smallAppsWithoutUselessCols = removeCols(smallApps, ['free', 'url', 'containsAds', 'contentRatingDescription', 'reviews'])
reviewsWithoutUselessCols= removeCols(reviews, ['reviewId', 'userImage', 'thumbsUpCount', 'replyContent', 'repliedAt'])

#removing duplicates
bigAppsUnique = removeDups(bigAppsWithoutUselessCols, 'App Name', 'first')
smallAppsUnique = removeDups(smallAppsWithoutUselessCols, 'appId', 'first')

#removing anonymous reviews
reviewsWithoutAnon = removeRowsWithValue(reviewsWithoutUselessCols, 'userName', 'A Google user')
reviewsWithoutAnon = removeRowsWithValue(reviewsWithoutAnon, 'userName', 'A Google User')

#extracting the user
users = reviewsWithoutAnon['userName']
uniqueUsers = users.drop_duplicates(keep='first')

#extracting the developers
developers = bigAppsUnique[['Developer Id', 'Developer Website', 'Developer Email']]
uniqueDevelopers = removeDups(developers, 'Developer Id', 'first')
uniqueDevelopers['Role']='Developer'

globalUsers = mergeUsers(uniqueDevelopers, uniqueUsers)
usersDF = globalUsers.rename(columns={'Developer Id': 'userName'},errors='raise')


smallAppsNewScheme = smallAppsUnique.rename(columns={'appId': 'App Id', 'adSupported': 'Ad Supported', 'released': 'Released',
                                                     'title': 'App Name', 'score': 'Rating', 'price':'Price', 'genre':'Category',
                                                     'ratings':'Rating Count', 'installs': 'Installs', 'size':'Size', 'contentRating':'Content Rating'},
                                            errors='raise')


result = mergeApps(smallAppsNewScheme, bigAppsUnique, 'App Id')





reviewsDF = reviewsWithoutAnon
appsDF = result.rename(columns={'Content Rating':'Age Group'},errors='raise')

appsDF.to_csv('apps.csv')
usersDF.to_csv('users.csv')
reviewsDF.to_csv('reviews.csv')


apps = get_entity_dict(appsDF, createAppDict, user=False, revDF=reviewsDF)
writeToJson('app.json', apps)

Users = get_entity_dict(usersDF, createUserDict, user=True, revDF=reviewsWithoutAnon)
writeToJson('user.json', Users)





