package clashsoft.mods.morepotions;

import clashsoft.clashsoftapi.CSUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class Potion2 extends Potion {

	private boolean instant;
	
	public Potion2(String par1, boolean par2, int par3, boolean par4, int par5, int par6)
	{
		super(getNextFreeID(), par2, par3);
		this.setPotionName(par1);
		instant = par4;
		this.setIconIndex(par5, par6);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.customEffects);
		return super.getStatusIconIndex();
	}
	
	public boolean isInstant()
	{
		return instant;
	}
	
	public static int getNextFreeID()
	{
		int id = 32;
		for (int i = 0; i < potionTypes.length; i++)
		{
			if (potionTypes[i] == null)
			{
				id = i;
			}
		}
		return id;
	}

}
