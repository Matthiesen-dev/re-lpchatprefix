package dev.matthiesen.common.relpchatprefix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
    public static final String MOD_ID = "relpchatprefix";
    public static final String ModName = "Re-LPChatPrefix";

    public static Logger LOGGER = LogManager.getLogger(ModName);

    public static void createInfoLog(String message) {
        LOGGER.info(message);
    }

    public static void createErrorLog(String message) {
        LOGGER.error(message);
    }
}
