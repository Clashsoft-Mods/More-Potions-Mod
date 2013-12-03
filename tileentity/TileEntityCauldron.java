package clashsoft.mods.morepotions.tileentity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.brewing.PotionType;
import clashsoft.brewingapi.brewing.PotionBase;
import clashsoft.brewingapi.brewing.PotionList;
import cpw.mods.fml.common.network.PacketDispatcher;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCauldron extends TileEntity
{
	public static final String	CHANNEL	= "MPMCauldron";
	
	public List<PotionType>		potionTypes;
	
	public ItemStack			output;
	public int					color;
	
	public TileEntityCauldron()
	{
		this.potionTypes = new ArrayList<PotionType>();
	}
	
	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	public boolean isItemValid(ItemStack stack)
	{
		if (stack != null)
		{
			if (Item.itemsList[stack.itemID].isPotionIngredient() || PotionType.getBrewingFromIngredient(stack) != null && stack.itemID != Item.gunpowder.itemID)
				return true;
		}
		return false;
	}
	
	public String addIngredient(ItemStack ingredient)
	{
		String out = "";
		if (ingredient.getItem() == Item.bucketWater)
		{
			if (!this.isWater())
			{
				for (PotionType potionType : this.potionTypes)
				{
					if (potionType.hasEffect())
						potionType.setEffect(new PotionEffect(potionType.getEffect().getPotionID(), Math.round(potionType.getEffect().getDuration() * 0.6F), Math.round(potionType.getEffect().getAmplifier() * 0.8F)));
				}
				out = I18n.getString("cauldron.effects.durations.decrease");
			}
			else
				out = I18n.getString("cauldron.addwater");
		}
		else if (ingredient.getItem() == Item.glowstone && !this.isWater()) // Improving
		{
			for (int var3 = 0; var3 < this.potionTypes.size(); var3++)
			{
				PotionType potionType = this.potionTypes.get(var3);
				if (potionType != PotionList.awkward)
				{
					this.potionTypes.set(var3, potionType.onImproved());
				}
			}
			out = I18n.getString("cauldron.effects.amplifiers.increase");
		}
		else if (ingredient.getItem() == Item.redstone && !this.isWater()) // Extending
		{
			for (int var3 = 0; var3 < this.potionTypes.size(); var3++)
			{
				PotionType potionType = this.potionTypes.get(var3);
				if (potionType != PotionList.awkward)
				{
					this.potionTypes.set(var3, potionType.onExtended());
				}
			}
			out = I18n.getString("cauldron.effects.durations.increase");
		}
		else if (ingredient.getItem() == Item.fermentedSpiderEye && !this.isWater()) // Inverting
		{
			for (int index = 0; index < this.potionTypes.size(); index++)
			{
				PotionType potionType = this.potionTypes.get(index);
				potionType = potionType.getInverted() != null ? potionType.getInverted() : potionType;
				potionType.setOpposite(null);
				this.potionTypes.set(index, potionType);
			}
			out = I18n.getString("cauldron.effects.invert");
		}
		else if (this.isWater()) // Other Base Ingredients
		{
			PotionBase base = PotionBase.getBrewingBaseFromIngredient(ingredient);
			if (base != null)
			{
				this.setBaseBrewing(base);
				out = I18n.getStringParams("cauldron.effects.add.base", base.basename);
			}
		}
		else
		// Normal ingredients
		{
			PotionType potionType = PotionType.getBrewingFromIngredient(ingredient);
			if (this.potionTypes.size() > 0 && potionType != null)
			{
				boolean contains = this.potionTypes.contains(potionType);
				PotionType stackBase = this.potionTypes.get(0);
				PotionBase requiredBase = potionType.getBase();
				if (requiredBase != null && stackBase != null && ((PotionBase) stackBase).basename == requiredBase.basename && !contains)
				{
					this.potionTypes.add(potionType);
					if (potionType.getEffect() != null)
					{
						out = I18n.getStringParams("cauldron.effects.add", I18n.getString(potionType.getEffect().getEffectName()));
					}
				}
				else if (contains)
					out = I18n.getString("cauldron.failed.existing");
				else if (requiredBase != null)
					out = I18n.getStringParams("cauldron.failed.wrongbase", requiredBase.basename);
			}
		}
		
		this.updateOutput();
		
		return out;
	}
	
	public void setBaseBrewing(PotionBase base)
	{
		if (this.potionTypes.size() != 0)
		{
			this.potionTypes.set(0, base);
		}
		else
		{
			this.potionTypes.add(base);
		}
	}
	
	public boolean isWater()
	{
		return this.potionTypes.size() <= 0;
	}
	
	protected void updateOutput()
	{
		this.output = this.brew(false);
		this.color = this.output.getItem().getColorFromItemStack(this.output, 0);
		this.sync();
	}
	
	public void sync()
	{
		if (!this.worldObj.isRemote)
			PacketDispatcher.sendPacketToAllPlayers(this.getDescriptionPacket());
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		Packet250CustomPayload packet = new Packet250CustomPayload();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try
		{
			dos.writeInt(this.xCoord);
			dos.writeInt(this.yCoord);
			dos.writeInt(this.zCoord);
			dos.writeInt(this.color);
			Packet.writeItemStack(this.output, dos);
		}
		catch (IOException ex)
		{
			
		}
		
		packet.channel = CHANNEL;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		
		return packet;
	}
	
	private ItemStack brew(boolean removeDuplicates)
	{
		if (this.isWater())
			return new ItemStack(BrewingAPI.potion2, 1, 0);
		
		ItemStack is = new ItemStack(BrewingAPI.potion2, 1, 1);
		this.potionTypes = removeDuplicates ? (List<PotionType>) PotionType.removeDuplicates(this.potionTypes) : this.potionTypes;
		
		if (this.potionTypes.size() == 1)
			this.potionTypes.get(0).addBrewingToItemStack(is);
		else
			for (int i = 1; i < this.potionTypes.size(); i++)
				this.potionTypes.get(i).addBrewingToItemStack(is);
		
		return is;
	}
	
	public int getColor()
	{
		return this.color;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		if (nbt.hasKey("PotionType"))
		{
			List result = new ArrayList();
			NBTTagList tagList = nbt.getTagList("PotionType");
			
			for (int index = 0; index < tagList.tagCount(); ++index)
			{
				NBTTagCompound brewingCompound = (NBTTagCompound) tagList.tagAt(index);
				PotionType potionType = new PotionType();
				potionType.readFromNBT(brewingCompound);
				result.add(potionType);
			}
			this.potionTypes = result;
		}
		
		this.updateOutput();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		if (!nbt.hasKey("PotionType"))
			nbt.setTag("PotionType", new NBTTagList("PotionType"));
		
		NBTTagList tagList = (NBTTagList) nbt.getTag("PotionType");
		for (PotionType potionType : this.potionTypes)
		{
			NBTTagCompound brewingCompound = new NBTTagCompound();
			potionType.writeToNBT(brewingCompound);
			tagList.appendTag(brewingCompound);
		}
	}
	
}
