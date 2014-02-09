package clashsoft.mods.morepotions.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemMortar extends Item
{
	public ItemMortar()
	{
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setMaxDamage(32);
		this.setNoRepair();
		this.setMaxStackSize(1);
	}
}
