package tk.shanebee.fakefurnace.recipe;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class Fuel implements Keyed {

    private final NamespacedKey key;
    private final Material fuel;
    private final int burnTime;

    public Fuel(NamespacedKey key, Material fuel, int burnTime) {
        this.key = key;
        this.fuel = fuel;
        this.burnTime = burnTime;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    public Material getFuel() {
        return this.fuel;
    }

    public int getBurnTime() {
        return this.burnTime;
    }

}
