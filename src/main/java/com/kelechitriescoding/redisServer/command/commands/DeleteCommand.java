package com.kelechitriescoding.redisServer.command.commands;

import com.kelechitriescoding.redisServer.command.Command;
import com.kelechitriescoding.redisServer.parsing.RESPParser;
import com.kelechitriescoding.redisServer.storage.KeyValueStore;
import com.kelechitriescoding.redisServer.storage.StoreValueItem;

import java.util.Map;

public class DeleteCommand implements Command {

    private static final Map<String, StoreValueItem> keyValueStore = KeyValueStore.getInstance();

    @Override
    public String execute(Object command){
        Object[] commandArr = (Object[]) command;
        int removedKeyCount = 0;

        for(int index = 1; index < commandArr.length; index++){
            Object key = commandArr[index];
            if(keyValueStore.remove(key)!=null){
                removedKeyCount++;
            };
        }
        return RESPParser.serialize(removedKeyCount);
    }

}
