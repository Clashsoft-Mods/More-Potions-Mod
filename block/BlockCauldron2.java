package clashsoft.mods.morepotions.block;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.client.MPMClientProxy;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
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
import net.minecraft.util.EnumChatFormatting;
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
	
	public BlockCauldron2(int blockID)
	{
		super(blockID);
	}
	
	@Override
	public int getRenderType()
	{
		return MPMClientProxy.cauldronRenderType;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		super.registerIcons(iconRegister);
		Block.cauldron.registerIcons(iconRegister);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (world.isRemote)
			return;
		else if (entity instanceof EntityItem)
		{
			EntityItem item = (EntityItem) entity;
			
			// Makes sure the item is *in* the cauldron
			if (item.posX >= x + 0.125D && item.posX <= x + 0.875D && item.posY >= y + 0.125D && item.posY <= y + 1D && item.posZ >= z + 0.125D && item.posZ <= z + 0.875D)
			{
				if (this.onItemAdded(world, x, y, z, null, item.getEntityItem()))
					item.setDead();
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		return onItemAdded(par1World, par2, par3, par4, par5EntityPlayer, par5EntityPlayer.getCurrentEquippedItem());
	}
	
	public boolean onItemAdded(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, ItemStack itemstack)
	{
		if (par1World.isRemote)
			return true;
		else if (par1World.getBlockTileEntity(par2, par3, par4) instanceof TileEntityCauldron)
		{
			TileEntityCauldron te = (TileEntityCauldron) par1World.getBlockTileEntity(par2, par3, par4);
			boolean flag = false;
			boolean itemDrop = par5EntityPlayer == null;
			String message = "";
			
			if (itemstack == null)
				return false;
			else
			{
				int i1 = par1World.getBlockMetadata(par2, par3, par4);
				
				if (itemstack.itemID == Item.bucketWater.itemID && !itemDrop)
				{
					if (i1 < 3)
					{
						if (itemDrop)
						{
							par1World.spawnEntityInWorld(new EntityItem(par1World, par2 + 0.5D, par3 + 0.5D, par4 + 1.5D, new ItemStack(Item.bucketEmpty)));
						}
						else if (!par5EntityPlayer.capabilities.isCreativeMode)
						{
							par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, new ItemStack(Item.bucketEmpty));
						}
						
						par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
						
						message = te.addIngredient(itemstack);
						flag = true;
					}
					else
						flag = false;
				}
				else if (itemstack.itemID == Item.glassBottle.itemID)
				{
					if (i1 > 0)
					{
						ItemStack itemstack1 = te.output;
						itemstack1.stackSize = 1;
						
						if (itemDrop || !par5EntityPlayer.inventory.addItemStackToInventory(itemstack1))
						{
							par5EntityPlayer.dropPlayerItem(itemstack1);
						}
						
						if (par5EntityPlayer instanceof EntityPlayerMP)
						{
							((EntityPlayerMP) par5EntityPlayer).sendContainerToPlayer(par5EntityPlayer.inventoryContainer);
						}
						
						--itemstack.stackSize;
						
						if (!itemDrop && itemstack.stackSize <= 0)
						{
							par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack) null);
						}
						
						--i1;
						
						par1World.setBlockMetadataWithNotify(par2, par3, par4, i1, 2);
						flag = true;
					}
					
					if (i1 == 0)
						te.brewings.clear();
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
						message = te.addIngredient(itemstack);
						flag = true;
					}
				}
			}
			
			if (flag)
			{				
				par1World.playSound(par2, par3, par4, "random.pop", 1F, 1F, true);
				if (MorePotionsMod.cauldronInfo && !itemDrop && !par1World.isRemote && message != null && !message.isEmpty())
					par5EntityPlayer.addChatMessage(EnumChatFormatting.DARK_AQUA + "Cauldron" + EnumChatFormatting.RESET + ": " + message);
			}
			
			te.sync();
			return flag;
		}
		else
			return false;
	}
	
	/**
	 * currently only used by BlockCauldron to increment metadata during rain
	 */
	@Override
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
