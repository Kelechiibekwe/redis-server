package com.kelechitriescoding.redisServer.command.commands;

import com.kelechitriescoding.redisServer.command.Command;
import com.kelechitriescoding.redisServer.storage.KeyValueStore;
import com.kelechitriescoding.redisServer.storage.StoreValueItem;
import com.kelechitriescoding.redisServer.parsing.RESPParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ExistsCommand implements Command {

    private static final Map<String, StoreValueItem> keyValueStore = KeyValueStore.getInstance();

    @Override
    public String execute(Object command) {
        Object[] commandArray = (Object[]) command;
        int keyCount = 0;

        for (int index = 1; index < commandArray.length; index++) {
            Object key = commandArray[index];
            // Check if it exists in keyValueStore.
            if (keyValueStore.containsKey(key)) {
                keyCount++;
            }
        }
        return RESPParser.serialize(keyCount);
    }
}
