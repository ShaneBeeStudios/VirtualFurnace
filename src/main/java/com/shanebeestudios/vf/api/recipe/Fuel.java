package com.shanebeestudios.vf.api.recipe;

import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Fuel for furnace
 */
@SuppressWarnings("unused")
public class Fuel implements Keyed {

    private static final List<Fuel> VANILLA_FUELS = new ArrayList<>();

    public static final Fuel LAVA_BUCKET = get("lava_bucket", Material.LAVA_BUCKET, 20000);
    public static final Fuel BLOCK_OF_COAL = get("block_of_coal", Material.COAL_BLOCK, 16000);
    public static final Fuel DRIED_KELP_BLOCK = get("dried_kelp_block", Material.DRIED_KELP_BLOCK, 4000);
    public static final Fuel BLAZE_ROD = get("blaze_rod", Material.BLAZE_ROD, 2400);
    public static final Fuel COAL = get("coal", Material.COAL, 1600);
    public static final Fuel CHARCOAL = get("charcoal", Material.CHARCOAL, 1600);
    public static final Fuel ANY_BOAT = get("any_boat", Tag.ITEMS_BOATS, 1200);
    public static final Fuel ANY_LOG = get("any_log", Tag.LOGS, 300);
    public static final Fuel ANY_PLANK = get("any_plank", Tag.PLANKS, 300);
    public static final Fuel ANY_WOOD_PRESSURE_PLATE = get("any_wood_pressure_plate", Tag.WOODEN_PRESSURE_PLATES, 300);
    public static final Fuel ANY_WOOD_STAIR = get("any_wood_stair", Tag.WOODEN_STAIRS, 300);
    public static final Fuel ANY_WOOD_TRAPDOOR = get("any_wood_trapdoor", Tag.WOODEN_TRAPDOORS, 300);
    public static final Fuel CRAFTING_TABLE = get("crafting_table", Material.CRAFTING_TABLE, 300);
    public static final Fuel BOOKSHELF = get("bookshelf", Material.BOOKSHELF, 300);
    public static final Fuel CHEST = get("chest", Material.CHEST, 300);
    public static final Fuel TRAPPED_CHEST = get("trapped_chest", Material.TRAPPED_CHEST, 300);
    public static final Fuel DAYLIGHT_DETECTOR = get("daylight_detector", Material.DAYLIGHT_DETECTOR, 300);
    public static final Fuel JUKEBOX = get("jukebox", Material.JUKEBOX, 300);
    public static final Fuel NOTE_BLOCK = get("note_block", Material.NOTE_BLOCK, 300);
    public static final Fuel MUSHROOM_STEM = get("mushroom_stem", Material.MUSHROOM_STEM, 300);
    public static final Fuel BROWN_MUSHROOM_BLOCK = get("brown_mushroom_block", Material.BROWN_MUSHROOM_BLOCK, 300);
    public static final Fuel RED_MUSHROOM_BLOCK = get("red_mushroom_block", Material.RED_MUSHROOM_BLOCK, 300);
    public static final Fuel ANY_BANNER = get("any_banner", Tag.BANNERS, 300);
    public static final Fuel ANY_WOODEN_SLAB = get("any_wooden_slab", Tag.WOODEN_SLABS, 150);
    public static final Fuel BOW = get("bow", Material.BOW, 300);
    public static final Fuel FISHING_ROD = get("fishing_rod", Material.FISHING_ROD, 300);
    public static final Fuel LADDER = get("ladder", Material.LADDER, 300);
    public static final Fuel ANY_WOODEN_BUTTON = get("any_wooden_button", Tag.WOODEN_BUTTONS, 100);
    public static final Fuel WOODEN_PICKAXE = get("wooden_pickaxe", Material.WOODEN_PICKAXE, 200);
    public static final Fuel WOODEN_SHOVEL = get("wooden_shovel", Material.WOODEN_SHOVEL, 200);
    public static final Fuel WOODEN_HOE = get("wooden_hoe", Material.WOODEN_HOE, 200);
    public static final Fuel WOODEN_AXE = get("wooden_axe", Material.WOODEN_AXE, 200);
    public static final Fuel WOODEN_SWORD = get("wooden_sword", Material.WOODEN_SWORD, 200);
    public static final Fuel ANY_WOODEN_DOOR = get("any_wooden_door", Tag.WOODEN_TRAPDOORS, 200);
    public static final Fuel BOWL = get("bowl", Material.BOWL, 100);
    public static final Fuel ANY_SAPLING = get("any_sapling", Tag.SAPLINGS, 100);
    public static final Fuel STICK = get("stick", Material.STICK, 100);
    public static final Fuel ANY_WOOL = get("any_wool", Tag.WOOL, 100);
    public static final Fuel ANY_CARPET = get("any_carpet", Tag.CARPETS, 67);

