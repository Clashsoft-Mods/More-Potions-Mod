package clashsoft.mods.morepotions.crafting;

import clashsoft.cslib.minecraft.item.CSStacks;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class MortarRecipe implements IRecipe
{
	private ItemStack input;
	private ItemStack output;
	
	public MortarRecipe(ItemStack input, ItemStack output)
	{
		this.input = input;
		this.output = output;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventory)
	{
		int size = inventory.getSizeInventory();
		
		int j = 0;
		for (int i = 0; i < size; i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null)
			{
				boolean flag1 = stack.getItem() == MorePotionsMod.mortar;
				boolean flag2 = CSStacks.equals(stack, this.input);
				if (flag1 || flag2)
				{
					j++;
				}
				else
				{
					return null;
				}
			}
		}
		
		if (j == 2)
		{
			return this.output;
		}
		return null;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
	
	@Override
	public int getRecipeSize()
	{
		return 2;
	}
	
	@Override
	public boolean matches(InventoryCrafting inventory, World world)
	{
		int size = inventory.getSizeInventory();
		
		int j = 0;
		for (int i = 0; i < size; i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null)
			{
				boolean flag1 = stack.getItem() == MorePotionsMod.mortar;
				boolean flag2 = CSStacks.equals(stack, this.input);
				if (flag1 || flag2)
				{
					j++;
				}
				else
				{
					return false;
				}
			}
		}
		
		return j == 2;
	}
}
