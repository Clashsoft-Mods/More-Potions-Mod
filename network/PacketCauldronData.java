package clashsoft.mods.morepotions.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketCauldronData extends CSPacket
{
	public World		world;
	public int			x;
	public int			y;
	public int			z;
	
	public int			color;
	public ItemStack	output;
	
	public PacketCauldronData(World world, int x, int y, int z, int color, ItemStack output)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.output = output;
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		this.writeWorld(buf, this.world);
		buf.writeInt(this.x);
		buf.writeInt(this.y);
		buf.writeInt(this.z);
		buf.writeInt(this.color);
		this.writeItemStack(buf, this.output);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.world = this.readWorld(buf);
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.color = buf.readInt();
		this.output = this.readItemStack(buf);
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
		TileEntity te = this.world.getTileEntity(x, y, z);
		if (te instanceof TileEntityCauldron)
		{
			TileEntityCauldron cauldron = (TileEntityCauldron) te;
			cauldron.color = color;
			cauldron.output = output;
		}
	}
	
	@Override
	public void handleServer(EntityPlayer player)
	{
	}
}