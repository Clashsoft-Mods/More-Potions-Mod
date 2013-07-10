package clashsoft.mods.morepotions.tileentity;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

import clashsoft.mods.morepotions.brewing.Brewing;
import clashsoft.mods.morepotions.item.ItemPotion2;
import clashsoft.mods.morepotions.lib.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityUnbrewingStand extends TileEntityBrewingStand2 implements IInventory, ISidedInventory
{
	public EntityPlayer thePlayer = null;

	private static final int[] field_102017_a = new int[] {3};
	private static final int[] field_102016_b = new int[] {0, 1, 2};

	private ItemStack potion;
	/** The itemstacks currently placed in the slots of the brewing stand */
	private ItemStack[] slots = new ItemStack[5];
	private int brewTime;

	/**
	 * an integer with each bit specifying whether that slot of the stand contains a potion
	 */
	private int filledSlots;

	public static int maxBrewTime = 400;

	public TileEntityUnbrewingStand()
	{

	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory()
	{
		return this.slots.length;
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
	 * ticks and creates a new spawn inside its implementation.
	 */
	public void updateEntity()
	{
		potion = slots[0];
		if (this.brewTime > 0)
		{
			--this.brewTime;

			if (this.brewTime == 0)
			{
				this.unbrew();
				this.onInventoryChanged();
			}
			else if (!this.canUnbrew())
			{
				this.brewTime = 0;
				this.onInventoryChanged();
			}
			else if (this.potion != this.slots[0])
            {
                this.brewTime = 0;
                this.onInventoryChanged();
            }
		}
		else if (this.canUnbrew())
		{
			this.brewTime = getMaxBrewTime();
			potion = slots[0];
		}

		super.updateEntity();
	}

	public int getMaxBrewTime()
	{
		if (thePlayer != null && thePlayer.capabilities.isCreativeMode)
		{
			return 40;
		}
		return 400;
	}

	public int getBrewTime()
	{
		return this.brewTime;
	}

	private boolean canUnbrew()
	{
		boolean outputsempty = true;
		for (int i = 1; i < slots.length; i++)
		{
			if (slots[i] != null)
			{
				outputsempty = false;
				break;
			}
		}
		if (potion != null && outputsempty)
		{
			List effects = ItemPotion2.getEffects(potion);
			if (potion.stackSize > 0 && effects != null && effects.size() > 0)
			{
				return true;
			}
		}
		return false;
	}

	private void unbrew()
	{
		if (canUnbrew())
		{
			if (slots[0] != null && slots[0].stackSize > 1)
			{
				slots[0].stackSize--;
			}
			else
			{
				slots[0] = null;
			}
			slots[1] = new ItemStack(Item.glassBottle);
			slots[2] = getGlowstone();
			slots[3] = getRedstone();
			slots[4] = getIngredient();
		}
	}

	private ItemStack getIngredient()
	{
		if (potion != null)
		{
		Brewing b = Brewing.getBrewingFromItemStack(potion);
		for (Brewing b2 : Brewing.brewingList)
		{
			if (b != null && b2 != null && b.getEffect() != null && b2.getEffect() != null && b.getEffect().getPotionID() == b2.getEffect().getPotionID())
			{
				return b2.getIngredient();
			}
		}
		}
		return null;
	}

	private ItemStack getRedstone()
	{
		int amount = slots[3] != null ? slots[3].stackSize : 0;
		for (Brewing b : ItemPotion2.getEffects(potion))
		{
			int normalDuration = b.getDefaultDuration();
			int duration = b.getEffect().getDuration();
			while (duration >= normalDuration)
			{
				duration /= 2;
				amount++;
			}
		}
		return new ItemStack(Item.redstone, amount);
	}

	private ItemStack getGlowstone()
	{
		int amount = slots[2] != null ? slots[2].stackSize : 0;
		for (Brewing b : ItemPotion2.getEffects(potion))
		{
			amount += b.getEffect().getAmplifier();
		}
		return new ItemStack(Item.glowstone, amount);
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.slots = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.slots.length)
			{
				this.slots[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		this.brewTime = par1NBTTagCompound.getShort("UnbrewTime");
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("UnbrewTime", (short)this.brewTime);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.slots.length; ++var3)
		{
			if (this.slots[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.slots[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int par1)
	{
		return par1 >= 0 && par1 < this.slots.length ? this.slots[par1] : null;
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (par1 >= 0 && par1 < this.slots.length)
		{
			ItemStack var3 = this.slots[par1];
			this.slots[par1] = null;
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
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (par1 >= 0 && par1 < this.slots.length)
		{
			ItemStack var2 = this.slots[par1];
			this.slots[par1] = null;
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
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		if (par1 >= 0 && par1 < this.slots.length)
		{
			this.slots[par1] = par2ItemStack;
		}
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
	 * this more of a set than a get?*
	 */
	public int getInventoryStackLimit()
	{
		return 1;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		thePlayer = par1EntityPlayer;
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	public void openChest() {}

	public void closeChest() {}

	@Override
	public int getStartInventorySide(ForgeDirection side)
	{
		return (side == ForgeDirection.UP ? 3 : 0);
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		return (side == ForgeDirection.UP ? 1 : 3);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketHandler.getPacket(this);
	}

	public void handlePacketData(int typeData, int[] intData)
	{
		TileEntityUnbrewingStand var1 = this;
		if (intData != null)
		{
			int pos = 0;
			if (intData.length < var1.slots.length * 3)
			{
				return;
			}
			for (int i = 0; i < var1.slots.length; i++)
			{
				if (intData[pos + 2] != 0)
				{
					ItemStack is = new ItemStack(intData[pos], intData[pos + 2], intData[pos + 1]);
					var1.slots[i] = is;
				}
				else
				{
					var1.slots[i] = null;
				}
				pos += 3;
			}
		}
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	 */
	public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack)
	{
		return par1 == 3 ? Item.itemsList[par2ItemStack.itemID].isPotionIngredient() : par2ItemStack.itemID == Item.potion.itemID || par2ItemStack.itemID == Item.glassBottle.itemID;
	}

	@SideOnly(Side.CLIENT)
	public void setBrewTime(int par1)
	{
		this.brewTime = par1;
	}

	/**
	 * returns an integer with each bit specifying wether that slot of the stand contains a potion
	 */
	public int getFilledSlots()
	{
		int i = 0;

		for (int j = 0; j < slots.length; ++j)
		{
			if (this.slots[j] != null)
			{
				i |= 1 << j;
			}
		}

		return i;
	}

	/**
	 * Get the size of the side inventory.
	 */
	public int[] getSizeInventorySide(int par1)
	{
		return par1 == 1 ? field_102017_a : field_102016_b;
	}
}
