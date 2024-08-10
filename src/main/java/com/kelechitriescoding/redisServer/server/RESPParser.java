package com.kelechitriescoding.redisServer.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class RESPParser {

    public static String serialize(Object obj) {
        if (obj == null) {
            return "$-1\r\n";
        } else if (obj instanceof String) {
            return "+" + obj + "\r\n";
        } else if (obj instanceof Integer) {
            return ":" + obj + "\r\n";
        } else if (obj instanceof String[]) {
            String[] array = (String[]) obj;
            StringBuilder sb = new StringBuilder();
            sb.append("*").append(array.length).append("\r\n");
            for (String element : array) {
                sb.append("$").append(element.length()).append("\r\n").append(element).append("\r\n");
            }
            return sb.toString();
        }
        throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
    }

    public static Object deserialize(String message) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(message));
        return parseRESP(reader);
    }

    private static Object parseRESP(BufferedReader reader) throws IOException {
        int firstChar = reader.read();
        if (firstChar == -1) {
            return null;
        }

        switch (firstChar) {
            case '+':
                return reader.readLine();
            case '-':
                return new RuntimeException(reader.readLine());
            case ':':
                return Integer.parseInt(reader.readLine());
            case '$':
                return parseBulkString(reader);
            case '*':
                return parseArray(reader);
            default:
                throw new IllegalArgumentException("Invalid RESP message");
        }
    }

    private static String parseBulkString(BufferedReader reader) throws IOException {
        int length = Integer.parseInt(reader.readLine());
        if (length == -1) {
            return null;
        }
        char[] buffer = new char[length];
        reader.read(buffer, 0, length);
        reader.readLine(); // consume \r\n
        return new String(buffer);
    }

    private static Object[] parseArray(BufferedReader reader) throws IOException {
        int length = Integer.parseInt(reader.readLine());
        if (length == -1) {
            return null;
        }
        Object[] array = new Object[length];
        for (int i = 0; i < length; i++) {
            array[i] = parseRESP(reader);
        }
        return array;
    }
}
