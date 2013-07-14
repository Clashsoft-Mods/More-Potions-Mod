package clashsoft.mods.morepotions;

import java.util.Random;

import clashsoft.brewingapi.entity.EntityPotion2;
import clashsoft.brewingapi.entity.RenderPotion2;
import clashsoft.brewingapi.item.ItemPotion2;
import clashsoft.mods.morepotions.tileentity.CauldronRenderer;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ClientProxy extends CommonProxy
{
	public static int mixerRenderType;
	public static int cauldronRenderType;
	public static int splashpotioncolor;
	
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
