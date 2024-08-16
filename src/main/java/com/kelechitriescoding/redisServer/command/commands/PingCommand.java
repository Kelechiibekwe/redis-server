package com.kelechitriescoding.redisServer.command.commands;

import com.kelechitriescoding.redisServer.command.Command;
import com.kelechitriescoding.redisServer.parsing.RESPParser;

public class PingCommand implements Command {

    @Override
    public String execute(Object command) {
        return RESPParser.serialize("PONG");
    }
}
