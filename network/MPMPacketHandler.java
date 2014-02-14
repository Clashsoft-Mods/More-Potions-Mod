package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.network.CSNetHandler;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

public class MPMPacketHandler extends CSNetHandler
{
	public static MPMPacketHandler	instance	= new MPMPacketHandler();
	
	public MPMPacketHandler()
	{
		super("MPM");
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