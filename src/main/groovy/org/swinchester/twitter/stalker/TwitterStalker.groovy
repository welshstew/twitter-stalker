package org.swinchester.twitter.stalker

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import twitter4j.IDs
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.User
import twitter4j.conf.ConfigurationBuilder

/**
 * Created by swinchester on 21/01/2016.
 */
class TwitterStalker {

    Logger log = LoggerFactory.getLogger(TwitterStalker)

    public void stalk(String twitterHandle, String fileLocation, List attributeList,
                      String oAuthConsumerKey, String oAuthConsumerSecret,
                      String oAuthAccessToken, String oAuthAccessTokenSecret){

        File f = new File(fileLocation)

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(oAuthConsumerKey)
                .setOAuthConsumerSecret(oAuthConsumerSecret)
                .setOAuthAccessToken(oAuthAccessToken)
                .setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        User twitterUser = null
        User twitterFollower = null

        RetryUtil.retry(15, 120000) {
            twitterUser = twitter.showUser(twitterHandle)
        }

        IDs ids
        long cursor = -1;
        RetryUtil.retry(15, 120000) {
            log.info("Getting followers")
            ids = twitter.getFollowersIDs(twitterHandle, cursor);
            log.info("Got followers : " + ids.getIDs().length)
        }

        int userCount = 1
        for (long id : ids.getIDs()) {
            RetryUtil.retry(15, 120000) {
                twitterFollower = twitter.showUser(id);
                log.info("Writing user: ${id} (${userCount}/${ids.getIDs().length})")
                StringBuilder sb = new StringBuilder()
                attributeList.each { att ->
                    sb.append("\"" + getProperty(twitterFollower, att) + "\",")
                }
                String line = sb.toString().substring(0, sb.size()-1)
                f.append(line + "\n")
                userCount++
            }
        }
    }

    def getProperty(object, String property) {

        property.tokenize('.').inject object, {obj, prop ->
            try {
                return obj[prop].toString().replaceAll("\n", "")
            }catch(Exception){
                return ''
            }
        }
    }
}
