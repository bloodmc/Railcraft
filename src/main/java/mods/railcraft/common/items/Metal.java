/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2016
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.items;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import mods.railcraft.api.core.IVariantEnum;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.aesthetics.generic.EnumGeneric;
import mods.railcraft.common.blocks.ore.EnumOre;
import mods.railcraft.common.core.IRailcraftObjectContainer;
import mods.railcraft.common.plugins.forge.OreDictPlugin;
import mods.railcraft.common.util.inventory.filters.StackFilters;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public enum Metal implements IVariantEnum {

    STEEL("Steel"),
    IRON("Iron"),
    GOLD("Gold"),
    COPPER("Copper"),
    TIN("Tin"),
    LEAD("Lead");
    public static final Metal[] VALUES = values();
    //    private static final EnumBiMap<Metal, EnumIngot> ingotMap = EnumBiMap.create(Metal.class, EnumIngot.class);
//    private static final EnumBiMap<Metal, EnumNugget> nuggetMap = EnumBiMap.create(Metal.class, EnumNugget.class);
    private static final BiMap<Metal, IVariantEnum> poorOreMap = HashBiMap.create();
    private static final BiMap<Metal, IVariantEnum> blockMap = HashBiMap.create();

    static {
//        metalObjects.add(BlockMetal.class);

//        ingotMap.put(STEEL, EnumIngot.STEEL);
//        ingotMap.put(COPPER, EnumIngot.COPPER);
//        ingotMap.put(TIN, EnumIngot.TIN);
//        ingotMap.put(LEAD, EnumIngot.LEAD);
//
//        nuggetMap.put(IRON, EnumNugget.IRON);
//        nuggetMap.put(STEEL, EnumNugget.STEEL);
//        nuggetMap.put(COPPER, EnumNugget.COPPER);
//        nuggetMap.put(TIN, EnumNugget.TIN);
//        nuggetMap.put(LEAD, EnumNugget.LEAD);

        poorOreMap.put(IRON, EnumOre.POOR_IRON);
        poorOreMap.put(GOLD, EnumOre.POOR_GOLD);
        poorOreMap.put(COPPER, EnumOre.POOR_COPPER);
        poorOreMap.put(TIN, EnumOre.POOR_TIN);
        poorOreMap.put(LEAD, EnumOre.POOR_LEAD);

        blockMap.put(STEEL, EnumGeneric.BLOCK_STEEL);
        blockMap.put(COPPER, EnumGeneric.BLOCK_COPPER);
        blockMap.put(TIN, EnumGeneric.BLOCK_TIN);
        blockMap.put(LEAD, EnumGeneric.BLOCK_LEAD);
    }

    public final Predicate<ItemStack> nuggetFilter;
    public final Predicate<ItemStack> ingotFilter;
    public final Predicate<ItemStack> blockFilter;
    private final String oreSuffix;
    private final String tag;

    Metal(String oreSuffix) {
        this.oreSuffix = oreSuffix;
        this.tag = name().toLowerCase(Locale.ROOT);
        nuggetFilter = StackFilters.ofOreType("nugget" + oreSuffix);
        ingotFilter = StackFilters.ofOreType("ingot" + oreSuffix);
        blockFilter = StackFilters.ofOreType("block" + oreSuffix);
    }

//    public static Metal get(EnumNugget nugget) {
//        return nuggetMap.inverse().get(nugget);
//    }
//
//    public static Metal get(EnumIngot ingot) {
//        return ingotMap.inverse().get(ingot);
//    }

    public static Metal get(EnumOre ore) {
        return poorOreMap.inverse().get(ore);
    }

    public static Metal get(EnumGeneric ore) {
        return blockMap.inverse().get(ore);
    }

    @Nullable
    @Override
    public Object getAlternate(String objectTag) {
        return Form.containerMap.inverse().get(objectTag).getOreDictTag(this);
    }

    @Override
    public String getName() {
        return tag;
    }

    public String getOreTag(Form form) {
        return form.orePrefix + oreSuffix;
    }

    @Nullable
    public ItemStack getStack(Form form) {
        return getStack(form, 1);
    }

    @Nullable
    public ItemStack getStack(Form form, int qty) {
        return form.getStack(this, qty);
    }

    public enum Form {
        NUGGET("nugget", RailcraftItems.NUGGET) {
            @Nullable
            @Override
            public ItemStack getStack(Metal metal, int qty) {
                switch (metal) {
                    case GOLD:
                        return new ItemStack(Items.GOLD_NUGGET, qty);
                }
                return super.getStack(metal, qty);
            }
        },
        INGOT("ingot", RailcraftItems.INGOT) {
            @Nullable
            @Override
            public ItemStack getStack(Metal metal, int qty) {
                switch (metal) {
                    case IRON:
                        return new ItemStack(Items.IRON_INGOT, qty);
                    case GOLD:
                        return new ItemStack(Items.GOLD_INGOT, qty);
                }
                return super.getStack(metal, qty);
            }
        },
        PLATE("plate", RailcraftItems.PLATE),
        BLOCK("block", RailcraftBlocks.GENERIC, blockMap) {
            @Nullable
            @Override
            public ItemStack getStack(Metal metal, int qty) {
                switch (metal) {
                    case IRON:
                        return new ItemStack(Blocks.IRON_BLOCK, qty);
                    case GOLD:
                        return new ItemStack(Blocks.GOLD_BLOCK, qty);
                }
                return super.getStack(metal, qty);
            }
        },
        POOR_ORE("poorOre", RailcraftBlocks.ORE, poorOreMap);
        private static final BiMap<Form, String> containerMap = HashBiMap.create();
        public static Form[] VALUES = values();
        private final String orePrefix;
        private final IRailcraftObjectContainer container;
        private final BiMap<Metal, IVariantEnum> variantMap;

        static {
            for (Form form : VALUES) {
                containerMap.put(form, form.container.getBaseTag());
            }
        }

        Form(String orePrefix, IRailcraftObjectContainer container) {
            this(orePrefix, container, null);
        }

        Form(String orePrefix, IRailcraftObjectContainer container, @Nullable BiMap<Metal, IVariantEnum> variantMap) {
            this.orePrefix = orePrefix;
            this.container = container;
            this.variantMap = variantMap;
        }

        public final String getOreDictTag(Metal metal) {
            return metal.getOreTag(this);
        }

        @Nullable
        public IVariantEnum getVariantObject(Metal metal) {
            if (variantMap != null)
                return variantMap.get(metal);
            return metal;
        }

        @Nullable
        public final ItemStack getStack(Metal metal) {
            return getStack(metal, 1);
        }

        @Nullable
        public ItemStack getStack(Metal metal, int qty) {
            IVariantEnum variant = getVariantObject(metal);
            ItemStack stack = null;
            if (variant != null)
                stack = container.getStack(qty, variant);
            if (stack == null)
                stack = OreDictPlugin.getOre(getOreDictTag(metal), qty);
            return stack;
        }
    }
}
