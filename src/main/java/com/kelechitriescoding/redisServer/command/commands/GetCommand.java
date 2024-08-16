package com.kelechitriescoding.redisServer.command.commands;

import com.kelechitriescoding.redisServer.command.Command;
import com.kelechitriescoding.redisServer.storage.KeyValueStore;
import com.kelechitriescoding.redisServer.storage.StoreValueItem;
import com.kelechitriescoding.redisServer.parsing.RESPParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GetCommand implements Command {

    private static final Map<String, StoreValueItem> keyValueStore = KeyValueStore.getInstance();

    @Override
    public String execute(Object command) {
        String key = (String) ((Object[]) command)[1];
        StoreValueItem valueItem = keyValueStore.get(key);
        String value = null;

        if (valueItem != null) {
            long currentTimeMillis = System.currentTimeMillis();
            long expiryTime = valueItem.getExpireTime();

            // If the item is not expired or has no expiry time, return its value.
            if (expiryTime == -1L || expiryTime > currentTimeMillis) {
                value = valueItem.getValue();
            }
            // If the item is expired, remove it from the store.
            else if (expiryTime < currentTimeMillis) {
                keyValueStore.remove(key);
            }
        }
        return RESPParser.serialize(value);
    }
}
