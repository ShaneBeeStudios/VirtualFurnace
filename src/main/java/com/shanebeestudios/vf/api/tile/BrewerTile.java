package com.shanebeestudios.vf.api.tile;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.Brewer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a {@link Tile} that connects a tickable {@link Brewer} to a {@link Block}
 */
@SuppressWarnings("unused")
public class BrewerTile extends Tile<Brewer> implements ConfigurationSerializable {

    public BrewerTile(@NotNull Brewer machine, int x, int y, int z, @NotNull World world) {
        super(machine, x, y, z, world);
    }

    public BrewerTile(@NotNull Brewer machine, @NotNull Block block) {
        super(machine, block);
    }

    private BrewerTile(@NotNull Brewer machine, int x, int y, int z, @NotNull String world, BlockData blockData) {
        super(machine, x, y, z, world, blockData);
    }

    @Override
    public void breakTile() {
        BukkitRunnable runnable = new BukkitRunnable() {
            final ItemStack fuel = machine.getFuelItem();
            final ItemStack ingredient = machine.getIngredient();
            final ItemStack b1 = machine.getBottle(0);
            final ItemStack b2 = machine.getBottle(1);
            final ItemStack b3 = machine.getBottle(2);
            final World world = getBukkitWorld();
            final Location loc = new Location(world, x + 0.5, y + 0.5, z + 0.5);
            final Vector vec = new Vector(0, 0, 0);

            private void drop(ItemStack item) {
                if (item != null) {
                    world.dropItem(loc, item).setVelocity(vec);
                }
            }

            @Override
            public void run() {
                drop(fuel);
                drop(ingredient);
                drop(b1);
                drop(b2);
                drop(b3);
            }
        };
        runnable.runTaskLater(VirtualFurnaceAPI.getInstance().getJavaPlugin(), 0);
        super.breakTile();
    }

    @Override
    public void tick() {
        super.tick();
        if (isChunkLoaded() && getBlock().getType() == Material.BREWING_STAND) {
            BrewingStand data = ((BrewingStand) getBlock().getBlockData());
            boolean updated = false;
            for (int i = 0; i < 3; i++) {
                boolean blockHasBot = data.hasBottle(i);
                boolean machineHasBot = machine.getBottle(i) != null;
                if (blockHasBot != machineHasBot) {
                    data.setBottle(i, machineHasBot);
                    updated = true;
                }
            }
            if (updated) {
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        getBlock().setBlockData(data);
                    }
                };
                runnable.runTaskLater(VirtualFurnaceAPI.getInstance().getJavaPlugin(), 0);

            }
        }
    }

    @Override
    public String getString() {
        return "BrewerTile{" +
                "machine-type=" + machine.getClass().getSimpleName() +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", world='" + world + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return "BrewerTile{" +
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
        result.put("blockdata", getBlockData().getAsString());
        return result;
    }

    public static BrewerTile deserialize(Map<String, Object> args) {
        int x = (int) args.get("x");
        int y = (int) args.get("y");
        int z = (int) args.get("z");
        String world = (String) args.get("world");
        BlockData data = Bukkit.createBlockData((String) args.get("blockdata"));
        return new BrewerTile((Brewer) args.get("machine"), x, y, z, world, data);
    }

}
