package clashsoft.mods.morepotions.common;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.client.gui.GuiMixer;
import clashsoft.mods.morepotions.client.gui.GuiUnbrewingStand;
import clashsoft.mods.morepotions.inventory.ContainerMixer;
import clashsoft.mods.morepotions.inventory.ContainerUnbrewingStand;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MPMCommonProxy implements IGuiHandler
{
	public void registerRenderInformation()
	{
		
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == MorePotionsMod.mixerTileEntityID)
		{
			return new GuiMixer(player.inventory, (TileEntityMixer) world.getBlockTileEntity(x, y, z));
		}
		else if (ID == MorePotionsMod.unbrewingStandTileEntityID)
		{
			return new GuiUnbrewingStand(player.inventory, (TileEntityUnbrewingStand) world.getBlockTileEntity(x, y, z));
		}
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityMixer)
		{
			TileEntityMixer mixer = (TileEntityMixer) tileEntity;
			return new ContainerMixer(player.inventory, mixer);
		}
		else if (tileEntity instanceof TileEntityUnbrewingStand)
		{
			TileEntityUnbrewingStand unbrewingStand = (TileEntityUnbrewingStand) tileEntity;
			return new ContainerUnbrewingStand(player.inventory, unbrewingStand);
		}
		else
		{
			return null;
		}
	}
	
	public World getClientWorld()
	{
		return null;
	}
	
	public void registerRenderers()
	{
	}
	
}