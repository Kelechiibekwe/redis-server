package com.kelechitriescoding.redisServer.server;

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
        try (ServerSocket serverSocket = new ServerSocket(6379)) {
            log.info("Redis Lite server is running on port 6379...");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    handleClient(socket);
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while starting server: ",e);
        }
    }

    public void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            String line = in.readLine();
            if (line != null && line.startsWith("*1")) {
                String bulkLength = in.readLine(); // should be $4
                String command = in.readLine();    // should be PING
                if ("$4".equals(bulkLength) && "PING".equalsIgnoreCase(command)) {
                    out.write("+PONG\r\n".getBytes());
                    out.flush();
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while handling client: ",e);
        }
    }


}
