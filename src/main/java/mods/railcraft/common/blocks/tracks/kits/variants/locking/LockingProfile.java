/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2016
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.common.blocks.tracks.kits.variants.locking;

import mods.railcraft.common.blocks.tracks.kits.variants.TrackKitLocking;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public abstract class LockingProfile {

    protected final TrackKitLocking track;

    public LockingProfile(TrackKitLocking track) {
        this.track = track;
    }

    public void onLock(EntityMinecart cart) {
    }

    public void onRelease(EntityMinecart cart) {
    }

    public void writeToNBT(NBTTagCompound data) {
    }

    public void readFromNBT(NBTTagCompound data) {
    }

    public void writePacketData(DataOutputStream data) throws IOException {
    }

    public void readPacketData(DataInputStream data) throws IOException {
    }
}
