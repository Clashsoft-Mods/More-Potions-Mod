package clashsoft.mods.morepotions;

import net.minecraft.item.ItemStack;

public class BrewingAPI
{
	public static Brewing addBrewing(Brewing brewing)
	{
		return brewing.register();
	}
	
	public static void registerIngredientHandler(IIngredientHandler par1iIngredientHandler)
	{
		System.out.println("Ingredient handler \"" + par1iIngredientHandler + "\" registered");
		Brewing.ingredientHandlers.add(par1iIngredientHandler);
	}
}
