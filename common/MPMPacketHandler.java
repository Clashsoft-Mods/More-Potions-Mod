package clashsoft.mods.morepotions.common;

import clashsoft.cslib.minecraft.network.CSCodec;
import clashsoft.cslib.minecraft.network.CSMessageHandler;
import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.cslib.minecraft.network.CSPacketHandler;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MPMPacketHandler extends CSPacketHandler
{
	public static MPMPacketHandler	instance	= new MPMPacketHandler();
	
	public MPMPacketHandler()
	{
		super("MPM");
	}
	
	private static class CauldronData extends CSPacket
	{
		public World		world;
		public int			x;
		public int			y;
		public int			z;
		
		public int			color;
		public ItemStack	output;
		
		public CauldronData(World world, int x, int y, int z, int color, ItemStack output)
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
	}
	
	@Override
	public CSCodec createCodec()
	{
		return new CSCodec<CauldronData>()
		{
			@Override
			public void addDiscriminators()
			{
				this.addDiscriminator(0, CauldronData.class);
			}
		};
	}
	
	@Override
	public CSMessageHandler createMessageHandler()
	{
		return new CSMessageHandler<CauldronData>()
		{
			@Override
			public void process(CauldronData msg)
			{
				TileEntity te = msg.world.getTileEntity(msg.x, msg.y, msg.z);
				if (te instanceof TileEntityCauldron)
				{
					TileEntityCauldron cauldron = (TileEntityCauldron) te;
					cauldron.color = msg.color;
					cauldron.output = msg.output;
				}
			}
		};
	}
	
	public void syncCauldron(TileEntityCauldron cauldron)
	{
		if (!cauldron.getWorldObj().isRemote)
		{
			CauldronData data = new CauldronData(cauldron.getWorldObj(), cauldron.xCoord, cauldron.yCoord, cauldron.zCoord, cauldron.color, cauldron.output);
			this.sendPacketToClient(data);
		}
	}
}