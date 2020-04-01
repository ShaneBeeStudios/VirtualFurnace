package tk.shanebee.fakefurnace.util;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import tk.shanebee.fakefurnace.FakeFurnace;

public class Util {

    public static String getColString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(FakeFurnace.getPlugin(), key);
    }

}
