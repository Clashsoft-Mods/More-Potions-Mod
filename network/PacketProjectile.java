package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

public class PacketProjectile extends CSPacket
{	
	@Override
	public void write(PacketBuffer buf)
	{
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
	}
	
	@Override
	public void handleServer(EntityPlayerMP player)
	{
		if (player.getCurrentEquippedItem() == null)
		{
			World world = player.worldObj;
			int amplifier = player.getActivePotionEffect(MorePotionsMod.projectile).getAmplifier();
			
			if (amplifier == 0)
			{
				player.worldObj.spawnEntityInWorld(new EntitySnowball(world, player));
			}
			else if (amplifier == 1)
			{
				EntityArrow projectile = new EntityArrow(world, player, 1F);
				projectile.canBePickedUp = 2;
				projectile.setKnockbackStrength(1);
				
				world.spawnEntityInWorld(projectile);
			}
			else
			{
				EntityFireball fireball = new EntitySmallFireball(world);
				world.spawnEntityInWorld(fireball);
			}
		}
	}
}
