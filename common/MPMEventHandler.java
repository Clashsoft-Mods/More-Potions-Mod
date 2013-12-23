package clashsoft.mods.morepotions.common;

import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class MPMEventHandler
{
	@ForgeSubscribe
	public void playerJoined(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			CSUpdate.doClashsoftUpdateCheck((EntityPlayer) event.entity, "More Potions Mod", "mpm", MorePotionsMod.VERSION);
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
	
	@ForgeSubscribe
	public void playerRightClick(PlayerInteractEvent event)
	{
		if (!event.entityPlayer.worldObj.isRemote)
		{
			if (event.action == Action.RIGHT_CLICK_BLOCK && event.entityLiving.isPotionActive(MorePotionsMod.greenThumb))
			{
				if (event.entityPlayer.getCurrentEquippedItem() == null)
				{
					int amplifier = event.entityPlayer.getActivePotionEffect(MorePotionsMod.greenThumb).getAmplifier();
					
					for (int i = 0; i < amplifier + 1; i++)
						Item.dyePowder.onItemUse(new ItemStack(Item.dyePowder, 1, 15), event.entityPlayer, event.entityPlayer.worldObj, event.x, event.y, event.z, event.face, 0F, 0F, 0F);
				}
			}
			if (event.action == Action.RIGHT_CLICK_AIR && event.entityLiving.isPotionActive(MorePotionsMod.projectile))
			{
				if (event.entityPlayer.getCurrentEquippedItem() == null)
				{
					int amplifier = event.entityPlayer.getActivePotionEffect(MorePotionsMod.projectile).getAmplifier();
					World world = event.entityPlayer.worldObj;
					
					if (amplifier == 0)
						world.spawnEntityInWorld(new EntitySnowball(world, event.entityPlayer));
					else
					{
						EntityArrow projectile = new EntityArrow(world, event.entityPlayer, 1F + 0.5F * amplifier);
						projectile.canBePickedUp = 2;
						projectile.setDamage(projectile.getDamage() + 0.5D * amplifier);
						projectile.setKnockbackStrength(1);
						
						world.spawnEntityInWorld(projectile);
					}
				}
			}
		}
	}
}