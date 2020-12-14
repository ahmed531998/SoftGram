import pandas


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


##["App Id", "Add Supported", "Released", "App Name", "Rating", "Price", "Category",
##                               "Rating Count", "Installs", "Size", "Content Rating", "Currency", "Developer Id",
##                               "Last Updated", "In App Purchases"]
##d = {'App Id': [1,2,3], 'Add Supported': [True,False,True], 'Released': [25,27,30], 'App Name': ["ahmed", "mohamed", "mirco"], 'Rating': [3.8,4,2],
##     'Price': [0,50,0], 'Category': ["Action", "Game", "cook"], 'Rating Count': [1000,2000,1500], 'Installs': [2000,4000,2000], 'Size': [70,100,35],
##     'Content Rating': ["a", "a", "a"], 'Currency': ["usd", "usd", "usd"],'Developer Id': [1,2,3], 'Last Updated': [3,4,5],
##     'In App Purchases': [False, False, False]}
##dfBig = pandas.DataFrame(data=d)
##
##
##d1 = {'App Id': [1,2,4], 'Add Supported': [True,False,True], 'Released': [27,27,30], 'App Name': ["ahmed", "mohamed", "mario"], 'Rating': [3.8,4,2],
##     'Price': [0,50,0], 'Category': ["Action", "Game", "cook"], 'Rating Count': [1000,2000,1500], 'Installs': [2000,4000,2000], 'Size': [70,100,35],
##     'Content Rating': ["a", "a", "a"]}
##dfSmall = pandas.DataFrame(data=d1)
##
##result = merge(dfSmall, dfBig, 'App Id')
##print(result)



def merge (dfSmall, dfBig, mergeKey):
    for index, row in dfSmall.iterrows():
        exists = row[mergeKey] in dfBig[mergeKey]

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
                 'Content Rating': [row['Content Rating']], 'Currency': [""],
                 'Developer Id': [""], 'Last Updated': [""],
                 'In App Purchases': [""]}
            df = pandas.DataFrame(data=d)
            dfBig = dfBig.append(df)
            
    return dfBig
        


bigApps = pandas.read_csv("appInfo.csv")
smallApps = pandas.read_csv("Apps.csv")
reviews = pandas.read_csv("Reviews.csv")


bigAppsWithoutUselessCols = removeCols(bigApps, ['Minimum Installs', 'Maximum Installs', 'Minimum Android', 'Privacy Policy', "Editors Choice", 'Free'])
smallAppsWithoutUselessCols = removeCols(smallApps, ['free', 'url', 'containsAds', 'contentRatingDescription', 'reviews'])
reviewsWithoutUselessCols= removeCols(reviews, ['reviewId', 'userImage', 'thumbsUpCount', 'replyContent', 'repliedAt'])



bigAppsUnique = removeDups(bigAppsWithoutUselessCols, 'App Name', 'first')
smallAppsUnique = removeDups(smallAppsWithoutUselessCols, 'appId', 'first')


reviewsWithoutAnon = removeRowsWithValue(reviewsWithoutUselessCols, 'userName', 'A Google user')
reviewsWithoutAnon = removeRowsWithValue(reviewsWithoutAnon, 'userName', 'A Google User')

users = reviewsWithoutAnon["userName"]
uniqueUsers = users.drop_duplicates(keep='first')


developers = bigAppsUnique[["Developer Id", "Developer Website", "Developer Email"]]
uniqueDevelopers = removeDups(developers, 'Developer Id', 'first')


bigAppsWithoutDevelopers = removeCols(bigAppsUnique, ['Developer Website', 'Developer Email'])




smallAppsNewScheme = smallAppsUnique.rename(columns={"appId": "App Id", "adSupported": "Ad Supported", "released": "Released",
                                                     "title": "App Name", "score": "Rating", "price":"Price", "genre":"Category",
                                                     "ratings":"Rating Count", "installs": "Installs", "size":"Size", "contentRating":"Content Rating"},
                                            errors="raise")


result = merge(smallAppsNewScheme, bigAppsWithoutDevelopers, 'App Id')

apps = result.rename(columns={"Content Rating":"Age Group"},errors="raise")



uniqueUsers.to_csv('users.csv', sep=';')
uniqueDevelopers.to_csv('developers.csv', sep=';')
reviewsWithoutAnon.to_csv('reviews.csv', sep=';')
apps.to_csv('apps.csv', sep=';')



