package clashsoft.mods.morepotions.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.block.BlockCauldron2;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.client.Minecraft;
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
		Tessellator tess = Tessellator.instance;
		
		int i1 = tileentity.blockMetadata;
		
		if (i1 > 0)
		{
			if (i1 > 3)
			{
				i1 = 3;
			}
			
			int color = tileentity.getColor();
			if (color != -1)
			{
				float r = (color >> 16 & 255) / 255.0F;
				float g = (color >> 8 & 255) / 255.0F;
				float b = (color & 255) / 255.0F;
				
				Icon icon2;
				
				if (color == 0x0C0CFF)
				{
					icon2 = BlockFluid.getFluidIcon("water_still");
					r = g = b = 1F;
				}
				else
				{
					icon2 = BlockCauldron2.getLiquidIcon();
				}
				
				double yPos = (6D + (i1 * 3.0D)) / 16.0D;
				
				tess.setTranslation(-x, -y, -z);
				renderer.setRenderBounds(0.125, yPos, 0.125, 0.875, yPos + 0.001D, 0.875);
				renderer.setOverrideBlockTexture(icon2);
				renderer.renderStandardBlockWithColorMultiplier(Block.grass, x, y, z, r, g, b);
			}
		}
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		TileEntityCauldron te = (TileEntityCauldron) Minecraft.getMinecraft().theWorld.getBlockTileEntity(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
		
		GL11.glPushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		Tessellator tessellator = Tessellator.instance;
		RenderBlocks renderBlocks = Minecraft.getMinecraft().renderGlobal.globalRenderBlocks;
		
		RenderHelper.disableStandardItemLighting();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		
		if (Minecraft.isAmbientOcclusionEnabled())
			GL11.glShadeModel(GL11.GL_SMOOTH);
		else
			GL11.glShadeModel(GL11.GL_FLAT);
		
		GL11.glTranslated(d0, d1, d2);
		tessellator.startDrawingQuads();
		
		tessellator.setTranslation(-te.xCoord, -te.yCoord, -te.zCoord);
		this.renderBlockCauldron(renderBlocks, te, MorePotionsMod.cauldron2, te.xCoord, te.yCoord, te.zCoord);
		tessellator.draw();
		tessellator.setTranslation(0, 0, 0);
		GL11.glPopMatrix();
		
		RenderHelper.enableStandardItemLighting();
	}
}