/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2016
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.render.carts;

import mods.railcraft.api.carts.bore.IBoreHead;
import mods.railcraft.client.render.models.programmatic.bore.ModelTunnelBore;
import mods.railcraft.client.render.tools.OpenGL;
import mods.railcraft.common.carts.EntityTunnelBore;
import mods.railcraft.common.core.RailcraftConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderTunnelBore extends Render<EntityTunnelBore> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RailcraftConstants.CART_TEXTURE_FOLDER + "tunnel_bore.png");

    public RenderTunnelBore(RenderManager renderManager) {
        super(renderManager);
        shadowSize = 0.5F;
        modelTunnelBore = new ModelTunnelBore();
    }

    @Override
    public void doRender(EntityTunnelBore bore, double x, double y, double z, float entityYaw, float partialTicks) {
        // System.out.println("Render Yaw = " + f);
        OpenGL.glPushMatrix();
        long var10 = (long) bore.getEntityId() * 493286711L;
        var10 = var10 * var10 * 4392167121L + var10 * 98761L;
        float tx = (((float) (var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float ty = (((float) (var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float tz = (((float) (var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        OpenGL.glTranslatef(tx, ty, tz);

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        if (renderManager.isDebugBoundingBox()) {
            OpenGL.glPushAttrib(GL11.GL_ENABLE_BIT);
//            OpenGL.glDepthMask(false);
            OpenGL.glDisable(GL11.GL_TEXTURE_2D);
            OpenGL.glDisable(GL11.GL_LIGHTING);
            OpenGL.glDisable(GL11.GL_CULL_FACE);
            OpenGL.glDisable(GL11.GL_BLEND);
            for (Entity part : bore.getParts()) {
                OpenGL.glPushMatrix();
                double posX = part.lastTickPosX + (part.posX - part.lastTickPosX) * (double) partialTicks - TileEntityRendererDispatcher.staticPlayerX;
                double posY = part.lastTickPosY + (part.posY - part.lastTickPosY) * (double) partialTicks - TileEntityRendererDispatcher.staticPlayerY;
                double posZ = part.lastTickPosZ + (part.posZ - part.lastTickPosZ) * (double) partialTicks - TileEntityRendererDispatcher.staticPlayerZ;
                OpenGL.glTranslatef((float) posX, (float) posY, (float) posZ);
                float halfWidth = part.width / 2.0F;
                RenderGlobal.func_189694_a(-halfWidth, 0.0, -halfWidth, halfWidth, part.height, halfWidth, 255, 255, 255, 255);
                OpenGL.glPopMatrix();
            }
//            OpenGL.glDepthMask(true);
            OpenGL.glPopAttrib();
        }

        OpenGL.glTranslatef((float) x, (float) y, (float) z);
        switch (bore.getFacing()) {
            case NORTH:
                entityYaw = 90;
                break;
            case EAST:
                entityYaw = 0;
                break;
            case SOUTH:
                entityYaw = 270;
                break;
            case WEST:
                entityYaw = 180;
                break;
        }
        OpenGL.glRotatef(entityYaw, 0.0F, 1.0F, 0.0F);
        float f3 = (float) bore.getRollingAmplitude() - partialTicks;
        float f4 = bore.getDamage() - partialTicks;
        if (f4 < 0.0F) {
            f4 = 0.0F;
        }
        if (f3 > 0.0F) {
            float angle = (MathHelper.sin(f3) * f3 * f4) / 10F;
            angle = Math.min(angle, 0.8f);
            angle = Math.copySign(angle, bore.getRollingDirection());
            OpenGL.glRotatef(angle, 1.0F, 0.0F, 0.0F);
        }
        float light = bore.getBrightness(partialTicks);
        light = light + ((1.0f - light) * 0.4f);


        int j = 0xffffff;
        float c1 = (float) (j >> 16 & 0xff) / 255F;
        float c2 = (float) (j >> 8 & 0xff) / 255F;
        float c3 = (float) (j & 0xff) / 255F;

        OpenGL.glColor4f(c1 * light, c2 * light, c3 * light, 1.0F);

        IBoreHead head = bore.getBoreHead();
        if (head != null) {
            bindTexture(head.getBoreTexture());
            modelTunnelBore.setRenderBoreHead(true);
        } else {
            bindTexture(TEXTURE);
            modelTunnelBore.setRenderBoreHead(false);
        }

        OpenGL.glScalef(-1F, -1F, 1.0F);

        modelTunnelBore.setBoreHeadRotation(bore.getBoreRotationAngle());
        modelTunnelBore.setBoreActive(bore.isMinecartPowered());
        modelTunnelBore.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        OpenGL.glPopMatrix();
    }

    protected ModelTunnelBore modelTunnelBore;

    @Override
    protected ResourceLocation getEntityTexture(EntityTunnelBore entity) {
        return TEXTURE;
    }
}
