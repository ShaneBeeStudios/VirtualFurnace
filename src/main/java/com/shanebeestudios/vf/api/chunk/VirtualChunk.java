package com.shanebeestudios.vf.api.chunk;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.tile.Tile;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a chunk which holds <b>{@link Tile Tiles}</b>
 * <p>These chunks are used to decide when a {@link Tile} needs to be ticked.
 * <br>Like a {@link Chunk Bukkit Chunk}, these chunks will load/unload based on player
 * positions.
 * <br>Tiles will only tick if the chunk is loaded.</p>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class VirtualChunk {

    private final List<Tile<?>> tiles = new ArrayList<>();
    private final List<String> tickets = new ArrayList<>();
    private final int x;
    private final int z;
    private final World world;

    public VirtualChunk(int x, int z, World world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    /**
     * Get the X coord of this chunk
     *
     * @return X coord of this chunk
     */
    public int getX() {
        return x;
    }

    /**
     * Get the Z coord of this chunk
     *
     * @return Z coord of this chunk
     */
    public int getZ() {
        return z;
    }

    /**
     * Get the world of this chunk
     *
     * @return World of this chunk
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get the {@link Chunk Bukkit Chunk} which correlates to this VirtualChunk
     *
     * @return Chunk that correlates to this VirtualChunk
     */
    public Chunk getBukkitChunk() {
        return world.getChunkAt(x, z);
    }

    /**
     * Get all the tiles in this chunk
     *
     * @return Tiles in this chunk
     */
    public List<Tile<?>> getTiles() {
        return tiles;
    }

    /**
     * Get a {@link Tile} which is linked to a Block in this chunk
     *
     * @param block Block to check for Tile
     * @return Tile which relates to a block, null if no tile exists
     */
    public Tile<?> getTile(@NotNull Block block) {
        for (Tile<?> tile : tiles) {
            if (tile.getX() == block.getX() && tile.getY() == block.getY() && tile.getZ() == block.getZ()) {
                return tile;
            }
        }
        return null;
    }

    /**
     * Add a {@link Tile} to this chunk
     * <p><b>NOTE:</b> This should only be used internally.</p>
     *
     * @param tile Tile to add
     * @return True if tile was added successfully
     */
    public boolean addTile(@NotNull Tile<?> tile) {
        if (!tiles.contains(tile)) {
            tiles.add(tile);
            return true;
        }
        return false;
    }

    /**
     * Remove a {@link Tile} from this chunk
     * <p><b>NOTE:</b> This should only be used internally.</p>
     *
     * @param tile Tile to remove
     * @return True if tile was successfully removed
     */
    public boolean removeTile(@NotNull Tile<?> tile) {
        if (tiles.contains(tile)) {
            tiles.remove(tile);
            return true;
        }
        return false;
    }

    /**
     * Check if this chunk is loaded
     *
     * @return True if loaded
     */
    public boolean isLoaded() {
        return VirtualFurnaceAPI.getInstance().getTileManager().isChunkLoaded(this);
    }

    /**
     * Check if the corresponding {@link Chunk Bukkit Chunk} is loaded
     *
     * @return True if loaded
     */
    public boolean isBukkitChunkLoaded() {
        return world.isChunkLoaded(x, z);
    }

    /**
     * Retrieves a collection specifying which plugins have tickets for this chunk.
     * <p>A plugin ticket will prevent a chunk from unloading until it is explicitly removed.
     * A plugin instance may only have one ticket per chunk, but each chunk can have multiple plugin tickets.</p>
     *
     * @return Unmodifiable collection containing which plugins have tickets for this chunk
     */
    public Collection<Plugin> getPluginChunkTickets() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        List<Plugin> plugins = new ArrayList<>();
        for (String ticket : tickets) {
            Plugin plugin = pluginManager.getPlugin(ticket);
            if (plugin != null) {
                plugins.add(plugin);
            }
        }
        return Collections.unmodifiableCollection(plugins);
    }

    /**
     * Adds a plugin ticket for this chunk, loading this chunk if it is not already loaded.
     * <p>A plugin ticket will prevent a chunk from unloading until
     * it is explicitly removed. A plugin instance may only have one
     * ticket per chunk, but each chunk can have multiple plugin tickets.</p>
     *
     * @param plugin Plugin which owns the ticket
     * @return true if a plugin ticket was added, false if the ticket already exists for the plugin
     */
    public boolean addPluginChunkTicket(@NotNull Plugin plugin) {
        String p = plugin.getDescription().getName();
        if (!tickets.contains(p)) {
            tickets.add(p);
            if (!isLoaded()) {
                VirtualFurnaceAPI.getInstance().getTileManager().loadChunk(this);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes the specified plugin's ticket for this chunk
     * <p>A plugin ticket will prevent a chunk from unloading until
     * it is explicitly removed. A plugin instance may only have one
     * ticket per chunk, but each chunk can have multiple plugin tickets.</p>
     *
     * @param plugin Plugin which owns the ticket
     * @return true if the plugin ticket was removed, false if there is no plugin ticket for the chunk
     */
    public boolean removePluginChunkTicket(@NotNull Plugin plugin) {
        String p = plugin.getDescription().getName();
        if (tickets.contains(p)) {
            tickets.remove(p);
            if (tickets.isEmpty()) {
                VirtualFurnaceAPI.getInstance().getTileManager().unloadChunk(this);
            }
            return true;
        }
        return false;
    }

    /**
     * Remove all plugin chunk tickets
     * <p>This is generally used internally and should not
     * be used by plugins.</p>
     */
    public void removeAllPluginChunkTickets() {
        tickets.clear();
        VirtualFurnaceAPI.getInstance().getTileManager().unloadChunk(this);
    }

    /**
     * Gets whether this chunk is force loaded
     * <p>A force loaded chunk will not unload due to player inactivity.</p>
     *
     * @return True if chunk is force loaded, false if not
     */
    public boolean isForceLoaded() {
        return !tickets.isEmpty();
    }

    /**
     * Tick the chunk
     * <p><b>NOTE:</b> This should only be used internally.</p>
     */
    public void tick() {
        for (Tile<?> tile : tiles) {
            tile.tick();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VirtualChunk chunk = (VirtualChunk) o;
        return x == chunk.x && z == chunk.z && Objects.equals(tiles, chunk.tiles) &&
                Objects.equals(tickets, chunk.tickets) && Objects.equals(world, chunk.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tiles, tickets, x, z, world);
    }

    @Override
    public String toString() {
        return "VirtualChunk{" +
                ", x=" + x +
                ", z=" + z +
                ", world=" + world.getName() +
                '}';
    }

}
