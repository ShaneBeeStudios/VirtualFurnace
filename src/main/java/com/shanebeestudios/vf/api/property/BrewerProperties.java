package com.shanebeestudios.vf.api.property;

import com.shanebeestudios.vf.api.machine.Brewer;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Properties for {@link Brewer Brewers}
 * <p>Used to manipulate the speed properties of a brewer</p>
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class BrewerProperties extends Properties implements ConfigurationSerializable {

    /**
     * Pre-made property to mimic a vanilla Minecraft brew stand
     * <p>Brew speed = 1.0
     * <br>Fuel speed = 1.0</p>
     */
    public static final BrewerProperties BREWER = build("brewer", 1.0, 1.0);

    private static BrewerProperties build(String key, double brewX, double fuelX) {
        return new BrewerProperties("brew_properties_" + key).brewMultiplier(brewX).fuelMultiplier(fuelX);
    }

    private static BrewerProperties getProperty(String key) {
        for (NamespacedKey namespacedKey : KEY_MAP.keySet()) {
            if (namespacedKey.getKey().equalsIgnoreCase(key)) {
                Properties property = KEY_MAP.get(namespacedKey);
                if (property instanceof BrewerProperties) {
                    return (BrewerProperties) KEY_MAP.get(namespacedKey);
                }
            }
        }
        return null;
    }

    private double brewX;
    private double fuelX;

    /** Create a new property for use in a {@link Brewer}
     * @param key Key for this property
     */
    BrewerProperties(String key) {
        super(Util.getKey(key.toLowerCase()));
        this.brewX = 1.0;
        this.fuelX = 1.0;
    }

    /** Set the brew speed multiplier for this property
     * @param amount Speed multiplier to set
     * @return Returns an instance of itself with the speed multiplier changed
     */
    public BrewerProperties brewMultiplier(double amount) {
        this.brewX = amount;
        return this;
    }

    /** Get this property's brew speed multiplier
     * @return brew speed multiplier
     */
    public double getBrewMultiplier() {
        return this.brewX;
    }

    /** Set the fuel burn speed multiplier for this property
     * @param amount Speed multiplier to set
     * @return Returns an instance of itself with the speed multiplier changed
     */
    public BrewerProperties fuelMultiplier(double amount) {
        this.fuelX = amount;
        return this;
    }

    /** Get this property's fuel burn speed multiplier
     * @return Fuel burn speed multiplier
     */
    public double getFuelMultiplier() {
        return this.fuelX;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrewerProperties that = (BrewerProperties) o;
        return Double.compare(that.brewX, brewX) == 0 && Double.compare(that.fuelX, fuelX) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(brewX, fuelX);
    }

    @Override
    public String toString() {
        return "BrewerProperties{" +
                "key=" + key +
                ", brewX=" + brewX +
                ", fuelX=" + fuelX +
                '}';
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("key", this.key.toString());
        result.put("brewX", this.brewX);
        result.put("fuelX", this.fuelX);
        return result;
    }

    public static BrewerProperties deserialize(Map<String, Object> args) {
        String stringKey = ((String) args.get("key")).split(":")[1];
        double brew = (double) args.get("brewX");
        double fuel = (double) args.get("fuelX");
        BrewerProperties brewerProperties = getProperty(stringKey);
        if (brewerProperties != null) {
            return brewerProperties;
        } else {
            return new BrewerProperties(stringKey).brewMultiplier(brew).fuelMultiplier(fuel);
        }
    }

}
