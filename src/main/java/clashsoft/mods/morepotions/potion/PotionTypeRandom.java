package clashsoft.mods.morepotions.potion;

import java.util.Random;

import clashsoft.brewingapi.potion.base.PotionBase;
import clashsoft.brewingapi.potion.type.IPotionType;
import clashsoft.brewingapi.potion.type.PotionType;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class PotionTypeRandom extends PotionType
{
	protected Random	rand	= new Random();
	
	public PotionTypeRandom(ItemStack ingredient, PotionBase base)
	{
		super(new PotionEffect(MorePotionsMod.random.id, 20 * 45, 0), 0, 20 * 90, ingredient, base);
	}
	
	@Override
	public int getMaxDuration()
	{
		return MorePotionsMod.randomMode == 0 ? 1 : super.getMaxDuration();
	}
	
	@Override
	public void apply_do(EntityLivingBase target, PotionEffect effect)
	{
		IPotionType type = PotionType.getRandom(this.rand);
		PotionEffect pe = type.getEffect();
		if (pe != null)
		{
			pe = new PotionEffect(pe.getPotionID(), effect.getDuration(), effect.getAmplifier());
			target.addPotionEffect(pe);
		}
	}
}
