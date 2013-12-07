package clashsoft.mods.morepotions.block;

import java.util.Random;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockUnbrewingStand extends BlockContainer
{
	private Random	rand	= new Random();
	public Icon		top;
	public Icon		side;
	public Icon		bottom;
	
	public BlockUnbrewingStand(int blockID)
	{
		super(blockID, Material.rock);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public Icon getIcon(int side, int metadata)
	{
		return side == 0 ? this.top : side == 1 ? this.top : this.side;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.top = iconRegister.registerIcon("unbrewingstand_top");
		this.bottom = iconRegister.registerIcon("unbrewingstand_bottom");
		this.side = iconRegister.registerIcon("unbrewingstand_side");
	}
	
	@Override
	public int getRenderBlockPass()
	{
		return 0;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		world.setBlockTileEntity(x, y, z, this.createNewTileEntity(world));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityUnbrewingStand();
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			TileEntityUnbrewingStand unbrewingStand = (TileEntityUnbrewingStand) world.getBlockTileEntity(x, y, z);
			
			if (unbrewingStand != null)
			{
				FMLNetworkHandler.openGui(player, MorePotionsMod.INSTANCE, MorePotionsMod.unbrewingStandTileEntityID, world, x, y, z);
			}
			
			return true;
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int oldBlockID, int oldBlockMetadata)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (tileEntity instanceof TileEntityUnbrewingStand)
		{
			TileEntityUnbrewingStand unbrewingStand = (TileEntityUnbrewingStand) tileEntity;
			
			for (int i = 0; i < unbrewingStand.getSizeInventory(); ++i)
			{
				ItemStack stack = unbrewingStand.getStackInSlot(i);
				
				if (stack != null)
				{
					float offsetX = this.rand.nextFloat() * 0.8F + 0.1F;
					float offsetY = this.rand.nextFloat() * 0.8F + 0.1F;
					float offsetZ = this.rand.nextFloat() * 0.8F + 0.1F;
					
					while (stack.stackSize > 0)
					{
						int randInt = this.rand.nextInt(21) + 10;
						
						if (randInt > stack.stackSize)
						{
							randInt = stack.stackSize;
						}
						
						stack.stackSize -= randInt;
						EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, new ItemStack(stack.itemID, randInt, stack.getItemDamage()));
						float velocityMultiplier = 0.05F;
						entityItem.motionX = (float) this.rand.nextGaussian() * velocityMultiplier;
						entityItem.motionY = (float) this.rand.nextGaussian() * velocityMultiplier + 0.2F;
						entityItem.motionZ = (float) this.rand.nextGaussian() * velocityMultiplier;
						world.spawnEntityInWorld(entityItem);
					}
				}
			}
		}
		
		super.breakBlock(world, x, y, z, oldBlockID, oldBlockMetadata);
	}
	
	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side)
	{
		return Container.calcRedstoneFromInventory((IInventory) world.getBlockTileEntity(x, y, z));
	}
}
