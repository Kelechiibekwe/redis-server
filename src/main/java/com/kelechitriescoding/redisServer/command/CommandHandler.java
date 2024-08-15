package com.kelechitriescoding.redisServer.command;

import com.kelechitriescoding.redisServer.parsing.RESPParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CommandHandler {

    private static final Map<String, StoreValueItem> keyValueStore = new ConcurrentHashMap<>();

    public static boolean isPingCommand(Object command) {
        return command instanceof Object[] && ((Object[]) command).length == 1 && "PING".equalsIgnoreCase((String) ((Object[]) command)[0]);
    }

    public static boolean isSetCommand(Object command) {
        return command instanceof Object[] && ((Object[]) command).length >= 3 && "SET".equalsIgnoreCase((String) ((Object[]) command)[0]);
    }

    public static boolean isGetCommand(Object command) {
        return command instanceof Object[] && ((Object[]) command).length == 2 && "GET".equalsIgnoreCase((String) ((Object[]) command)[0]);
    }

    public static String handlePingCommand() {
        return RESPParser.serialize("PONG");
    }

    public static String handleSetCommand(Object command) {
        Object[] commandArray = (Object[]) command;
        int length = commandArray.length;
        String key = (String) commandArray[1];
        String value = (String) commandArray[2];

        if (length == 3) {
            StoreValueItem valueItem = new StoreValueItem(value);
            keyValueStore.put(key, valueItem);
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            StoreValueItem valueItem = createStoreValueItem(commandArray, currentTimeMillis, value);
            keyValueStore.put(key, valueItem);
        }
        return RESPParser.serialize("OK");
    }

    private static StoreValueItem createStoreValueItem(Object[] command, long currentTimeMillis, String value) {
        String expireTimeStr = (String) command[4];
        String expirationType = (String) command[3];
        long expireTime = calculateExpiryTime(expirationType, expireTimeStr, currentTimeMillis);
        return new StoreValueItem(value, expireTime);
    }

    private static long calculateExpiryTime(String expirationType, String expireTimeStr, long currentTimeMillis) {
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

    public static String handleGetCommand(Object command) {
        String key = (String) ((Object[]) command)[1];
        StoreValueItem valueItem = keyValueStore.get(key);
        String value = null;

        if (valueItem != null) {
            long currentTimeMillis = System.currentTimeMillis();
            long expiryTime = valueItem.getExpireTime();

            // Check if the item is not expired or has no expiry time
            if (expiryTime == -1L || expiryTime > currentTimeMillis) {
                value = valueItem.getValue();
            }
            // Remove the item if it is expired
            else if (expiryTime < currentTimeMillis) {
                keyValueStore.remove(key);
            }
        }
        return RESPParser.serialize(value);
    }
}

@Data
@AllArgsConstructor
@RequiredArgsConstructor
class StoreValueItem {

    @NonNull
    String value;
    long expireTime = -1L;//-1L indicates expiry not set

}
