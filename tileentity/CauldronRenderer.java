package clashsoft.mods.morepotions.tileentity;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.block.BlockCauldron2;

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
		Tessellator tessellator = Tessellator.instance;
		
		tessellator.setBrightness(cauldron.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
		
		int i1 = renderer.blockAccess.getBlockMetadata(x, y, z);
		
		if (i1 > 0)
		{
			Icon icon2 = BlockFluid.getFluidIcon("lava_still");
			
			if (i1 > 3)
			{
				i1 = 3;
			}
			
			double yPos = (y - 1.0D + (6D + (double) i1 * 3.0D) / 16.0D) + 0.001D;

			renderer.renderFaceYPos(cauldron, x, y, z, icon2);
		}
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
	{
		TileEntityCauldron te = (TileEntityCauldron) tileentity;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		
		RenderHelper.disableStandardItemLighting();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		
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
		
		tessellator.setTranslation(-te.xCoord, -te.yCoord, -te.zCoord);
		this.renderBlockCauldron(renderBlocks, te, MorePotionsMod.cauldron2, te.xCoord, te.yCoord, te.zCoord);
		tessellator.draw();
		tessellator.setTranslation(0, 0, 0);
		GL11.glPopMatrix();
		
		RenderHelper.enableStandardItemLighting();
	}
}