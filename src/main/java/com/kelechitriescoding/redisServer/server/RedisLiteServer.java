package com.kelechitriescoding.redisServer.server;

import com.kelechitriescoding.redisServer.command.CommandHandler;
import com.kelechitriescoding.redisServer.parsing.RESPParser;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@Component
public class RedisLiteServer {

    @PostConstruct
    public void start() {
        boolean listening = true;
        try (ServerSocket serverSocket = new ServerSocket(6379)) {
            log.info("Redis Lite server is running on port 6379...");

            while (listening) {
                try {
                    Socket socket = serverSocket.accept();
                    log.info("Accepted connection from {}", socket.getRemoteSocketAddress());
                    handleClient(socket);
                } catch (Exception e) {
                    log.error("Exception occurred while accepting connection: ", e);
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while starting server: ", e);
        }
    }

    private void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            log.info("Handling client from {}", socket.getRemoteSocketAddress());

            String line = in.readLine();
            if (line != null) {
                log.debug("Received line: {}", line);

                StringBuilder commandBuilder = new StringBuilder(line).append("\r\n");
                while (in.ready()) {
                    line = in.readLine();
                    log.debug("Appending line: {}", line);
                    commandBuilder.append(line).append("\r\n");
                }

                String commandString = commandBuilder.toString();
                log.debug("Full command: {}", commandString);

                Object command = RESPParser.deserialize(commandString);
                log.debug("Deserialized command: {}", command);

                String response = handleCommand(command);
                log.debug("Response: {}", response);

                out.write(response.getBytes());
                out.flush();
                log.info("Response sent to client from {}", socket.getRemoteSocketAddress());
            }
        } catch (Exception e) {
            log.error("Exception occurred while handling client: ", e);
        } finally {
            try {
                socket.close();
                log.info("Closed connection to {}", socket.getRemoteSocketAddress());
            } catch (Exception e) {
                log.error("Exception occurred while closing socket: ", e);
            }
        }
    }

    public static String handleCommand(Object command) {
        if (CommandHandler.isPingCommand(command)) {
            log.info("Processing PING command");
            return CommandHandler.handlePingCommand();
        } else if (CommandHandler.isSetCommand(command)) {
            log.info("Processing SET command");
            return CommandHandler.handleSetCommand(command);
        } else if (CommandHandler.isGetCommand(command)) {
            log.info("Processing GET command");
            return CommandHandler.handleGetCommand(command);
        } else {
            log.warn("Unknown command: {}", command);
            return RESPParser.serialize("Unknown command");
        }
    }
}
