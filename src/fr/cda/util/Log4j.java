package fr.cda.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe de definition du Logger
 */
public class Log4j {
    /**
     * Le logger unique et constant, il est utilise par tout le programme de maniere statique
     */
    public static final Logger logger = LogManager.getLogger(Log4j.class);
}
