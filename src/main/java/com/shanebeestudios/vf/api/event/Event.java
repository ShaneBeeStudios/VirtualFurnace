package com.shanebeestudios.vf.api.event;

import org.bukkit.Bukkit;

/**
 * Abstract event class for easy event calls
 */
public abstract class Event extends org.bukkit.event.Event {

    /**
     * Call the event
     * <p>This method just simplifies the need to use Bukkit's
     * PluginManager to call events</p>
     */
    public void callEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }

}
