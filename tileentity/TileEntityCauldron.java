package clashsoft.mods.morepotions.tileentity;

import java.util.ArrayList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.potion.type.IPotionType;
import clashsoft.brewingapi.potion.type.PotionBase;
import clashsoft.brewingapi.potion.type.PotionType;
import clashsoft.cslib.minecraft.lang.I18n;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

public class TileEntityCauldron extends TileEntity
{
	public static final String	CHANNEL	= "MPMCauldron";
	
	public List<IPotionType>	potionTypes;
	
	public ItemStack			output;
	public int					color;
	
	public TileEntityCauldron()
	{
		this.potionTypes = new ArrayList();
	}
	
	public boolean isItemValid(ItemStack stack)
	{
		if (stack != null)
		{
			if (stack.getItem() != Items.gunpowder && PotionType.getFromIngredient(stack) != null)
			{
				return true;
			}
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
				for (IPotionType potionType : this.potionTypes)
				{
					potionType.onDiluted();
				}
				out = new ChatComponentTranslation("cauldron.effects.durations.decrease");
			}
			else
			{
				out = new ChatComponentTranslation("cauldron.addwater");
			}
		}
		else if (ingredient.getItem() == Items.glowstone_dust && !this.isWater()) // Improving
		{
			for (int i = 0; i < this.potionTypes.size(); i++)
			{
				IPotionType potionType = this.potionTypes.get(i);
				this.potionTypes.set(i, potionType.onImproved());
			}
			out = new ChatComponentTranslation("cauldron.effects.amplifiers.increase");
		}
		else if (ingredient.getItem() == Items.redstone && !this.isWater()) // Extending
		{
			for (int i = 0; i < this.potionTypes.size(); i++)
			{
				IPotionType potionType = this.potionTypes.get(i);
				this.potionTypes.set(i, potionType.onExtended());
			}
			out = new ChatComponentTranslation("cauldron.effects.durations.increase");
		}
		else if (ingredient.getItem() == Items.fermented_spider_eye && !this.isWater()) // Inverting
		{
			for (int i = 0; i < this.potionTypes.size(); i++)
			{
				IPotionType potionType = this.potionTypes.get(i);
				this.potionTypes.set(i, potionType.onInverted());
			}
			out = new ChatComponentTranslation("cauldron.effects.invert");
		}
		else if (this.isWater()) // Other Base Ingredients
		{
			PotionBase base = PotionBase.getFromIngredient(ingredient);
			if (base != null)
			{
				this.setBaseBrewing(base);
				out = new ChatComponentTranslation("cauldron.effects.add.base", base.getName());
			}
		}
		else
		// Normal ingredients
		{
			IPotionType potionType = PotionType.getFromIngredient(ingredient);
			if (this.potionTypes.size() > 0 && potionType != null)
			{
				boolean contains = this.potionTypes.contains(potionType);
				IPotionType stackBase = this.potionTypes.get(0);
				PotionBase requiredBase = potionType.getBase();
				if (requiredBase != null && stackBase != null && requiredBase.matches((PotionBase) stackBase) && !contains)
				{
					this.potionTypes.add(potionType);
					if (potionType.getEffect() != null)
					{
						out = new ChatComponentTranslation("cauldron.effects.add", I18n.getString(potionType.getEffectName()));
					}
				}
				else if (contains)
				{
					out = new ChatComponentTranslation("cauldron.failed.existing");
				}
				else if (requiredBase != null)
				{
					out = new ChatComponentTranslation("cauldron.failed.wrongbase", requiredBase.getName());
				}
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
	
	public void updateOutput()
	{
		this.output = this.brew(false);
		this.color = this.output.getItem().getColorFromItemStack(this.output, 0);
		this.sync();
	}
	
	public void sync()
	{
		if (this.worldObj != null && !this.worldObj.isRemote)
		{
			MorePotionsMod.instance.netHandler.syncCauldron(this);
		}
	}
	
	private ItemStack brew(boolean removeDuplicates)
	{
		if (this.isWater())
			return new ItemStack(BrewingAPI.potion2, 1, 0);
		
		ItemStack is = new ItemStack(BrewingAPI.potion2, 1, 1);
		this.potionTypes = removeDuplicates ? PotionType.removeDuplicates(this.potionTypes) : this.potionTypes;
		
		if (this.potionTypes.size() == 1)
		{
			this.potionTypes.get(0).apply(is);
		}
		else
		{
			for (int i = 1; i < this.potionTypes.size(); i++)
			{
				this.potionTypes.get(i).apply(is);
			}
		}
		
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
		
		if (nbt.hasKey("PotionTypes"))
		{
			List result = new ArrayList();
			NBTTagList tagList = nbt.getTagList("PotionTypes", Constants.NBT.TAG_COMPOUND);
			
			for (int i = 0; i < tagList.tagCount(); ++i)
			{
				NBTTagCompound brewingCompound = tagList.getCompoundTagAt(i);
				IPotionType potionType = PotionType.getFromNBT(brewingCompound);
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
		
		NBTTagList tagList = new NBTTagList();
		for (IPotionType potionType : this.potionTypes)
		{
			NBTTagCompound brewingCompound = new NBTTagCompound();
			potionType.writeToNBT(brewingCompound);
			tagList.appendTag(brewingCompound);
		}
		nbt.setTag("PotionTypes", tagList);
	}
}
