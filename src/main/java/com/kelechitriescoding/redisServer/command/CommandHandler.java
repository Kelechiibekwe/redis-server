package com.kelechitriescoding.redisServer.command;

import com.kelechitriescoding.redisServer.command.commands.*;
import com.kelechitriescoding.redisServer.parsing.RESPParser;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class CommandHandler {

    // A map to store command names and their corresponding Command objects.
    private static final Map<String, Command> commandMap = new HashMap<>();

    static {
        // Registering commands with their corresponding implementations.
        registerCommand("PING", new PingCommand());
        registerCommand("SET", new SetCommand());
        registerCommand("GET", new GetCommand());
        registerCommand("EXISTS", new ExistsCommand());
        registerCommand("DEL", new DeleteCommand());
        // Additional commands can be registered here.
    }

    // Method to register a command in the map.
    private static void registerCommand(String commandName, Command command) {
        commandMap.put(commandName.toUpperCase(), command);
    }

    // The main method for handling commands. Delegates execution to the appropriate Command.
    public static String handleCommand(Object command) {
        try {
            Object[] commandArray = (Object[]) command;
            String commandName = getCommandName(commandArray);

            Command cmd = commandMap.get(commandName);
            if (cmd != null) {
                log.debug("Processing {} command", commandName);
                return cmd.execute(command);
            } else {
                log.warn("Unknown command: {}", commandName);
                return RESPParser.serialize("Unknown command");
            }
        } catch (Exception e) {
            log.error("Exception occurred while processing command: ", e);
            return RESPParser.serialize("Error processing command");
        }
    }

    // Helper method to extract and validate the command name.
    private static String getCommandName(Object[] commandArray) {
        if (commandArray.length == 0 || !(commandArray[0] instanceof String)) {
            throw new IllegalArgumentException("Invalid command format");
        }
        return ((String) commandArray[0]).toUpperCase();
    }

}
