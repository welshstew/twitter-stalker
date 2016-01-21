def attList = ['id',
               'screenName',
               'urlEntity.expandedURL',
               'name',
               'location',
               'description',
               'timeZone',
               'followersCount',
               'friendsCount']

def oAuthConsumerKey = ""
def oAuthConsumerSecret = ""
def oAuthAccessToken = ""
def oAuthAccessTokenSecret = ""
String fileOutputPath = ""
String twitterUserId = ""

String fileName = twitterUserId + '-' + new Date().format('yyyyMMdd') + '.csv'
String filePath = fileOutputPath + '/' + fileName

def ts = new org.swinchester.twitter.stalker.TwitterStalker()
ts.stalk(twitterUserId, filePath , attList, oAuthConsumerKey, oAuthConsumerSecret, oAuthAccessToken, oAuthAccessTokenSecret)