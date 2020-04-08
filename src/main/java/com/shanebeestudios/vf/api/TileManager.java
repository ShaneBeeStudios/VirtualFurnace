package com.shanebeestudios.vf.api;

import com.shanebeestudios.vf.api.chunk.ChunkKey;
import com.shanebeestudios.vf.api.chunk.MachineChunk;
import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.property.FurnaceProperties;
import com.shanebeestudios.vf.api.tile.FurnaceTile;
import com.shanebeestudios.vf.api.tile.Tile;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager for {@link Tile Tiles}
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class TileManager {

    private final Map<ChunkKey, MachineChunk> chunkMap = new HashMap<>();
    private final List<Tile<?>> tiles;

    private final VirtualFurnaceAPI virtualFurnaceAPI;
    private File tileFile;
    private FileConfiguration tileConfig;

    public TileManager(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.virtualFurnaceAPI = virtualFurnaceAPI;
        loadTileConfig();
        tiles = loadTiles();
        loadChunks(tiles);
    }

    private void loadChunks(List<Tile<?>> tiles) {
        for (Tile<?> tile : tiles) {
            int x = tile.getX() >> 4;
            int z = tile.getZ() >> 4;
            ChunkKey key = new ChunkKey(x, z);
            if (!chunkMap.containsKey(key)) {
                MachineChunk chunk = new MachineChunk(x, z, tile.getBukkitWorld());
                if (tile.getBukkitWorld().isChunkLoaded(x, z)) {
                    chunk.setLoaded(true);
                }
                chunk.addTile(tile);
                chunkMap.put(key, chunk);
            } else {
                chunkMap.get(key).addTile(tile);
            }
        }
    }

    private void loadTileConfig() {
        if (this.tileFile == null) {
            this.tileFile = new File(this.virtualFurnaceAPI.getJavaPlugin().getDataFolder(), "tiles.yml");
        }
        if (!tileFile.exists()) {
            this.virtualFurnaceAPI.getJavaPlugin().saveResource("tiles.yml", false);
        }
        this.tileConfig = YamlConfiguration.loadConfiguration(this.tileFile);
    }

    private List<Tile<?>> loadTiles() {
        ConfigurationSection section = this.tileConfig.getConfigurationSection("tiles");
        List<Tile<?>> tiles = new ArrayList<>();
        if (section != null) {
            for (String string : section.getKeys(true)) {
                if (section.get(string) instanceof FurnaceTile) {
                    tiles.add((FurnaceTile) section.get(string));
                } else {
                    Util.log("&cFailed to load tile: " + section.get(string));
                }
            }
        }
        return tiles;
    }

    /**
     * Save a tile to YAML storage
     * <p><b>NOTE:</b> If choosing not to save to file, this change will not take effect
     * in the YAML file, this may be useful for saving a large batch and saving file at the
     * end of the batch change, use {@link #saveConfig()} to save all changes to file</p>
     *
     * @param tile       Tile to save
     * @param saveToFile Whether to save to file
     */
    public void saveTile(@NotNull Tile<?> tile, boolean saveToFile) {
        this.tileConfig.set("tiles." + tile.getString(), tile);
        if (saveToFile)
            saveConfig();
    }

    /**
     * Save all current {@link Tile Tiles} to file
     */
    public void saveAllTiles() {
        for (Tile<?> tile : tiles) {
            saveTile(tile, false);
        }
        saveConfig();
    }

    /**
     * Save current tile YAML from RAM to file
     */
    public void saveConfig() {
        try {
            tileConfig.save(tileFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get all {@link MachineChunk MachineChunks}
     *
     * @return Collection of all MachineChunks
     */
    public Collection<MachineChunk> getChunks() {
        return chunkMap.values();
    }

    /**
     * Get all loaded {@link MachineChunk MachineChunks}
     *
     * @return Collection of all loaded MachineChunks
     */
    public Collection<MachineChunk> getLoadedChunks() {
        List<MachineChunk> loaded = new ArrayList<>();
        for (MachineChunk chunk : getChunks()) {
            if (chunk.isLoaded()) {
                loaded.add(chunk);
            }
        }
        return loaded;
    }

    /**
     * Get a {@link MachineChunk} based on a {@link Chunk Bukkit Chunk's} coordinates
     *
     * @param x X coordinate of Chunk
     * @param z Z coordinate of Chunk
     * @return MachineChunk at coordinates
     */
    public MachineChunk getChunk(int x, int z) {
        return chunkMap.get(new ChunkKey(x, z));
    }

    /**
     * Get a {@link MachineChunk} based on a {@link Chunk Bukkit Chunk}
     *
     * @param chunk Chunk to check
     * @return MachineChunk relevant to Chunk
     */
    public MachineChunk getChunk(@NotNull Chunk chunk) {
        for (MachineChunk mChunk : chunkMap.values()) {
            if (chunk.getX() == mChunk.getX() && chunk.getZ() == mChunk.getZ()) {
                return mChunk;
            }
        }
        return null;
    }

    /**
     * Get all {@link Tile Tiles}
     *
     * @return List of all Tiles
     */
    public List<Tile<?>> getAllTiles() {
        return tiles;
    }

    /**
     * Get a {@link Tile} at a specific {@link Block}
     *
     * @param block Block to check for tile
     * @return Tile if Tile exists at Block, otherwise null
     */
    public Tile<?> getTile(@NotNull Block block) {
        return getTile(block.getX(), block.getY(), block.getZ(), block.getWorld());
    }

    /**
     * Get a {@link Tile} at a specific location
     *
     * @param x     X coord of Tile
     * @param y     Y coord of Tile
     * @param z     Z coord of Tile
     * @param world World of Tile
     * @return Tile if Tile exists at location, otherwise null
     */
    public Tile<?> getTile(int x, int y, int z, @NotNull World world) {
        for (Tile<?> tile : tiles) {
            if (tile.getX() == z && tile.getY() == y && tile.getZ() == z && tile.getBukkitWorld() == world) {
                return tile;
            }
        }
        return null;
    }

    /**
     * Remove a {@link Tile}
     * <p>Will remove the tile from the {@link MachineChunk} and yaml file.</p>
     *
     * @param tile Tile to remove
     * @return True if Tile was successfully removed
     */
    public boolean removeTile(@NotNull Tile<?> tile) {
        ChunkKey key = new ChunkKey(tile.getX() >> 4, tile.getZ() >> 4);
        MachineChunk machineChunk = chunkMap.get(key);
        if (machineChunk != null) {
            machineChunk.removeTile(tile);
            tiles.remove(tile);
            tileConfig.set("tiles." + tile.getString(), null);
            saveConfig();
            return true;
        }
        return false;
    }

    public FurnaceTile createFurnaceTile(int x, int y, int z, @NotNull World world, @NotNull String name, @NotNull FurnaceProperties properties) {
        Furnace furnace = new Furnace(name, properties);
        return createFurnaceTile(x, y, z, world, furnace);
    }

    public FurnaceTile createFurnaceTile(@NotNull Block block, @NotNull Furnace furnace) {
        return createFurnaceTile(block.getX(), block.getY(), block.getZ(), block.getWorld(), furnace);
    }

    public FurnaceTile createFurnaceTile(int x, int y, int z, @NotNull World world, @NotNull Furnace furnace) {
        FurnaceTile tile = new FurnaceTile(furnace, x, y, z, world);
        tiles.add(tile);
        saveTile(tile, true);
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        ChunkKey key = new ChunkKey(chunkX, chunkZ);
        if (!chunkMap.containsKey(key)) {
            chunkMap.put(key, new MachineChunk(chunkX, chunkZ, world));
        }
        MachineChunk machineChunk = chunkMap.get(key);
        if (machineChunk.isBukkitChunkLoaded()) {
            machineChunk.setLoaded(true);
        }
        machineChunk.addTile(tile);
        return tile;
    }

}
