package clashsoft.mods.morepotions;

import java.util.LinkedList;
import java.util.List;

import clashsoft.brewingapi.api.IPotionEffectHandler;
import clashsoft.brewingapi.brewing.Brewing;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.ForgeDirection;

public class MorePotionsModEffectHandler implements IPotionEffectHandler
{
	private float				tick				= 0;
	
	private List<PotionEffect>	addEffectQueue		= new LinkedList<PotionEffect>();
	private List<Integer>		removeEffectQueue	= new LinkedList<Integer>();
	
	@Override
	public void onPotionUpdate(EntityLivingBase living, PotionEffect effect)
	{
		if (effect.getPotionID() == MorePotionsMod.fire.id)
		{
			int x = (int) Math.floor(living.posX);
			int y = (int) (living.posY - living.getYOffset());
			int z = (int) Math.floor(living.posZ);
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
		else if (effect.getPotionID() == MorePotionsMod.doubleJump.id)
		{
			// if (living instanceof EntityPlayer)
			// {
			// boolean canJump = !living.isPlayerSleeping() && living.is;
			// if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && living.motionY <
			// 0.07 && !hasJumped && canJump) //Waaaaaay more checks than
			// necessary
			// {
			// double motionY = 0.41999998688697815D;
			//
			// if (living.isPotionActive(Potion.jump))
			// {
			// motionY +=
			// (double)((float)(living.getActivePotionEffect(Potion.jump).getAmplifier()
			// + 1) * 0.1F);
			// }
			// //checks for armour/abilities...
			// living.addVelocity(0, motionY, 0);
			// living.setAir(0);
			// hasJumped = true;
			// }
			// if(living.onGround)
			// {
			// hasJumped = false;
			// }
			// }
		}
		else if (effect.getPotionID() == MorePotionsMod.antiHunger.id)
		{
			if (living instanceof EntityPlayer && ((int) tick) % 40 == 0) // True
																			// every
																			// 2
																			// seconds
			{
				((EntityPlayer) living).getFoodStats().addStats(1, 0.1F);
			}
		}
		else if (effect.getPotionID() == MorePotionsMod.explosiveness.id)
		{
			if (((int) tick) % 40 == 0)
			{
				living.worldObj.createExplosion(living, living.posX, living.posY, living.posZ, (effect.getAmplifier() + 1) * 2, true);
			}
		}
		else if (effect.getPotionID() == MorePotionsMod.random.id)
		{
			if (MorePotionsMod.randomMode == 0)
			{
				this.addEffectQueue.clear();
				this.addEffectQueue.add(Brewing.effectBrewings.get(living.getRNG().nextInt(Brewing.effectBrewings.size() - 1)).getEffect());
				this.removeEffectQueue.add(MorePotionsMod.random.id);
			}
			else
			{
				if (((int) tick) % 40 == 0)
				{
					PotionEffect pe = Brewing.combinableEffects.get(living.getRNG().nextInt(Brewing.effectBrewings.size() - 1)).getEffect();
					if (pe.getDuration() >= 2)
					{
						pe = new PotionEffect(pe.getPotionID(), 2 * 20, pe.getAmplifier());
					}
					this.addEffectQueue.add(pe);
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
	
	@Override
	public List<PotionEffect> getAddQueue()
	{
		return addEffectQueue;
	}
	
	@Override
	public List<Integer> getRemoveQueue()
	{
		return removeEffectQueue;
	}
	
	@Override
	public void clearAddQueue()
	{
		addEffectQueue.clear();
	}
	
	@Override
	public void clearRemoveQueue()
	{
		removeEffectQueue.clear();
	}
}