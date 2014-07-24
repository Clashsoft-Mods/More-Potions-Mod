package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

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
		writeWorld(buf, this.world);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.color);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.world = readWorld(buf);
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.color = buf.readInt();
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
		TileEntity te = player.worldObj.getTileEntity(this.x, this.y, this.z);
		if (te instanceof TileEntityCauldron)
		{
			TileEntityCauldron cauldron = (TileEntityCauldron) te;
			cauldron.color = this.color;
		}
	}
	
	@Override
	public void handleServer(EntityPlayerMP player)
	{
	}
}