/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2016
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.carts;

import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.items.IMinecartItem;
import mods.railcraft.common.blocks.tracks.TrackTools;
import mods.railcraft.common.core.IRailcraftObject;
import mods.railcraft.common.core.RailcraftConfig;
import mods.railcraft.common.gui.tooltips.ToolTip;
import mods.railcraft.common.plugins.forge.LocalizationPlugin;
import mods.railcraft.common.util.misc.Game;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCart extends ItemMinecart implements IMinecartItem, IRailcraftObject {

    private final ICartType type;
    private int rarity = 0;

    public ItemCart(ICartType cart) {
        super(EntityMinecart.Type.RIDEABLE);
        maxStackSize = RailcraftConfig.getMinecartStackSize();
        this.type = cart;
        setUnlocalizedName(cart.getTag());
        setMaxDamage(0);
        setHasSubtypes(true);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorDefaultDispenseItem());
    }

    public ItemCart setRarity(int rarity) {
        this.rarity = rarity;
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.values()[rarity];
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!TrackTools.isRailBlockAt(world, pos))
            return EnumActionResult.FAIL;
        if (Game.isHost(world)) {
            EntityMinecart placedCart = placeCart(player.getGameProfile(), stack, world, pos);
            if (placedCart != null) {
                stack.stackSize--;
            }
        }
        return EnumActionResult.SUCCESS;
    }

    public ICartType getCartType() {
        return type;
    }

    @Override
    public boolean canBePlacedByNonPlayer(ItemStack cart) {
        return true;
    }

    @Nullable
    @Override
    public EntityMinecart placeCart(GameProfile owner, ItemStack cartStack, World world, BlockPos pos) {
        return CartTools.placeCart(type, owner, cartStack, world, pos);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> info, boolean adv) {
        super.addInformation(stack, player, info, adv);
        ToolTip tip = ToolTip.buildToolTip(stack.getUnlocalizedName() + ".tip");
        if (tip != null)
            info.addAll(tip.convertToStrings());
        ItemStack filter = CartBaseFiltered.getFilterFromCartItem(stack);
        if (filter != null) {
            info.add(TextFormatting.BLUE + LocalizationPlugin.translate("railcraft.gui.filter") + ": " + filter.getDisplayName());
        }
    }

}
