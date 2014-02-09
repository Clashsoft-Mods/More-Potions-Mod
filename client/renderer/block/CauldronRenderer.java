package clashsoft.mods.morepotions.client.renderer.block;

import clashsoft.mods.morepotions.block.BlockCauldron2;
import clashsoft.mods.morepotions.client.MPMClientProxy;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class CauldronRenderer implements ISimpleBlockRenderingHandler
{
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		BlockCauldron2 cauldron = (BlockCauldron2) block;
		
		renderer.renderStandardBlock(cauldron, x, y, z);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(cauldron.getMixedBrightnessForBlock(world, x, y, z));
		int l = cauldron.colorMultiplier(world, x, y, z);
		float r = (l >> 16 & 255) / 255.0F;
		float g = (l >> 8 & 255) / 255.0F;
		float b = (l & 255) / 255.0F;
		
		if (EntityRenderer.anaglyphEnable)
		{
			float r1 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
			float g1 = (r * 30.0F + g * 70.0F) / 100.0F;
			float b1 = (r * 30.0F + b * 70.0F) / 100.0F;
			r = r1;
			g = g1;
			b = b1;
		}
		
		tessellator.setColorOpaque_F(r, g, b);
		IIcon icon = cauldron.getBlockTextureFromSide(2);
		renderer.renderFaceXPos(cauldron, x - 1.0F + 0.125F, y, z, icon);
		renderer.renderFaceXNeg(cauldron, x + 1.0F - 0.125F, y, z, icon);
		renderer.renderFaceZPos(cauldron, x, y, z - 1.0F + 0.125F, icon);
		renderer.renderFaceZNeg(cauldron, x, y, z + 1.0F - 0.125F, icon);
		IIcon icon1 = BlockCauldron.getCauldronIcon("inner");
		renderer.renderFaceYPos(cauldron, x, y - 1.0F + 0.25F, z, icon1);
		renderer.renderFaceYNeg(cauldron, x, y + 1.0F - 0.75F, z, icon1);
		int i1 = world.getBlockMetadata(x, y, z);
		
		if (i1 > 0)
		{
			if (i1 > 3)
			{
				i1 = 3;
			}
			
			TileEntityCauldron tileEntity = (TileEntityCauldron) world.getTileEntity(x, y, z);
			int color = tileEntity.getColor();
			
			r = (color >> 16 & 255) / 255.0F;
			g = (color >> 8 & 255) / 255.0F;
			b = (color & 255) / 255.0F;
			
			IIcon icon2;
			
			if (color == 0 || color == 0x0C0CFF || color == 0xFFFFFF)
			{
				color = 0xFFFFFF;
				icon2 = Blocks.water.getIcon(0, 0);
				tessellator.setColorOpaque_F(1F, 1F, 1F);
			}
			else
			{
				icon2 = BlockCauldron2.getLiquidIcon();
				if (EntityRenderer.anaglyphEnable)
				{
					float r1 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
					float g1 = (r * 30.0F + g * 70.0F) / 100.0F;
					float b1 = (r * 30.0F + b * 70.0F) / 100.0F;
					r = r1;
					g = g1;
					b = b1;
				}
				
				tessellator.setColorOpaque_F(r, g, b);
			}
			
			renderer.renderFaceYPos(cauldron, x, y - 1.0F + (6.0F + i1 * 3.0F) / 16.0F, z, icon2);
		}
		
		return true;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int metadata)
	{
		return false;
	}
	
	@Override
	public int getRenderId()
	{
		return MPMClientProxy.cauldronRenderType;
	}
}
