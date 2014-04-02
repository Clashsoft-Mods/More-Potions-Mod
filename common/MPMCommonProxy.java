package clashsoft.mods.morepotions.common;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.inventory.ContainerMixer;
import clashsoft.mods.morepotions.inventory.ContainerUnbrewingStand;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MPMCommonProxy implements IGuiHandler
{
	public void registerRenderInformation()
	{
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == MorePotionsMod.mixerTileEntityID)
		{
			return new ContainerMixer(player.inventory, (TileEntityMixer) world.getTileEntity(x, y, z));
		}
		else if (ID == MorePotionsMod.unbrewingStandTileEntityID)
		{
			return new ContainerUnbrewingStand(player.inventory, (TileEntityUnbrewingStand) world.getTileEntity(x, y, z));
		}
		return null;
	}
	
	public World getClientWorld()
	{
		return null;
	}
	
	public void registerRenderers()
	{
	}
}