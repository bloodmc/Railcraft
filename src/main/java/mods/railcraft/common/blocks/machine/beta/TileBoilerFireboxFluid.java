/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.common.blocks.machine.beta;

import mods.railcraft.api.fuel.FuelManager;
import mods.railcraft.common.blocks.machine.MultiBlockPattern;
import mods.railcraft.common.blocks.machine.TileMultiBlock;
import mods.railcraft.common.fluids.FluidHelper;
import mods.railcraft.common.fluids.FluidItemHelper;
import mods.railcraft.common.fluids.Fluids;
import mods.railcraft.common.fluids.tanks.BoilerFuelTank;
import mods.railcraft.common.gui.EnumGui;
import mods.railcraft.common.gui.GuiHandler;
import mods.railcraft.common.util.inventory.InvTools;
import mods.railcraft.common.util.steam.FluidFuelProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TileBoilerFireboxFluid extends TileBoilerFirebox {

    private static final int TANK_FUEL = 2;
    private static final int[] SLOTS = InvTools.buildSlotArray(0, 2);
    protected final BoilerFuelTank tankFuel = new BoilerFuelTank(FluidHelper.BUCKET_VOLUME * 16, this);

    public TileBoilerFireboxFluid() {
        super(2);
        tankManager.add(tankFuel);
        boiler.setFuelProvider(new FluidFuelProvider(tankFuel));
    }

    public static void placeFluidBoiler(World world, BlockPos pos, int width, int height, boolean highPressure, int water, FluidStack fuel) {
        for (MultiBlockPattern pattern : TileBoiler.patterns) {
            if (pattern.getPatternHeight() - 3 == height && pattern.getPatternWidthX() - 2 == width) {
                Map<Character, IBlockState> blockMapping = new HashMap<Character, IBlockState>();
                blockMapping.put('F', EnumMachineBeta.BOILER_FIREBOX_FLUID.getState());
                blockMapping.put('H', highPressure ? EnumMachineBeta.BOILER_TANK_HIGH_PRESSURE.getState() : EnumMachineBeta.BOILER_TANK_LOW_PRESSURE.getState());
                TileEntity tile = pattern.placeStructure(world, pos, blockMapping);
                if (tile instanceof TileBoilerFireboxFluid) {
                    TileBoilerFireboxFluid master = (TileBoilerFireboxFluid) tile;
                    master.tankWater.setFluid(Fluids.WATER.get(water));
                    master.tankFuel.setFluid(fuel);
                }
                return;
            }
        }
    }

    @Override
    public EnumMachineBeta getMachineType() {
        return EnumMachineBeta.BOILER_FIREBOX_FLUID;
    }

    @Override
    public boolean openGui(EntityPlayer player) {
//        ItemStack current = player.getCurrentEquippedItem();
//        if (current != null && current.itemID == getBlockId()) {
//            return false;
//        }
        TileMultiBlock mBlock = getMasterBlock();
        if (mBlock != null) {
            GuiHandler.openGui(EnumGui.BOILER_LIQUID, player, worldObj, mBlock.getPos());
            return true;
        }
        return false;
    }

    @Override
    protected boolean handleClick(EntityPlayer player, EnumFacing side) {
        if (FluidHelper.handleRightClick(this, side, player, true, false))
            return true;
        return super.handleClick(player, side);
    }

    @Override
    protected void process() {
    }

    @Override
    protected void processBuckets() {
        super.processBuckets();

        FluidHelper.drainContainers(this, inventory, SLOT_LIQUID_INPUT, SLOT_LIQUID_OUTPUT);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        if (fluid == null) return false;
        if (FuelManager.getBoilerFuelValue(fluid) > 0) return true;
        return super.canFill(from, fluid);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (Fluids.WATER.is(resource))
            return fill(TANK_WATER, resource, doFill);
        return fill(TANK_FUEL, resource, doFill);
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == SLOT_LIQUID_OUTPUT;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (!isStructureValid())
            return false;
        switch (slot) {
            case SLOT_LIQUID_INPUT:
                Fluid fluid = FluidItemHelper.getFluidInContainer(stack);
                if (fluid == null)
                    return false;
                if (Fluids.WATER.is(fluid) || FuelManager.getBoilerFuelValue(fluid) > 0)
                    return true;
        }
        return false;
    }
}
