package com.kelechitriescoding.redisServer.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore {

    // Singleton instance of the key-value store.
    private static final Map<String, StoreValueItem> keyValueStore = new ConcurrentHashMap<>();

    private KeyValueStore() {
        // Private constructor to prevent instantiation.
    }

    public static Map<String, StoreValueItem> getInstance() {
        return keyValueStore;
    }
}



