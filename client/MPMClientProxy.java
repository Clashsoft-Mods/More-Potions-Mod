package clashsoft.mods.morepotions.client;

import clashsoft.mods.morepotions.client.renderer.tileentity.CauldronRenderer;
import clashsoft.mods.morepotions.common.MPMCommonProxy;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class MPMClientProxy extends MPMCommonProxy
{
	public static int	mixerRenderType;
	public static int	cauldronRenderType;
	public static int	splashpotioncolor;
	
	@Override
	public void registerRenderers()
	{
		setCustomRenderers();
	}
	
	public static void setCustomRenderers()
	{
		mixerRenderType = RenderingRegistry.getNextAvailableRenderId();
		cauldronRenderType = 24;
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCauldron.class, new CauldronRenderer());
	}
	
	@Override
	public World getClientWorld()
	{
		return Minecraft.getMinecraft().thePlayer.worldObj;
	}
}
