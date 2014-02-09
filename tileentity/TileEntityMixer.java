package clashsoft.mods.morepotions.tileentity;

import java.util.ArrayList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.brewing.PotionType;
import clashsoft.brewingapi.item.ItemPotion2;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityMixer extends TileEntity implements IInventory
{
	private ItemStack[]	mixingItemStacks	= new ItemStack[4];
	public int			mixTime;
	
	public static int	maxMixTime			= 100;
	private ItemStack	output;
	
	public EntityPlayer	player;
	
	public TileEntityMixer()
	{
	}
	
	@Override
	public String getInventoryName()
	{
		return "container.mixer";
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.mixingItemStacks.length;
	}
	
	@Override
	public void updateEntity()
	{
		if (this.mixTime > 0)
		{
			--this.mixTime;
			
			if (this.mixTime == 0)
			{
				this.mixPotions();
				this.markDirty();
			}
			else if (!this.canMix())
			{
				this.mixTime = 0;
				this.markDirty();
			}
			else if (this.output != this.mixingItemStacks[3])
			{
				this.mixTime = 0;
				this.markDirty();
			}
		}
		else if (this.canMix())
		{
			this.mixTime = maxMixTime;
			this.output = this.mixingItemStacks[3];
		}
		
		super.updateEntity();
	}
	
	private void mixPotions()
	{
		this.mixingItemStacks[3] = this.getOutput();
		for (int i = 0; i < 3; i++)
		{
			this.mixingItemStacks[i] = null;
		}
	}
	
	public ItemStack getOutput()
	{
		List<PotionType> potionTypes = new ArrayList<PotionType>();
		for (int potionIndex = 0; potionIndex < 3; potionIndex++)
		{
			if (this.mixingItemStacks[potionIndex] != null && this.mixingItemStacks[potionIndex].getItem() instanceof ItemPotion2)
			{
				ItemPotion2 item = (ItemPotion2) this.mixingItemStacks[potionIndex].getItem();
				potionTypes.addAll(item.getEffects(this.mixingItemStacks[potionIndex]));
			}
		}
		if (!potionTypes.isEmpty())
		{
			potionTypes = (List<PotionType>) PotionType.removeDuplicates(potionTypes);
			int damage = this.mixingItemStacks[0] != null ? this.mixingItemStacks[0].getItemDamage() : this.mixingItemStacks[1] != null ? this.mixingItemStacks[1].getItemDamage() : this.mixingItemStacks[2] != null ? this.mixingItemStacks[2].getItemDamage() : 0;
			ItemStack ret = new ItemStack(BrewingAPI.potion2, 1, damage);
			for (PotionType b : potionTypes)
			{
				b.addPotionTypeToItemStack(ret);
			}
			return ret;
		}
		
		return null;
	}
	
	private boolean canMix()
	{
		if (this.mixingItemStacks[3] == null && this.getFilledSlots() >= 2 && this.mixingItemStacks[3] == null)
		{
			boolean flag = false;
			for (int index = 0; index < 3; index++)
			{
				if (this.mixingItemStacks[index] != null && this.mixingItemStacks[index].getItem() instanceof ItemPotion2)
				{
					if (((ItemPotion2) this.mixingItemStacks[index].getItem()).getEffects(this.mixingItemStacks[index]) == null)
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
		return this.mixTime;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.mixingItemStacks = new ItemStack[this.getSizeInventory()];
		
		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			NBTTagCompound compound = tagList.getCompoundTagAt(i);
			byte slotID = compound.getByte("Slot");
			
			if (slotID >= 0 && slotID < this.mixingItemStacks.length)
			{
				this.mixingItemStacks[slotID] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		
		this.mixTime = nbt.getShort("BrewTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("BrewTime", (short) this.mixTime);
		NBTTagList tagList = new NBTTagList();
		
		for (int i = 0; i < this.mixingItemStacks.length; ++i)
		{
			if (this.mixingItemStacks[i] != null)
			{
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) i);
				this.mixingItemStacks[i].writeToNBT(compound);
				tagList.appendTag(compound);
			}
		}
		
		nbt.setTag("Items", tagList);
	}
	
	@Override
	public ItemStack getStackInSlot(int slotID)
	{
		return slotID >= 0 && slotID < this.mixingItemStacks.length ? this.mixingItemStacks[slotID] : null;
	}
	
	@Override
	public ItemStack decrStackSize(int slotID, int amount)
	{
		if (slotID >= 0 && slotID < this.mixingItemStacks.length)
		{
			ItemStack stack = this.mixingItemStacks[slotID];
			this.mixingItemStacks[slotID] = null;
			return stack;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slotID)
	{
		if (slotID >= 0 && slotID < this.mixingItemStacks.length)
		{
			ItemStack stack = this.mixingItemStacks[slotID];
			this.mixingItemStacks[slotID] = null;
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
		if (slotID >= 0 && slotID < this.mixingItemStacks.length)
		{
			this.mixingItemStacks[slotID] = stack;
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
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this && player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}
	
	@SideOnly(Side.CLIENT)
	public void setBrewTime(int brewTime)
	{
		this.mixTime = brewTime;
	}
	
	public int getFilledSlots()
	{
		int filledSlots = 0;
		for (int i = 0; i < 3; i++)
		{
			if (this.mixingItemStacks[i] != null)
			{
				filledSlots++;
			}
		}
		return filledSlots;
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack stack)
	{
		return stack.getItem() == Items.potionitem && slotID != 3;
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
