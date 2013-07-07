package clashsoft.mods.morepotions.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.brewing.Brewing;
import clashsoft.mods.morepotions.brewing.BrewingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class TileEntityCauldron extends TileEntity
{
	public boolean water;
	public List<Brewing> brewings = new LinkedList<Brewing>();
	
	public TileEntityCauldron()
	{
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
	
	public void addIngredient(ItemStack ingredient)
	{
		if (ingredient.getItem() == Item.bucketWater)
		{
			if (!water && brewings.size() > 0)
			{
				for (Brewing b : brewings)
				{
					if (b.getEffect() != null)
					{
						b.setEffect(new PotionEffect(b.getEffect().getPotionID(), Math.round(b.getEffect().getDuration() * 0.6F), Math.round(b.getEffect().getAmplifier() * 0.8F)));
					}
				}
			}
			else
			{
				water = true;
			}
		}
		else if (ingredient.getItem() == Item.lightStoneDust)
		{
			if (water)
			{
				if (brewings.size() != 0)
				{
					brewings.set(0, Brewing.thick);
				}
				else
				{
					brewings.add(Brewing.thick);
				}
			}
			else
			{
				for (int var3 = 0; var3 < brewings.size(); var3++)
				{
					Brewing brewing = brewings.get(var3);
					if (brewing != Brewing.awkward)
					{
						brewings.set(var3, brewing.onImproved());
					}
				}
			}
			water = false;
		}
		else if (ingredient.getItem() == Item.redstone)
		{
			if (water)
			{
				if (brewings.size() != 0)
				{
					brewings.set(0, Brewing.thin);
				}
				else
				{
					brewings.add(Brewing.thin);
				}
			}
			else
			{
				for (int var3 = 0; var3 < brewings.size(); var3++)
				{
					Brewing brewing = brewings.get(var3);
					if (brewing != Brewing.awkward)
					{
						brewings.set(var3, brewing.onExtended());
					}
				}
			}
			water = false;
		}
		else if (ingredient.getItem() == Item.fermentedSpiderEye)
		{
			if (water)
			{
				if (brewings.size() != 0)
				{
					brewings.set(0, Brewing.getBrewingFromIngredient(ingredient));
				}
				else
				{
					brewings.add(Brewing.getBrewingFromItemStack(ingredient));
				}
			}
			else
			{
				for (int var3 = 0; var3 < brewings.size(); var3++)
				{
					Brewing brewing = brewings.get(var3);
					brewing = brewing.getOpposite() != null ? brewing.getOpposite() : brewing;
					brewing.setOpposite(null);
					brewings.set(var3, brewing);
				}
			}
			water = false;
		}
		else if (ingredient.getItem() == Item.netherStalkSeeds)
		{
			if (brewings.size() != 0)
			{
				brewings.set(0, Brewing.awkward);
			}
			else
			{
				brewings.add(Brewing.awkward);
			}
			water = false;
		}
		else if (water)
		{
			if (BrewingBase.getBrewingBaseFromIngredient(ingredient) != null)
			{
				brewings.add(BrewingBase.getBrewingBaseFromIngredient(ingredient));
			}
			water = false;
		}
		else
		{
			if (brewings.size() > 0 && Brewing.getBrewingFromIngredient(ingredient) != null)
			{
				Brewing stackBase = brewings.get(0);
				Brewing requiredBase = Brewing.getBrewingFromIngredient(ingredient).getBase();
				if (((BrewingBase)stackBase).basename == ((BrewingBase)requiredBase).basename)
				{
					brewings.add(Brewing.getBrewingFromIngredient(ingredient));
				}
				water = false;
			}
		}
	}
	
	public ItemStack brew()
	{
		if (water)
		{
			return new ItemStack(MorePotionsMod.potion2, 1, 0);
		}
		ItemStack is = new ItemStack(MorePotionsMod.potion2, 1, 1);
		if (brewings.size() == 1)
		{
			return brewings.get(0).addBrewingToItemStack(is);
		}
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
		water = par1NBTTagCompound.getBoolean("Water");
		
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
		par1NBTTagCompound.setBoolean("Water", water);
		
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
