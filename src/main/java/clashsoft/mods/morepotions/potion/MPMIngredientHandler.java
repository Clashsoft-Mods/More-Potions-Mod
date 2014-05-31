package clashsoft.mods.morepotions.potion;

import clashsoft.brewingapi.potion.IIngredientHandler;

import net.minecraft.item.ItemStack;

public class MPMIngredientHandler implements IIngredientHandler
{
	@Override
	public boolean canHandleIngredient(ItemStack ingredient)
	{
		return false;
	}
	
	@Override
	public boolean canApplyIngredient(ItemStack ingredient, ItemStack potion)
	{
		return false;
	}
	
	@Override
	public ItemStack applyIngredient(ItemStack ingredient, ItemStack potion)
	{
		return potion;
	}
}