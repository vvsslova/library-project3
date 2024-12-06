package com.github.vvsslova.libraryrest.util.hashing;

import org.springframework.stereotype.Component;

@Component
public class IDHashing {
    private static final int SECRET_KEY = 6543570;

    public static int hashingId(int id) {
        return id ^ SECRET_KEY;
    }
}
