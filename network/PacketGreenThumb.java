package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.item.CSStacks;
import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.PacketBuffer;

public class PacketGreenThumb extends CSPacket
{
	public int x;
	public int y;
	public int z;
	public int face;
	
	public PacketGreenThumb()
	{
	}
	
	public PacketGreenThumb(int x, int y, int z, int face)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
	}

	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.face);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.face = buf.readInt();
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
	}
	
	@Override
	public void handleServer(EntityPlayer player)
	{
		if (player.getCurrentEquippedItem() == null)
		{
			int amplifier = player.getActivePotionEffect(MorePotionsMod.greenThumb).getAmplifier();
			
			for (int i = 0; i < amplifier + 1; i++)
			{
				Items.dye.onItemUse(CSStacks.bonemeal, player, player.worldObj, this.x, this.y, this.z, this.face, 0F, 0F, 0F);
			}
		}
	}
	
}
