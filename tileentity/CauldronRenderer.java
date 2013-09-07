package clashsoft.mods.morepotions.tileentity;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.block.BlockCauldron2;

import net.minecraft.block.BlockFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;

public class CauldronRenderer extends TileEntitySpecialRenderer
{
	public void renderBlockCauldron(RenderBlocks renderer, TileEntityCauldron tileentity, BlockCauldron2 cauldron, int x, int y, int z)
	{	
		Tessellator tessellator = Tessellator.instance;
		
		tessellator.setBrightness(cauldron.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
		
		// Color
		float f = 1.0F;
		int l = cauldron.colorMultiplier(renderer.blockAccess, x, y, z);
		float f1 = (l >> 16 & 255) / 255.0F;
		float f2 = (l >> 8 & 255) / 255.0F;
		float f3 = (l & 255) / 255.0F;
		float f4;
		
		if (EntityRenderer.anaglyphEnable)
		{
			float f5 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			f4 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = f5;
			f2 = f4;
			f3 = f6;
		}
		
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		
		// Cauldron
		Icon icon = cauldron.getBlockTextureFromSide(2);
		f4 = 0.125F;
		renderer.renderFaceXPos(cauldron, x - 1.0F + f4, y, z, icon);
		renderer.renderFaceXNeg(cauldron, x + 1.0F - f4, y, z, icon);
		renderer.renderFaceZPos(cauldron, x, y, z - 1.0F + f4, icon);
		renderer.renderFaceZNeg(cauldron, x, y, z + 1.0F - f4, icon);
		Icon icon1 = BlockCauldron2.func_94375_b("cauldron_inner");
		renderer.renderFaceYPos(cauldron, x, y - 1.0F + 0.25F, z, icon1);
		renderer.renderFaceYNeg(cauldron, x, y + 1.0F - 0.75F, z, icon1);
		int i1 = renderer.blockAccess.getBlockMetadata(x, y, z);
		
		// Water/Liquid
		if (i1 > 0)
		{
			Icon icon2 = tileentity.water() ? BlockFluid.getFluidIcon("water_still") : BlockFluid.getFluidIcon("lava_still");
			
			if (i1 > 3)
			{
				i1 = 3;
			}
			
			renderer.renderFaceYPos(cauldron, x, y - 1.0F + (6.0F + i1 * 3.0F) / 16.0F, z, icon2);
		}
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		TileEntityCauldron te = (TileEntityCauldron) tileentity;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		
		// Disable standard lighting. This is done the same as in
		// TileEntityRendererPiston.
		RenderHelper.disableStandardItemLighting();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		// GL11.glDisable(GL11.GL_CULL_FACE); This was initially part of the
		// code that was taken from the piston, but it was causing some of the
		// rendering to look of, e.g. with a cobweb (it would look thicker as
		// both sides were rendering due to this being disabled).
		
		if (Minecraft.isAmbientOcclusionEnabled())
		{
			GL11.glShadeModel(GL11.GL_SMOOTH);
		}
		else
		{
			GL11.glShadeModel(GL11.GL_FLAT);
		}
		GL11.glPushMatrix();
		GL11.glTranslated(d0, d1, d2);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		RenderBlocks renderBlocks = new RenderBlocks(tileentity.worldObj);
		
		// To get the block to render in the right place, I had to pass in 0, 0,
		// 0 as the coordinates for renderBlockByRenderType. This however means
		// that it will use the properties of the block at 0, 0, 0 for some
		// things. This translation allows for the actual block coordinates to
		// be passed in.
		tessellator.setTranslation(-te.xCoord, -te.yCoord, -te.zCoord);
		renderBlockCauldron(renderBlocks, te, MorePotionsMod.cauldron2, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
		tessellator.draw();
		tessellator.setTranslation(0, 0, 0);
		GL11.glPopMatrix();
		
		// Reset to standard lighting
		RenderHelper.enableStandardItemLighting();
	}
}