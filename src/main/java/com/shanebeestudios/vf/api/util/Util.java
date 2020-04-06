package com.shanebeestudios.vf.api.util;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

public class Util {

    private static final String PREFIX = "&7[&bVirtual&3Furnace&7] ";
    private static final String[] VERSION = Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.");

    /**
     * Get a string using color codes
     * <p>Mainly used internally</p>
     *
     * @param string String with color codes
     * @return New String including colors
     */
    public static String getColString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Get a NamespacedKey associated with the plugin running this API
     *
     * @param key Key to create
     * @return New NamespacedKey
     */
    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(VirtualFurnaceAPI.getInstance().getJavaPlugin(), key);
    }

    /**
     * Check if server is running a minimum Minecraft version
     *
     * @param major Major version to check (Most likely just going to be 1)
     * @param minor Minor version to check
     * @return True if running this version or higher
     */
    public static boolean isRunningMinecraft(int major, int minor) {
        return isRunningMinecraft(major, minor, 0);
    }

    /**
     * Check if server is running a minimum Minecraft version
     *
     * @param major    Major version to check (Most likely just going to be 1)
     * @param minor    Minor version to check
     * @param revision Revision to check
     * @return True if running this version or higher
     */
    public static boolean isRunningMinecraft(int major, int minor, int revision) {
        int maj = Integer.parseInt(VERSION[0]);
        int min = Integer.parseInt(VERSION[1]);
        int rev;
        try {
            rev = Integer.parseInt(VERSION[2]);
        } catch (Exception ignore) {
            rev = 0;
        }
        return maj >= major && min >= minor && rev >= revision;
    }

    /**
     * Log a message to console
     * <p>This message will be prefixed with VirtualFurnace</p>
     *
     * @param message Message to log
     */
    public static void log(String message) {
        log(Bukkit.getConsoleSender(), message);
    }

    /**
     * Log an error message to console
     *
     * @param error Error to log to console
     */
    public static void error(String error) {
        Bukkit.getLogger().severe(getColString(PREFIX + error));
    }

    /**
     * Log a message to a player/console
     * <p>This message will be prefixed with VirtualFurnace</p>
     *
     * @param sender  player/console to receive message
     * @param message Message to log
     */
    public static void log(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + message));
    }

    /**
     * Check if a class exists
     *
     * @param className The {@link Class#getCanonicalName() canonical name} of the class
     * @return True if the class exists
     */
    public static boolean classExists(final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

}
