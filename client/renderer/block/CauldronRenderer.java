package clashsoft.mods.morepotions.client.renderer.block;

import clashsoft.mods.morepotions.block.BlockCauldron2;
import clashsoft.mods.morepotions.client.MPMClientProxy;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockFluid;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
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
        float r = (float)(l >> 16 & 255) / 255.0F;
        float g = (float)(l >> 8 & 255) / 255.0F;
        float b = (float)(l & 255) / 255.0F;
        
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
        Icon icon = cauldron.getBlockTextureFromSide(2);
        renderer.renderFaceXPos(cauldron, (double)((float)x - 1.0F + 0.125F), (double)y, (double)z, icon);
        renderer.renderFaceXNeg(cauldron, (double)((float)x + 1.0F - 0.125F), (double)y, (double)z, icon);
        renderer.renderFaceZPos(cauldron, (double)x, (double)y, (double)((float)z - 1.0F + 0.125F), icon);
        renderer.renderFaceZNeg(cauldron, (double)x, (double)y, (double)((float)z + 1.0F - 0.125F), icon);
        Icon icon1 = BlockCauldron.getCauldronIcon("inner");
        renderer.renderFaceYPos(cauldron, (double)x, (double)((float)y - 1.0F + 0.25F), (double)z, icon1);
        renderer.renderFaceYNeg(cauldron, (double)x, (double)((float)y + 1.0F - 0.75F), (double)z, icon1);
        int i1 = world.getBlockMetadata(x, y, z);

        if (i1 > 0)
        {
        	if (i1 > 3)
            {
                i1 = 3;
            }
        	
        	TileEntityCauldron tileEntity = (TileEntityCauldron) world.getBlockTileEntity(x, y, z);
        	int color = tileEntity.getColor();
        	
            r = (float)(color >> 16 & 255) / 255.0F;
            g = (float)(color >> 8 & 255) / 255.0F;
            b = (float)(color & 255) / 255.0F;

            Icon icon2;
            
            if (color == 0 || color == 0x0C0CFF || color == 0xFFFFFF)
            {
            	color = 0xFFFFFF;
            	icon2 = BlockFluid.getFluidIcon("water_still");
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
            
            renderer.renderFaceYPos(cauldron, (double)x, (double)((float)y - 1.0F + (6.0F + (float)i1 * 3.0F) / 16.0F), (double)z, icon2);
        }

        return true;
	}
	
	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}
	
	@Override
	public int getRenderId()
	{
		return MPMClientProxy.cauldronRenderType;
	}
	
}
