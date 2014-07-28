package clashsoft.mods.morepotions.client.renderer.block;

import clashsoft.mods.morepotions.block.BlockCauldron2;
import clashsoft.mods.morepotions.client.MPMClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlockCauldron implements ISimpleBlockRenderingHandler
{
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		BlockCauldron2 cauldron = (BlockCauldron2) block;
		IIcon icon = cauldron.getBlockTextureFromSide(2);
		IIcon icon2 = BlockCauldron.getCauldronIcon("inner");
		
		renderer.renderStandardBlock(cauldron, x, y, z);
		renderer.renderFaceXPos(cauldron, x - 0.875F, y, z, icon);
		renderer.renderFaceXNeg(cauldron, x + 0.875F, y, z, icon);
		renderer.renderFaceZPos(cauldron, x, y, z - 0.875F, icon);
		renderer.renderFaceZNeg(cauldron, x, y, z + 0.875F, icon);
		renderer.renderFaceYPos(cauldron, x, y - 0.75F, z, icon2);
		renderer.renderFaceYNeg(cauldron, x, y + 0.25F, z, icon2);
		
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
