package clashsoft.mods.morepotions.item;

import clashsoft.mods.morepotions.MorePotionsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemReed;

public class ItemBrewingStand2 extends ItemReed
{
	public ItemBrewingStand2(int id)
	{
		super(id, MorePotionsMod.brewingStand2);
		this.func_111206_d("brewing_stand");
	}
}
