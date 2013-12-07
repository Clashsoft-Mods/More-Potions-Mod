package clashsoft.mods.morepotions.client.gui;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.inventory.ContainerUnbrewingStand;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiUnbrewingStand extends GuiContainer
{
	private TileEntityUnbrewingStand			unbrewingStand;
	public static ResourceLocation	unbrewingStandGUI	= new ResourceLocation("gui/unbrewingstand_gui.png");
	
	public GuiUnbrewingStand(InventoryPlayer inventory, TileEntityUnbrewingStand unbrewingStand)
	{
		super(new ContainerUnbrewingStand(inventory, unbrewingStand));
		this.unbrewingStand = unbrewingStand;
		unbrewingStand.player = inventory.player;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String title = StatCollector.translateToLocal("tile.unbrewingStand.name");
		this.fontRenderer.drawString(title, (this.xSize - this.fontRenderer.getStringWidth(title)) / 2, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}
	
	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float fpt, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(unbrewingStandGUI);
		int centerX = (this.width - this.xSize) / 2;
		int centerY = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(centerX, centerY, 0, 0, this.xSize, this.ySize);
		int unbrewTime = this.unbrewingStand.getUnbrewTime();
		
		if (unbrewTime > 0)
		{
			int scaledUnbrewTime = (int) (28.0F * (1.0F - (float) unbrewTime / TileEntityUnbrewingStand.maxUnbrewTime));
			
			if (scaledUnbrewTime > 0)
			{
				this.drawTexturedModalRect(centerX + 97, centerY + 16, 176, 0, 9, scaledUnbrewTime);
			}
			
			int bubbleIndex = unbrewTime / 2 % 7;
			
			switch (bubbleIndex)
			{
			case 0:
				scaledUnbrewTime = 29;
				break;
			case 1:
				scaledUnbrewTime = 24;
				break;
			case 2:
				scaledUnbrewTime = 20;
				break;
			case 3:
				scaledUnbrewTime = 16;
				break;
			case 4:
				scaledUnbrewTime = 11;
				break;
			case 5:
				scaledUnbrewTime = 6;
				break;
			case 6:
				scaledUnbrewTime = 0;
			}
			
			if (scaledUnbrewTime > 0)
			{
				this.drawTexturedModalRect(centerX + 65, centerY + 14 + 29 - scaledUnbrewTime, 185, 29 - scaledUnbrewTime, 12, scaledUnbrewTime);
			}
		}
	}
}
