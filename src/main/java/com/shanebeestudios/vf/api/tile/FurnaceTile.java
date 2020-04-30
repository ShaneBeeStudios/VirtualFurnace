package com.shanebeestudios.vf.api.tile;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.Furnace;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a {@link Tile} that connects a tickable {@link Furnace} to a {@link Block}
 */
@SuppressWarnings("unused")
public class FurnaceTile extends Tile<Furnace> implements ConfigurationSerializable {

    // TODO javadocs that point to TileManager for creation
    public FurnaceTile(@NotNull Furnace machine, @NotNull Block block) {
        super(machine, block);
    }

    public FurnaceTile(@NotNull Furnace machine, int x, int y, int z, @NotNull World world) {
        super(machine, x, y, z, world);
    }

    // Used for de-serializing
    private FurnaceTile(@NotNull Furnace machine, int x, int y, int z, @NotNull String world) {
        super(machine, x, y, z, world);
    }

    @Override
    public void breakTile() {
        BukkitRunnable runnable = new BukkitRunnable() {
            final ItemStack fuel = machine.getFuel();
            final ItemStack input = machine.getInput();
            final ItemStack output = machine.getOutput();
            final float xp = machine.extractExperience();
            final Vector vec = new Vector(0, 0, 0);

            @Override
            public void run() {
                World world = getBukkitWorld();
                Location drop = new Location(world, x + 0.5, y + 0.5, z + 0.5);
                if (fuel != null) {
                    world.dropItem(drop, fuel).setVelocity(vec);
                }
                if (input != null) {
                    world.dropItem(drop, input).setVelocity(vec);
                }
                if (output != null) {
                    world.dropItem(drop, output).setVelocity(vec);
                }
                if (xp > 0) {
                    world.spawn(drop, ExperienceOrb.class, orb -> orb.setExperience((int) xp)).setVelocity(vec);
                }
            }
        };
        runnable.runTaskLater(VirtualFurnaceAPI.getInstance().getJavaPlugin(), 0);
        super.breakTile();
    }

    @Override
    public String getString() {
        return "FurnaceTile{" +
                "machine-type=" + machine.getClass().getSimpleName() +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", world='" + world + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "FurnaceTile{" +
                "machine=" + machine +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", world='" + world + '\'' +
                "} ";
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("machine", machine);
        result.put("x", x);
        result.put("y", y);
        result.put("z", z);
        result.put("world", world);
        return result;
    }

    public static FurnaceTile deserialize(Map<String, Object> args) {
        int x = (int) args.get("x");
        int y = (int) args.get("y");
        int z = (int) args.get("z");
        String world = (String) args.get("world");
        return new FurnaceTile((Furnace) args.get("machine"), x, y, z, world);
    }

}
