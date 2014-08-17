package clashsoft.mods.morepotions.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMortar extends Item
{
	public ItemMortar()
	{
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setMaxDamage(32);
		this.setNoRepair();
		this.setMaxStackSize(1);
	}
	
	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		ItemStack copy = stack.copy();
		if (copy.attemptDamageItem(1, itemRand))
		{
			copy = null;
		}
		return copy;
	}
	
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
	{
		return false;
	}
}
