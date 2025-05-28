package edu.sena.bibliotecaspring_1.util;

import org.slf4j.LoggerFactory;

public class Logger {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Logger.class);

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message, Throwable e) {
        logger.error(message, e);
    }
}