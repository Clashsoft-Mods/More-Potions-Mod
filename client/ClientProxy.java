package clashsoft.mods.morepotions.client;

import clashsoft.mods.morepotions.client.renderer.tileentity.CauldronRenderer;
import clashsoft.mods.morepotions.common.CommonProxy;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
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
		cauldronRenderType = RenderingRegistry.getNextAvailableRenderId();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCauldron.class, new CauldronRenderer());
	}
}
