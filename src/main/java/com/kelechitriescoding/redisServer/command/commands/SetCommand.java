package com.kelechitriescoding.redisServer.command.commands;

import com.kelechitriescoding.redisServer.command.Command;
import com.kelechitriescoding.redisServer.storage.KeyValueStore;
import com.kelechitriescoding.redisServer.storage.StoreValueItem;
import com.kelechitriescoding.redisServer.parsing.RESPParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class SetCommand implements Command {

    // Shared key-value store used across commands.
    private static final Map<String, StoreValueItem> keyValueStore = KeyValueStore.getInstance();

    @Override
    public String execute(Object command) {
        Object[] commandArray = (Object[]) command;
        int length = commandArray.length;
        String key = (String) commandArray[1];
        String value = (String) commandArray[2];

        // If only key and value are provided, store them directly.
        if (length == 3) {
            StoreValueItem valueItem = new StoreValueItem(value);
            keyValueStore.put(key, valueItem);
        } else {
            // Handle optional expiration parameters.
            long currentTimeMillis = System.currentTimeMillis();
            StoreValueItem valueItem = createStoreValueItem(commandArray, currentTimeMillis, value);
            keyValueStore.put(key, valueItem);
        }
        return RESPParser.serialize("OK");
    }

    // Creates a StoreValueItem with an optional expiration time.
    private StoreValueItem createStoreValueItem(Object[] command, long currentTimeMillis, String value) {
        String expireTimeStr = (String) command[4];
        String expirationType = (String) command[3];
        long expireTime = calculateExpiryTime(expirationType, expireTimeStr, currentTimeMillis);
        return new StoreValueItem(value, expireTime);
    }

    // Calculates the expiration time based on the expiration type.
    private long calculateExpiryTime(String expirationType, String expireTimeStr, long currentTimeMillis) {
        long timeMillis;
        switch (expirationType) {
            case "EP":
                timeMillis = Long.parseLong(expireTimeStr) * 1000;
                return timeMillis + currentTimeMillis;
            case "PX":
                timeMillis = Long.parseLong(expireTimeStr);
                return timeMillis + currentTimeMillis;
            case "EXAT":
                return Long.parseLong(expireTimeStr) * 1000;
            case "PXAT":
                return Long.parseLong(expireTimeStr);
            default:
                throw new IllegalArgumentException("Unknown expiration type: " + expirationType);
        }
    }
}