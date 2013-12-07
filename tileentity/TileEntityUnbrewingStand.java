package clashsoft.mods.morepotions.tileentity;

import java.util.List;

import clashsoft.brewingapi.brewing.PotionType;
import clashsoft.brewingapi.item.ItemPotion2;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityUnbrewingStand extends TileEntity implements IInventory
{
	/**
	 * The itemstacks currently placed in the slots of the unbrewing stand 0 = potion 1 = redstone 2 = glowstone 3 = gunpowder 4 = bottle 5 = ingredient
	 */
	private ItemStack[]	itemStacks		= new ItemStack[6];
	public int			unbrewTime;
	
	public static int	maxUnbrewTime	= 100;
	
	public EntityPlayer	player;
	
	public TileEntityUnbrewingStand()
	{
	}
	
	/**
	 * Returns the name of the inventory.
	 */
	@Override
	public String getInvName()
	{
		return "container.unbrewingstand";
	}
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return this.itemStacks.length;
	}
	
	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count ticks and creates a new spawn inside its implementation.
	 */
	@Override
	public void updateEntity()
	{
		if (this.unbrewTime > 0)
		{
			--this.unbrewTime;
			
			if (this.unbrewTime == 0)
			{
				this.unbrew();
				this.onInventoryChanged();
			}
			else if (!this.canUnbrew())
			{
				this.unbrewTime = 0;
				this.onInventoryChanged();
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
			List<PotionType> potionTypes = potionItem.getEffects(potion);
			int redstoneAmount = 0;
			int glowstoneAmount = 0;
			int bottleAmount = potion.stackSize;
			ItemStack ingredient = null;
			
			for (PotionType pt : potionTypes)
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
			
			if (redstoneAmount > 0)
				this.itemStacks[1] = new ItemStack(Item.redstone, redstoneAmount * bottleAmount);
			if (glowstoneAmount > 0)
				this.itemStacks[2] = new ItemStack(Item.glowstone, glowstoneAmount * bottleAmount);
			if (potionItem.isSplash(potion.getItemDamage()))
				this.itemStacks[3] = new ItemStack(Item.gunpowder, bottleAmount);
			
			this.itemStacks[4] = new ItemStack(Item.glassBottle, bottleAmount);
			
			if (ingredient != null)
			{
				ingredient.stackSize *= bottleAmount;
				this.itemStacks[5] = ingredient;
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
		for (int i = 1; i < this.itemStacks.length; i++)
		{
			if (this.itemStacks[i] != null)
				return false;
		}
		return this.getPotion() != null && this.getPotion().getItem() instanceof ItemPotion2;
	}
	
	public int getUnbrewTime()
	{
		return this.unbrewTime;
	}
	
	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList tagList = nbt.getTagList("Items");
		this.itemStacks = new ItemStack[this.getSizeInventory()];
		
		for (int index = 0; index < tagList.tagCount(); ++index)
		{
			NBTTagCompound slotCompound = (NBTTagCompound) tagList.tagAt(index);
			byte slotID = slotCompound.getByte("Slot");
			
			if (slotID >= 0 && slotID < this.itemStacks.length)
			{
				this.itemStacks[slotID] = ItemStack.loadItemStackFromNBT(slotCompound);
			}
		}
		
		this.unbrewTime = nbt.getShort("UnbrewTime");
	}
	
	/**
	 * Writes a tile entity to NBT.
	 */
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
	
	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int slotID)
	{
		return slotID >= 0 && slotID < this.itemStacks.length ? this.itemStacks[slotID] : null;
	}
	
	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int slotID, int amount)
	{
		if (slotID >= 0 && slotID < this.itemStacks.length)
		{
			ItemStack var3 = this.itemStacks[slotID];
			this.itemStacks[slotID] = null;
			return var3;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem - like when you close a workbench GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int slotID)
	{
		if (slotID >= 0 && slotID < this.itemStacks.length)
		{
			ItemStack var2 = this.itemStacks[slotID];
			this.itemStacks[slotID] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int slotID, ItemStack stack)
	{
		if (slotID >= 0 && slotID < this.itemStacks.length)
		{
			this.itemStacks[slotID] = stack;
		}
	}
	
	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}
	
	@Override
	public void openChest()
	{
	}
	
	@Override
	public void closeChest()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void setBrewTime(int brewTime)
	{
		this.unbrewTime = brewTime;
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
	{
		return par2ItemStack.getItem() instanceof ItemPotion2;
	}
}
