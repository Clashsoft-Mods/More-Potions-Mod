package clashsoft.mods.morepotions.brewing;

import clashsoft.brewingapi.api.IPotionEffectHandler;
import clashsoft.brewingapi.brewing.PotionType;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.ForgeDirection;

public class MPMEffectHandler implements IPotionEffectHandler
{	
	@Override
	public void onPotionUpdate(int tick, EntityLivingBase living, PotionEffect effect)
	{
		if (effect.getPotionID() == MorePotionsMod.fire.id)
		{
			int x = (int) living.posX;
			int y = (int) (living.posY - living.getYOffset());
			int z = (int) living.posZ;
			int id = living.worldObj.getBlockId(x, y - 1, z);
			if (Block.blocksList[id] != null && Block.blocksList[id].isBlockSolidOnSide(living.worldObj, x, y - 1, z, ForgeDirection.UP) && living.worldObj.getBlockId(x, y, z) == 0)
			{
				living.worldObj.setBlock(x, y, z, Block.fire.blockID);
			}
			
			living.setFire(1);
		}
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
			if (living.worldObj.getBlockId(x, y - 1, z) == 9 && living.worldObj.getBlockId(x, y, z) == 0)
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
			int id = living.worldObj.getBlockId(x, y - 1, z);
			if (id == Block.waterMoving.blockID || id == Block.waterStill.blockID)
			{
				living.worldObj.setBlock(x, y - 1, z, Block.ice.blockID);
			}
			if (living.getActivePotionEffect(MorePotionsMod.coldness).getAmplifier() > 0 && Block.blocksList[id] != null && Block.blocksList[id].isBlockSolidOnSide(living.worldObj, x, y - 1, z, ForgeDirection.UP) && living.worldObj.getBlockId(x, y, z) == 0)
			{
				living.worldObj.setBlock(x, y, z, Block.snow.blockID);
			}
		}
		else if (effect.getPotionID() == MorePotionsMod.explosiveness.id)
		{
			if (((int) tick) % 40 == 0)
				living.worldObj.createExplosion(living, living.posX, living.posY, living.posZ, (effect.getAmplifier() + 1) * 2, true);
		}
		else if (effect.getPotionID() == MorePotionsMod.random.id)
		{
			if (MorePotionsMod.randomMode == 0)
			{
				living.addPotionEffect(PotionType.getRandom(living.getRNG()).getEffect());
				living.removePotionEffect(MorePotionsMod.random.id);
			}
			else
			{
				if ((int) (tick % 40) == 0)
				{
					PotionEffect pe = PotionType.getRandom(living.getRNG()).getEffect();
					if (pe.getDuration() > 1)
					{
						pe.duration = 40;
					}
					living.addPotionEffect(effect);
				}
			}
		}
		tick += 1F / (living.getActivePotionEffects().size());
	}
	
	@Override
	public boolean canHandle(PotionEffect effect)
	{
		return true;
	}
}