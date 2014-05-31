package clashsoft.mods.morepotions.client.gui;

import org.lwjgl.opengl.GL11;

import clashsoft.mods.morepotions.inventory.ContainerMixer;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiMixer extends GuiContainer
{
	private TileEntityMixer			mixer;
	public static ResourceLocation	mixerTexture	= new ResourceLocation("morepotions", "textures/gui/mixer.png");
	
	public GuiMixer(EntityPlayer player, TileEntityMixer mixer)
	{
		super(new ContainerMixer(player, mixer));
		this.mixer = mixer;
		mixer.player = player;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String title = StatCollector.translateToLocal("tile.mixer.name");
		this.fontRendererObj.drawString(title, (this.xSize - this.fontRendererObj.getStringWidth(title)) / 2, 6, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 94, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(mixerTexture);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		int time = this.mixer.getMixTime();
		
		if (time > 0)
		{
			int progress = (int) (28.0F * (1.0F - (float) time / TileEntityMixer.maxTime));
			
			if (progress > 0)
			{
				this.drawTexturedModalRect(x + 96, y + 41, 176, 0, 9, progress);
				this.drawTexturedModalRect(x + 67, y + 41, 176, 0, 9, progress);
			}
			
			int bubbles = time / 2 % 7;
			
			switch (bubbles)
			{
			case 0:
				progress = 29;
				break;
			case 1:
				progress = 24;
				break;
			case 2:
				progress = 20;
				break;
			case 3:
				progress = 16;
				break;
			case 4:
				progress = 11;
				break;
			case 5:
				progress = 6;
				break;
			case 6:
				progress = 0;
			}
			
			if (progress > 0)
			{
				this.drawTexturedModalRect(x + 65, y + 50 - progress, 185, 29 - progress, 12, progress);
			}
		}
	}
}
