package com.shanebeestudios.vf.api.chunk;

import com.shanebeestudios.vf.api.tile.Tile;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a chunk which holds <b>{@link Tile Tiles}</b>
 * <p>These chunks are used to decide when a {@link Tile} needs to be ticked.
 * <br>Like a {@link Chunk Bukkit Chunk}, these chunks will load/unload based on player
 * positions.
 * <br>Tiles will only tick if the chunk is loaded.</p>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class MachineChunk {

    private final List<Tile<?>> tiles = new ArrayList<>();
    private final int x;
    private final int z;
    private final World world;
    private boolean loaded;

    public MachineChunk(int x, int z, World world) {
        this.x = x;
        this.z = z;
        this.world = world;
        loaded = false;
    }

    /** Get the X coord of this chunk
     * @return X coord of this chunk
     */
    public int getX() {
        return x;
    }

    /** Get the Z coord of this chunk
     * @return Z coord of this chunk
     */
    public int getZ() {
        return z;
    }

    /** Get the world of this chunk
     * @return World of this chunk
     */
    public World getWorld() {
        return world;
    }

    /** Get the {@link Chunk Bukkit Chunk} which correlates to this MachineChunk
     * @return Chunk that correlates to this MachineChunk
     */
    public Chunk getBukkitChunk() {
        return world.getChunkAt(x, z);
    }

    /** Get all the tiles in this chunk
     * @return Tiles in this chunk
     */
    public List<Tile<?>> getTiles() {
        return tiles;
    }

    /** Get a {@link Tile} which is linked to a Block in this chunk
     * @param block Block to check for Tile
     * @return Tile which relates to a block, null if no tile exists
     */
    public Tile<?> getTile(Block block) {
        for (Tile<?> tile : tiles) {
            if (tile.getX() == block.getX() && tile.getY() == block.getY() && tile.getZ() == block.getZ()) {
                return tile;
            }
        }
        return null;
    }

    /** Add a {@link Tile} to this chunk
     * <p><b>NOTE:</b> This should only be used internally.</p>
     * @param tile Tile to add
     * @return True if tile was added successfully
     */
    public boolean addTile(Tile<?> tile) {
        if (!tiles.contains(tile)) {
            tiles.add(tile);
            return true;
        }
        return false;
    }

    /** Remove a {@link Tile} from this chunk
     * <p><b>NOTE:</b> This should only be used internally.</p>
     * @param tile Tile to remove
     * @return True if tile was successfully removed
     */
    public boolean removeTile(Tile<?> tile) {
        if (tiles.contains(tile)) {
            tiles.remove(tile);
            return true;
        }
        return false;
    }

    /** Check if this chunk is loaded
     * @return True if loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /** Check if the corresponding {@link Chunk Bukkit Chunk} is loaded
     * @return True if loaded
     */
    public boolean isBukkitChunkLoaded() {
        return world.isChunkLoaded(x, z);
    }

    /** Set whether or not this chunk is loaded
     * @param loaded True to load, false to unload
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
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
    public String toString() {
        return "MachineChunk{" +
                ", x=" + x +
                ", z=" + z +
                ", world=" + world +
                ", loaded=" + loaded +
                '}';
    }

}
