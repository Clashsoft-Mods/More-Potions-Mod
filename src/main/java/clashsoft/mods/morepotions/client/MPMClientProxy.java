package clashsoft.mods.morepotions.client;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.client.gui.GuiMixer;
import clashsoft.mods.morepotions.client.gui.GuiUnbrewingStand;
import clashsoft.mods.morepotions.client.renderer.block.RenderBlockCauldron;
import clashsoft.mods.morepotions.client.renderer.tileentity.CauldronRenderer;
import clashsoft.mods.morepotions.common.MPMProxy;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MPMClientProxy extends MPMProxy
{
	public static int	mixerRenderType;
	public static int	cauldronRenderType;
	public static int	splashpotioncolor;
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == MorePotionsMod.mixerTileEntityID)
		{
			return new GuiMixer(player, (TileEntityMixer) world.getTileEntity(x, y, z));
		}
		else if (ID == MorePotionsMod.unbrewingStandTileEntityID)
		{
			return new GuiUnbrewingStand(player, (TileEntityUnbrewingStand) world.getTileEntity(x, y, z));
		}
		return null;
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		mixerRenderType = RenderingRegistry.getNextAvailableRenderId();
		cauldronRenderType = RenderingRegistry.getNextAvailableRenderId();
		
		RenderingRegistry.registerBlockHandler(cauldronRenderType, new RenderBlockCauldron());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCauldron.class, new CauldronRenderer());
	}
	
	@Override
	public World getClientWorld()
	{
		return Minecraft.getMinecraft().thePlayer.worldObj;
	}
}
