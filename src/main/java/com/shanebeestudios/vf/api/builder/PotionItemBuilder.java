package com.shanebeestudios.vf.api.builder;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Builder to easily create potion {@link ItemStack ItemStacks}
 * <p>Create a new builder, manipulate it's meta,
 * and finish off using <b>{@link #build()}</b> to get your <b>{@link ItemStack}</b></p>
 */
@SuppressWarnings("unused")
public class PotionItemBuilder extends ItemBuilder {

    private PotionMeta potionMeta;

    /**
     * Start a new ItemBuilder
     * <p>Material will default to a <b>{@link Material#POTION}</b>. Amount will default to 1</p>
     */
    public PotionItemBuilder() {
        super(Material.POTION, 1);
    }

    /**
     * Start a new ItemBuilder
     * <p>Material must be of the potion variant. Amount will default to 1</p>
     *
     * @param material Material for this new item
     */
    public PotionItemBuilder(@NotNull Material material) {
        super(material);
        if (isNotPotion(material)) {
            super.itemStack.setType(Material.POTION);
        }
    }

    /**
     * Start a new ItemBuilder
     * <p>Material must be of the potion variant.</p>
     *
     * @param material Material for this new item
     * @param amount   Amount for this item
     */
    public PotionItemBuilder(@NotNull Material material, int amount) {
        super(material, amount);
        if (isNotPotion(material)) {
            super.itemStack.setType(Material.POTION);
        }
    }

    /**
     * Start a new ItemBuilder
     * <p>Material must be of the potion variant.
     * You can use the consumer to quickly manipulate the item meta if you choose</p>
     *
     * @param material Material for this new item
     * @param meta     Consumer to manipulate meta
     */
    public PotionItemBuilder(@NotNull Material material, @NotNull Consumer<ItemMeta> meta) {
        super(material, meta);
        if (isNotPotion(material)) {
            super.itemStack.setType(Material.POTION);
        }
    }

    /**
     * Start a new ItemBuilder
     * <p>Material must be of the potion variant.
     * You can use the consumer to quickly manipulate the item meta if you choose</p>
     *
     * @param material Material for this new item
     * @param amount   Amount for this item
     * @param meta     Consumer to manipulate meta
     */
    public PotionItemBuilder(@NotNull Material material, int amount, @NotNull Consumer<ItemMeta> meta) {
        super(material, amount, meta);
        if (isNotPotion(material)) {
            super.itemStack.setType(Material.POTION);
        }
    }

    /**
     * Add a {@link PotionEffect} to this item
     *
     * @param potionEffect PotionEffect to add
     * @return This PotionItemBuilder
     */
    public PotionItemBuilder addPotion(@NotNull PotionEffect potionEffect) {
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).addCustomEffect(potionEffect, false);
        }
        return this;
    }

    /**
     * Set the base potion of this item
     *
     * @param type     Base potion type
     * @param extended whether the potion is extended, <b>{@link PotionType#isExtendable()}</b> must be true
     * @param upgraded whether the potion is upgraded, <b>{@link PotionType#isUpgradeable()}</b> must be true
     * @return This PotionItemBuilder
     */
    public PotionItemBuilder basePotion(@NotNull PotionType type, boolean extended, boolean upgraded) {
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).setBasePotionData(new PotionData(type, extended, upgraded));
        }
        return this;
    }

    /**
     * Add a new potion effect to this item
     * <p>Icon and particles will default to true,
     * ambient will default to false, will not have an amplifier</p>
     *
     * @param type     Type of effect
     * @param duration Duration of effect (in ticks)
     * @return This PotionItemBuilder
     */
    public PotionItemBuilder addPotion(@NotNull PotionEffectType type, int duration) {
        if (itemMeta instanceof PotionMeta) {
            PotionEffect potionEffect = new PotionEffect(type, duration, 0, false, true, true);
            ((PotionMeta) itemMeta).addCustomEffect(potionEffect, false);
        }
        return this;
    }

    /**
     * Add a new potion effect to this item
     * <p>Icon and particles will default to true,
     * ambient will default to false</p>
     *
     * @param type      Type of effect
     * @param duration  Duration of effect (in ticks)
     * @param amplifier Amplifier for this effect
     * @return This PotionItemBuilder
     */
    public PotionItemBuilder addPotion(@NotNull PotionEffectType type, int duration, int amplifier) {
        if (itemMeta instanceof PotionMeta) {
            PotionEffect potionEffect = new PotionEffect(type, duration, amplifier, false, true, true);
            ((PotionMeta) itemMeta).addCustomEffect(potionEffect, false);
        }
        return this;
    }

    /**
     * Add a new potion effect to this item
     * <p>Icon and particles will default to true</p>
     *
     * @param type      Type of effect
     * @param duration  Duration of effect (in ticks)
     * @param amplifier Amplifier for this effect
     * @param ambient   Whether the effect is ambient or not
     * @return This PotionItemBuilder
     */
    public PotionItemBuilder addPotion(@NotNull PotionEffectType type, int duration, int amplifier, boolean ambient) {
        if (itemMeta instanceof PotionMeta) {
            PotionEffect potionEffect = new PotionEffect(type, duration, amplifier, ambient, true, true);
            ((PotionMeta) itemMeta).addCustomEffect(potionEffect, false);
        }
        return this;
    }

    /**
     * Add a new potion effect to this item
     * <p>Icon will default to true</p>
     *
     * @param type      Type of effect
     * @param duration  Duration of effect (in ticks)
     * @param amplifier Amplifier for this effect
     * @param ambient   Whether the effect is ambient or not
     * @param particles Whether the effect will show particles
     * @return This PotionItemBuilder
     */
    public PotionItemBuilder addPotion(@NotNull PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
        if (itemMeta instanceof PotionMeta) {
            PotionEffect potionEffect = new PotionEffect(type, duration, amplifier, ambient, particles, true);
            ((PotionMeta) itemMeta).addCustomEffect(potionEffect, false);
        }
        return this;
    }

    /**
     * Add a new potion effect to this item
     *
     * @param type      Type of effect
     * @param duration  Duration of effect (in ticks)
     * @param amplifier Amplifier for this effect
     * @param ambient   Whether the effect is ambient or not
     * @param particles Whether the effect will show particles
     * @param icon      Whether the effect's icon will show
     * @return This PotionItemBuilder
     */
    public PotionItemBuilder addPotion(@NotNull PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
        if (itemMeta instanceof PotionMeta) {
            PotionEffect potionEffect = new PotionEffect(type, duration, amplifier, ambient, particles, icon);
            ((PotionMeta) itemMeta).addCustomEffect(potionEffect, false);
        }
        return this;
    }

    /**
     * Set the color of this item
     *
     * @param color Color to set
     * @return This PotionItemBuilder
     */
    public PotionItemBuilder color(@NotNull Color color) {
        if (itemMeta instanceof PotionMeta) {
            ((PotionMeta) itemMeta).setColor(color);
        }
        return this;
    }

    private boolean isNotPotion(@NotNull Material material) {
        switch (material) {
            case POTION:
            case LINGERING_POTION:
            case SPLASH_POTION:
                return false;
        }
        return true;
    }

}
