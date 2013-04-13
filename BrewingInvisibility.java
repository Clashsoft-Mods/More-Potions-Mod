package clashsoft.mods.morepotions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class BrewingInvisibility extends Brewing {

	public BrewingInvisibility(PotionEffect par1PotionEffect, int par2, int par3)
	{
		super(par1PotionEffect, par2, par3, awkward);
	}

	public BrewingInvisibility(PotionEffect par1PotionEffect, int par2, int par3, Brewing par4Brewing)
	{
		super(par1PotionEffect, par2, par3, par4Brewing, awkward);

	}

	public BrewingInvisibility(PotionEffect par1PotionEffect, int par2, int par3, ItemStack par4ItemStack)
	{
		super(par1PotionEffect, par2, par3, par4ItemStack, awkward);
	}

	public BrewingInvisibility(PotionEffect par1PotionEffect, int par2, int par3, Brewing par4Brewing, ItemStack par5ItemStack)
	{
		super(par1PotionEffect, par2, par3, par4Brewing, awkward);
	}
	
//	@Override
//	public Brewing onImproved()
//	{
//		Brewing var1 = illusion;
//		return var1.setEffect(new PotionEffect(var1.getEffect().getPotionID(), this.getEffect().getDuration(), 0));
//	}
	
	@Override
	public List<Brewing> getSubTypes()
	{
		List<Brewing> list = new ArrayList<Brewing>();
		list.add(this);
		if (this.isExtendable())
		{
			list.add(new Brewing(this.getEffect() != null ? (new PotionEffect(this.getEffect().getPotionID(), this.getEffect().getDuration() * 2, this.getEffect().getAmplifier())) : null, 0, this.getMaxDuration(), this.getOpposite(), this.getIngredient(), this.getBase()));
		}
		return list;
	}

}