    // 1.14+
    public static final Fuel SCAFFOLDING;
    public static final Fuel CARTOGRAPHY_TABLE;
    public static final Fuel FLETCHING_TABLE;
    public static final Fuel SMITHING_TABLE;
    public static final Fuel LECTERN;
    public static final Fuel COMPOSTER;
    public static final Fuel BARREL;
    public static final Fuel LOOM;
    public static final Fuel ANY_SIGN;
    public static final Fuel BAMBOO;
    public static final Fuel ANY_WOOD_FENCE;

    static {
        if (Util.isRunningMinecraft(1, 14)) {
            SCAFFOLDING = get("scaffolding", Material.SCAFFOLDING, 400);
            CARTOGRAPHY_TABLE = get("cartography_table", Material.CARTOGRAPHY_TABLE, 300);
            FLETCHING_TABLE = get("fletching_table", Material.FLETCHING_TABLE, 300);
            SMITHING_TABLE = get("smithing_table", Material.SMITHING_TABLE, 300);
            LECTERN = get("lectern", Material.LECTERN, 300);
            COMPOSTER = get("composter", Material.COMPOSTER, 300);
            BARREL = get("barrel", Material.BARREL, 300);
            LOOM = get("loom", Material.LOOM, 300);
            ANY_SIGN = get("any_sign", Tag.SIGNS, 200);
            BAMBOO = get("bamboo", Material.BAMBOO, 50);
            ANY_WOOD_FENCE = get("any_wood_fence", Tag.WOODEN_FENCES, 300);
        } else {
            SCAFFOLDING = null;
            CARTOGRAPHY_TABLE = null;
            FLETCHING_TABLE = null;
            SMITHING_TABLE = null;
            LECTERN = null;
            COMPOSTER = null;
            BARREL = null;
            LOOM = null;
            BAMBOO = null;
            ANY_SIGN = get("any_sign", Material.getMaterial("SIGN"), 200);
            ANY_WOOD_FENCE = get("any_wood_fence", Material.OAK_FENCE, 300);
        }
    }

    private static Fuel get(String name, Material fuel, int burnTicks) {
        Fuel fuel1 = new Fuel(Util.getKey("mc_fuel_" + name), fuel, burnTicks);
        VANILLA_FUELS.add(fuel1);
        return fuel1;
    }

    private static Fuel get(String name, Tag<Material> fuelTag, int burnTicks) {
        Fuel fuel = new Fuel(Util.getKey("mc_fuel_" + name), fuelTag, burnTicks);
        VANILLA_FUELS.add(fuel);
        return fuel;
    }

    private final NamespacedKey key;
    private final Material material;
    private final Tag<Material> tag;
    private final int burnTime;

    /**
     * Get an immutable list of vanilla MC fuels
     *
     * @return List of vanilla fuels
     */
    public static List<Fuel> getVanillaFuels() {
        return Collections.unmodifiableList(VANILLA_FUELS);
    }

    /**
     * Create a new fuel for furnaces
     *
     * @param key          Key for recipe
     * @param fuelMaterial Fuel to register
     * @param burnTime     Time this fuel will burn for (in ticks)
     */
    public Fuel(NamespacedKey key, Material fuelMaterial, int burnTime) {
        this.key = key;
        this.material = fuelMaterial;
        this.tag = null;
        this.burnTime = burnTime;
    }

    /**
     * Create a new fuel for furnaces
     *
     * @param key      Key for recipe
     * @param fuelTag  Tag to use as fuel
     * @param burnTime Time this fuel will burn for (in ticks)
     */
    public Fuel(NamespacedKey key, Tag<Material> fuelTag, int burnTime) {
        this.key = key;
        this.material = null;
        this.tag = fuelTag;
        this.burnTime = burnTime;
    }

    /**
     * Get the key from this fuel
     *
     * @return Key from fuel
     */
    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    /**
     * Get the material of this fuel
     *
     * @return Material of fuel (null if non-existent)
     */
    public Material getFuel() {
        return this.material;
    }

    /**
     * Get the tag of this fuel
     *
     * @return Tag of fuel (null if non-existent)
     */
    public Tag<Material> getTag() {
        return tag;
    }

    /**
     * Check if a material matches this fuel
     *
     * @param material Material to match
     * @return True if material matches
     */
    public boolean matchFuel(Material material) {
        if (this.material != null && this.material == material) {
            return true;
        } else return tag != null && tag.isTagged(material);
    }

    /**
     * Get the time this fuel will burn for
     *
     * @return Time fuel will burn
     */
    public int getBurnTime() {
        return this.burnTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fuel fuel = (Fuel) o;
        return burnTime == fuel.burnTime && Objects.equals(key, fuel.key) &&
                material == fuel.material && Objects.equals(tag, fuel.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, material, tag, burnTime);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public String toString() {
        return "Fuel{" +
                "key=" + key +
                (material != null ? ", material=" + material : ", tag=" + tag.getKey()) +
                ", burnTime=" + burnTime +
                '}';
    }

}
