package com.kelechitriescoding.redisServer.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RESPParserTest {

    @Test
    public void testSerializeNull() {
        String expected = "$-1\r\n";
        assertEquals(expected, RESPParser.serialize(null));
    }

    @Test
    public void testSerializeSimpleString() {
        String expected = "+OK\r\n";
        assertEquals(expected, RESPParser.serialize("OK"));
    }

    @Test
    public void testSerializeInteger() {
        String expected = ":1000\r\n";
        assertEquals(expected, RESPParser.serialize(1000));
    }

    @Test
    public void testSerializeArray() throws Exception {
        String expected = "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n";
        Object[] deserialized = (Object[]) RESPParser.deserialize(expected);

        // Convert Object[] to String[] safely
        String[] result = new String[deserialized.length];
        for (int i = 0; i < deserialized.length; i++) {
            result[i] = (String) deserialized[i];
        }

        assertArrayEquals(new String[]{"foo", "bar"}, result);
    }

    @Test
    public void testDeserializeNullBulkString() throws Exception {
        String message = "$-1\r\n";
        assertNull(RESPParser.deserialize(message));
    }

    @Test
    public void testDeserializeSimpleString() throws Exception {
        String message = "+OK\r\n";
        assertEquals("OK", RESPParser.deserialize(message));
    }

    @Test
    public void testDeserializeInteger() throws Exception {
        String message = ":1000\r\n";
        assertEquals(1000, RESPParser.deserialize(message));
    }

    @Test
    public void testDeserializeArray() throws Exception {
        String message = "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n";
        Object[] expected = new Object[]{"foo", "bar"};
        assertArrayEquals(expected, (Object[]) RESPParser.deserialize(message));
    }
}
