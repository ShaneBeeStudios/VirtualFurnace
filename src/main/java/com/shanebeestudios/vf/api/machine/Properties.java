package com.shanebeestudios.vf.api.machine;

import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings({"unused", "SameParameterValue"})
public class Properties implements Keyed, ConfigurationSerializable {

    private static final Map<NamespacedKey, Properties> KEY_MAP = new HashMap<>();

    /**
     * Pre-made property to mimic a vanilla Minecraft furnace
     * <p>Cook speed = 1.0
     * <br>Fuel speed = 1.0</p>
     */
    public static final Properties FURNACE = build("furnace", 1.0, 1.0);
    /**
     * Pre-made property to mimic a vanilla Minecraft blast furnace
     * <p>Cook speed = 2.0
     * <br>Fuel speed = 1.0</p>
     */
    public static final Properties BLAST_FURNACE = build("blast_furnace", 2.0, 1.0);
    /**
     * Pre-made property to mimic a vanilla Minecraft smoker
     * <p>Cook speed = 2.0
     * <br>Fuel speed = 1.0</p>
     */
    public static final Properties SMOKER = build("smoker", 2.0, 1.0);

    private static Properties build(String key, double cookX, double fuelX) {
        return new Properties("properties_" + key).cookMultiplier(cookX).fuelMultiplier(fuelX);
    }

    private static Properties getProperty(String key) {
        for (NamespacedKey namespacedKey : KEY_MAP.keySet()) {
            if (namespacedKey.getKey().equalsIgnoreCase(key)) {
                return KEY_MAP.get(namespacedKey);
            }
        }
        return null;
    }

    private final NamespacedKey key;
    private double cookX;
    private double fuelX;

    /** Create a new property for use in a {@link Furnace}
     * @param key Key for this property
     */
    public Properties(String key) {
        this.key = Util.getKey(key.toLowerCase());
        this.cookX = 1.0;
        this.fuelX = 1.0;
        KEY_MAP.put(this.key, this);
    }

    /** Set the cook speed multiplier for this property
     * @param amount Speed multiplier to set
     * @return Returns an instance of itself with the speed multiplier changed
     */
    public Properties cookMultiplier(double amount) {
        this.cookX = amount;
        return this;
    }

    /** Get this property's cook speed multiplier
     * @return Cook speed multiplier
     */
    public double getCookMultiplier() {
        return this.cookX;
    }

    /** Set the fuel burn speed multiplier for this property
     * @param amount Speed multiplier to set
     * @return Returns an instance of itself with the speed multiplier changed
     */
    public Properties fuelMultiplier(double amount) {
        this.fuelX = amount;
        return this;
    }

    /** Get this property's fuel burn speed multiplier
     * @return Fuel burn speed multiplier
     */
    public double getFuelMultiplier() {
        return this.fuelX;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "key=" + key +
                ", cookX=" + cookX +
                ", fuelX=" + fuelX +
                '}';
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("key", this.key.toString());
        result.put("cookX", this.cookX);
        result.put("fuelX", this.fuelX);
        return result;
    }

    public static Properties deserialize(Map<String, Object> args) {
        String stringKey = ((String) args.get("key")).split(":")[1];
        double cook = (double) args.get("cookX");
        double fuel = (double) args.get("fuelX");
        Properties properties = getProperty(stringKey);
        if (properties != null) {
            return properties;
        } else {
            return new Properties(stringKey).cookMultiplier(cook).fuelMultiplier(fuel);
        }
    }

}
