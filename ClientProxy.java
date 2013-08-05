package clashsoft.mods.morepotions;

import clashsoft.mods.morepotions.tileentity.CauldronRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public static int mixerRenderType;
	public static int cauldronRenderType;
	public static int splashpotioncolor;
	
	@Override
	public void registerRenderers()
	{
		setCustomRenderers();
	}
	
	public static void setCustomRenderers()
    {
        mixerRenderType = RenderingRegistry.getNextAvailableRenderId();
        cauldronRenderType = RenderingRegistry.getNextAvailableRenderId();
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCauldron.class, new CauldronRenderer());
        RenderingRegistry.registerBlockHandler(cauldronRenderType, new CauldronRenderer());
    }
}
