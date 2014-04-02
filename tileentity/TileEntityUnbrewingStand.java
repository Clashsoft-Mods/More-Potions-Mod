package clashsoft.mods.morepotions.tileentity;

import java.util.List;

import clashsoft.brewingapi.item.ItemPotion2;
import clashsoft.brewingapi.potion.type.IPotionType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityUnbrewingStand extends TileEntity implements ISidedInventory
{
	private static int[]	inputSlots		= { 0 };
	private static int[]	outputSlots		= { 5, 4, 3, 2, 1 };
	
	/**
	 * The itemstacks currently placed in the slots of the unbrewing stand.
	 * <p>
	 * 0 = potion 1 = redstone 2 = glowstone 3 = gunpowder 4 = bottle 5 = ingredient
	 */
	private ItemStack[]		itemStacks		= new ItemStack[6];
	
	public int				unbrewTime;
	
	public static int		maxUnbrewTime	= 100;
	
	public EntityPlayer		player;
	
	public TileEntityUnbrewingStand()
	{
	}
	
	@Override
	public String getInventoryName()
	{
		return "container.unbrewingstand";
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.itemStacks.length;
	}
	
	@Override
	public void updateEntity()
	{
		if (this.unbrewTime > 0)
		{
			--this.unbrewTime;
			
			if (this.unbrewTime == 0)
			{
				this.unbrew();
				this.markDirty();
			}
			else if (!this.canUnbrew())
			{
				this.unbrewTime = 0;
				this.markDirty();
			}
		}
		else if (this.canUnbrew())
		{
			this.unbrewTime = maxUnbrewTime;
		}
		
		super.updateEntity();
	}
	
	private void unbrew()
	{
		ItemStack potion = this.getPotion();
		if (potion != null && potion.getItem() instanceof ItemPotion2)
		{
			ItemPotion2 potionItem = (ItemPotion2) potion.getItem();
			List<IPotionType> potionTypes = potionItem.getEffects(potion);
			
			int potionCount = potion.stackSize;
			
			if (potionCount > 0)
			{
				int oldRedstoneAmount = this.itemStacks[1] != null ? this.itemStacks[1].stackSize : 0;
				int oldGlowstoneAmount = this.itemStacks[2] != null ? this.itemStacks[2].stackSize : 0;
				int oldGunPowderAmount = this.itemStacks[3] != null ? this.itemStacks[3].stackSize : 0;
				int oldBottleAmount = this.itemStacks[4] != null ? this.itemStacks[4].stackSize : 0;
				
				int redstoneAmount = 0;
				int glowstoneAmount = 0;
				int gunPowderAmount = potionItem.isSplashDamage(potion.getItemDamage()) ? potionCount : 0;
				int bottleAmount = potionCount;
				
				ItemStack ingredient = null;
				
				for (IPotionType pt : potionTypes)
				{
					redstoneAmount += pt.getRedstoneAmount();
					glowstoneAmount += pt.getGlowstoneAmount();
					
					ItemStack ingredient1 = pt.getIngredient();
					
					if (ingredient1 != null)
					{
						if (ingredient == null)
							ingredient = ingredient1.copy();
						else if (ingredient1.isItemEqual(ingredient))
							ingredient.stackSize += ingredient1.stackSize;
					}
				}
				
				redstoneAmount = oldRedstoneAmount + redstoneAmount * potionCount;
				glowstoneAmount = oldGlowstoneAmount + glowstoneAmount * potionCount;
				gunPowderAmount = oldGunPowderAmount + gunPowderAmount * potionCount;
				bottleAmount = oldBottleAmount + bottleAmount;
				
				this.itemStacks[1] = redstoneAmount > 0 ? new ItemStack(Items.redstone, redstoneAmount) : null;
				this.itemStacks[2] = glowstoneAmount > 0 ? new ItemStack(Items.glowstone_dust, glowstoneAmount) : null;
				this.itemStacks[3] = gunPowderAmount > 0 ? new ItemStack(Items.gunpowder, gunPowderAmount) : null;
				this.itemStacks[4] = bottleAmount > 0 ? new ItemStack(Items.glass_bottle, bottleAmount) : null;
				
				if (ingredient != null)
				{
					ingredient.stackSize *= potionCount;
					this.itemStacks[5] = ingredient;
				}
			}
			
			this.itemStacks[0] = null;
		}
	}
	
	public ItemStack getPotion()
	{
		return this.itemStacks[0];
	}
	
	public boolean canUnbrew()
	{
		for (int i = 1; i < 5; i++)
		{
			if (this.itemStacks[i] != null)
				if (this.itemStacks[i].stackSize > this.itemStacks[i].getMaxStackSize())
					return false;
		}
		if (this.itemStacks[5] != null)
			return false;
		
		return this.getPotion() != null && this.getPotion().getItem() instanceof ItemPotion2;
	}
	
	public int getUnbrewTime()
	{
		return this.unbrewTime;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.itemStacks = new ItemStack[this.getSizeInventory()];
		
		for (int index = 0; index < tagList.tagCount(); ++index)
		{
			NBTTagCompound slotCompound = tagList.getCompoundTagAt(index);
			byte slotID = slotCompound.getByte("Slot");
			
			if (slotID >= 0 && slotID < this.itemStacks.length)
			{
				this.itemStacks[slotID] = ItemStack.loadItemStackFromNBT(slotCompound);
			}
		}
		
		this.unbrewTime = nbt.getShort("UnbrewTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("UnbrewTime", (short) this.unbrewTime);
		NBTTagList tagList = new NBTTagList();
		
		for (int index = 0; index < this.itemStacks.length; ++index)
		{
			if (this.itemStacks[index] != null)
			{
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) index);
				this.itemStacks[index].writeToNBT(compound);
				tagList.appendTag(compound);
			}
		}
		
		nbt.setTag("Items", tagList);
	}
	
	@Override
	public ItemStack getStackInSlot(int slotID)
	{
		return slotID >= 0 && slotID < this.itemStacks.length ? this.itemStacks[slotID] : null;
	}
	
	@Override
	public ItemStack decrStackSize(int slotID, int amount)
	{
		ItemStack[] aitemstack = this.itemStacks;
		
		if (aitemstack[slotID] != null)
		{
			ItemStack itemstack;
			
			if (aitemstack[slotID].stackSize <= amount)
			{
				itemstack = aitemstack[slotID];
				aitemstack[slotID] = null;
				return itemstack;
			}
			else
			{
				itemstack = aitemstack[slotID].splitStack(amount);
				
				if (aitemstack[slotID].stackSize == 0)
				{
					aitemstack[slotID] = null;
				}
				
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slotID)
	{
		if (slotID >= 0 && slotID < this.itemStacks.length)
		{
			ItemStack stack = this.itemStacks[slotID];
			this.itemStacks[slotID] = null;
			return stack;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public void setInventorySlotContents(int slotID, ItemStack stack)
	{
		if (slotID >= 0 && slotID < this.itemStacks.length)
		{
			this.itemStacks[slotID] = stack;
		}
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}
	
	@SideOnly(Side.CLIENT)
	public void setBrewTime(int brewTime)
	{
		this.unbrewTime = brewTime;
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack stack)
	{
		return stack.getItem() instanceof ItemPotion2;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 0 ? outputSlots : inputSlots;
	}
	
	@Override
	public boolean canInsertItem(int slotID, ItemStack stack, int side)
	{
		return side != 0 && this.isItemValidForSlot(slotID, stack);
	}
	
	@Override
	public boolean canExtractItem(int slotID, ItemStack stack, int side)
	{
		return side == 0;
	}
	
	@Override
	public void openInventory()
	{
	}
	
	@Override
	public void closeInventory()
	{
	}
}
