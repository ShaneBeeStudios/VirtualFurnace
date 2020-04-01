package tk.shanebee.fakefurnace;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tk.shanebee.fakefurnace.machine.Furnace;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FurnaceManager {

    private final FakeFurnace plugin;
    private File furnaceFile;
    private FileConfiguration furnaceConfig;
    private final Map<UUID, Furnace> furnaceMap;

    FurnaceManager(FakeFurnace plugin) {
        this.plugin = plugin;
        this.furnaceMap = new HashMap<>();
        loadFurnaceConfig();
    }

    public Map<UUID, Furnace> getFurnaceMap() {
        return furnaceMap;
    }

    /** Create a new furnace
     * <p>This will create a new furnace, add it to the tick list, and save to file</p>
     * @param name Name of new furnace (This shows up in the inventory view)
     * @return Instance of this new furnace
     */
    public Furnace createFurnace(String name) {
        Furnace furnace = new Furnace(name);
        this.furnaceMap.put(furnace.getUuid(), furnace);
        saveFurnace(furnace, true);
        return furnace;
    }

    public Furnace getByID(UUID uuid) {
        return this.furnaceMap.get(uuid);
    }

    private void loadFurnaceConfig() {
        if (this.furnaceFile == null) {
            this.furnaceFile = new File(this.plugin.getDataFolder(), "furnaces.yml");
        }
        if (!furnaceFile.exists()) {
            this.plugin.saveResource("furnaces.yml", false);
        }
        this.furnaceConfig = YamlConfiguration.loadConfiguration(this.furnaceFile);
        loadFurnaces();
    }

    private void loadFurnaces() {
        ConfigurationSection section = this.furnaceConfig.getConfigurationSection("furnaces");
        if (section == null) return;
        for (String string : section.getKeys(true)) {
            if (section.get(string) instanceof Furnace) {
                Furnace furnace = ((Furnace) section.get(string));
                this.furnaceMap.put(UUID.fromString(string), (Furnace) section.get(string));
            }
        }
    }

    private void saveFurnace(Furnace furnace, boolean save) {
        this.furnaceConfig.set("furnaces." + furnace.getUuid(), furnace);
        if (save)
            saveConfig();
    }

    private void removeFurnaceFromConfig(Furnace furnace) {
        this.furnaceConfig.set("furnaces." + furnace.getUuid(), null);
    }

    public void saveAll() {
        for (Furnace furnace : this.furnaceMap.values()) {
            saveFurnace(furnace, false);
        }
        saveConfig();
    }

    private void saveConfig() {
        try {
            furnaceConfig.save(furnaceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
