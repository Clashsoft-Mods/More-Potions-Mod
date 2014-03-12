package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.network.CSNetHandler;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

public class MPMPacketHandler extends CSNetHandler
{
	public MPMPacketHandler()
	{
		super("MPM");
		this.registerPacket(PacketCauldronData.class);
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