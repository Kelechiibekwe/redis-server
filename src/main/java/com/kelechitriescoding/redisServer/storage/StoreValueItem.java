package com.kelechitriescoding.redisServer.storage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class StoreValueItem {
    @NonNull
    String value;
    long expireTime = -1L; // -1L indicates expiry not set
}