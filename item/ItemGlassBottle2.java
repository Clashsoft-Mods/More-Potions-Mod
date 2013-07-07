package clashsoft.mods.morepotions.item;

import clashsoft.mods.morepotions.MorePotionsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.util.Icon;

public class ItemGlassBottle2 extends ItemGlassBottle
{
	public ItemGlassBottle2(int par1)
	{
		super(par1);
		this.func_111206_d("potion");
	}
	
	/**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int par1)
    {
        return MorePotionsMod.potion2.getIconFromDamage(1);
    }

}
