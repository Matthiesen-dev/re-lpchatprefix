package dev.matthiesen.common.relpchatprefix;

import net.kyori.adventure.text.format.NamedTextColor;
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

    public static NamedTextColor getAdventureColor(String color) {
        color = color.toLowerCase();
        return switch (color) {
            case "black" -> NamedTextColor.BLACK;
            case "dark_blue" -> NamedTextColor.DARK_BLUE;
            case "dark_green" -> NamedTextColor.DARK_GREEN;
            case "dark_aqua" -> NamedTextColor.DARK_AQUA;
            case "dark_red" -> NamedTextColor.DARK_RED;
            case "dark_purple" -> NamedTextColor.DARK_PURPLE;
            case "gold" -> NamedTextColor.GOLD;
            case "gray" -> NamedTextColor.GRAY;
            case "dark_gray" -> NamedTextColor.DARK_GRAY;
            case "blue" -> NamedTextColor.BLUE;
            case "green" -> NamedTextColor.GREEN;
            case "aqua" -> NamedTextColor.AQUA;
            case "red" -> NamedTextColor.RED;
            case "light_purple" -> NamedTextColor.LIGHT_PURPLE;
            case "yellow" -> NamedTextColor.YELLOW;
            default -> NamedTextColor.WHITE;
        };
    }
}
