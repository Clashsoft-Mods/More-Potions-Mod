package clashsoft.mods.morepotions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.potion.PotionEffect;

public interface IPotionEffectHandler
{
	public void onPotionUpdate(EntityLiving entity, PotionEffect effect);
	public boolean canHandle(PotionEffect effect);
}
