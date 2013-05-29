package clashsoft.mods.morepotions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class BrewingAPI
{
	public static List<IPotionEffectHandler> effectHandlers = new LinkedList<IPotionEffectHandler>();
	
	public static Brewing addBrewing(Brewing brewing)
	{
		return brewing.register();
	}
	
	public static void registerIngredientHandler(IIngredientHandler par1iIngredientHandler)
	{
		System.out.println("Ingredient handler \"" + par1iIngredientHandler + "\" registered");
		Brewing.ingredientHandlers.add(par1iIngredientHandler);
	}
	
	public static void registerEffectHandler(IPotionEffectHandler par1iPotionEffectHandler)
	{
		if (!effectHandlers.contains(par1iPotionEffectHandler))
		{
			effectHandlers.add(par1iPotionEffectHandler);
		}
	}
	
	@ForgeSubscribe
	public void onEntityUpdate(LivingUpdateEvent event)
	{
		Collection c = event.entityLiving.getActivePotionEffects();
		for (IPotionEffectHandler handler : effectHandlers)
		{
			for (Object effect : c)
			{
				if (handler.canHandle((PotionEffect)effect))
				{
					handler.onPotionUpdate(event.entityLiving, (PotionEffect)effect);
				}
			}
			for (PotionEffect pe : handler.addEffectQueue)
			{
				event.entityLiving.addPotionEffect(pe);
			}
			handler.addEffectQueue.clear();
			for (int i : handler.removeEffectQueue)
			{
				event.entityLiving.removePotionEffect(i);
			}
			handler.removeEffectQueue.clear();
		}
	}
}
