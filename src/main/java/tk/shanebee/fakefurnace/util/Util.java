package tk.shanebee.fakefurnace.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import tk.shanebee.fakefurnace.FakeFurnace;

public class Util {

    public static String getColString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(FakeFurnace.getPlugin(), key);
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
        String[] version = Bukkit.getServer().getBukkitVersion().split("-")[0].split("\\.");
        int maj = Integer.parseInt(version[0]);
        int min = Integer.parseInt(version[1]);
        int rev;
        try {
            rev = Integer.parseInt(version[2]);
        } catch (Exception ignore) {
            rev = 0;
        }
        return maj >= major && min >= minor && rev >= revision;
    }

    /**
     * Log a message to console
     * <p>This message will be prefixed with the plugin's name</p>
     *
     * @param message Message to log
     */
    public static void log(String message) {
        log(Bukkit.getConsoleSender(), message);
    }

    /**
     * Log a message to a player/console
     * <p>This message will be prefixed with the plugin's name</p>
     *
     * @param sender  player/console to reclieve message
     * @param message Message to log
     */
    public static void log(CommandSender sender, String message) {
        String prefix = "&7[&bFake&3Furnace&7] ";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

}
