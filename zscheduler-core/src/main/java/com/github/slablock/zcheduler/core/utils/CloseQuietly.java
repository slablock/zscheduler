
package com.github.slablock.zcheduler.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;


public class CloseQuietly {

    private static final Logger log = LoggerFactory.getLogger(CloseQuietly.class);

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            log.error("IOException thrown while closing Closeable.", e);
        }
    }
}
