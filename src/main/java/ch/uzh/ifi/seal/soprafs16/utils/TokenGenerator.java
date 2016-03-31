package ch.uzh.ifi.seal.soprafs16.utils;

import java.util.UUID;

/**
 * Moved to this isolated location, so that it's easier to mock.
 * I'm not very proud of that solution.
 *
 * Created by soyabeen on 31.03.16.
 */
public class TokenGenerator {

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

}
