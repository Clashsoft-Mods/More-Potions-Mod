package clashsoft.mods.morepotions.tileentity;

import clashsoft.mods.morepotions.ClientProxy;
import clashsoft.mods.morepotions.block.BlockCauldron2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class CauldronRenderer implements ISimpleBlockRenderingHandler
{
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		return renderCauldron(world, (BlockCauldron2)block, x, y, z, renderer);
	}
	
	public boolean renderCauldron(IBlockAccess par0, BlockCauldron2 par1BlockCauldron, int par2, int par3, int par4, RenderBlocks renderer)
	{
		renderer.renderStandardBlock(par1BlockCauldron, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(par1BlockCauldron.getMixedBrightnessForBlock(renderer.blockAccess, par2, par3, par4));
        float f = 1.0F;
        int l = par1BlockCauldron.colorMultiplier(renderer.blockAccess, par2, par3, par4);
        float f1 = (float)(l >> 16 & 255) / 255.0F;
        float f2 = (float)(l >> 8 & 255) / 255.0F;
        float f3 = (float)(l & 255) / 255.0F;
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
        Icon icon = par1BlockCauldron.getBlockTextureFromSide(2);
        f4 = 0.125F;
        renderer.renderFaceXPos(par1BlockCauldron, (double)((float)par2 - 1.0F + f4), (double)par3, (double)par4, icon);
        renderer.renderFaceXNeg(par1BlockCauldron, (double)((float)par2 + 1.0F - f4), (double)par3, (double)par4, icon);
        renderer.renderFaceZPos(par1BlockCauldron, (double)par2, (double)par3, (double)((float)par4 - 1.0F + f4), icon);
        renderer.renderFaceZNeg(par1BlockCauldron, (double)par2, (double)par3, (double)((float)par4 + 1.0F - f4), icon);
        Icon icon1 = BlockCauldron2.func_94375_b("cauldron_inner");
        renderer.renderFaceYPos(par1BlockCauldron, (double)par2, (double)((float)par3 - 1.0F + 0.25F), (double)par4, icon1);
        renderer.renderFaceYNeg(par1BlockCauldron, (double)par2, (double)((float)par3 + 1.0F - 0.75F), (double)par4, icon1);
        int i1 = renderer.blockAccess.getBlockMetadata(par2, par3, par4);

        if (i1 > 0)
        {
        	TileEntityCauldron te = (TileEntityCauldron) Minecraft.getMinecraft().theWorld.getBlockTileEntity(par2, par3, par4);
        	System.out.println(te.water());
            Icon icon2 = te.water() ? BlockFluid.func_94424_b("water_still") : BlockFluid.func_94424_b("lava_still");

            if (i1 > 3)
            {
                i1 = 3;
            }

            renderer.renderFaceYPos(par1BlockCauldron, (double)par2, (double)((float)par3 - 1.0F + (6.0F + (float)i1 * 3.0F) / 16.0F), (double)par4, icon2);
        }

        return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return ClientProxy.cauldronRenderType;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		
	}

}