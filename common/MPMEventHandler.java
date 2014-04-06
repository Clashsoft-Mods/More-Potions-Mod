package clashsoft.mods.morepotions.common;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.network.PacketGreenThumb;
import clashsoft.mods.morepotions.network.PacketProjectile;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class MPMEventHandler
{
	@SubscribeEvent(priority = EventPriority.LOW)
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
					String message = "<\u00a7kCLASHSOFT\u00a7r>: \u00a7bYour life has just been saved by a magical power. Be careful next time, it wont help you again.";
					((EntityPlayer) event.entityLiving).addChatMessage(new ChatComponentText(message));
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
			{
				attacker.attackEntityFrom(DamageSource.cactus, event.entityLiving.getActivePotionEffect(MorePotionsMod.thorns).getAmplifier() / 3F);
			}
		}
	}
	
	@SubscribeEvent
	public void playerRightClick(PlayerInteractEvent event)
	{
		if (event.action == Action.RIGHT_CLICK_BLOCK && event.entityLiving.isPotionActive(MorePotionsMod.greenThumb))
		{
			MorePotionsMod.instance.netHandler.sendToServer(new PacketGreenThumb(event.x, event.y, event.z, event.face));
		}
		if (event.action == Action.RIGHT_CLICK_AIR && event.entityLiving.isPotionActive(MorePotionsMod.projectile))
		{
			MorePotionsMod.instance.netHandler.sendToServer(new PacketProjectile());
		}
	}
}