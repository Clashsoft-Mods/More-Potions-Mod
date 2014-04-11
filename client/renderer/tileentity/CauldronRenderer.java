package clashsoft.mods.morepotions.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.block.BlockCauldron2;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class CauldronRenderer extends TileEntitySpecialRenderer
{
	public static RenderBlocks	renderBlocks	= new RenderBlocks();
	
	public void renderBlockCauldron(RenderBlocks renderer, TileEntityCauldron tileentity, BlockCauldron2 cauldron, int x, int y, int z)
	{
		Tessellator tessellator = Tessellator.instance;
		
		int i1 = tileentity.getWorldObj().getBlockMetadata(x, y, z);
		
		if (i1 > 0)
		{
			if (i1 > 3)
			{
				i1 = 3;
			}
			
			int color = tileentity.getColor();
			if (color != -1)
			{
				IIcon icon2;
				
				if (color == 0x0C0CFF || color == 0)
				{
					icon2 = Blocks.water.getIcon(0, 0);
				}
				else
				{
					icon2 = BlockCauldron2.getLiquidIcon();
					tessellator.setColorOpaque_I(color);
				}
				
				float yPos = (6F + (i1 * 3)) / 16.0F;
				
				tessellator.setTranslation(-x, -y, -z);
				renderer.setRenderBounds(0.125D, yPos, 0.125D, 0.875D, yPos + 0.0001D, 0.875D);
				renderer.setOverrideBlockTexture(icon2);
				renderer.renderFaceYPos(cauldron, x, y, z, icon2);
			}
		}
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		TileEntityCauldron te = (TileEntityCauldron) Minecraft.getMinecraft().theWorld.getTileEntity(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
		
		GL11.glPushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		Tessellator tessellator = Tessellator.instance;
		
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