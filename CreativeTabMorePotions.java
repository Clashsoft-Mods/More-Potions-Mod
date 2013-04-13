package clashsoft.mods.morepotions;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabMorePotions extends CreativeTabs
{

	public CreativeTabMorePotions(int par1, String par2Str)
	{
		super(par1, par2Str);
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
	    return new ItemStack(Item.eyeOfEnder);
	}

}
