package clashsoft.mods.morepotions.tileentity;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.item.ItemPotion2;
import clashsoft.brewingapi.potion.base.IPotionBase;
import clashsoft.brewingapi.potion.recipe.*;
import clashsoft.brewingapi.potion.type.IPotionType;
import clashsoft.brewingapi.potion.type.PotionType;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class TileEntityCauldron extends TileEntity
{
	public static final String	CHANNEL	= "MPMCauldron";
	
	public ItemStack			output;
	public int					color;
	
	public TileEntityCauldron()
	{
		this.output = new ItemStack(BrewingAPI.potion2, 1, 0);
		this.color = 0x0C0CFF;
	}
	
	public boolean isItemValid(ItemStack stack)
	{
		if (stack != null)
		{
			if (stack.getItem() != Items.gunpowder && PotionType.getFromIngredient(stack) != null)
			{
				return true;
			}
		}
		return false;
	}
	
	public IChatComponent addIngredient(ItemStack ingredient)
	{
		if (ingredient.getItem() == Items.water_bucket)
		{
			return new ChatComponentTranslation("cauldron.addwater");
		}
		
		IChatComponent out = null;
		IPotionRecipe recipe = PotionRecipes.get(ingredient);
		
		if (recipe instanceof PotionRecipeAmplify)
		{
			out = new ChatComponentTranslation("cauldron.effects.amplifiers.increase");
		}
		else if (recipe instanceof PotionRecipeExtend)
		{
			out = new ChatComponentTranslation("cauldron.effects.durations.increase");
		}
		else if (recipe instanceof PotionRecipeDilute)
		{
			out = new ChatComponentTranslation("cauldron.effects.durations.decrease");
		}
		else if (recipe instanceof PotionRecipeInvert)
		{
			out = new ChatComponentTranslation("cauldron.effects.invert");
		}
		else if (recipe instanceof PotionRecipe)
		{
			PotionRecipe recipe1 = (PotionRecipe) recipe;
			IPotionType output = recipe1.getOutput();
			if (output instanceof IPotionBase)
			{
				out = new ChatComponentTranslation("cauldron.effects.add.base", ((IPotionBase) output).getName());
			}
			else
			{
				out = new ChatComponentTranslation("cauldron.effects.add", output.getDisplayName().toString());
			}
		}
		
		if (recipe.canApply(this.output))
		{
			recipe.apply(this.output);
		}
		
		this.updateOutput();
		
		return out;
	}
	
	public void clear()
	{
		this.output = new ItemStack(BrewingAPI.potion2, 1, 0);
		this.updateOutput();
	}
	
	public boolean isWater()
	{
		return ((ItemPotion2) this.output.getItem()).isWater(this.output);
	}
	
	public void updateOutput()
	{
		this.color = this.output.getItem().getColorFromItemStack(this.output, 0);
		this.sync();
	}
	
	public void sync()
	{
		if (this.worldObj != null && !this.worldObj.isRemote)
		{
			MorePotionsMod.instance.netHandler.syncCauldron(this);
		}
	}
	
	public int getColor()
	{
		return this.color;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.output = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("PotionTypes"));
		this.updateOutput();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		NBTTagCompound output = new NBTTagCompound();
		this.output.writeToNBT(output);
		nbt.setTag("PotionTypes", nbt);
	}
}
