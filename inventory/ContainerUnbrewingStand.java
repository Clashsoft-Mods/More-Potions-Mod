package clashsoft.mods.morepotions.inventory;

import clashsoft.brewingapi.inventory.slot.SlotPotion;
import clashsoft.mods.morepotions.inventory.slot.SlotOutput;
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
		this.addSlotToContainer(new SlotOutput(inventory.player, unbrewingStand, 1, 57, 61));
		this.addSlotToContainer(new SlotOutput(inventory.player, unbrewingStand, 2, 79, 61));
		this.addSlotToContainer(new SlotOutput(inventory.player, unbrewingStand, 3, 101, 61));
		this.addSlotToContainer(new SlotOutput(inventory.player, unbrewingStand, 4, 38, 44));
		this.addSlotToContainer(new SlotOutput(inventory.player, unbrewingStand, 5, 120, 44));
		
		int var3;
		
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(inventory, var3, 8 + var3 * 18, 142));
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting)
	{
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0, this.mixer.getUnbrewTime());
	}
	
	/**
	 * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
	 */
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (int var1 = 0; var1 < this.crafters.size(); ++var1)
		{
			ICrafting var2 = (ICrafting) this.crafters.get(var1);
			
			if (this.brewTime != this.mixer.getUnbrewTime())
			{
				var2.sendProgressBarUpdate(this, 0, this.mixer.getUnbrewTime());
			}
		}
		
		this.brewTime = this.mixer.getUnbrewTime();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
		{
			this.mixer.setBrewTime(par2);
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.mixer.isUseableByPlayer(player);
	}
	
	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
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
				if (!this.mergeItemStack(itemstack1, 0, 1, false));
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
