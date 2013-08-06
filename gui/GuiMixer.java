package clashsoft.mods.morepotions.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.inventory.ContainerMixer;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;

@SideOnly(Side.CLIENT)
public class GuiMixer extends GuiContainer
{
	private TileEntityMixer			mixer;
	public static ResourceLocation	mixer_gui	= new ResourceLocation("gui/mixer_gui.png");
	
	public GuiMixer(InventoryPlayer par1InventoryPlayer, TileEntityMixer par2TileEntityMixxer)
	{
		super(new ContainerMixer(par1InventoryPlayer, par2TileEntityMixxer));
		this.mixer = par2TileEntityMixxer;
		par2TileEntityMixxer.player = par1InventoryPlayer.player;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(StatCollector.translateToLocal("tile.mixer.name"), 60, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.func_110434_K().func_110577_a(mixer_gui);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
		int var7 = this.mixer.getMixTime();
		
		if (var7 > 0)
		{
			int var8 = (int) (28.0F * (1.0F - (float) var7 / TileEntityMixer.maxMixTime));
			
			if (var8 > 0)
			{
				this.drawTexturedModalRect(var5 + 97, var6 + 41, 176, 0, 9, var8);
				this.drawTexturedModalRect(var5 + 68, var6 + 41, 176, 0, 9, var8);
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
				this.drawTexturedModalRect(var5 + 65, var6 + 50 - var8, 185, 29 - var8, 12, var8);
			}
		}
	}
}
