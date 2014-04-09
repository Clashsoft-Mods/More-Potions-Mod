package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.network.CSNetHandler;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

import net.minecraft.world.World;

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
		World world = cauldron.getWorldObj();
		if (!world.isRemote)
		{
			PacketCauldronData data = new PacketCauldronData(world, cauldron.xCoord, cauldron.yCoord, cauldron.zCoord, cauldron.color);
			this.sendToAll(data);
		}
	}
}