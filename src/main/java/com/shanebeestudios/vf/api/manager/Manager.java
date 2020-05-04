package com.shanebeestudios.vf.api.manager;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.Machine;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Base Manager for {@link Machine Machines}
 * @param <M> {@link Machine} type
 */
@SuppressWarnings("unused")
public abstract class Manager<M extends Machine> {

    private final VirtualFurnaceAPI API;
    private File machineFile;
    private FileConfiguration machineConfig;
    final Map<UUID, M> machineMap;
    private final String typePlural;
    final NamespacedKey key;

    Manager(@NotNull VirtualFurnaceAPI API, @NotNull String type) {
        this.API = API;
        this.typePlural = type + "s";
        this.machineMap = new HashMap<>();
        this.key = Util.getKey(type + "ID");
        loadConfig();
    }

    /**
     * Get a collection of all {@link Machine Machines} in this manager
     *
     * @return Collection of all machines in this manager
     */
    public Collection<M> getAllMachines() {
        return Collections.unmodifiableCollection(this.machineMap.values());
    }

    /**
     * Get a {@link Machine} by ID
     *
     * @param uuid ID of machine to grab
     * @return Machine from ID (null if a machine with this ID does not exist)
     */
    public M getByID(@NotNull UUID uuid) {
        return this.machineMap.get(uuid);
    }

    private void loadConfig() {
        if (this.machineFile == null) {
            this.machineFile = new File(this.API.getJavaPlugin().getDataFolder(), typePlural + ".yml");
        }
        if (!machineFile.exists()) {
            this.API.getJavaPlugin().saveResource(typePlural + ".yml", false);
        }
        this.machineConfig = YamlConfiguration.loadConfiguration(this.machineFile);
        loadMachines();
    }

    @SuppressWarnings("unchecked")
    void loadMachines() {
        ConfigurationSection section = this.machineConfig.getConfigurationSection( typePlural);
        if (section != null) {
            for (String string : section.getKeys(true)) {
                if (section.get(string) instanceof Machine) {
                    try {
                        this.machineMap.put(UUID.fromString(string), (M) section.get(string));
                    } catch (ClassCastException ignore) {
                    }
                }
            }
        }
        Util.log("Loaded: &b" + this.machineMap.size() + "&7 " + typePlural);
    }

    /**
     * Save a machine to YAML storage
     * <p><b>NOTE:</b> If choosing not to save to file, this change will not take effect
     * in the YAML file, this may be useful for saving a large batch and saving file at the
     * end of the batch change, use {@link #saveConfig()} to save all changes to file</p>
     *
     * @param machine    Machine to save
     * @param saveToFile Whether to save to file
     */
    public void saveMachine(@NotNull M machine, boolean saveToFile) {
        this.machineConfig.set(typePlural + "." + machine.getUniqueID(), machine);
        if (saveToFile)
            saveConfig();
    }

    /**
     * Remove a machine from YAML storage
     * <p><b>NOTE:</b> If choosing not to save to file, this change will not take effect
     * in the YAML file, this may be useful it removing a large batch and saving file at the
     * end of the batch change, use {@link #saveConfig()} to save all changes to file</p>
     *
     * @param machine    Machine to remove
     * @param saveToFile Whether to save changes to file
     */
    public void removeMachineFromConfig(@NotNull M machine, boolean saveToFile) {
        this.machineConfig.set( typePlural + "." + machine.getUniqueID(), null);
        if (saveToFile)
            saveConfig();
    }

    /**
     * Save all machines to file
     */
    public void saveAll() {
        for (M machine : this.machineMap.values()) {
            saveMachine(machine, false);
        }
        saveConfig();
    }

    /**
     * Save current machine YAML from RAM to file
     */
    public void saveConfig() {
        try {
            machineConfig.save(machineFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save and clear all machines on shutdown
     * <p><b>NOTE:</b> Mainly used internally</p>
     */
    public void shutdown() {
        saveAll();
        machineMap.clear();
    }

}
