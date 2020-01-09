package com.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class GlobalVariableService {
    private static HashMap<String, SignState> globalObjectMap = new HashMap<>();
    private static Queue<GlobalObjectRemoveMapper> globalObjectRemoveMapperQueue = new LinkedList<>();
    private static Date lastCacheClearCallTime = new Date();
    private static final int CacheClearTimeDiffInMin = 1;

    public static SignState getStateByUid(String key) {
        return globalObjectMap.remove(key);
    }

    // Put data in global cache variable
    public static void putStateByUid(String key, SignState signState) {
        //Ensuring cache is being cleared after each five minutes
        if(((new Date()).getTime() - lastCacheClearCallTime.getTime()) / (60 * 1000) > CacheClearTimeDiffInMin) {
            //Call the cache clear function here cause time of cache clear exceeds
            try {
                Date cacheCleaningCapTime = new Date(System.currentTimeMillis() - CacheClearTimeDiffInMin *60*1000);
                while (cacheCleaningCapTime.compareTo(globalObjectRemoveMapperQueue.peek().creationTime) > 0) {       //Top queue object creation time is after LastCacheClearCallTime
                    GlobalObjectRemoveMapper removeMapper = globalObjectRemoveMapperQueue.remove();
                    try {
                        globalObjectMap.remove(removeMapper.hash);
                    }
                    catch (Exception ex) {
                        System.out.println(ex.toString());
                    }
                }
            }
            catch (Exception ex) {
                System.out.println(ex.toString());
            }
            //Update last cache clear execution time after all ends
            lastCacheClearCallTime = new Date();
        }
        globalObjectMap.put(key, signState);
        globalObjectRemoveMapperQueue.add(new GlobalObjectRemoveMapper(key));
        System.out.println("Test OK");
    }
}