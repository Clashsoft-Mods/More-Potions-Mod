package clashsoft.mods.morepotions.crafting;

import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class MPMCraftingHandler
{
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
		{
			if (craftMatrix.getStackInSlot(i) != null)
			{
				ItemStack j = craftMatrix.getStackInSlot(i);
				if (j.getItem() != null && j.getItem() == MorePotionsMod.mortar)
				{
					ItemStack k = new ItemStack(MorePotionsMod.mortar, 2, j.getItemDamage() + 1);
					if (k.getItemDamage() >= k.getMaxDamage())
					{
						k.stackSize--;
					}
					craftMatrix.setInventorySlotContents(i, k);
				}
			}
		}
	}
}