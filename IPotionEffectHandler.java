package clashsoft.mods.morepotions;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.potion.PotionEffect;

public interface IPotionEffectHandler
{
	public List<PotionEffect> addEffectQueue = new LinkedList<PotionEffect>();
	public List<Integer> removeEffectQueue = new LinkedList<Integer>();
	
	public void onPotionUpdate(EntityLiving entity, PotionEffect effect);
	public boolean canHandle(PotionEffect effect);
}
