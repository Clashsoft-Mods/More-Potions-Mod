package clashsoft.mods.morepotions.item;

import clashsoft.cslib.minecraft.item.ItemCraftingHelper;

import net.minecraft.creativetab.CreativeTabs;

public class ItemMortar extends ItemCraftingHelper
{
	public ItemMortar()
	{
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setMaxDamage(32);
		this.setNoRepair();
		this.setMaxStackSize(1);
	}
}
