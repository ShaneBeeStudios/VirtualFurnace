package com.shanebeestudios.vf.api.machine;

import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Abstract machine class
 * <p>Other machines will extend from this class</p>
 */
public abstract class Machine {

    private final String name;
    private final UUID uniqueID;

    Machine(UUID uniqueID, String name) {
        this.uniqueID = uniqueID;
        this.name = name;
    }

    /**
     * Get this machine's unique ID
     *
     * @return Unique ID of this machine
     */
    public UUID getUniqueID() {
        return uniqueID;
    }

    /**
     * Get this machine's name
     *
     * @return Name of this machine
     */
    public String getName() {
        return name;
    }

    /**
     * Tick this machine.
     */
    public void tick() {
    }

    /**
     * Open the inventory of this machine to a player
     *
     * @param player Player to open inventory to
     */
    public abstract void openInventory(Player player);

    @Override
    public String toString() {
        return "Machine{" +
                "name='" + name + '\'' +
                ", uniqueID=" + uniqueID +
                '}';
    }

}
