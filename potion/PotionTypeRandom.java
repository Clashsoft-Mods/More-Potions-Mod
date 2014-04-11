package clashsoft.mods.morepotions.potion;

import java.util.Random;

import clashsoft.brewingapi.potion.type.PotionBase;
import clashsoft.brewingapi.potion.type.PotionType;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

public class PotionTypeRandom extends PotionType
{
	protected Random rand = new Random();
	
	public PotionTypeRandom(ItemStack ingredient, PotionBase base)
	{
		super(new PotionEffect(MorePotionsMod.random.id, 20 * 45, 0), 0, 20 * 90, ingredient, base);
	}
	
	@Override
	public PotionEffect getEffect()
	{
		if (MorePotionsMod.randomMode == 0)
		{
			PotionEffect effect = PotionType.getRandom(this.rand).getEffect();
			return effect;
		}
		return super.getEffect();
	}
	
	@Override
	public int getMaxDuration()
	{
		return MorePotionsMod.randomMode == 0 ? 1 : super.getMaxDuration();
	}
}
