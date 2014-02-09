package clashsoft.mods.morepotions.tileentity;

import java.util.ArrayList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.brewing.PotionBase;
import clashsoft.brewingapi.brewing.PotionType;
import clashsoft.cslib.minecraft.lang.I18n;
import clashsoft.mods.morepotions.common.MPMPacketHandler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

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
	
	public boolean isItemValid(ItemStack stack)
	{
		if (stack != null)
		{
			if (PotionType.getPotionTypeFromIngredient(stack) != null && stack.getItem() != Items.gunpowder)
				return true;
		}
		return false;
	}
	
	public IChatComponent addIngredient(ItemStack ingredient)
	{
		IChatComponent out = null;
		if (ingredient.getItem() == Items.water_bucket)
		{
			if (!this.isWater())
			{
				for (PotionType potionType : this.potionTypes)
				{
					if (potionType.hasEffect())
						potionType.setEffect(new PotionEffect(potionType.getEffect().getPotionID(), Math.round(potionType.getEffect().getDuration() * 0.6F), Math.round(potionType.getEffect().getAmplifier() * 0.8F)));
				}
				out = new ChatComponentTranslation("cauldron.effects.durations.decrease");
			}
			else
				out = new ChatComponentTranslation("cauldron.addwater");
		}
		else if (ingredient.getItem() == Items.glowstone_dust && !this.isWater()) // Improving
		{
			for (int i = 0; i < this.potionTypes.size(); i++)
			{
				PotionType potionType = this.potionTypes.get(i);
				this.potionTypes.set(i, potionType.onImproved());
			}
			out = new ChatComponentTranslation("cauldron.effects.amplifiers.increase");
		}
		else if (ingredient.getItem() == Items.redstone && !this.isWater()) // Extending
		{
			for (int i = 0; i < this.potionTypes.size(); i++)
			{
				PotionType potionType = this.potionTypes.get(i);
				this.potionTypes.set(i, potionType.onExtended());
			}
			out = new ChatComponentTranslation("cauldron.effects.durations.increase");
		}
		else if (ingredient.getItem() == Items.fermented_spider_eye && !this.isWater()) // Inverting
		{
			for (int i = 0; i < this.potionTypes.size(); i++)
			{
				PotionType potionType = this.potionTypes.get(i);
				this.potionTypes.set(i, potionType.onInverted());
			}
			out = new ChatComponentTranslation("cauldron.effects.invert");
		}
		else if (this.isWater()) // Other Base Ingredients
		{
			PotionBase base = PotionBase.getBrewingBaseFromIngredient(ingredient);
			if (base != null)
			{
				this.setBaseBrewing(base);
				out = new ChatComponentTranslation("cauldron.effects.add.base", base.basename);
			}
		}
		else
		// Normal ingredients
		{
			PotionType potionType = PotionType.getPotionTypeFromIngredient(ingredient);
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
						out = new ChatComponentTranslation("cauldron.effects.add", I18n.getString(potionType.getEffectName()));
					}
				}
				else if (contains)
					out = new ChatComponentTranslation("cauldron.failed.existing");
				else if (requiredBase != null)
					out = new ChatComponentTranslation("cauldron.failed.wrongbase", requiredBase.basename);
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
		if (this.worldObj != null && !this.worldObj.isRemote)
		{
			MPMPacketHandler.instance.syncCauldron(this);
		}
	}
	
	private ItemStack brew(boolean removeDuplicates)
	{
		if (this.isWater())
			return new ItemStack(BrewingAPI.potion2, 1, 0);
		
		ItemStack is = new ItemStack(BrewingAPI.potion2, 1, 1);
		this.potionTypes = removeDuplicates ? (List<PotionType>) PotionType.removeDuplicates(this.potionTypes) : this.potionTypes;
		
		if (this.potionTypes.size() == 1)
			this.potionTypes.get(0).addPotionTypeToItemStack(is);
		else
			for (int i = 1; i < this.potionTypes.size(); i++)
				this.potionTypes.get(i).addPotionTypeToItemStack(is);
		
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
			NBTTagList tagList = nbt.getTagList("PotionType", Constants.NBT.TAG_COMPOUND);
			
			for (int i = 0; i < tagList.tagCount(); ++i)
			{
				NBTTagCompound brewingCompound = tagList.getCompoundTagAt(i);
				PotionType potionType = PotionType.getPotionTypeFromNBT(brewingCompound);
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
		{
			nbt.setTag("PotionType", new NBTTagList());
		}
		
		NBTTagList tagList = (NBTTagList) nbt.getTag("PotionType");
		for (PotionType potionType : this.potionTypes)
		{
			NBTTagCompound brewingCompound = new NBTTagCompound();
			potionType.writeToNBT(brewingCompound);
			tagList.appendTag(brewingCompound);
		}
	}
}
