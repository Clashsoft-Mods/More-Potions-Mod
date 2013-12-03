package clashsoft.mods.morepotions.handlers;

import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.api.IIngredientHandler;
import clashsoft.brewingapi.brewing.PotionType;
import clashsoft.brewingapi.brewing.PotionBase;

import net.minecraft.item.ItemStack;

public class MPMIngredientHandler implements IIngredientHandler
{
	@Override
	public boolean canHandleIngredient(ItemStack ingredient)
	{
		return PotionType.getBrewingFromIngredient(ingredient) != null;
	}
	
	@Override
	public boolean canApplyIngredient(ItemStack ingredient, ItemStack potion)
	{
		List<PotionType> effects = BrewingAPI.potion2.getEffects(potion);
		PotionBase base = null;
		for (PotionType b : effects)
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
		PotionType newBrewing = PotionType.getBrewingFromIngredient(ingredient);
		List<PotionType> oldBrewings = BrewingAPI.potion2.getEffects(potion);
		ItemStack newPotion = new ItemStack(potion.getItem(), potion.stackSize, potion.getItemDamage());
		
		for (PotionType b : oldBrewings)
		{
			if (b.getEffect() != null)
				b.getEffect().duration *= 0.75;
			
			b.addBrewingToItemStack(newPotion);
		}
		newBrewing.addBrewingToItemStack(newPotion);
		
		return newPotion;
	}
}