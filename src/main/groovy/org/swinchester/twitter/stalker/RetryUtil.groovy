package org.swinchester.twitter.stalker

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by swinchester on 4/01/2016.
 */
class RetryUtil {

    private static Logger log = LoggerFactory.getLogger(RetryUtil);

    static retry(int times, long sleeptime, Closure c){
        Throwable catchedThrowable = null
        for(int i=0; i<times; i++){
            try {
                return c.call()
            } catch(Throwable t){
                catchedThrowable = t
                log.warn("failed to call closure. ${i+1} of $times runs.")
                Thread.sleep(sleeptime)
            }
        }
        log.error("finally failed to call closure after $times tries.")
        throw catchedThrowable
    }
}