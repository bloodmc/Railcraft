/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.common.carts;

import mods.railcraft.common.core.RailcraftConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraft.inventory.EntityEquipmentSlot.MAINHAND;

//TODO: Test this
public class EntityCartPumpkin extends CartBaseSurprise {
    private static final CartBaseSurprise.SurpriseCategory MOBS = createSurpriseCategory(EntityCartPumpkin.class, 100);
    private static final CartBaseSurprise.SurpriseCategory POTIONS = createSurpriseCategory(EntityCartPumpkin.class, 100);

    static {
        POTIONS.add(new SurprisePotion(100));

        MOBS.add(SurpriseEntity.create(EntityBat.class, 75, 3));
        MOBS.add(SurpriseEntity.create(EntityWitch.class, 25, 1));
        MOBS.add(SurpriseEntity.create(EntityGhast.class, 25, 1));
        MOBS.add(SurpriseEntity.create(EntityPigZombie.class, 25, 1));
        MOBS.add(SurpriseEntity.create(EntityWither.class, 5, 1));

        MOBS.add(SurpriseEntity.create(EntitySkeleton.class, 50, 1, (cart, skeleton) -> {
            Random rand = cart.getRandom();
            if (rand.nextInt(4) == 0) {
                skeleton.tasks.addTask(4, new EntityAIAttackMelee(skeleton, 0.25F, false));
                skeleton.setSkeletonType(SkeletonType.WITHER);
                skeleton.setItemStackToSlot(MAINHAND, new ItemStack(Items.STONE_SWORD));
            } else {
                skeleton.tasks.addTask(4, new EntityAIAttackRanged(skeleton, 0.25F, 60, 10.0F));
                skeleton.setItemStackToSlot(MAINHAND, new ItemStack(Items.BOW));
            }

            skeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(rand.nextFloat() < 0.25F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
        }));
    }

    public EntityCartPumpkin(World world) {
        super(world);
    }

    public EntityCartPumpkin(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public ICartType getCartType() {
        return RailcraftCarts.PUMPKIN;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        setBlastRadius(1.5f);
    }

    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.PUMPKIN.getDefaultState();
    }

    @Override
    public List<ItemStack> getItemsDropped() {
        List<ItemStack> items = new ArrayList<ItemStack>();
        if (RailcraftConfig.doCartsBreakOnDrop()) {
            items.add(new ItemStack(Items.MINECART));
            items.add(new ItemStack(Blocks.PUMPKIN));
        } else
            items.add(getCartItem());
        return items;
    }

    @Override
    protected float getMinBlastRadius() {
        return 0.5f;
    }

    @Override
    protected float getMaxBlastRadius() {
        return 4;
    }

    @Override
    protected void spawnSurprises() {
        MOBS.spawnSurprises(this);
        POTIONS.spawnSurprises(this);
    }
}
