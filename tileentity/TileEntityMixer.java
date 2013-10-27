package clashsoft.mods.morepotions.tileentity;

import java.util.LinkedList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.brewing.Brewing;
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

public class TileEntityMixer extends TileEntity implements IInventory
{
	/** The itemstacks currently placed in the slots of the brewing stand */
	private ItemStack[]	mixingItemStacks	= new ItemStack[4];
	public int			mixTime;
	
	/**
	 * an integer with each bit specifying whether that slot of the stand
	 * contains a potion
	 */
	private int			filledSlots;
	public static int	maxMixTime			= 100;
	private ItemStack	output;
	
	public EntityPlayer	player;
	
	public TileEntityMixer()
	{
		
	}
	
	/**
	 * Returns the name of the inventory.
	 */
	@Override
	public String getInvName()
	{
		return "container.brewing";
	}
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return this.mixingItemStacks.length;
	}
	
	/**
	 * Allows the entity to update its state. Overridden in most subclasses,
	 * e.g. the mob spawner uses this to count ticks and creates a new spawn
	 * inside its implementation.
	 */
	@Override
	public void updateEntity()
	{
		if (this.mixTime > 0)
		{
			--this.mixTime;
			
			if (this.mixTime == 0)
			{
				this.mixPotions();
				this.onInventoryChanged();
			}
			else if (!this.canMix())
			{
				this.mixTime = 0;
				this.onInventoryChanged();
			}
			else if (this.output != this.mixingItemStacks[3])
			{
				this.mixTime = 0;
				this.onInventoryChanged();
			}
		}
		else if (this.canMix())
		{
			this.mixTime = maxMixTime;
			this.output = this.mixingItemStacks[3];
		}
		
		int var1 = this.getFilledSlots();
		
		if (var1 != this.filledSlots)
		{
			this.filledSlots = var1;
			this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, var1, 0);
		}
		
		super.updateEntity();
	}
	
	private void mixPotions()
	{
		mixingItemStacks[3] = getOutput();
		for (int var1 = 0; var1 < 3; var1++)
		{
			mixingItemStacks[var1] = null;
		}
	}
	
	public ItemStack getOutput()
	{
		List<Brewing> brewings = new LinkedList<Brewing>();
		for (int var1 = 0; var1 < 3; var1++)
		{
			if (mixingItemStacks[var1] != null)
			{
				NBTTagCompound compound = this.mixingItemStacks[var1].stackTagCompound;
				NBTTagList list = (compound != null) ? compound.getTagList("Brewing") : (NBTTagList) null;
				Brewing[] brewing = new Brewing[list != null ? list.tagCount() : 1];
				if (list != null && list.tagCount() > 0)
				{
					for (int var2 = 0; var2 < list.tagCount(); var2++)
					{
						Brewing b = Brewing.readFromNBT((NBTTagCompound) list.tagAt(var2));
						if (!brewings.contains(b) && b.getEffect() != null)
						{
							brewings.add(b);
						}
					}
				}
			}
		}
		if (brewings != null && brewings.size() > 0)
		{
			brewings = (List<Brewing>) Brewing.removeDuplicates(brewings);
			int damage = mixingItemStacks[0] != null ? mixingItemStacks[0].getItemDamage() : mixingItemStacks[1] != null ? mixingItemStacks[1].getItemDamage() : mixingItemStacks[2] != null ? mixingItemStacks[2].getItemDamage() : 0;
			if (damage == 0)
			{
			}
			ItemStack ret = new ItemStack(BrewingAPI.potion2, 1, damage);
			for (Brewing b : brewings)
			{
				ret = b.addBrewingToItemStack(ret);
			}
			return ret;
		}
		
		return null;
	}
	
	private boolean canMix()
	{
		if (this.mixingItemStacks[3] == null && getFilledSlots() >= 2 && mixingItemStacks[3] == null)
		{
			boolean var1 = false;
			for (int var2 = 0; var2 < 3; var2++)
			{
				if (mixingItemStacks[var2] != null && mixingItemStacks[var2].getItem() instanceof ItemPotion2)
				{
					if (((ItemPotion2) mixingItemStacks[var2].getItem()).getEffects(mixingItemStacks[var2]) == null)
					{
						var1 = false;
						break;
					}
					else
					{
						var1 = true;
					}
				}
			}
			return var1;
		}
		else
		{
			return false;
		}
	}
	
	public int getMixTime()
	{
		return this.mixTime;
	}
	
	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.mixingItemStacks = new ItemStack[this.getSizeInventory()];
		
		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");
			
			if (var5 >= 0 && var5 < this.mixingItemStacks.length)
			{
				this.mixingItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
		
		this.mixTime = par1NBTTagCompound.getShort("BrewTime");
	}
	
	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("BrewTime", (short) this.mixTime);
		NBTTagList var2 = new NBTTagList();
		
		for (int var3 = 0; var3 < this.mixingItemStacks.length; ++var3)
		{
			if (this.mixingItemStacks[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.mixingItemStacks[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}
		
		par1NBTTagCompound.setTag("Items", var2);
	}
	
	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return par1 >= 0 && par1 < this.mixingItemStacks.length ? this.mixingItemStacks[par1] : null;
	}
	
	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (par1 >= 0 && par1 < this.mixingItemStacks.length)
		{
			ItemStack var3 = this.mixingItemStacks[par1];
			this.mixingItemStacks[par1] = null;
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
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (par1 >= 0 && par1 < this.mixingItemStacks.length)
		{
			ItemStack var2 = this.mixingItemStacks[par1];
			this.mixingItemStacks[par1] = null;
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
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		if (par1 >= 0 && par1 < this.mixingItemStacks.length)
		{
			this.mixingItemStacks[par1] = par2ItemStack;
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
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
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
	public void setBrewTime(int par1)
	{
		this.mixTime = par1;
	}
	
	/**
	 * returns an integer with each bit specifying wether that slot of the stand
	 * contains a potion
	 */
	public int getFilledSlots()
	{
		int var1 = 0;
		for (int var2 = 0; var2 < 3; var2++)
		{
			if (mixingItemStacks[var2] != null)
			{
				var1++;
			}
		}
		return var1;
	}
	
	public void handlePacketData(int typeData, int[] intData)
	{
		TileEntityMixer var1 = this;
		if (intData != null)
		{
			int pos = 0;
			if (intData.length < var1.mixingItemStacks.length * 3)
			{
				return;
			}
			for (int i = 0; i < var1.mixingItemStacks.length; i++)
			{
				if (intData[pos + 2] != 0)
				{
					ItemStack is = new ItemStack(intData[pos], intData[pos + 2], intData[pos + 1]);
					var1.mixingItemStacks[i] = is;
				}
				else
				{
					var1.mixingItemStacks[i] = null;
				}
				pos += 3;
			}
		}
	}
	
	public int[] buildIntDataList()
	{
		return null;
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
