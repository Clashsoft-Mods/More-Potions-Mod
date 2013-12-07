package clashsoft.mods.morepotions.tileentity;

import java.util.ArrayList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
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
	/** The itemstacks currently placed in the slots of the brewing stand */
	private ItemStack[]	itemStacks	= new ItemStack[4];
	public int			unbrewTime;
	
	public static int	maxUnbrewTime			= 100;
	private ItemStack	output;
	
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
	 * Allows the entity to update its state. Overridden in most subclasses,
	 * e.g. the mob spawner uses this to count ticks and creates a new spawn
	 * inside its implementation.
	 */
	@Override
	public void updateEntity()
	{
		if (this.unbrewTime > 0)
		{
			--this.unbrewTime;
			
			if (this.unbrewTime == 0)
			{
				this.mixPotions();
				this.onInventoryChanged();
			}
			else if (!this.canMix())
			{
				this.unbrewTime = 0;
				this.onInventoryChanged();
			}
			else if (this.output != this.itemStacks[3])
			{
				this.unbrewTime = 0;
				this.onInventoryChanged();
			}
		}
		else if (this.canMix())
		{
			this.unbrewTime = maxUnbrewTime;
			this.output = this.itemStacks[3];
		}
		
		super.updateEntity();
	}
	
	private void mixPotions()
	{
		itemStacks[3] = getOutput();
		for (int var1 = 0; var1 < 3; var1++)
		{
			itemStacks[var1] = null;
		}
	}
	
	public ItemStack getOutput()
	{
		List<PotionType> potionTypes = new ArrayList<PotionType>();
		for (int potionIndex = 0; potionIndex < 3; potionIndex++)
		{
			if (itemStacks[potionIndex] != null && itemStacks[potionIndex].getItem() instanceof ItemPotion2)
			{
				ItemPotion2 item = (ItemPotion2) itemStacks[potionIndex].getItem();
				potionTypes.addAll(item.getEffects(itemStacks[potionIndex]));
			}
		}
		if (!potionTypes.isEmpty())
		{
			potionTypes = (List<PotionType>) PotionType.removeDuplicates(potionTypes);
			int damage = itemStacks[0] != null ? itemStacks[0].getItemDamage() : itemStacks[1] != null ? itemStacks[1].getItemDamage() : itemStacks[2] != null ? itemStacks[2].getItemDamage() : 0;
			ItemStack ret = new ItemStack(BrewingAPI.potion2, 1, damage);
			for (PotionType b : potionTypes)
			{
				b.addBrewingToItemStack(ret);
			}
			return ret;
		}
		
		return null;
	}
	
	private boolean canMix()
	{
		if (this.itemStacks[3] == null && getFilledSlots() >= 2 && itemStacks[3] == null)
		{
			boolean flag = false;
			for (int index = 0; index < 3; index++)
			{
				if (itemStacks[index] != null && itemStacks[index].getItem() instanceof ItemPotion2)
				{
					if (((ItemPotion2) itemStacks[index].getItem()).getEffects(itemStacks[index]) == null)
					{
						flag = false;
						break;
					}
					else
					{
						flag = true;
					}
				}
			}
			return flag;
		}
		else
		{
			return false;
		}
	}
	
	public int getMixTime()
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
		
		this.unbrewTime = nbt.getShort("BrewTime");
	}
	
	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("BrewTime", (short) this.unbrewTime);
		NBTTagList tagList = new NBTTagList();
		
		for (int index = 0; index < this.itemStacks.length; ++index)
		{
			if (this.itemStacks[index] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) index);
				this.itemStacks[index].writeToNBT(var4);
				tagList.appendTag(var4);
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
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
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
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
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
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
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
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended. *Isn't this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
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
	
	/**
	 * returns an integer with each bit specifying wether that slot of the stand
	 * contains a potion
	 */
	public int getFilledSlots()
	{
		int filledSlots = 0;
		for (int i = 0; i < 3; i++)
		{
			if (itemStacks[i] != null)
			{
				filledSlots++;
			}
		}
		return filledSlots;
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
	{
		return (par2ItemStack.itemID == Item.potion.itemID || par2ItemStack.itemID == Item.glassBottle.itemID) && par1 != 3;
	}
}
