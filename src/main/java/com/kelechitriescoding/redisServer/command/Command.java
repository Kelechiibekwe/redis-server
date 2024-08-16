package com.kelechitriescoding.redisServer.command;

public interface Command {
    String execute(Object command);
}

