package com.shanebeestudios.vf.api.builder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Builder to easily create {@link ItemStack ItemStacks} with {@link BlockData}
 * <p>Create a new builder, manipulate it's meta,
 * and finish off using <b>{@link #build()}</b> to get your <b>{@link ItemStack}</b></p>
 */
@SuppressWarnings("unused")
public class BlockDataItemBuilder extends ItemBuilder {

    /**
     * Start a new ItemBuilder
     * <p>Amount will default to 1</p>
     *
     * @param material Material for this new item
     */
    public BlockDataItemBuilder(@NotNull Material material) {
        super(material);
    }

    /**
     * Start a new ItemBuilder
     *
     * @param material Material for this new item
     * @param amount   Amount for this item
     */
    public BlockDataItemBuilder(@NotNull Material material, int amount) {
        super(material, amount);
    }

    /**
     * Start a new ItemBuilder
     * <p>You can use the consumer to quickly manipulate the item meta if you choose</p>
     *
     * @param material Material for this new item
     * @param meta     Consumer to manipulate meta
     */
    public BlockDataItemBuilder(@NotNull Material material, @NotNull Consumer<ItemMeta> meta) {
        super(material, meta);
    }

    /**
     * Start a new ItemBuilder
     * <p>You can use the consumer to quickly manipulate the item meta if you choose</p>
     *
     * @param material Material for this new item
     * @param amount   Amount for this item
     * @param meta     Consumer to manipulate meta
     */
    public BlockDataItemBuilder(@NotNull Material material, int amount, @NotNull Consumer<ItemMeta> meta) {
        super(material, amount, meta);
    }

    /** Add BlockData to this item
     * @param blockData BlockData to add
     * @return This BlockDataItemBuilder
     */
    public BlockDataItemBuilder blockData(@NotNull BlockData blockData) {
        if (itemMeta instanceof BlockDataMeta) {
            ((BlockDataMeta) itemMeta).setBlockData(blockData);
        }
        return this;
    }

    /** Add BlockData to this item
     * <p>Uses the <b>{@link Bukkit#createBlockData(String)}</b>
     * method to create the data from a string</p>
     * @param blockDataString String to create BlockData out of
     * @return This BlockDataItemBuilder
     */
    public BlockDataItemBuilder blockData(@NotNull String blockDataString) {
        if (itemMeta instanceof BlockDataMeta) {
            BlockData blockData = Bukkit.createBlockData(blockDataString);
            ((BlockDataMeta) itemMeta).setBlockData(blockData);
        }
        return this;
    }

}
