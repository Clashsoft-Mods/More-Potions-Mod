package clashsoft.mods.morepotions;

import clashsoft.brewingapi.inventory.ContainerBrewingStand2;
import clashsoft.brewingapi.tileentity.TileEntityBrewingStand2;
import clashsoft.mods.morepotions.gui.GuiMixer;
import clashsoft.mods.morepotions.inventory.ContainerMixer;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler
{
	public void registerRenderInformation()
	{
		
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == MorePotionsMod.MixerTEID)
		{
			return new GuiMixer(player.inventory, (TileEntityMixer) world.getBlockTileEntity(x, y, z));
		}
		return null;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z)
	{
		TileEntity te = world.getBlockTileEntity(X, Y, Z);
		if (te != null && te instanceof TileEntityBrewingStand2)
		{
			TileEntityBrewingStand2 bs = (TileEntityBrewingStand2) te;
			return new ContainerBrewingStand2(player.inventory, bs);
		}
		else if (te != null && te instanceof TileEntityMixer)
		{
			TileEntityMixer m = (TileEntityMixer) te;
			return new ContainerMixer(player.inventory, m);
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