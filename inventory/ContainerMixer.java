package clashsoft.mods.morepotions.inventory;

import clashsoft.brewingapi.inventory.slot.SlotPotion;
import clashsoft.cslib.minecraft.inventory.SlotOutput;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMixer extends Container
{
	private TileEntityMixer	mixer;
	
	/** Instance of Slot. */
	private final Slot		theSlot;
	private int				brewTime	= 0;
	
	public ContainerMixer(InventoryPlayer inventory, TileEntityMixer mixer)
	{
		this.mixer = mixer;
		this.theSlot = this.addSlotToContainer(new SlotPotion(inventory.player, mixer, 0, 56, 23));
		this.addSlotToContainer(new SlotPotion(inventory.player, mixer, 1, 79, 16));
		this.addSlotToContainer(new SlotPotion(inventory.player, mixer, 2, 102, 23));
		this.addSlotToContainer(new SlotOutput(mixer, 3, 79, 52));
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
	public void addCraftingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.mixer.getMixTime());
	}
	
	/**
	 * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
	 */
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			
			if (this.brewTime != this.mixer.getMixTime())
			{
				icrafting.sendProgressBarUpdate(this, 0, this.mixer.getMixTime());
			}
		}
		
		this.brewTime = this.mixer.getMixTime();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int type, int value)
	{
		if (type == 0)
		{
			this.mixer.setBrewTime(value);
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.mixer.isUseableByPlayer(par1EntityPlayer);
	}
	
	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
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
			
			if ((slotID < 0 || slotID > 3) && slotID != 3)
			{
				if (!this.theSlot.getHasStack() && this.theSlot.isItemValid(itemstack1))
				{
					if (!this.mergeItemStack(itemstack1, 0, 4, false))
					{
						return null;
					}
				}
				else if (SlotPotion.canHoldPotion(itemstack))
				{
					if (!this.mergeItemStack(itemstack1, 1, 3, false))
					{
						return null;
					}
				}
				else if (slotID >= 4 && slotID < 31)
				{
					if (!this.mergeItemStack(itemstack1, 31, 40, false))
					{
						return null;
					}
				}
				else if (slotID >= 31 && slotID < 40)
				{
					if (!this.mergeItemStack(itemstack1, 4, 31, false))
					{
						return null;
					}
				}
				else if (!this.mergeItemStack(itemstack1, 4, 40, false))
				{
					return null;
				}
			}
			else
			{
				if (!this.mergeItemStack(itemstack1, 4, 40, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
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
