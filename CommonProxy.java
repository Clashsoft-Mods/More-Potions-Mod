package clashsoft.mods.morepotions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
    public void registerRenderInformation()
    {

    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == MorePotionsMod.BrewingStand2_ID)
        {
        	return new GuiBrewingStand2(player.inventory, (TileEntityBrewingStand2)world.getBlockTileEntity(x, y, z));
        }
        else if (ID == MorePotionsMod.Mixer_ID)
        {
        	return new GuiMixer(player.inventory, (TileEntityMixer)world.getBlockTileEntity(x, y, z));
        }
        else if (ID == MorePotionsMod.UnbrewingStand_ID)
        {
        	return new GuiUnbrewingStand(player.inventory, (TileEntityUnbrewingStand)world.getBlockTileEntity(x, y, z));
        }
        return null;
    }

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
        else if (te != null && te instanceof TileEntityUnbrewingStand)
        {
        	TileEntityUnbrewingStand ubs = (TileEntityUnbrewingStand) te;
        	return new ContainerUnbrewingStand(player.inventory, ubs);
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
    
    public void registerRenderers() {}

	public void playSplashEffect(World par0World, int par1, int par2, int par3, ItemStack par4ItemStack) {}

}