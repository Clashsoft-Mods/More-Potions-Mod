package clashsoft.mods.morepotions.common;

import clashsoft.cslib.minecraft.common.BaseProxy;
import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.inventory.ContainerMixer;
import clashsoft.mods.morepotions.inventory.ContainerUnbrewingStand;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MPMProxy extends BaseProxy
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == MorePotionsMod.mixerTileEntityID)
		{
			return new ContainerMixer(player, (TileEntityMixer) world.getTileEntity(x, y, z));
		}
		else if (ID == MorePotionsMod.unbrewingStandTileEntityID)
		{
			return new ContainerUnbrewingStand(player, (TileEntityUnbrewingStand) world.getTileEntity(x, y, z));
		}
		return null;
	}
	
	public World getClientWorld()
	{
		return null;
	}
}