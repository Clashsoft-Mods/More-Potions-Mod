package clashsoft.mods.morepotions.lib;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import clashsoft.brewingapi.tileentity.TileEntityBrewingStand2;
import clashsoft.mods.morepotions.MorePotionsMod;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		byte typ = dat.readByte();
		boolean hasStacks = dat.readByte() != 0;
		int[] items = new int[0];
		if (hasStacks)
		{
			items = new int[24];
			for (int i = 0; i < items.length; i++)
			{
				items[i] = dat.readInt();
			}
		}
		World world = MorePotionsMod.proxy.getClientWorld();
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TileEntityBrewingStand2)
		{
			TileEntityBrewingStand2 icte = (TileEntityBrewingStand2) te;
			icte.handlePacketData(typ, items);
		}
	}
	
	public static Packet getPacket(TileEntity tileEntity)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
		DataOutputStream dos = new DataOutputStream(bos);
		int x = tileEntity.xCoord;
		int y = tileEntity.yCoord;
		int z = tileEntity.zCoord;
		int[] items = null;
		boolean hasStacks = (items != null);
		try
		{
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(z);
			dos.writeByte(hasStacks ? 1 : 0);
			if (hasStacks)
			{
				for (int i = 0; i < 24; i++)
				{
					// dos.writeInt(items[i]);
				}
			}
		}
		catch (IOException e)
		{
		}
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "MorePotions";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = true;
		return pkt;
	}
}
