package com.shanebeestudios.vf.api.recipe;

import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
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
    public static final Fuel BLAZE_ROD = get("blaze_rod", Material.BLAZE_ROD, 2400);
    public static final Fuel COAL = get("coal", Material.COAL, 1600);
    public static final Fuel CHARCOAL = get("charcoal", Material.CHARCOAL, 1600);
    public static final Fuel LOGS = get("logs", Tag.LOGS, 300);
    public static final Fuel PLANKS = get("planks", Tag.PLANKS, 300);
    public static final Fuel WOODEN_STAIRS = get("wooden_stairs", Tag.WOODEN_STAIRS, 300);
    public static final Fuel WOODEN_SLABS = get("wooden_slabs", Tag.WOODEN_SLABS, 150);
    public static final Fuel WOODEN_TRAPDOORS = get("wooden_trapdoors", Tag.WOODEN_TRAPDOORS, 300);
    public static final Fuel WOODEN_PRESSURE_PLATES = get("wooden_pressure_plates", Tag.WOODEN_PRESSURE_PLATES, 300);
    public static final Fuel WOODEN_FENCES = get("wooden_fences", Tag.WOODEN_FENCES, 300);
    public static final Fuel FENCE_GATES = get("fence_gates", Tag.FENCE_GATES, 300);
    public static final Fuel NOTE_BLOCK = get("note_block", Material.NOTE_BLOCK, 300);
    public static final Fuel BOOKSHELF = get("bookshelf", Material.BOOKSHELF, 300);
    public static final Fuel CHISELED_BOOKSHELF;
    public static final Fuel LECTERN = get("lectern", Material.LECTERN, 300);
    public static final Fuel JUKEBOX = get("jukebox", Material.JUKEBOX, 300);
    public static final Fuel CHEST = get("chest", Material.CHEST, 300);
    public static final Fuel TRAPPED_CHEST = get("trapped_chest", Material.TRAPPED_CHEST, 300);
    public static final Fuel CRAFTING_TABLE = get("crafting_table", Material.CRAFTING_TABLE, 300);
    public static final Fuel DAYLIGHT_DETECTOR = get("daylight_detector", Material.DAYLIGHT_DETECTOR, 300);
    public static final Fuel BANNERS = get("banners", Tag.BANNERS, 300);
    public static final Fuel BOW = get("bow", Material.BOW, 300);
    public static final Fuel FISHING_ROD = get("fishing_rod", Material.FISHING_ROD, 300);
    public static final Fuel LADDER = get("ladder", Material.LADDER, 300);
    public static final Fuel SIGNS = get("signs", Tag.SIGNS, 200);
    public static final Fuel HANGING_SIGNS;
    public static final Fuel WOODEN_SHOVEL = get("wooden_shovel", Material.WOODEN_SHOVEL, 200);
    public static final Fuel WOODEN_SWORD = get("wooden_sword", Material.WOODEN_SWORD, 200);
    public static final Fuel WOODEN_HOE = get("wooden_hoe", Material.WOODEN_HOE, 200);
    public static final Fuel WOODEN_AXE = get("wooden_axe", Material.WOODEN_AXE, 200);
    public static final Fuel WOODEN_PICKAXE = get("wooden_pickaxe", Material.WOODEN_PICKAXE, 200);
    public static final Fuel WOODEN_DOORS = get("wooden_doors", Tag.WOODEN_DOORS, 200);
    public static final Fuel BOATS = get("boats", Tag.ITEMS_BOATS, 1200);
    public static final Fuel WOOL = get("wool", Tag.WOOL, 100);
    public static final Fuel WOODEN_BUTTONS = get("wooden_buttons", Tag.WOODEN_BUTTONS, 100);
    public static final Fuel STICK = get("stick", Material.STICK, 100);
    public static final Fuel SAPLINGS = get("saplings", Tag.SAPLINGS, 100);
    public static final Fuel BOWL = get("bowl", Material.BOWL, 100);
    public static final Fuel WOOL_CARPETS = get("wool_carpets", Tag.WOOL_CARPETS, 67);
    public static final Fuel DRIED_KELP_BLOCK = get("dried_kelp_block", Material.DRIED_KELP_BLOCK, 4001);
    public static final Fuel CROSSBOW = get("crossbow", Material.CROSSBOW, 300);
    public static final Fuel BAMBOO = get("bamboo", Material.BAMBOO, 50);
    public static final Fuel DEAD_BUSH = get("dead_bush", Material.DEAD_BUSH, 100);
    public static final Fuel SCAFFOLDING = get("scaffolding", Material.SCAFFOLDING, 50);
    public static final Fuel LOOM = get("loom", Material.LOOM, 300);
    public static final Fuel BARREL = get("barrel", Material.BARREL, 300);
    public static final Fuel CARTOGRAPHY_TABLE = get("cartography_table", Material.CARTOGRAPHY_TABLE, 300);
    public static final Fuel FLETCHING_TABLE = get("fletching_table", Material.FLETCHING_TABLE, 300);
    public static final Fuel SMITHING_TABLE = get("smithing_table", Material.SMITHING_TABLE, 300);
    public static final Fuel COMPOSTER = get("composter", Material.COMPOSTER, 300);
    public static final Fuel AZALEA = get("azalea", Material.AZALEA, 100);
    public static final Fuel FLOWERING_AZALEA = get("flowering_azalea", Material.FLOWERING_AZALEA, 100);
    public static final Fuel MANGROVE_ROOTS;

    static {
        if (Util.isRunningMinecraft(1, 19)) {
            MANGROVE_ROOTS = get("mangrove_roots", Material.MANGROVE_ROOTS, 300);
        } else {
            MANGROVE_ROOTS = null;
        }
        if (Util.isRunningMinecraft(1,20)) {
            CHISELED_BOOKSHELF = get("chiseled_bookshelf", Material.CHISELED_BOOKSHELF, 300);
            HANGING_SIGNS = get("hanging_signs", Tag.ITEMS_HANGING_SIGNS, 800);
        } else {
            CHISELED_BOOKSHELF = null;
            HANGING_SIGNS = null;
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
     * @deprecated Use {@link #getFuelMaterial()} instead
     * <p>Will be working on using {@link ItemStack ItemStacks} for recipes in the future</p>
     */
    @Deprecated
    public Material getFuel() {
        return this.material;
    }

    /**
     * Get the {@link Material} of this fuel
     *
     * @return Material of fuel (null if non-existent)
     */
    public Material getFuelMaterial() {
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
