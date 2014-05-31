package clashsoft.mods.morepotions.tileentity;

import java.util.List;

import clashsoft.brewingapi.item.ItemPotion2;
import clashsoft.brewingapi.potion.type.IPotionType;
import clashsoft.cslib.minecraft.item.CSStacks;
import clashsoft.cslib.minecraft.tileentity.TileEntityInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityUnbrewingStand extends TileEntityInventory implements ISidedInventory
{
	private static int[]	inputSlots		= { 0 };
	private static int[]	outputSlots		= { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
	
	public int				unbrewTime;
	
	public static int		maxUnbrewTime	= 100;
	
	public EntityPlayer		player;
	
	public TileEntityUnbrewingStand()
	{
		super(14);
	}
	
	@Override
	public int getSizeInventory()
	{
		return 14;
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
			List<IPotionType> potionTypes = potionItem.getPotionTypes(potion);
			
			int redstone = this.itemStacks[1] == null ? 0 : this.itemStacks[1].stackSize;
			int glowstone = this.itemStacks[2] == null ? 0 : this.itemStacks[2].stackSize;
			int gunpowder = this.itemStacks[3] == null ? 0 : this.itemStacks[3].stackSize;
			int bottles = this.itemStacks[4] == null ? 0 : this.itemStacks[4].stackSize;
			
			bottles++;
			if (potionItem.isSplash(potion))
			{
				gunpowder++;
			}
			
			for (IPotionType pt : potionTypes)
			{
				redstone += pt.getRedstoneAmount();
				glowstone += pt.getGlowstoneAmount();
				
				ItemStack stack = pt.getIngredient();
				if (stack != null)
				{
					CSStacks.mergeItemStack(this.itemStacks, 5, stack);
				}
			}
			
			this.itemStacks[1] = redstone <= 0 ? null : new ItemStack(Items.redstone, redstone);
			this.itemStacks[2] = glowstone <= 0 ? null : new ItemStack(Items.glowstone_dust, glowstone);
			this.itemStacks[3] = gunpowder <= 0 ? null : new ItemStack(Items.gunpowder, gunpowder);
			this.itemStacks[4] = bottles <= 0 ? null : new ItemStack(Items.glass_bottle, bottles);
			
			potion.stackSize--;
			this.decrStackSize(0, 1);
		}
	}
	
	public ItemStack getPotion()
	{
		return this.itemStacks[0];
	}
	
	public boolean canUnbrew()
	{
		if (this.getPotion() == null || !(this.getPotion().getItem() instanceof ItemPotion2))
		{
			return false;
		}
		
		for (int i = 1; i < this.itemStacks.length; i++)
		{
			ItemStack stack = this.itemStacks[i];
			if (stack != null && stack.stackSize >= stack.getMaxStackSize())
			{
				return false;
			}
		}
		return true;
	}
	
	public int getUnbrewTime()
	{
		return this.unbrewTime;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.unbrewTime = nbt.getShort("UnbrewTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("UnbrewTime", (short) this.unbrewTime);
	}
	
	public void setBrewTime(int brewTime)
	{
		this.unbrewTime = brewTime;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return stack.getItem() instanceof ItemPotion2;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 0 ? outputSlots : inputSlots;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side)
	{
		return side != 0 && this.isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side)
	{
		return side == 0;
	}
}
