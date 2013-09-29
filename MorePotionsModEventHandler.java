package clashsoft.mods.morepotions;

import clashsoft.clashsoftapi.util.CSUpdate;
import clashsoft.clashsoftapi.util.update.ModUpdate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class MorePotionsModEventHandler
{
	@ForgeSubscribe
	public void playerJoined(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			ModUpdate update = CSUpdate.checkForUpdate("More Potions Mod", MorePotionsMod.VERSION);
			CSUpdate.notifyUpdate((EntityPlayer) event.entity, "More Potions Mod", update);
		}
	}
	
	@ForgeSubscribe(priority = EventPriority.LOW)
	public void onEntityDamaged(LivingAttackEvent event)
	{
		if (event.entityLiving.isPotionActive(MorePotionsMod.doubleLife))
		{
			if (event.entityLiving.getHealth() - event.ammount <= 0)
			{
				event.setCanceled(true);
				event.entityLiving.setHealth(event.entityLiving.getMaxHealth());
				event.entityLiving.removePotionEffect(MorePotionsMod.doubleLife.id);
				if (event.entityLiving instanceof EntityPlayer)
				{
					((EntityPlayer) event.entityLiving).addChatMessage("<\u00a7kCLASHSOFT\u00a7r>: \u00a7bYour life has just been saved by a magical power. Be careful next time, it wont help you again.");
				}
			}
		}
		if (event.entityLiving.isPotionActive(MorePotionsMod.ironSkin))
		{
			event.entityLiving.addPotionEffect(new PotionEffect(Potion.resistance.id, event.entityLiving.getActivePotionEffect(MorePotionsMod.ironSkin).getDuration(), 2));
			if (event.source == DamageSource.inFire || event.source == DamageSource.onFire)
			{
				event.entityLiving.extinguish();
				event.setCanceled(true);
			}
		}
		if (event.entityLiving.isPotionActive(MorePotionsMod.obsidianSkin))
		{
			event.entityLiving.addPotionEffect(new PotionEffect(Potion.resistance.id, event.entityLiving.getActivePotionEffect(MorePotionsMod.obsidianSkin).getDuration(), 2));
			if (event.source == DamageSource.lava || event.source == DamageSource.inFire || event.source == DamageSource.onFire)
			{
				event.entityLiving.extinguish();
				event.setCanceled(true);
			}
		}
		if (event.entityLiving.isPotionActive(MorePotionsMod.thorns))
		{
			Entity attacker = event.source.getSourceOfDamage();
			if (attacker != null)
				attacker.attackEntityFrom(DamageSource.cactus, event.entityLiving.getActivePotionEffect(MorePotionsMod.thorns).getAmplifier() / 3F);
		}
	}
}