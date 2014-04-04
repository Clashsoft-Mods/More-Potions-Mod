package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.network.CSNetHandler;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

public class MPMNetHandler extends CSNetHandler
{
	public MPMNetHandler()
	{
		super("MPM");
		this.registerPacket(PacketCauldronData.class);
		this.registerPacket(PacketGreenThumb.class);
		this.registerPacket(PacketProjectile.class);
	}
	
	public void syncCauldron(TileEntityCauldron cauldron)
	{
		if (!cauldron.getWorldObj().isRemote)
		{
			PacketCauldronData data = new PacketCauldronData(cauldron.getWorldObj(), cauldron.xCoord, cauldron.yCoord, cauldron.zCoord, cauldron.color, cauldron.output);
			this.sendToAll(data);
		}
	}
}