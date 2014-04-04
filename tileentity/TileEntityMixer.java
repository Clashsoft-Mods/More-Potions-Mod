package clashsoft.mods.morepotions.tileentity;

import java.util.ArrayList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.item.ItemPotion2;
import clashsoft.brewingapi.potion.type.IPotionType;
import clashsoft.brewingapi.potion.type.PotionType;
import clashsoft.cslib.minecraft.tileentity.TileEntityInventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityMixer extends TileEntityInventory implements IInventory
{
	public int			time;
	
	public static int	maxTime	= 100;
	private ItemStack	output;
	
	public EntityPlayer	player;
	
	public TileEntityMixer()
	{
		super(4);
	}
	
	@Override
	public int getSizeInventory()
	{
		return 4;
	}
	
	@Override
	public void updateEntity()
	{
		if (this.time > 0)
		{
			--this.time;
			
			if (this.time == 0)
			{
				this.mixPotions();
				this.markDirty();
			}
			else if (!this.canMix())
			{
				this.time = 0;
				this.markDirty();
			}
			else if (this.output != this.itemStacks[3])
			{
				this.time = 0;
				this.markDirty();
			}
		}
		else if (this.canMix())
		{
			this.time = maxTime;
			this.output = this.itemStacks[3];
		}
		
		super.updateEntity();
	}
	
	public void mixPotions()
	{
		this.itemStacks[3] = this.getOutput();
		
		for (int i = 0; i < 3; i++)
		{
			ItemStack stack = this.itemStacks[i];
			if (stack != null)
			{
				stack.stackSize--;
				if (stack.stackSize <= 0)
				{
					this.itemStacks[i] = null;
				}
			}
		}
	}
	
	public ItemStack getOutput()
	{
		List<IPotionType> types = new ArrayList();
		int potions = 0;
		for (int i = 0; i < 3; i++)
		{
			ItemStack stack = this.itemStacks[i];
			if (stack != null)
			{
				Item item = stack.getItem();
				if (item instanceof ItemPotion2)
				{
					potions++;
					
					List<IPotionType> types1 = ((ItemPotion2) item).getEffects(stack);
					types.addAll(types1);
				}
			}
		}
		
		if (!types.isEmpty())
		{
			types = PotionType.removeDuplicates(types);
			ItemStack stack = new ItemStack(BrewingAPI.potion2, potions, 1);
			for (IPotionType b : types)
			{
				b.apply(stack);
			}
			return stack;
		}
		
		return null;
	}
	
	public boolean canMix()
	{
		if (this.itemStacks[3] == null && this.getFilledSlots() >= 2)
		{
			for (int index = 0; index < 3; index++)
			{
				ItemStack stack = this.itemStacks[index];
				if (stack != null && stack.getItem() instanceof ItemPotion2)
				{
					if (((ItemPotion2) stack.getItem()).hasEffects(stack))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public int getMixTime()
	{
		return this.time;
	}
	
	public void setMixTime(int mixTime)
	{
		this.time = mixTime;
	}
	
	public int getFilledSlots()
	{
		int filledSlots = 0;
		for (int i = 0; i < 3; i++)
		{
			if (this.itemStacks[i] != null)
			{
				filledSlots++;
			}
		}
		return filledSlots;
	}
	
	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack stack)
	{
		return stack.getItem() == Items.potionitem && slotID != 3;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.time = nbt.getShort("BrewTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("BrewTime", (short) this.time);
	}
}
