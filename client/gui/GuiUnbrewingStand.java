package clashsoft.mods.morepotions.client.gui;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.inventory.ContainerUnbrewingStand;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiUnbrewingStand extends GuiContainer
{
	public TileEntityUnbrewingStand	unbrewingStand;
	public static ResourceLocation	unbrewingTexture	= new ResourceLocation("morepotions", "textures/gui/unbrewing_stand.png");
	
	public GuiUnbrewingStand(EntityPlayer player, TileEntityUnbrewingStand unbrewingStand)
	{
		super(new ContainerUnbrewingStand(player, unbrewingStand));
		this.unbrewingStand = unbrewingStand;
		this.unbrewingStand.player = player;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String title = StatCollector.translateToLocal("tile.unbrewing_stand.name");
		this.fontRendererObj.drawString(title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 6, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 94, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(unbrewingTexture);
		int centerX = (this.width - this.xSize) / 2;
		int centerY = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(centerX, centerY, 0, 0, this.xSize, this.ySize);
		int unbrewTime = this.unbrewingStand.getUnbrewTime();
		
		if (unbrewTime > 0)
		{
			int scaledUnbrewTime = (int) (28.0F * (1.0F - (float) unbrewTime / TileEntityUnbrewingStand.maxUnbrewTime));
			
			if (scaledUnbrewTime > 0)
			{
				this.drawTexturedModalRect(centerX + 35, centerY + 7, 176, 0, 9, scaledUnbrewTime);
			}
		}
	}
}
