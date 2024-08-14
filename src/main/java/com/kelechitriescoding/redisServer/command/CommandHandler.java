package com.kelechitriescoding.redisServer.command;

import com.kelechitriescoding.redisServer.parsing.RESPParser;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommandHandler {

    private static final Map<String, String> keyValueStore = new HashMap<>();

    public static boolean isPingCommand(Object command) {
        return command instanceof Object[] && ((Object[]) command).length == 1 && "PING".equalsIgnoreCase((String) ((Object[]) command)[0]);
    }

    public static boolean isSetCommand(Object command) {
        return command instanceof Object[] && ((Object[]) command).length == 3 && "SET".equalsIgnoreCase((String) ((Object[]) command)[0]);
    }

    public static boolean isGetCommand(Object command) {
        return command instanceof Object[] && ((Object[]) command).length == 2 && "GET".equalsIgnoreCase((String) ((Object[]) command)[0]);
    }

    public static String handlePingCommand() {
        return RESPParser.serialize("PONG");
    }

    public static String handleSetCommand(Object command) {
        keyValueStore.put((String) ((Object[]) command)[1], (String) ((Object[]) command)[2]);
        return RESPParser.serialize("OK");
    }

    public static String handleGetCommand(Object command) {
        String value = keyValueStore.get((String) ((Object[]) command)[1]);
        return RESPParser.serialize(value);
    }
}
