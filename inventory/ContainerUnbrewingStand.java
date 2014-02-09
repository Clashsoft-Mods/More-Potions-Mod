package clashsoft.mods.morepotions.inventory;

import clashsoft.brewingapi.inventory.slot.SlotPotion;
import clashsoft.cslib.minecraft.inventory.SlotOutput;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerUnbrewingStand extends Container
{
	private TileEntityUnbrewingStand	mixer;
	private Slot						theSlot;
	private int							brewTime	= 0;
	
	public ContainerUnbrewingStand(InventoryPlayer inventory, TileEntityUnbrewingStand unbrewingStand)
	{
		this.mixer = unbrewingStand;
		this.theSlot = this.addSlotToContainer(new SlotPotion(inventory.player, unbrewingStand, 0, 79, 17));
		this.addSlotToContainer(new SlotOutput(unbrewingStand, 1, 57, 61));
		this.addSlotToContainer(new SlotOutput(unbrewingStand, 2, 79, 61));
		this.addSlotToContainer(new SlotOutput(unbrewingStand, 3, 101, 61));
		this.addSlotToContainer(new SlotOutput(unbrewingStand, 4, 38, 44));
		this.addSlotToContainer(new SlotOutput(unbrewingStand, 5, 120, 44));
		
		int i;
		
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.mixer.getUnbrewTime());
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			
			if (this.brewTime != this.mixer.getUnbrewTime())
			{
				icrafting.sendProgressBarUpdate(this, 0, this.mixer.getUnbrewTime());
			}
		}
		
		this.brewTime = this.mixer.getUnbrewTime();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int time)
	{
		if (id == 0)
		{
			this.mixer.setBrewTime(time);
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.mixer.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotID);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (slotID < 6)
			{
				if (!this.mergeItemStack(itemstack1, 6, 42, false))
				{
					return null;
				}
			}
			else if (!this.theSlot.getHasStack() && this.theSlot.isItemValid(itemstack))
			{
				if (!this.mergeItemStack(itemstack1, 0, 1, false))
					;
			}
			else if (slotID >= 6 && slotID < 33)
			{
				if (!this.mergeItemStack(itemstack1, 33, 42, false))
				{
					return null;
				}
			}
			else if (slotID >= 33 && slotID < 42)
			{
				if (!this.mergeItemStack(itemstack1, 6, 33, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 6, 42, false))
			{
				return null;
			}
			
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
		}
		
		return itemstack;
	}
}
