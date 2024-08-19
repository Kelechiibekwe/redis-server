package com.kelechitriescoding.redisServer.server;

import com.kelechitriescoding.redisServer.command.CommandHandler;
import com.kelechitriescoding.redisServer.parsing.RESPParser;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * Switched from a traditional multi-threaded approach to a non-blocking I/O model using
 * Java NIO to improve scalability and efficiency:
 *
 * 1. **Scalability**: Handles many connections with a single thread, avoiding the overhead
 *    of managing a large thread pool, which was a limitation in the previous multi-threaded approach.
 *
 * 2. **Efficiency**: Non-blocking I/O reduces idle thread time by processing connections only
 *    when I/O is ready, leading to better CPU utilization and overall performance.
 *
 * 3. **Resource Optimization**: Lowers memory and CPU usage by reducing the number of threads needed,
 *    which helps in handling high-concurrency environments more effectively.
 *
 * This approach simplifies concurrency management and boosts performance in high-load scenarios.
 */


@Slf4j
@Component
public class RedisLiteServer {

    private Selector selector;

    @PostConstruct
    public void start() {
        try {
            // Initialize the selector and server channel
            selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(6379));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            log.info("Redis Lite server is running on port 6379...");

            while (true) {
                // Wait for an event (e.g., incoming connection, data ready to read, etc.)
                selector.select();

                // Get the set of ready keys and iterate over them
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove(); // Remove key from set to avoid processing it multiple times

                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Exception occurred while starting server: ", e);
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        log.debug("Accepted connection from {}", clientChannel.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == -1) {
                clientChannel.close();
                log.debug("Closed connection to {}", clientChannel.getRemoteAddress());
                return;
            }

            buffer.flip();
            String commandString = new String(buffer.array(), 0, bytesRead).trim();
            log.debug("Received command: {}", commandString);

            Object command = RESPParser.deserialize(commandString);
            log.debug("Deserialized command: {}", command);

            String response = CommandHandler.handleCommand(command);
            log.debug("Response: {}", response);

            // Write the response back to the client
            buffer.clear();
            buffer.put(response.getBytes());
            buffer.flip();
            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }
            log.debug("Response sent to client from {}", clientChannel.getRemoteAddress());

        } catch (IOException e) {
//            log.error("Exception occurred while handling read: ", e.getMessage());
            try {
                clientChannel.close();
            } catch (IOException ex) {
                log.error("Failed to close client channel: ", ex);
            }
        }
    }
}
