package clashsoft.mods.morepotions.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.brewing.Brewing;
import clashsoft.brewingapi.item.ItemPotion2;
import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.lib.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityMixer extends TileEntity implements IInventory
{
	/** The itemstacks currently placed in the slots of the brewing stand */
	private ItemStack[] mixxingItemStacks = new ItemStack[4];
	public int mixTime;

	/**
	 * an integer with each bit specifying whether that slot of the stand contains a potion
	 */
	private int filledSlots;
	public static int maxMixTime = 100;
	private ItemStack output;
	
	public EntityPlayer player;

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
		return this.mixxingItemStacks.length;
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
	 * ticks and creates a new spawn inside its implementation.
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
			else if (this.output != this.mixxingItemStacks[3])
			{
				this.mixTime = 0;
				this.onInventoryChanged();
			}
		}
		else if (this.canMix())
		{
			this.mixTime = maxMixTime;
			this.output = this.mixxingItemStacks[3];
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
		mixxingItemStacks[3] = getOutput();
		for (int var1 = 0; var1 < 3; var1++)
		{
			mixxingItemStacks[var1] = null;
		}
	}

	public ItemStack getOutput()
	{
		List<Brewing> brewings = new LinkedList<Brewing>();
		for (int var1 = 0; var1 < 3; var1++)
		{
			if (mixxingItemStacks[var1] != null)
			{
				NBTTagCompound compound = this.mixxingItemStacks[var1].stackTagCompound;
				NBTTagList list = (compound != null) ? compound.getTagList("Brewing") : (NBTTagList)null;
				Brewing[] brewing = new Brewing[list != null ? list.tagCount() : 1];
				if (list != null && list.tagCount() > 0)
				{
					for (int var2 = 0; var2 < list.tagCount(); var2++)
					{
						Brewing b = Brewing.readFromNBT((NBTTagCompound)list.tagAt(var2));
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
			int damage = mixxingItemStacks[0] != null ? mixxingItemStacks[0].getItemDamage() : mixxingItemStacks[1] != null ? mixxingItemStacks[1].getItemDamage() : mixxingItemStacks[2] != null ? mixxingItemStacks[2].getItemDamage() : 0;
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
		if (this.mixxingItemStacks[3] == null && getFilledSlots() >= 2 && mixxingItemStacks[3] == null)
		{
			boolean var1 = false;
			for (int var2 = 0; var2 < 3; var2++)
			{
				if (mixxingItemStacks[var2] != null)
				{
					if (ItemPotion2.getEffects(mixxingItemStacks[var2]) == null)
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
		this.mixxingItemStacks = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.mixxingItemStacks.length)
			{
				this.mixxingItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
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
		par1NBTTagCompound.setShort("BrewTime", (short)this.mixTime);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.mixxingItemStacks.length; ++var3)
		{
			if (this.mixxingItemStacks[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.mixxingItemStacks[var3].writeToNBT(var4);
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
		return par1 >= 0 && par1 < this.mixxingItemStacks.length ? this.mixxingItemStacks[par1] : null;
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (par1 >= 0 && par1 < this.mixxingItemStacks.length)
		{
			ItemStack var3 = this.mixxingItemStacks[par1];
			this.mixxingItemStacks[par1] = null;
			return var3;
		}
		else
		{
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (par1 >= 0 && par1 < this.mixxingItemStacks.length)
		{
			ItemStack var2 = this.mixxingItemStacks[par1];
			this.mixxingItemStacks[par1] = null;
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
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		if (par1 >= 0 && par1 < this.mixxingItemStacks.length)
		{
			this.mixxingItemStacks[par1] = par2ItemStack;
		}
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
	 * this more of a set than a get?*
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
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@SideOnly(Side.CLIENT)
	public void setBrewTime(int par1)
	{
		this.mixTime = par1;
	}

	/**
	 * returns an integer with each bit specifying wether that slot of the stand contains a potion
	 */
	public int getFilledSlots()
	{
		int var1 = 0;
		for (int var2 = 0; var2 < 3; var2++)
		{
			if (mixxingItemStacks[var2] != null)
			{
				var1++;
			}
		}
		return var1;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketHandler.getPacket(this);
	}

	public void handlePacketData(int typeData, int[] intData)
	{
		TileEntityMixer var1 = this;
		if (intData != null)
		{
			int pos = 0;
			if (intData.length < var1.mixxingItemStacks.length * 3)
			{
				return;
			}
			for (int i = 0; i < var1.mixxingItemStacks.length; i++)
			{
				if (intData[pos + 2] != 0)
				{
					ItemStack is = new ItemStack(intData[pos], intData[pos + 2], intData[pos + 1]);
					var1.mixxingItemStacks[i] = is;
				}
				else
				{
					var1.mixxingItemStacks[i] = null;
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
