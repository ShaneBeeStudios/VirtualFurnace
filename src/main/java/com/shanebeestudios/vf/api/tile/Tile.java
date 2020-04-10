package com.shanebeestudios.vf.api.tile;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.Machine;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/** A tile that connects a tickable {@link Machine} to a {@link Block}
 * @param <M> Type of machine connected to this tile
 */
@SuppressWarnings("unused")
public abstract class Tile<M extends Machine> {

    final M machine;
    final int x;
    final int y;
    final int z;
    final String world;
    private final BlockData blockData;

    Tile(@NotNull M machine, int x, int y, int z, @NotNull World world) {
        this(machine, world.getBlockAt(x, y, z));
    }

    Tile(@NotNull M machine, int x, int y, int z, @NotNull String world) {
        this.machine = machine;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld != null) {
            this.blockData = bukkitWorld.getBlockAt(x, y, z).getBlockData();
        } else {
            this.blockData = null;
        }
    }

    Tile(@NotNull M machine, @NotNull Block block) {
        this.machine = machine;
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.world = block.getWorld().getName();
        this.blockData = block.getBlockData();
    }

    /**
     * Get the {@link Machine} belonging to this tile
     *
     * @return Machine belonging to this tile
     */
    public M getMachine() {
        return machine;
    }

    /**
     * Get the X coord of this tile
     *
     * @return X coord of this tile
     */
    public int getX() {
        return x;
    }

    /**
     * Get the Y coord of this tile
     *
     * @return Y coord of this tile
     */
    public int getY() {
        return y;
    }

    /**
     * Get the Z coord of this tile
     *
     * @return Z coord of this tile
     */
    public int getZ() {
        return z;
    }

    /**
     * Get the name of the world of this tile
     * <p>Returns the name of the world</p>
     *
     * @return Name of the world of this tile
     */
    public String getWorld() {
        return world;
    }

    /**
     * Get the world of this tile
     *
     * @return World of this tile
     */
    public World getBukkitWorld() {
        return Bukkit.getWorld(world);
    }

    /**
     * Get the block this tile is connected to
     *
     * @return Block this tile is connected to
     */
    public Block getBlock() {
        return getBukkitWorld().getBlockAt(x, y, z);
    }

    /**
     * Get the {@link BlockData} of the block this tile is connected to
     *
     * @return BlockData of the block this tile is connected to
     */
    public BlockData getBlockData() {
        return blockData;
    }

    /**
     * Check if the {@link BlockData} of a block matches the BlockData of this tile
     *
     * @param block Block to check data for
     * @return True if BlockData matches
     */
    public boolean blockDataMatches(@NotNull Block block) {
        return this.blockData.equals(block.getBlockData());
    }

    /**
     * Activate this tile for a player
     * <p>In the case of a machine it will open the inventory to the player</p>
     *
     * @param player Player to activate this tile for
     */
    public void activate(@NotNull Player player) {
        machine.openInventory(player);
    }

    /**
     * Break this tile
     * <p>This is run when a player breaks the block connected to this tile.
     * The tile will be removed and no longer tick.</p>
     */
    public void breakTile() {
        VirtualFurnaceAPI.getInstance().getTileManager().removeTile(this);
    }

    /**
     * Tick this tile
     */
    public void tick() {
        if (blockDataMatches(getBlock())) {
            machine.tick();
        } else {
            breakTile();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile<?> tile = (Tile<?>) o;
        return x == tile.x && y == tile.y && z == tile.z && Objects.equals(machine, tile.machine) &&
                Objects.equals(world, tile.world) && Objects.equals(blockData, tile.blockData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machine, x, y, z, world, blockData);
    }

    /**
     * Get a string version of this tile
     * <p>This is used when saving to file</p>
     *
     * @return String version of this tile
     */
    public String getString() {
        return "Tile{" +
                "machine-type=" + machine.getClass().getSimpleName() +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", world='" + world + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "Tile{" +
                "machine=" + machine +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", world='" + world + '\'' +
                ", blockData=" + blockData +
                '}';
    }

}
