package clashsoft.mods.morepotions.potion;

import clashsoft.brewingapi.potion.IPotionEffectHandler;
import clashsoft.brewingapi.potion.type.PotionType;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.ForgeDirection;

public class MPMEffectHandler implements IPotionEffectHandler
{
	@Override
	public void onPotionUpdate(int tick, EntityLivingBase living, PotionEffect effect)
	{
		if (effect.getPotionID() == (MorePotionsMod.effectRemove.id))
		{
			for (int i = 0; i < Potion.potionTypes.length; i++)
			{
				if (i != MorePotionsMod.effectRemove.id)
				{
					living.removePotionEffect(i);
				}
			}
		}
		else if (effect.getPotionID() == (MorePotionsMod.waterWalking.id))
		{
			int x = (int) Math.floor(living.posX);
			int y = (int) (living.posY - living.getYOffset());
			int z = (int) Math.floor(living.posZ);
			
			if (living.worldObj.getBlock(x, y - 1, z) == Blocks.water && living.worldObj.isAirBlock(x, y, z))
			{
				if (living.motionY < 0 && living.boundingBox.minY < y)
				{
					living.motionY = 0;
					living.fallDistance = 0;
					living.onGround = true;
					if (living.isSneaking())
					{
						living.motionY -= 0.1F;
					}
				}
			}
		}
		else if (effect.getPotionID() == (MorePotionsMod.coldness.id))
		{
			int x = (int) Math.floor(living.posX);
			int y = (int) (living.posY - living.getYOffset());
			int z = (int) Math.floor(living.posZ);
			
			Block block = living.worldObj.getBlock(x, y - 1, z);
			if (block == Blocks.water || block == Blocks.flowing_water)
			{
				living.worldObj.setBlock(x, y - 1, z, Blocks.ice);
			}
			if (effect.getAmplifier() > 0 && block.isSideSolid(living.worldObj, x, y - 1, z, ForgeDirection.UP) && living.worldObj.isAirBlock(x, y, z))
			{
				living.worldObj.setBlock(x, y, z, Blocks.snow_layer);
			}
		}
		else if (effect.getPotionID() == MorePotionsMod.explosiveness.id)
		{
			if (tick % 40 == 0)
			{
				int amplifier = effect.getAmplifier();
				float size = amplifier + 2F;
				living.worldObj.createExplosion(living, living.posX, living.posY, living.posZ, size, true);
			}
		}
		else if (effect.getPotionID() == MorePotionsMod.random.id)
		{
			if (tick % 40 == 0)
			{
				PotionEffect pe = PotionType.getRandom(living.getRNG()).getEffect();
				if (pe.getDuration() > 1)
				{
					pe = new PotionEffect(pe.getPotionID(), 40, pe.getAmplifier());
				}
				living.addPotionEffect(effect);
			}
		}
	}
	
	@Override
	public boolean canHandle(PotionEffect effect)
	{
		return true;
	}
}