package clashsoft.mods.morepotions.handlers;

import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.api.IIngredientHandler;
import clashsoft.brewingapi.brewing.Brewing;
import clashsoft.brewingapi.brewing.BrewingBase;

import net.minecraft.item.ItemStack;

public class MPMIngredientHandler implements IIngredientHandler
{
	@Override
	public boolean canHandleIngredient(ItemStack ingredient)
	{
		return Brewing.getBrewingFromIngredient(ingredient) != null;
	}
	
	@Override
	public boolean canApplyIngredient(ItemStack ingredient, ItemStack potion)
	{
		List<Brewing> effects = BrewingAPI.potion2.getEffects(potion);
		BrewingBase base = null;
		for (Brewing b : effects)
			if (b != null)
			{
				if (base == null)
					base = b.getBase();
				else if (!base.equals(b.getBase()))
					return false;
			}
		return false;
	}
	
	@Override
	public ItemStack applyIngredient(ItemStack ingredient, ItemStack potion)
	{
		Brewing newBrewing = Brewing.getBrewingFromIngredient(ingredient);
		List<Brewing> oldBrewings = BrewingAPI.potion2.getEffects(potion);
		ItemStack newPotion = new ItemStack(potion.getItem(), potion.stackSize, potion.getItemDamage());
		
		for (Brewing b : oldBrewings)
		{
			if (b.getEffect() != null)
				b.getEffect().duration *= 0.75;
			
			b.addBrewingToItemStack(newPotion);
		}
		newBrewing.addBrewingToItemStack(newPotion);
		
		return newPotion;
	}
}