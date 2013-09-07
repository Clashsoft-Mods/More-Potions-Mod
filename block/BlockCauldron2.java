package clashsoft.mods.morepotions.block;

import java.util.List;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockCauldron2 extends BlockCauldron implements ITileEntityProvider
{
	@SideOnly(Side.CLIENT)
	public Icon	inner;
	@SideOnly(Side.CLIENT)
	public Icon	top;
	@SideOnly(Side.CLIENT)
	public Icon	bottom;
	
	public BlockCauldron2(int par1)
	{
		super(par1);
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
	}
	
	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public Icon getIcon(int par1, int par2)
	{
		return par1 == 1 ? this.top : (par1 == 0 ? this.bottom : this.blockIcon);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.inner = par1IconRegister.registerIcon("cauldron_inner");
		this.top = par1IconRegister.registerIcon("cauldron_top");
		this.bottom = par1IconRegister.registerIcon("cauldron_bottom");
		this.blockIcon = par1IconRegister.registerIcon("cauldron_side");
	}
	
	@SideOnly(Side.CLIENT)
	public static Icon func_94375_b(String par0Str)
	{
		return par0Str == "cauldron_inner" ? MorePotionsMod.cauldron2.inner : (par0Str == "cauldron_bottom" ? MorePotionsMod.cauldron2.bottom : null);
	}
	
	@Override
	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
	 * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		float f = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		this.setBlockBoundsForItemRender();
	}
	
	@Override
	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if (par1World.isRemote)
		{
			return true;
		}
		else if (par1World.getBlockTileEntity(par2, par3, par4) != null && par1World.getBlockTileEntity(par2, par3, par4) instanceof TileEntityCauldron)
		{
			ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
			TileEntityCauldron te = (TileEntityCauldron) par1World.getBlockTileEntity(par2, par3, par4);
			boolean flag = false;
			
			if (itemstack == null)
			{
				flag = false;
			}
			else if (par5EntityPlayer.isSneaking())
			{
				
			}
			else
			{
				int i1 = par1World.getBlockMetadata(par2, par3, par4);
				
				if (itemstack.itemID == Item.bucketWater.itemID)
				{
					if (i1 < 3)
					{
						if (!par5EntityPlayer.capabilities.isCreativeMode)
						{
							par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, new ItemStack(Item.bucketEmpty));
						}
						
						par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
					}
					flag = true;
					String s = te.addIngredient(itemstack);
					if (s != null && s != "")
						par5EntityPlayer.addChatMessage(s);
				}
				else if (itemstack.itemID == Item.glassBottle.itemID)
				{
					if (i1 > 0)
					{
						ItemStack itemstack1 = te.brew();
						
						if (!par5EntityPlayer.inventory.addItemStackToInventory(itemstack1))
						{
							par1World.spawnEntityInWorld(new EntityItem(par1World, par2 + 0.5D, par3 + 1.5D, par4 + 0.5D, itemstack1));
						}
						else if (par5EntityPlayer instanceof EntityPlayerMP)
						{
							((EntityPlayerMP) par5EntityPlayer).sendContainerToPlayer(par5EntityPlayer.inventoryContainer);
						}
						
						--itemstack.stackSize;
						
						if (itemstack.stackSize <= 0)
						{
							par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack) null);
						}
						
						if (i1 - 1 == 0)
							te.brewings.clear();
						par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 - 1, 2);
						flag = true;
					}
					else
					{
						te.brewings.clear();
					}
				}
				else if (i1 > 0 && itemstack.getItem() instanceof ItemArmor && ((ItemArmor) itemstack.getItem()).getArmorMaterial() == EnumArmorMaterial.CLOTH)
				{
					ItemArmor itemarmor = (ItemArmor) itemstack.getItem();
					itemarmor.removeColor(itemstack);
					par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 - 1, 2);
					flag = true;
				}
				else
				{
					if (te.isItemValid(itemstack) && i1 > 0)
					{
						String s = te.addIngredient(itemstack);
						if (s != null && s != "")
							par5EntityPlayer.addChatMessage(s);
						flag = true;
					}
				}
			}
			par1World.setBlockTileEntity(par2, par3, par4, te);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	/**
	 * currently only used by BlockCauldron to incrament meta-data during rain
	 */
	public void fillWithRain(World par1World, int par2, int par3, int par4)
	{
		if (par1World.rand.nextInt(20) == 1)
		{
			int l = par1World.getBlockMetadata(par2, par3, par4);
			
			if (l < 3)
			{
				par1World.setBlockMetadataWithNotify(par2, par3, par4, l + 1, 2);
			}
		}
	}
	
	/**
	 * ejects contained items into the world, and notifies neighbours of an
	 * update, as appropriate
	 */
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
		par1World.removeBlockTileEntity(par2, par3, par4);
	}
	
	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By
	 * default, passes it on to the tile entity at this location. Args: world,
	 * x, y, z, blockID, EventID, event parameter
	 */
	@Override
	public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityCauldron();
	}
}
