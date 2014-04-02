package clashsoft.mods.morepotions.block;

import java.util.Random;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockUnbrewingStand extends BlockContainer
{
	private Random	rand	= new Random();
	public IIcon	top;
	public IIcon	side;
	public IIcon	bottom;
	
	public BlockUnbrewingStand()
	{
		super(Material.rock);
		this.setCreativeTab(CreativeTabs.tabBrewing);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		return side == 0 ? this.top : side == 1 ? this.top : this.side;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.top = iconRegister.registerIcon("morepotions:unbrewingstand_top");
		this.bottom = iconRegister.registerIcon("morepotions:unbrewingstand_bottom");
		this.side = iconRegister.registerIcon("morepotions:unbrewingstand_side");
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
		world.setTileEntity(x, y, z, this.createNewTileEntity(world, 0));
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
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
		if (!world.isRemote && world.getTileEntity(x, y, z) != null)
		{
			FMLNetworkHandler.openGui(player, MorePotionsMod.instance, MorePotionsMod.unbrewingStandTileEntityID, world, x, y, z);
		}
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldBlockMetadata)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
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
						EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, new ItemStack(stack.getItem(), randInt, stack.getItemDamage()));
						float velocityMultiplier = 0.05F;
						entityItem.motionX = (float) this.rand.nextGaussian() * velocityMultiplier;
						entityItem.motionY = (float) this.rand.nextGaussian() * velocityMultiplier + 0.2F;
						entityItem.motionZ = (float) this.rand.nextGaussian() * velocityMultiplier;
						world.spawnEntityInWorld(entityItem);
					}
				}
			}
		}
		
		super.breakBlock(world, x, y, z, oldBlock, oldBlockMetadata);
	}
	
	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side)
	{
		return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
	}
}
