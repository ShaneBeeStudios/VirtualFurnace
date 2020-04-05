package com.shanebeestudios.vf.api.property;

/**
 * Interface for a machine that holds properties
 */
@SuppressWarnings("unused")
public interface PropertyHolder<P extends Properties> {

    /**
     * Get the properties associated with this property holder
     *
     * @return Properties associated with this property holder
     */
    P getProperties();

}
