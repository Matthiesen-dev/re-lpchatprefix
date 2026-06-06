package dev.matthiesen.common.relpchatprefix;

import dev.matthiesen.common.relpchatprefix.util.MetricManager;
import dev.matthiesen.libs.faststats.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
    public static final String MOD_ID = "relpchatprefix";
    public static final String ModName = "Re-LPChatPrefix";
    public static @Token final String METRICS_TOKEN = "b34b7080ee595daa3d1ecd8dbfe6ada7";

    public static Logger LOGGER = LogManager.getLogger(ModName);

    public static void createInfoLog(String message) {
        LOGGER.info(message);
    }

    public static void createErrorLog(String message) {
        LOGGER.error(message);
    }

    public static void createErrorLog(String message, Throwable throwable) {
        MetricManager.ERROR_TRACKER.trackError(throwable);
        LOGGER.error(message, throwable);
    }
}
