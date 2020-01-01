package com.service;

import java.util.HashMap;

public class GlobalVariableService {
    private static HashMap<String, SignState> globalObjectMap = new HashMap<>();
    //public static final String GLOBAL_USER_NAME = "RAMESH"; // GLOBAL VARIABLE
    //public static final int ID = 100; // GLOBAL VARIABLE

    public static SignState getStateByUid(String key) {
        return globalObjectMap.remove(key);
    }

    // Put data in global cache variable
    public static void putStateByUid(String key, SignState signState) {
        globalObjectMap.put(key, signState);
    }
}