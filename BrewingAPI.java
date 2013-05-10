package clashsoft.mods.morepotions;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
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
	
	public static void registerPotionEffectHandler(IPotionEffectHandler par1iPotionEffectHandler)
	{
		if (!effectHandlers.contains(par1iPotionEffectHandler))
		{
			effectHandlers.add(par1iPotionEffectHandler);
		}
	}
	
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		for (IPotionEffectHandler handler : effectHandlers)
		{
			for (Object effect : event.entityLiving.getActivePotionEffects())
			{
				if (handler.canHandle((PotionEffect) effect))
				{
					handler.onPotionUpdate(event.entityLiving, (PotionEffect) effect);
				}
			}
		}
	}
}
