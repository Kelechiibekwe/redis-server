# Redis Lite Server

A lightweight Redis-like server implemented in Java using Spring Boot, built with a lot of enthusiasm and curiosity. This project was not only a fun challenge but also a great learning experience that deepened my understanding of Redis by diving into its documentation and concepts.

## Features

### RESP Message Handling
- Supports serialization and deserialization of RESP (Redis Serialization Protocol) messages, enabling communication using Redis-compatible commands.

### Basic Command Support
- **PING**: Responds with `PONG`.
- **SET**: Stores a key-value pair with optional expiration times.
- **GET**: Retrieves the value associated with a key.
- **EXISTS**: Checks if a key exists in the database.
- **DELETE**: Removes the specified key from the database.

### Expiration Options for SET Command
- **EX**: Set the specified expiration time, in seconds.
- **PX**: Set the specified expiration time, in milliseconds.
- **EXAT**: Set the specified Unix time at which the key will expire, in seconds.
- **PXAT**: Set the specified Unix time at which the key will expire, in milliseconds.

### Non-Blocking I/O
- Switched from a traditional multi-threaded approach to a non-blocking I/O model using Java NIO to improve scalability and efficiency. This allows the server to handle many connections with a single thread, reducing overhead and improving performance in high-concurrency environments.

## Benchmarking Results

The server's performance was evaluated using the `redis-benchmark` tool, focusing on `SET` and `GET` operations. Below are the results comparing the official Redis server with Redis Lite Server:

### Official Redis Server (C-based)
- **SET**: ~83,000 to 94,000 requests per second
- **GET**: ~83,000 to 96,000 requests per second

### Redis Lite Server (Java-based)
- **SET**: ~69,000 to 81,000 requests per second
- **GET**: ~69,000 to 81,000 requests per second

**Note:** While the Redis Lite Server achieves approximately 70% to 85% of the official Redis server's throughput, it demonstrates the effectiveness of Java and non-blocking I/O in handling high-concurrency scenarios.

## Skills Demonstrated

### Java & Spring Boot
- Building scalable server applications using modern frameworks and principles.

### Network Programming
- Managing socket connections using Java NIO to handle multiple client connections concurrently, showcasing efficient resource management and scalability.

### RESP Protocol
- Implementing message serialization and deserialization to handle Redis-like commands, ensuring compatibility with Redis clients.

### Test-Driven Development (TDD)
- Ensuring functionality through comprehensive testing using JUnit and other testing frameworks, providing a reliable and maintainable codebase.

## Conclusion

This project was not just about building a Redis-like server but was also a journey that enhanced my understanding of Redis itself. By reading through the Redis documentation and implementing similar features, I gained a deeper appreciation for the intricacies of its design. It was a rewarding experience that combined learning with the fun of coding.
