package clashsoft.mods.morepotions.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.BrewingLoader;
import clashsoft.brewingapi.brewing.Brewing;
import clashsoft.brewingapi.brewing.BrewingBase;
import clashsoft.mods.morepotions.MorePotionsMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public class TileEntityCauldron extends TileEntity
{
	public List<Brewing> brewings;

	public TileEntityCauldron()
	{
		brewings = new LinkedList<Brewing>();
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		if (par1ItemStack != null)
		{
			if (Item.itemsList[par1ItemStack.itemID].isPotionIngredient() || Brewing.getBrewingFromIngredient(par1ItemStack) != null && par1ItemStack.getItem() != Item.gunpowder)
			{
				return true;
			}
		}
		return false;
	}

	public String addIngredient(ItemStack ingredient)
	{
		if (ingredient.getItem() == Item.bucketWater)
		{
			if (!water())
			{
				for (Brewing b : brewings)
				{
					if (b.getEffect() != null)
					{
						b.setEffect(new PotionEffect(b.getEffect().getPotionID(), Math.round(b.getEffect().getDuration() * 0.6F), Math.round(b.getEffect().getAmplifier() * 0.8F)));
					}
				}
				return "Effects thinned";
			}
			return "Cauldron filled with water";
		}
		else if (ingredient.getItem() == Item.glowstone && !water()) //Improving
		{
			for (int var3 = 0; var3 < brewings.size(); var3++)
			{
				Brewing brewing = brewings.get(var3);
				if (brewing != BrewingLoader.awkward)
				{
					brewings.set(var3, brewing.onImproved());
				}
			}
			return "Effect amplifiers increased";
		}
		else if (ingredient.getItem() == Item.redstone && !water()) //Extension
		{
			for (int var3 = 0; var3 < brewings.size(); var3++)
			{
				Brewing brewing = brewings.get(var3);
				if (brewing != BrewingLoader.awkward)
				{
					brewings.set(var3, brewing.onExtended());
				}
			}
			return "Effect durations extended";
		}
		else if (ingredient.getItem() == Item.fermentedSpiderEye && !water()) //Opposites
		{
			for (int var3 = 0; var3 < brewings.size(); var3++)
			{
				Brewing brewing = brewings.get(var3);
				brewing = brewing.getOpposite() != null ? brewing.getOpposite() : brewing;
				brewing.setOpposite(null);
				brewings.set(var3, brewing);
			}
			return "Effects inverted";
		}
		else if (water()) //Other Base Ingredients
		{
			BrewingBase base = BrewingBase.getBrewingBaseFromIngredient(ingredient);
			if (base != null)
			{
				setBaseBrewing(base);
				return "Base Brewing set: \'" + base.basename + "\'";
			}
		}
		else
		{
			Brewing b = Brewing.getBrewingFromIngredient(ingredient);
			if (brewings.size() > 0 && b != null)
			{
				boolean contains = brewings.contains(b);
				Brewing stackBase = brewings.get(0);
				BrewingBase requiredBase = b.getBase();
				if (requiredBase != null && stackBase != null && ((BrewingBase)stackBase).basename == requiredBase.basename && !contains)
				{
					brewings.add(b);
					if (b.getEffect() != null)
					{
						return "Effect added: \'" + StatCollector.translateToLocal(b.getEffect().getEffectName()) + "\'";
					}
				}
				if (contains)
					return "Unable to add Effect, this Effect has already been added.";
				if (requiredBase != null)
					return "Unable to add Effect, required base \'" + requiredBase.basename + "\' is not found.";
			}
		}
		return "";
	}

	public void setBaseBrewing(BrewingBase base)
	{
		if (brewings.size() != 0)
		{
			brewings.set(0, base);
		}
		else
		{
			brewings.add(base);
		}
	}
	
	public boolean water()
	{
		return brewings.size() <= 0;
	}

	public ItemStack brew()
	{
		if (water())
		{
			return new ItemStack(BrewingAPI.potion2, 1, 0);
		}
		ItemStack is = new ItemStack(BrewingAPI.potion2, 1, 1);
		if (brewings.size() == 1)
		{
			return brewings.get(0).addBrewingToItemStack(is);
		}
		this.brewings = (List<Brewing>) Brewing.removeDuplicates(brewings);
		for (Brewing b : this.brewings)
		{
			if (b.getEffect() != null)
			{
				b.addBrewingToItemStack(is);
			}
		}
		return is;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		if (par1NBTTagCompound.hasKey("Brewing"))
		{
			List var6 = new ArrayList();
			NBTTagList var3 = par1NBTTagCompound.getTagList("Brewing");
			boolean var2 = true;

			for (int var4 = 0; var4 < var3.tagCount(); ++var4)
			{
				NBTTagCompound var5 = (NBTTagCompound)var3.tagAt(var4);
				Brewing b = Brewing.readFromNBT(var5);
				var6.add(b);
			}
			brewings = var6;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);

		if (!par1NBTTagCompound.hasKey("Brewing"))
		{
			par1NBTTagCompound.setTag("Brewing", new NBTTagList("Brewing"));
		}
		NBTTagList var2 = (NBTTagList)par1NBTTagCompound.getTag("Brewing");
		for (Brewing b : brewings)
		{
			var2.appendTag(b instanceof BrewingBase ? ((BrewingBase)b).createNBT() : b.createNBT());
		}
	}

}
