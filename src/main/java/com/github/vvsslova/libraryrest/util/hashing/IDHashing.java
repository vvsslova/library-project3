package com.github.vvsslova.libraryrest.util.hashing;

import org.springframework.stereotype.Component;

@Component
public class IDHashing {
    private static final int SECRET_KEY = 6543570;

    public static int toHashedId(int originalId) {
        return originalId ^ SECRET_KEY;
    }

    public static int toOriginalId(int hashedId) {
        return hashedId ^ SECRET_KEY;
    }
}
