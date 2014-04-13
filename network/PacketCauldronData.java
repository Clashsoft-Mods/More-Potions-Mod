package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketCauldronData extends CSPacket
{
	public World	world;
	public int		x;
	public int		y;
	public int		z;
	
	public int		color;
	
	public PacketCauldronData()
	{
	}
	
	public PacketCauldronData(World world, int x, int y, int z, int color)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		this.writeWorld(buf, this.world);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.color);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.world = this.readWorld(buf);
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.color = buf.readInt();
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
		TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(this.x, this.y, this.z);
		if (te instanceof TileEntityCauldron)
		{
			System.out.println("Settings cauldron color of " + te.toString() + " to " + Integer.toHexString(this.color));
			TileEntityCauldron cauldron = (TileEntityCauldron) te;
			cauldron.color = this.color;
		}
	}
	
	@Override
	public void handleServer(EntityPlayerMP player)
	{
	}
}