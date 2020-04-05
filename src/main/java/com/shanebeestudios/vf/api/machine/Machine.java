package com.shanebeestudios.vf.api.machine;

import java.util.UUID;

/**
 * Abstract machine class
 * <p>Other machines will extend from this class</p>
 */
public abstract class Machine {

    private final UUID uniqueID;
    private final String name;

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

    @Override
    public String toString() {
        return "Machine{" +
                "uniqueID=" + uniqueID +
                ", name='" + name + '\'' +
                '}';
    }

}
