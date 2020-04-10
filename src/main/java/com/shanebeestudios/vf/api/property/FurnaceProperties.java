package com.shanebeestudios.vf.api.property;

import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Properties for {@link Furnace Furnaces}
 * <p>Used to manipulate the speed properties of a furnace</p>
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class FurnaceProperties extends Properties implements Keyed, ConfigurationSerializable {

    /**
     * Pre-made property to mimic a vanilla Minecraft furnace
     * <p>Cook speed = 1.0
     * <br>Fuel speed = 1.0</p>
     */
    public static final FurnaceProperties FURNACE = build("furnace", 1.0, 1.0);
    /**
     * Pre-made property to mimic a vanilla Minecraft blast furnace
     * <p>Cook speed = 2.0
     * <br>Fuel speed = 1.0</p>
     */
    public static final FurnaceProperties BLAST_FURNACE = build("blast_furnace", 2.0, 1.0);
    /**
     * Pre-made property to mimic a vanilla Minecraft smoker
     * <p>Cook speed = 2.0
     * <br>Fuel speed = 1.0</p>
     */
    public static final FurnaceProperties SMOKER = build("smoker", 2.0, 1.0);

    private static FurnaceProperties build(String key, double cookX, double fuelX) {
        return new FurnaceProperties("properties_" + key).cookMultiplier(cookX).fuelMultiplier(fuelX);
    }

    private static FurnaceProperties getProperty(String key) {
        for (NamespacedKey namespacedKey : KEY_MAP.keySet()) {
            if (namespacedKey.getKey().equalsIgnoreCase(key)) {
                return (FurnaceProperties) KEY_MAP.get(namespacedKey);
            }
        }
        return null;
    }

    private double cookX;
    private double fuelX;

    /** Create a new property for use in a {@link Furnace}
     * @param key Key for this property
     */
    public FurnaceProperties(String key) {
        super(Util.getKey(key.toLowerCase()));
        this.cookX = 1.0;
        this.fuelX = 1.0;
        //KEY_MAP.put(this.key, this);
    }

    /** Set the cook speed multiplier for this property
     * @param amount Speed multiplier to set
     * @return Returns an instance of itself with the speed multiplier changed
     */
    public FurnaceProperties cookMultiplier(double amount) {
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
    public FurnaceProperties fuelMultiplier(double amount) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FurnaceProperties that = (FurnaceProperties) o;
        return Double.compare(that.cookX, cookX) == 0 && Double.compare(that.fuelX, fuelX) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cookX, fuelX);
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

    public static FurnaceProperties deserialize(Map<String, Object> args) {
        String stringKey = ((String) args.get("key")).split(":")[1];
        double cook = (double) args.get("cookX");
        double fuel = (double) args.get("fuelX");
        FurnaceProperties furnaceProperties = getProperty(stringKey);
        if (furnaceProperties != null) {
            return furnaceProperties;
        } else {
            return new FurnaceProperties(stringKey).cookMultiplier(cook).fuelMultiplier(fuel);
        }
    }

}
