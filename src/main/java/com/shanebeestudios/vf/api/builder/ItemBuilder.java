package com.shanebeestudios.vf.api.builder;

import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Builder to easily create {@link ItemStack ItemStacks}
 * <p>Create a new builder, manipulate it's meta,
 * and finish off using <b>{@link #build()}</b> to get your <b>{@link ItemStack}</b></p>
 */
@SuppressWarnings("unused")
public class ItemBuilder {

    final ItemStack itemStack;
    final ItemMeta itemMeta;

    /**
     * Start a new ItemBuilder
     * <p>Amount will default to 1</p>
     *
     * @param material Material for this new item
     */
    public ItemBuilder(@NotNull Material material) {
        this(material, 1);
    }

    /**
     * Start a new ItemBuilder
     *
     * @param material Material for this new item
     * @param amount   Amount for this item
     */
    public ItemBuilder(@NotNull Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    /**
     * Start a new ItemBuilder
     * <p>You can use the consumer to quickly manipulate the item meta if you choose</p>
     *
     * @param material Material for this new item
     * @param meta     Consumer to manipulate meta
     */
    public ItemBuilder(@NotNull Material material, @NotNull Consumer<ItemMeta> meta) {
        this(material, 1, meta);
    }

    /**
     * Start a new ItemBuilder
     * <p>You can use the consumer to quickly manipulate the item meta if you choose</p>
     *
     * @param material Material for this new item
     * @param amount   Amount for this item
     * @param meta     Consumer to manipulate meta
     */
    public ItemBuilder(@NotNull Material material, int amount, @NotNull Consumer<ItemMeta> meta) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
        meta.accept(itemMeta);
    }

    /**
     * Set the name of this item
     * <p>Will accept color codes</p>
     *
     * @param name Name to set
     * @return This ItemBuilder
     */
    public ItemBuilder name(@NotNull String name) {
        itemMeta.setDisplayName(Util.getColString(name));
        return this;
    }

    /**
     * Add lore to this item
     * <p>Will stack onto previous lore.
     * Will accept color codes</p>
     *
     * @param lore Lore to add
     * @return This ItemBuilder
     */
    public ItemBuilder addLore(@NotNull String lore) {
        List<String> lores = itemMeta.getLore();
        if (lores == null) {
            lores = new ArrayList<>();
        }
        lores.add(Util.getColString(lore));
        itemMeta.setLore(lores);
        return this;
    }

    /**
     * Set the lore of this item
     * <p>This will wipe any previous lore, will not accept color codes.
     * <br>You will need to manage colors yourself, consider using
     * <b>{@link Util#getColString(String)}</b></p>
     *
     * @param lores List of lore to add
     * @return This ItemBuilder
     */
    public ItemBuilder lore(@NotNull List<String> lores) {
        itemMeta.setLore(lores);
        return this;
    }

    /**
     * Add an enchantment to this item
     *
     * @param enchantment Enchantment to add
     * @param level       Level of enchantment to add
     * @return This ItemBuilder
     */
    public ItemBuilder addEnchant(@NotNull Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Hide the enchantments on this item
     *
     * @return This ItemBuilder
     */
    public ItemBuilder hideEnchants() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Hide the attributes on this item
     *
     * @return This ItemBuilder
     */
    public ItemBuilder hideAttributes() {
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    /**
     * Hide the unbreakable flag on this item
     *
     * @return This ItemBuilder
     */
    public ItemBuilder hideUnbreakable() {
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    /**
     * Hide the potion effects on this item
     *
     * @return This ItemBuilder
     */
    public ItemBuilder hidePotionEffects() {
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    /**
     * Set the custom model data this item will have
     *
     * @param modelData Custom model data to set
     * @return This ItemBuilder
     */
    public ItemBuilder customModelData(int modelData) {
        itemMeta.setCustomModelData(modelData);
        return this;
    }

    /**
     * Make this item unbreakable
     *
     * @return This ItemBuilder
     */
    public ItemBuilder unbreakable() {
        itemMeta.setUnbreakable(true);
        return this;
    }

    /**
     * Get the ItemStack built by this builder
     *
     * @return ItemStack built by this builder
     */
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
