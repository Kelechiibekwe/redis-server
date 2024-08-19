package com.kelechitriescoding.redisServer.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {

    // Singleton instance of the key-value store.
    private static final Map<String, StoreValueItem> keyValueStore = new HashMap<>();

    private KeyValueStore() {
        // Private constructor to prevent instantiation.
    }

    public static Map<String, StoreValueItem> getInstance() {
        return keyValueStore;
    }
}



