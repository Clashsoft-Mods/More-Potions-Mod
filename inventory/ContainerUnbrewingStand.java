package clashsoft.mods.morepotions.inventory;

import clashsoft.brewingapi.inventory.slot.SlotBrewingStandPotion2;
import clashsoft.mods.morepotions.inventory.slot.SlotMixerOutput;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBrewingStand;

public class ContainerUnbrewingStand extends Container
{
    private TileEntityUnbrewingStand unbrewingStand;

    /** Instance of Slot. */
    private final Slot theSlot;
    private int brewTime = 0;

    public ContainerUnbrewingStand(InventoryPlayer par1InventoryPlayer, TileEntityUnbrewingStand par2TileEntityUnbrewingStand)
    {
        this.unbrewingStand = par2TileEntityUnbrewingStand;
        this.theSlot = this.addSlotToContainer(new SlotBrewingStandPotion2(par1InventoryPlayer.player, par2TileEntityUnbrewingStand, 0, 79, 17));
        this.addSlotToContainer(new SlotMixerOutput(par1InventoryPlayer.player, par2TileEntityUnbrewingStand, 1000, 48, 46));
        this.addSlotToContainer(new SlotMixerOutput(par1InventoryPlayer.player, par2TileEntityUnbrewingStand, 2000, 91, 61));
        this.addSlotToContainer(new SlotMixerOutput(par1InventoryPlayer.player, par2TileEntityUnbrewingStand, 3000, 67, 61));
        this.addSlotToContainer(new SlotMixerOutput(par1InventoryPlayer.player, par2TileEntityUnbrewingStand, 4000, 110, 46));
        int l;
        int i1;

        for (l = 0; l < 3; ++l)
        {
            for (i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, l, 8 + l * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.unbrewingStand.getBrewTime());
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);

            if (this.brewTime != this.unbrewingStand.getBrewTime())
            {
                var2.sendProgressBarUpdate(this, 0, this.unbrewingStand.getBrewTime());
            }
        }

        this.brewTime = this.unbrewingStand.getBrewTime();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.unbrewingStand.setBrewTime(par2);
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.unbrewingStand.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
    	ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if ((par2 < 0 || par2 > 5) && par2 != 5)
            {
                if (!this.theSlot.getHasStack() && this.theSlot.isItemValid(var5))
                {
                    if (!this.mergeItemStack(var5, 0, 4, false))
                    {
                        return null;
                    }
                }
                else if (SlotBrewingStandPotion2.canHoldPotion(var3))
                {
                    if (!this.mergeItemStack(var5, 1, 3, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 4 && par2 < 31)
                {
                    if (!this.mergeItemStack(var5, 31, 40, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 31 && par2 < 40)
                {
                    if (!this.mergeItemStack(var5, 4, 31, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(var5, 4, 40, false))
                {
                    return null;
                }
            }
            else
            {
                if (!this.mergeItemStack(var5, 0, 40, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }
        return var3;
    }
}
