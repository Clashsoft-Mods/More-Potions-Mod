package clashsoft.mods.morepotions.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.inventory.ContainerUnbrewingStand;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;

@SideOnly(Side.CLIENT)
public class GuiUnbrewingStand extends GuiContainer
{
	public static ResourceLocation unbrewingstand_gui = new ResourceLocation("morepotionsmod:gui/extractor_gui.png");
	
    private TileEntityUnbrewingStand unbrewingStand;

    public GuiUnbrewingStand(InventoryPlayer par1InventoryPlayer, TileEntityUnbrewingStand par2TileEntityUnbrewingStand)
    {
        super(new ContainerUnbrewingStand(par1InventoryPlayer, par2TileEntityUnbrewingStand));
        this.unbrewingStand = par2TileEntityUnbrewingStand;
        unbrewingStand.thePlayer = par1InventoryPlayer.player;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	String s = this.unbrewingStand.isInvNameLocalized() ? this.unbrewingStand.getInvName() : StatCollector.translateToLocal(this.unbrewingStand.getInvName());
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.func_110577_a(unbrewingstand_gui);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        int var7 = this.unbrewingStand.getBrewTime();

        if (var7 > 0)
        {
            int var8 = (int)(28.0F * (1.0F - (float)var7 / unbrewingStand.getMaxBrewTime()));

            if (var8 > 0)
            {
                this.drawTexturedModalRect(var5 + 97, var6 + 16, 176, 0, 9, var8);
            }

            int var9 = var7 / 2 % 7;

            switch (var9)
            {
                case 0:
                    var8 = 29;
                    break;
                case 1:
                    var8 = 24;
                    break;
                case 2:
                    var8 = 20;
                    break;
                case 3:
                    var8 = 16;
                    break;
                case 4:
                    var8 = 11;
                    break;
                case 5:
                    var8 = 6;
                    break;
                case 6:
                    var8 = 0;
            }

            if (var8 > 0)
            {
                this.drawTexturedModalRect(var5 + 65, var6 + 14 + 29 - var8, 185, 29 - var8, 12, var8);
            }
        }
    }
}
