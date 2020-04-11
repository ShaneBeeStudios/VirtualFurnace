package com.shanebeestudios.vf.api;

import com.shanebeestudios.vf.api.chunk.ChunkKey;
import com.shanebeestudios.vf.api.chunk.VirtualChunk;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Manager for {@link Tile Tiles} and {@link VirtualChunk VirtualChunks}
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class TileManager {

    private final Map<ChunkKey, VirtualChunk> chunkMap = new HashMap<>();
    private final List<VirtualChunk> loadedChunks = new ArrayList<>();
    private final List<Tile<?>> tiles = new ArrayList<>();

    private final VirtualFurnaceAPI virtualFurnaceAPI;
    private File tileFile;
    private FileConfiguration tileConfig;

    public TileManager(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.virtualFurnaceAPI = virtualFurnaceAPI;
    }

    public void load() {
        loadTileConfig();
        loadTiles();
        loadChunks();
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

    private void loadTiles() {
        ConfigurationSection section = this.tileConfig.getConfigurationSection("tiles");
        if (section != null) {
            for (String string : section.getKeys(true)) {
                if (section.get(string) instanceof FurnaceTile) {
                    tiles.add((FurnaceTile) section.get(string));
                } else {
                    Util.log("&cFailed to load tile: " + section.get(string));
                }
            }
        }
        Util.log("Loaded: &b" + tiles.size() + "&7 tiles");
    }

    private void loadChunks() {
        for (Tile<?> tile : tiles) {
            int x = tile.getX() >> 4;
            int z = tile.getZ() >> 4;
            ChunkKey key = new ChunkKey(x, z);
            VirtualChunk chunk;
            if (!chunkMap.containsKey(key)) {
                chunkMap.put(key, new VirtualChunk(x, z, tile.getBukkitWorld()));
            }
            chunk = chunkMap.get(key);
            chunk.addTile(tile);
            if (tile.getBukkitWorld().isChunkLoaded(x, z)) {
                loadedChunks.add(chunk);
            }
        }
        Util.log("Loaded: &b" + loadedChunks.size() + "&7/&b" + chunkMap.values().size() + "&7 virtual chunks");
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

    void shutdown() {
        saveAllTiles();
        List<VirtualChunk> chunks = new ArrayList<>(loadedChunks);
        for (VirtualChunk chunk : chunks) {
            chunk.removeAllPluginChunkTickets();
        }
        loadedChunks.clear();
        chunkMap.clear();
        tiles.clear();
    }

    /**
     * Get all {@link VirtualChunk VirtualChunks}
     *
     * @return Collection of all VirtualChunks
     */
    public Collection<VirtualChunk> getChunks() {
        return Collections.unmodifiableCollection(chunkMap.values());
    }

    /**
     * Get all loaded {@link VirtualChunk VirtualChunks}
     *
     * @return Unmodifiable collection of all loaded VirtualChunks
     */
    public Collection<VirtualChunk> getLoadedChunks() {
        return Collections.unmodifiableCollection(loadedChunks);
    }

    /**
     * Load a chunk
     *
     * @param chunk Chunk to load
     * @return True if chunk was loaded, false if it did not load
     */
    public boolean loadChunk(@NotNull VirtualChunk chunk) {
        return loadedChunks.add(chunk);
    }

    /**
     * Unload a chunk
     * <p>If the chunk has a ticket, it will not unload</p>
     *
     * @param chunk Chunk to unload
     * @return True if unloaded, false if it did not unload
     */
    public boolean unloadChunk(@NotNull VirtualChunk chunk) {
        if (loadedChunks.contains(chunk) && !chunk.isForceLoaded()) {
            loadedChunks.remove(chunk);
            return true;
        }
        return false;
    }

    /**
     * Check if a chunk is loaded
     *
     * @param chunk Chunk to check
     * @return True if loaded, false if not loaded
     */
    public boolean isChunkLoaded(@NotNull VirtualChunk chunk) {
        return loadedChunks.contains(chunk);
    }

    /**
     * Get a {@link VirtualChunk} based on a {@link Chunk Bukkit Chunk's} coordinates
     *
     * @param x X coordinate of Chunk
     * @param z Z coordinate of Chunk
     * @return VirtualChunk at coordinates
     */
    public VirtualChunk getChunk(int x, int z) {
        return chunkMap.get(new ChunkKey(x, z));
    }

    /**
     * Get a {@link VirtualChunk} based on a {@link Chunk Bukkit Chunk}
     *
     * @param chunk Chunk to check
     * @return VirtualChunk relevant to Chunk
     */
    public VirtualChunk getChunk(@NotNull Chunk chunk) {
        for (VirtualChunk mChunk : chunkMap.values()) {
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
            if (tile.getX() == x && tile.getY() == y && tile.getZ() == z && tile.getBukkitWorld() == world) {
                return tile;
            }
        }
        return null;
    }

    /**
     * Remove a {@link Tile}
     * <p>Will remove the tile from the {@link VirtualChunk} and yaml file.</p>
     *
     * @param tile Tile to remove
     * @return True if Tile was successfully removed
     */
    public boolean removeTile(@NotNull Tile<?> tile) {
        ChunkKey key = new ChunkKey(tile.getX() >> 4, tile.getZ() >> 4);
        VirtualChunk virtualChunk = chunkMap.get(key);
        if (virtualChunk != null) {
            virtualChunk.removeTile(tile);
            tiles.remove(tile);
            tileConfig.set("tiles." + tile.getString(), null);
            saveConfig();
            return true;
        }
        return false;
    }

    /**
     * Create a new FurnaceTile attached to a block
     * <p><b>NOTE:</b> DO NOT use <b>{@link FurnaceManager#createFurnace(String)}</b> methods in this part,
     * instead create a new <b>{@link Furnace}</b> object to prevent double ticking/saving.</p>
     *
     * @param block   Block to create new FurnaceTile at
     * @param furnace Furnace to add to this tile
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(@NotNull Block block, @NotNull Furnace furnace) {
        return createFurnaceTile(block.getX(), block.getY(), block.getZ(), block.getWorld(), furnace, null, null);
    }

    /**
     * Create a new FurnaceTile attached to a block
     * <p><b>NOTE:</b> DO NOT use <b>{@link FurnaceManager#createFurnace(String)}</b> methods in this part,
     * instead create a new <b>{@link Furnace}</b> object to prevent double ticking/saving.</p>
     *
     * @param block               Block to create new FurnaceTile at
     * @param furnace             Furnace to add to this tile
     * @param furnaceTileConsumer Consumer to manipulate this tile
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(@NotNull Block block, @NotNull Furnace furnace, @NotNull Consumer<FurnaceTile> furnaceTileConsumer) {
        return createFurnaceTile(block.getX(), block.getY(), block.getZ(), block.getWorld(), furnace, null, furnaceTileConsumer);
    }

    /**
     * Create a new FurnaceTile attached to a block
     *
     * @param block      Block to create new FurnaceTile at
     * @param name       Name of new Furnace
     * @param properties Properties of new Furnace
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(@NotNull Block block, @NotNull String name, @NotNull FurnaceProperties properties) {
        Furnace furnace = new Furnace(name, properties);
        return createFurnaceTile(block.getX(), block.getY(), block.getZ(), block.getWorld(), furnace, null, null);
    }

    /**
     * Create a new FurnaceTile attached to a block
     *
     * @param block               Block to create new FurnaceTile at
     * @param name                Name of new Furnace
     * @param properties          Properties of new Furnace
     * @param furnaceTileConsumer Consumer to manipulate this tile
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(@NotNull Block block, @NotNull String name, @NotNull FurnaceProperties properties, @NotNull Consumer<FurnaceTile> furnaceTileConsumer) {
        Furnace furnace = new Furnace(name, properties);
        return createFurnaceTile(block.getX(), block.getY(), block.getZ(), block.getWorld(), furnace, null, furnaceTileConsumer);
    }

    /**
     * Create a new FurnaceTile attached to a block
     *
     * @param x          X location of block
     * @param y          Y location of block
     * @param z          Z location of block
     * @param world      World of block
     * @param name       Name of new Furnace
     * @param properties Properties for new Furnace
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(int x, int y, int z, @NotNull World world, @NotNull String name, @NotNull FurnaceProperties properties) {
        Furnace furnace = new Furnace(name, properties);
        return createFurnaceTile(x, y, z, world, furnace, null, null);
    }

    /**
     * Create a new FurnaceTile attached to a block
     *
     * @param x               X location of block
     * @param y               Y location of block
     * @param z               Z location of block
     * @param world           World of block
     * @param name            Name of new Furnace
     * @param properties      Properties for new Furnace
     * @param furnaceConsumer Consumer to manipulate this furnace
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(int x, int y, int z, @NotNull World world, @NotNull String name, @NotNull FurnaceProperties properties, @NotNull Consumer<Furnace> furnaceConsumer) {
        Furnace furnace = new Furnace(name, properties);
        return createFurnaceTile(x, y, z, world, furnace, furnaceConsumer, null);
    }

    /**
     * Create a new FurnaceTile attached to a block
     * <p><b>NOTE:</b> DO NOT use <b>{@link FurnaceManager#createFurnace(String)}</b> methods in this part,
     * instead create a new <b>{@link Furnace}</b> object to prevent double ticking/saving.</p>
     *
     * @param x       X location of block
     * @param y       Y location of block
     * @param z       Z location of block
     * @param world   World of block
     * @param furnace Furnace to add to this tile
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(int x, int y, int z, @NotNull World world, @NotNull Furnace furnace) {
        return createFurnaceTile(x, y, z, world, furnace, null, null);
    }

    /**
     * Create a new FurnaceTile attached to a block
     * <p><b>NOTE:</b> DO NOT use <b>{@link FurnaceManager#createFurnace(String)}</b> methods in this part,
     * instead create a new <b>{@link Furnace}</b> object to prevent double ticking/saving.</p>
     *
     * @param x               X location of block
     * @param y               Y location of block
     * @param z               Z location of block
     * @param world           World of block
     * @param furnace         Furnace to add to this tile
     * @param furnaceConsumer Consumer to manipulate this furnace
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(int x, int y, int z, @NotNull World world, @NotNull Furnace furnace, @NotNull Consumer<Furnace> furnaceConsumer) {
        return createFurnaceTile(x, y, z, world, furnace, furnaceConsumer, null);
    }

    /**
     * Create a new FurnaceTile attached to a block
     * <p><b>NOTE:</b> DO NOT use <b>{@link FurnaceManager#createFurnace(String)}</b> methods in this part,
     * instead create a new <b>{@link Furnace}</b> object to prevent double ticking/saving.</p>
     *
     * @param x                   X location of block
     * @param y                   Y location of block
     * @param z                   Z location of block
     * @param world               World of block
     * @param furnace             Furnace to add to this tile
     * @param furnaceConsumer     Consumer to manipulate this furnace
     * @param furnaceTileConsumer Consumer to manipulate this tile
     * @return Instance of the new FurnaceTile
     */
    public FurnaceTile createFurnaceTile(int x, int y, int z, @NotNull World world, @NotNull Furnace furnace, Consumer<Furnace> furnaceConsumer, Consumer<FurnaceTile> furnaceTileConsumer) {
        if (furnaceConsumer != null) {
            furnaceConsumer.accept(furnace);
        }
        FurnaceTile tile = new FurnaceTile(furnace, x, y, z, world);
        if (furnaceTileConsumer != null) {
            furnaceTileConsumer.accept(tile);
        }
        tiles.add(tile);
        saveTile(tile, true);
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        ChunkKey key = new ChunkKey(chunkX, chunkZ);
        if (!chunkMap.containsKey(key)) {
            chunkMap.put(key, new VirtualChunk(chunkX, chunkZ, world));
        }
        VirtualChunk virtualChunk = chunkMap.get(key);
        if (virtualChunk.isBukkitChunkLoaded()) {
            loadedChunks.add(virtualChunk);
        }
        virtualChunk.addTile(tile);
        return tile;
    }

}
