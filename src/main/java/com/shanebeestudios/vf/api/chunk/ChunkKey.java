package com.shanebeestudios.vf.api.chunk;

import org.bukkit.Chunk;

import java.util.Objects;

/**
 * Represents a key for storing {@link VirtualChunk MachineChunks} in maps
 * <p>These keys store an X and Z coordinate.</p>
 */
public class ChunkKey {

    final int x, z;

    public ChunkKey(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public ChunkKey(Chunk chunk) {
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    @Override
    public String toString() {
        return "ChunkKey{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkKey chunkKey = (ChunkKey) o;
        return x == chunkKey.x && z == chunkKey.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

}
