package clashsoft.mods.morepotions.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import clashsoft.cslib.util.CSLog;
import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MPMPacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
	{
		if (TileEntityCauldron.CHANNEL.equals(packet.channel))
		{
			World world = MorePotionsMod.proxy.getClientWorld();
			
			if (world != null)
			{
				ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
				DataInputStream dis = new DataInputStream(bis);
				
				try
				{
					int xCoord = dis.readInt();
					int yCoord = dis.readInt();
					int zCoord = dis.readInt();
					int color = dis.readInt();
					ItemStack stack = Packet.readItemStack(dis);
					
					TileEntity te = world.getBlockTileEntity(xCoord, yCoord, zCoord);
					if (te instanceof TileEntityCauldron)
					{
						TileEntityCauldron tec = (TileEntityCauldron) te;
						tec.color = color;
						tec.output = stack;
					}
				}
				catch (IOException ex)
				{
					CSLog.error(ex);
				}
			}
		}
	}
}