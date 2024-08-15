# Redis Lite Server

A lightweight Redis-like server implemented in Java using Spring Boot, demonstrating skills in network programming, RESP protocol handling, and Test-Driven Development (TDD).

## Features

- **RESP message handling:**
  - Supports serialization and deserialization of RESP (Redis Serialization Protocol) messages.
  
- **Basic command support:**
  - **PING:** Responds with PONG.
  - **SET:** Stores a key-value pair with optional expiration times.
  - **GET:** Retrieves the value associated with a key.

- **Expiration options for SET command:**
  - **EX:** Set the specified expire time, in seconds.
  - **PX:** Set the specified expire time, in milliseconds.
  - **EXAT:** Set the specified Unix time at which the key will expire, in seconds.
  - **PXAT:** Set the specified Unix time at which the key will expire, in milliseconds.

- **Logging:**
  - Logs server status, commands, and errors for easier debugging and monitoring.

## Skills Demonstrated

- **Java & Spring Boot:**
  - Building scalable server applications using modern frameworks.
  
- **Network Programming:**
  - Managing socket connections to handle multiple client connections concurrently.

- **RESP Protocol:**
  - Implementing message serialization and deserialization to handle Redis-like commands.

- **TDD:**
  - Ensuring functionality through comprehensive testing using JUnit and other testing frameworks.

## Getting Started
