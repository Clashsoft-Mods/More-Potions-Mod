package clashsoft.mods.morepotions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.src.ModLoader;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Class that stores all data that the new potion need and all new potion types
 * @author Clashsoft
 *
 */
public class Brewing
{
	/** List that stores ALL brewings **/
	public static List<Brewing> brewingList = new LinkedList<Brewing>();
	/** List that stores all brewings with good effects **/
	public static List<Brewing> goodEffects = new LinkedList<Brewing>();
	/** List that stores all brewings with bad effects **/
	public static List<Brewing> badEffects = new LinkedList<Brewing>();
	/** List that stores brewings that can be used together with other brewings (e.g. Effect Remover potion is not inside) **/
	public static List<Brewing> combinableEffects = new LinkedList<Brewing>();
	/** List that stores all base brewing (e.g. awkward, mundane, ...) **/
	public static List<BrewingBase> baseBrewings = new LinkedList<BrewingBase>();
	/** List that stores all brewings with effects **/
	public static List<Brewing> effectBrewings = new LinkedList<Brewing>();
	
	/** List that stores ingredient handlers. **/
	public static List<IIngredientHandler> ingredientHandlers = new LinkedList<IIngredientHandler>();
	
	/** Version identifier for NBTs. **/
	public static String NBTVersion = "1.0.1";

	/** The effect **/
	private PotionEffect effect;
	/** Max effect amplifier **/
	private int maxAmplifier;
	/** Max effect duration **/
	private int maxDuration;
	/** Fermented Spider Eye **/
	private Brewing opposite = null;
	/** The ingredient to brew the potion **/
	private ItemStack ingredient = new ItemStack(0, 0, 0);
	/** Used by the random potion **/
	private boolean isRandom = false;
	/** Determines the base that is needed to brew the potion **/
	private BrewingBase base;

	/** Base needed for all potions **/
	public static BrewingBase awkward = new BrewingBase("awkward", new ItemStack(Item.netherStalkSeeds));
	public static BrewingBase mundane = new BrewingBase("mundane", new ItemStack(Block.mushroomBrown));
	public static BrewingBase uninteresting = new BrewingBase("uninteresting", new ItemStack(Item.paper));
	public static BrewingBase bland = new BrewingBase("bland", new ItemStack(Item.melon));
	public static BrewingBase clear = new BrewingBase("clear", new ItemStack(Item.clay));
	public static BrewingBase milky = new BrewingBase("milky", new ItemStack(Block.sapling));
	public static BrewingBase diffuse = new BrewingBase("diffuse", new ItemStack(Item.wheat));
	public static BrewingBase artless = new BrewingBase("artless", new ItemStack(Item.reed));
	/** Needed for some higher tier extendable potions **/
	public static BrewingBase thin = new BrewingBase("thin", new ItemStack(Item.redstone));
	public static BrewingBase flat = new BrewingBase("flat", new ItemStack(Block.waterlily));
	public static BrewingBase bulky = new BrewingBase("bulky", new ItemStack(Block.sand));
	public static BrewingBase bungling = new BrewingBase("bungling", new ItemStack(Block.mushroomRed));
	public static BrewingBase buttered = new BrewingBase("buttered", new ItemStack(Item.dyePowder, 1, 11));
	public static BrewingBase smooth = new BrewingBase("smooth", new ItemStack(Item.seeds));
	public static BrewingBase suave = new BrewingBase("suave", new ItemStack(Item.dyePowder, 1, 3));
	public static BrewingBase debonair = new BrewingBase("debonair", new ItemStack(Item.dyePowder, 1, 2));
	/** Needed for some higher tier improvable potions **/
	public static BrewingBase thick = new BrewingBase("thick", new ItemStack(Item.lightStoneDust));
	public static BrewingBase elegant = new BrewingBase("elegant", new ItemStack(Item.enderPearl));
	public static BrewingBase fancy = new BrewingBase("fancy", new ItemStack(Item.flint));
	public static BrewingBase charming = new BrewingBase("charming", new ItemStack(Block.plantRed));
	/** Needed for potions that change movement **/
	public static BrewingBase dashing = new BrewingBase("dashing", new ItemStack(Item.silk));
	public static BrewingBase refined = new BrewingBase("refined", new ItemStack(Item.slimeBall));
	public static BrewingBase cordial = new BrewingBase("cordial", new ItemStack(Item.dyePowder, 1, 15));
	public static BrewingBase sparkling = new BrewingBase("sparkling", new ItemStack(Item.goldNugget));
	public static BrewingBase potent = new BrewingBase("potent", new ItemStack(Item.blazePowder));
	public static BrewingBase foul = new BrewingBase("foul", new ItemStack(Item.rottenFlesh));
	public static BrewingBase odorless = new BrewingBase("odorless", new ItemStack(Item.bread));
	public static BrewingBase rank = new BrewingBase("rank", new ItemStack(Item.egg));
	public static BrewingBase harsh = new BrewingBase("harsh", new ItemStack(Block.slowSand));
	/** Needed for potions that poison the player **/
	public static BrewingBase acrid = new BrewingBase("acrid", new ItemStack(Item.fermentedSpiderEye));
	public static BrewingBase gross = new BrewingBase("gross", new ItemStack(Item.pumpkinSeeds));
	public static BrewingBase stinky = new BrewingBase("stinky", new ItemStack(Item.fishRaw));
	
	public static Brewing moveSlowdown = new Brewing(new PotionEffect(Potion.moveSlowdown.id, 20*90, 0), 4, 20*240, getBaseBrewing(dashing));
	public static Brewing moveSpeed = new Brewing(new PotionEffect(Potion.moveSpeed.id, 20*180, 0), 7, 20*360, moveSlowdown, new ItemStack(Item.sugar), getBaseBrewing(dashing));
	public static Brewing digSlowdown = new Brewing(new PotionEffect(Potion.digSlowdown.id, 20*90, 0), 4, 20*240, getBaseBrewing(dashing));
	public static Brewing digSpeed = new Brewing(new PotionEffect(Potion.digSpeed.id, 20*180, 0), 7, 20*360, digSlowdown, new ItemStack(Item.carrot), getBaseBrewing(dashing));
	public static Brewing weakness = new Brewing(new PotionEffect(Potion.weakness.id, 20*90, 0), 2, 20*240, new ItemStack(Item.fermentedSpiderEye), awkward);
	public static Brewing damageBoost = new Brewing(new PotionEffect(Potion.damageBoost.id, 20*180, 0), 4, 20*300, weakness, new ItemStack(Item.blazePowder), awkward);
	public static Brewing harm = new Brewing(new PotionEffect(Potion.harm.id, 1, 0), 1, 0, getBaseBrewing(thick));
	public static Brewing heal = new Brewing(new PotionEffect(Potion.heal.id, 1, 0), 1, 0, harm, new ItemStack(Item.speckledMelon), getBaseBrewing(thick));
	public static Brewing jump = new Brewing(new PotionEffect(Potion.jump.id, 20*180, 0), 4, 20*300, getBaseBrewing(dashing));
	public static Brewing doubleJump = new Brewing(new PotionEffect(MorePotionsMod.doubleJump.id, 20*180, 0), 4, 20*3000, jump, new ItemStack(Item.feather), getBaseBrewing(dashing));
	public static Brewing confusion = new Brewing(new PotionEffect(Potion.confusion.id, 20*90, 0), 2, 20*180, new ItemStack(Item.poisonousPotato), awkward);
	public static Brewing regeneration = new Brewing(new PotionEffect(Potion.regeneration.id, 20*45, 0), 2, 20*180, moveSlowdown, new ItemStack(Item.ghastTear), awkward);
	public static Brewing resistance = new Brewing(new PotionEffect(Potion.resistance.id, 20*180, 0), 3, 20*240, new ItemStack(Item.diamond), getBaseBrewing(thick));
	public static Brewing ironSkin = new Brewing(new PotionEffect(MorePotionsMod.ironSkin.id, 20*120, 0), 1, 20*240, new ItemStack(MorePotionsMod.dust, 1, 0), getBaseBrewing(thick));
	public static Brewing obsidianSkin = new Brewing(new PotionEffect(MorePotionsMod.obsidianSkin.id, 20*120, 0), 1, 20*240, new ItemStack(MorePotionsMod.dust, 1, 1), getBaseBrewing(thick));
	public static Brewing fireResistance = new Brewing(new PotionEffect(Potion.fireResistance.id, 20*180, 0), 0, 20*360, moveSlowdown, new ItemStack(Item.magmaCream), awkward);
	public static Brewing waterWalking = new Brewing(new PotionEffect(MorePotionsMod.waterWalking.id, 20*120, 0), 0, 240*20, awkward);
	public static Brewing waterBreathing = new Brewing(new PotionEffect(Potion.waterBreathing.id, 20*180, 0), 2, 20*360, waterWalking, new ItemStack(Item.bone), awkward);
	public static Brewing coldness = new Brewing(new PotionEffect(MorePotionsMod.coldness.id, 20*180, 0), 1, 20*360, new ItemStack(Item.snowball), awkward);
	public static Brewing invisibility = new Brewing(new PotionEffect(Potion.invisibility.id, 20*180, 0), 0, 720*20, (Brewing)null, getBaseBrewing(thin));
	public static Brewing blindness = new Brewing(new PotionEffect(Potion.blindness.id, 20*90, 0), 0, 20*240, new ItemStack(Item.dyePowder, 1, 0), getBaseBrewing(thin));
	public static Brewing nightVision = new Brewing(new PotionEffect(Potion.nightVision.id, 20*180, 0), 0, 20*300, invisibility, new ItemStack(Item.goldenCarrot), getBaseBrewing(thin));
	public static Brewing poison = new Brewing(new PotionEffect(Potion.poison.id, 20*45, 0), 2, 20*60, new ItemStack(Item.spiderEye), getBaseBrewing(acrid));
	public static Brewing hunger = new Brewing(new PotionEffect(Potion.hunger.id, 20*45, 0), 3, 20*60, poison, new ItemStack(Block.dirt), getBaseBrewing(acrid));
	public static Brewing wither = new Brewing(new PotionEffect(Potion.wither.id, 450, 0), 1, 20*60, new ItemStack(Item.coal), getBaseBrewing(acrid));
	public static Brewing fire = new Brewing(new PotionEffect(MorePotionsMod.fire.id, 20*10, 0), 0, 20*20, new ItemStack(Item.fireballCharge), awkward);
	public static Brewing effectRemove = new Brewing(new PotionEffect(MorePotionsMod.effectRemove.id, 20*45, 0), 0, 20*90, new ItemStack(Item.bucketMilk), awkward);
	
	//public static Brewing randomEffect = new Brewing(new PotionEffect(1, 1, 1), 0, 0, new ItemStack(Item.bed)).setIsRandom(true);
	
	private static BrewingBase getBaseBrewing(BrewingBase par1BrewingBase)
	{
		if (MorePotionsMod.defaultAwkwardBrewing)
		{
			return awkward;
		}
		return par1BrewingBase;
	}

	/**
	 * Creates a new Brewing
	 * @param par1PotionEffect Effect
	 * @param par2 Max Amplifier
	 * @param par3 Max Duration
	 * @param par4BrewingBase base
	 */
	public Brewing(PotionEffect par1PotionEffect, int par2, int par3, BrewingBase par4BrewingBase)
	{
		effect = par1PotionEffect;
		isRandom = false;
		maxAmplifier = par2;
		maxDuration = par3;
		base = par4BrewingBase;
	}

	/**
	 * Creates a new Brewing
	 * @param par1PotionEffect Effect
	 * @param par2 Improvable
	 * @param par3 Extendable
	 * @param par4Brewing Opposite
	 */
	public Brewing(PotionEffect par1PotionEffect, int par2, int par3, Brewing par4Brewing, BrewingBase par5BrewingBase)
	{
		this(par1PotionEffect, par2, par3, par5BrewingBase);
		opposite = par4Brewing;
	}

	/**
	 * Creates a new Brewing
	 * @param par1PotionEffect Effect
	 * @param par2 Improvable
	 * @param par3 Extendable
	 * @param par4ItemStack Ingredient
	 */
	public Brewing(PotionEffect par1PotionEffect, int par2, int par3, ItemStack par4ItemStack, BrewingBase par5BrewingBase)
	{
		this(par1PotionEffect, par2, par3, par5BrewingBase);
		ingredient = par4ItemStack;
	}

	/**
	 * Creates a new Brewing
	 * @param par1PotionEffect Effect
	 * @param par2 Improvable
	 * @param par3 Extendable
	 * @param par4Brewing Opposite
	 * @param par5ItemStack Ingredient
	 */
	public Brewing(PotionEffect par1PotionEffect, int par2, int par3, Brewing par4Brewing, ItemStack par5ItemStack, BrewingBase par6BrewingBase)
	{
		this(par1PotionEffect, par2, par3, par4Brewing, par6BrewingBase);
		ingredient = par5ItemStack;
	}


	/**
	 * Determines if the Effect is bad or not
	 */
	public boolean isBadEffect()
	{
		if (this.getEffect() != null)
		{
			int fireid = MorePotionsMod.fire.id;
			switch(this.getEffect().getPotionID())
			{
			case 2:
			case 4:
			case 7:
			case 9:
			case 15:
			case 17:
			case 18:
			case 19:
			case 20: return true;
			}
			if (this.getEffect().getPotionID() == fireid)
			{
				return true;
			}
		}
		return false;
	}

	public PotionEffect getEffect() { return effect; }
	public int getMaxAmplifier() { return maxAmplifier; }
	public int getMaxDuration() { return maxDuration; }
	public boolean isImprovable() { return effect != null ? effect.getAmplifier() < maxAmplifier : false; }
	public boolean isExtendable() { return effect != null ? effect.getDuration() < maxDuration : false; }
	public Brewing getOpposite()
	{
		if (opposite != null && opposite.getEffect() != null)
		{
			opposite.setEffect(new PotionEffect(opposite.getEffect().getPotionID(), this.getEffect().getDuration() / 2, this.isImprovable() ? this.getEffect().getAmplifier() : 0));
		}
		return opposite;
	}
	public ItemStack getIngredient() { return ingredient; }
	public boolean isRandom() { return isRandom; }
	public BrewingBase getBase() { return base; }
	public boolean isBase() { return this.getEffect() == null; }
	public int getLiquidColor() { return this.getEffect() != null && this.getEffect().getPotionID() > 0 ? Potion.potionTypes[effect.getPotionID()].getLiquidColor() : 0x0C0CFF; }
	
	public int getDefaultDuration()
	{
		for (Brewing b : this.brewingList)
		{
			if (b.getEffect() != null && b.getEffect().getPotionID() == this.getEffect().getPotionID())
			{
				return b.getEffect().getDuration();
			}
		}
		return this.getEffect().getDuration();
	}

	public Brewing setEffect(PotionEffect par1) { effect = par1; return this; }
	public Brewing setMaxAmplifier(int par1) { maxAmplifier = par1; return this; }
	public Brewing setMaxDuration(int par1) { maxDuration = par1; return this; }
	public Brewing setOpposite(Brewing par1) { opposite = par1; return this; }
	public Brewing setIngredient(ItemStack par1) { ingredient = par1; return this; }
	public Brewing setIsRandom(boolean par1) { isRandom = par1; return this; }
	public Brewing setBase(BrewingBase par1) { base = par1; return this; }
	
	public Brewing onImproved()
	{
		if (isImprovable() && this.getEffect() != null)
		{
			return this.setEffect(new PotionEffect(this.getEffect().getPotionID(), this.getEffect().getDuration(), this.getEffect().getAmplifier() + 1)).setMaxDuration(this.getEffect().getDuration());
		}
		return this;
	}
	
	public Brewing onExtended()
	{
		if (isExtendable() && this.getEffect() != null)
		{
			return this.setEffect(new PotionEffect(this.getEffect().getPotionID(), this.getEffect().getDuration() * 2, this.getEffect().getAmplifier())).setMaxAmplifier(this.getEffect().getAmplifier());
		}
		return this;
	}

	/**
	 * Sorts the Brewing in the lists
	 * @return Brewing
	 */
	public Brewing register()
	{
		brewingList.add(this);
		if (this.getEffect() != null && this.getEffect().getPotionID() > 0)
		{
			if (this.isBadEffect())
			{
				badEffects.add(this);
			}
			else
			{
				goodEffects.add(this);
			}
		}
		if (!this.isBase() && !(this instanceof BrewingBase))
		{
			effectBrewings.add(this);
		}
		if (!(this instanceof BrewingBase) && this != effectRemove)
		{
			combinableEffects.add(this);
		}
		if (this instanceof BrewingBase)
		{
			baseBrewings.add((BrewingBase)this);
		}
		
		return this;
	}

	/**
	 * Registers all brewings in their correct order.
	 */
	public static void registerBrewings()
	{
		registerBaseBrewings();
		
		regeneration.register();
		moveSpeed.register();
		moveSlowdown.register();
		digSpeed.register();
		digSlowdown.register();
		fireResistance.register();
		waterBreathing.register();
		waterWalking.register();
		coldness.register();
		heal.register();
		harm.register();
		poison.register();
		hunger.register();
		wither.register();
		confusion.register();
		nightVision.register();
		invisibility.register();
		blindness.register();
		damageBoost.register();
		weakness.register();
		jump.register();
		doubleJump.register();
		resistance.register();
		ironSkin.register();
		obsidianSkin.register();
		fire.register();
		effectRemove.register();
	}
	
	public static void registerBaseBrewings()
	{
		awkward.register();
		mundane.register();
		thick.register();
		thin.register();
		dashing.register();
		acrid.register();
		
		if (MorePotionsMod.showAllBaseBrewings)
		{
			elegant.register();
			uninteresting.register();
			bland.register();
			clear.register();
			milky.register();
			diffuse.register();
			artless.register();
			flat.register();
			bulky.register();
			bungling.register();
			buttered.register();
			smooth.register();
			suave.register();
			debonair.register();
			fancy.register();
			charming.register();
			dashing.register();
			refined.register();
			cordial.register();
			sparkling.register();
			potent.register();
			foul.register();
			odorless.register();
			rank.register();
			harsh.register();
			gross.register();
			stinky.register();
		}
	}

	/**
	 * Returns the subtypes for one Potion depending on improvability and extendability
	 * @return
	 */
	public List<Brewing> getSubTypes()
	{
		List<Brewing> list = new ArrayList<Brewing>();
		list.add(this);
		if (this.isImprovable())
		{
			list.add(new Brewing(this.getEffect() != null ? (new PotionEffect(this.getEffect().getPotionID(), this.getEffect().getDuration(), this.getEffect().getAmplifier() + 1)) : null, maxAmplifier, this.getEffect().getDuration(), this.getOpposite(), this.getIngredient(), this.getBase()));
		}
		if (this.isExtendable())
		{
			list.add(new Brewing(this.getEffect() != null ? (new PotionEffect(this.getEffect().getPotionID(), this.getEffect().getDuration() * 2, this.getEffect().getAmplifier())) : null, 0, maxDuration, this.getOpposite(), this.getIngredient(), this.getBase()));
		}
		//		if (this.isExtendable() && this.isImprovable())
		//		{
		//			list.add(new Brewing(this.getEffect() != null ? (new PotionEffect(this.getEffect().getPotionID(), this.getEffect().getDuration() * 2, 1)) : null, true, true, this.getOpposite(), this.getIngredient()));
		//		}
		return list;
	}

	/**
	 * Writes the Brewing to the ItemStack NBT
	 * @param par1 ItemStack
	 * @return ItemStack with Brewing NBT
	 */
	public ItemStack addBrewingToItemStack(ItemStack par1)
	{
		if (par1 != null)
		{
			if (par1.stackTagCompound == null)
			{
				par1.setTagCompound(new NBTTagCompound());
			}

			if (!par1.stackTagCompound.hasKey("Brewing"))
			{
				par1.stackTagCompound.setTag("Brewing", new NBTTagList("Brewing"));
			}
			NBTTagList var2 = (NBTTagList)par1.stackTagCompound.getTag("Brewing");
			var2.appendTag(this instanceof BrewingBase ? ((BrewingBase)this).createNBT() : this.createNBT());
			return par1;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the first Brewing that can be read from the ItemStack NBT
	 * @param par1ItemStack
	 * @return First Brewing read from ItemStack NBT
	 * @deprecated Use ItemPotion2.getEffects instead to get all Brewings from the ItemStack NBT
	 */
	@Deprecated
	public static Brewing getBrewingFromItemStack(ItemStack par1ItemStack)
	{
		if (par1ItemStack != null && par1ItemStack.hasTagCompound())
		{
			NBTTagList list = par1ItemStack.stackTagCompound.getTagList("Brewing");
			if (list != null && list.tagCount() > 0)
			{
				NBTTagCompound compound = (NBTTagCompound) list.tagAt(0);
				return readFromNBT(compound);
			}
		}
		return awkward;
	}
	
	/**
	 * Checks if the ingredient has any effect on a potion
	 * @param ingredient
	 * @return
	 */
	public static boolean isPotionIngredient(ItemStack ingredient)
	{
		if (getHandlerForIngredient(ingredient) != null)
		{
			return true;
		}
		return getBrewingFromIngredient(ingredient) != null || (getHandlerForIngredient(ingredient) != null);
	}
	
	/**
	 * Finds the first registered IngredientHandler that can handle the ingredient
	 * @param ingredient
	 * @return
	 */
	public static IIngredientHandler getHandlerForIngredient(ItemStack ingredient)
	{
		for (IIngredientHandler handler : ingredientHandlers)
		{
			if (handler.canHandleIngredient(ingredient))
			{
				return handler;
			}
		}
		return null;
	}
	
	/**
	 * Checks if the ingredient needs an IngredientHandler to be applied to a potion
	 * @param ingredient
	 * @return
	 */
	public static boolean isSpecialIngredient(ItemStack ingredient)
	{
		return getHandlerForIngredient(ingredient) != null;
	}
	
	/**
	 * Applys an ingredient to a potion
	 * @param ingredient Ingredient
	 * @param potion Potion
	 * @return Potion with applied ingredients
	 */
	public static ItemStack applyIngredient(ItemStack ingredient, ItemStack potion)
	{
		if (getBrewingFromIngredient(ingredient) != null) //The ingredient is a normal ingredient or a base ingredient
		{
			return getBrewingFromIngredient(ingredient).addBrewingToItemStack(potion);
		}
		else //The ingredient is a special one that must NOT only add a Brewing NBT to the ItemStack NBT
		{
			return getHandlerForIngredient(ingredient).applyIngredient(ingredient, potion);
		}
	}
	
	/**
	 * Returns a brewing that is brewed with the itemstack. it doesn't check for the amount.
	 * Ignores Special Ingredient Handlers
	 * @param par1
	 * @return Brewing that is brewed with the ItemStack
	 */
	public static Brewing getBrewingFromIngredient(ItemStack par1)
	{
		if (par1 != null)
		{
			for (Brewing b : brewingList)
			{
				if (b.getIngredient() != null)
				{
					//Include Ore Dictionary
					if (OreDictionary.itemMatches(b.getIngredient(), par1, true))
					{
						return b;
					}
					if (b.getIngredient().getItem() == par1.getItem() && b.getIngredient().getItemDamage() == par1.getItemDamage())
					{
						return b;
					}
				}
			}
			if (BrewingBase.getBrewingBaseFromIngredient(par1) != null)
			{
				return Brewing.getBrewingFromIngredient(par1);
			}
		}
		return null;
	}

	/**
	 * Creates an NBTTagCompound in which the Brewing is stored
	 * @return NBTTagCompound with Brewing data
	 */
	public NBTTagCompound createNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("VERSION", NBTVersion);
		if (isBase() && this instanceof BrewingBase)
		{
			return ((BrewingBase)this).createNBT();
		}
		if (effect != null)
		{
			if (effect.getPotionID() > 0)
			{
				nbt.setInteger("PotionID", effect.getPotionID());
			}
			if (effect.getDuration() > 0)
			{
				nbt.setInteger("PotionDuration", effect.getDuration());
			}
			if (effect.getAmplifier() > 0)
			{
				nbt.setInteger("PotionAmplifier", effect.getAmplifier());
			}
		}
		if (maxAmplifier > 0)
		{
			nbt.setInteger("MaxAmplifier", maxAmplifier);
		}
		if (effect != null && maxDuration > effect.getDuration())
		{
			nbt.setInteger("MaxDuration", maxDuration);
		}
		if (opposite != null)
		{
			nbt.setCompoundTag("Opposite", opposite.createNBT());
		}
		return nbt;
	}

	/**
	 * Reads a Brewing from a NBTTagCompound
	 * @param par1NBTTagCompound NBTTagCompound to read Brewing from
	 * @return Brewing read from NBTTagCompound
	 */
	public static Brewing readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		int potionID = par1NBTTagCompound.hasKey("PotionID") ? par1NBTTagCompound.getInteger("PotionID") : 0;
		int potionDuration = par1NBTTagCompound.hasKey("PotionDuration") ? par1NBTTagCompound.getInteger("PotionDuration") :  0;
		int potionAmplifier = par1NBTTagCompound.hasKey("PotionAmplifier") ? par1NBTTagCompound.getInteger("PotionAmplifier") : 0;
		
		if (potionID == 0 || par1NBTTagCompound.hasKey("BaseName"))
		{
			return BrewingBase.readFromNBT(par1NBTTagCompound);
		}

		int maxamp = par1NBTTagCompound.hasKey("MaxAmplifier") ? par1NBTTagCompound.getInteger("MaxAmplifier") : (par1NBTTagCompound.hasKey("Improvable") ? (par1NBTTagCompound.getBoolean("Improvable") ? 1 : 0) : 0);
		int maxdur = par1NBTTagCompound.hasKey("MaxDuration") ? par1NBTTagCompound.getInteger("MaxDuration") : (par1NBTTagCompound.hasKey("Extendable") ? (par1NBTTagCompound.getBoolean("Extendable") ? potionDuration * 2 : potionDuration) : potionDuration);

		int ingredientID = par1NBTTagCompound.hasKey("IngredientID") ? par1NBTTagCompound.getInteger("IngredientID") : 0;
		int ingredientAmount = par1NBTTagCompound.hasKey("IngredientAmount") ? par1NBTTagCompound.getInteger("IngredientAmount") : 0;
		int ingredientDamage = par1NBTTagCompound.hasKey("IngredientDamage") ? par1NBTTagCompound.getInteger("IngredientDamage") : 0;
		Brewing opposite = par1NBTTagCompound.hasKey("Opposite") ? readFromNBT(par1NBTTagCompound.getCompoundTag("Opposite")) : null;
		BrewingBase base = (BrewingBase) (par1NBTTagCompound.hasKey("Base") ? BrewingBase.readFromNBT(par1NBTTagCompound) : awkward);

		return new Brewing(new PotionEffect(potionID, potionDuration, potionAmplifier), maxamp, maxdur, opposite, new ItemStack(ingredientID, ingredientAmount, ingredientDamage), base);
	}

	/**
	 * Returns a random potion. Used by the random potion.
	 * @return Random Potion
	 */
	public static Brewing random()
	{
		Random rnd = new Random();
		return combinableEffects.get(rnd.nextInt(combinableEffects.size())).setIsRandom(true);
	}

	/**
	 * Calculates the experience given for brewing this potion
	 * @param par1ItemStack potion
	 * @return Experience float
	 */
	public static float getExperience(ItemStack par1ItemStack)
	{
		List<Brewing> effects = ItemPotion2.getEffects(par1ItemStack);
		float f = ItemPotion2.isSplash(par1ItemStack.getItemDamage()) ? 0.3F : 0.2F;
		for (Brewing b : effects)
		{
			if (b.getEffect() != null)
			{
				if (b.isBadEffect())
				{
					f += 0.4F + b.getEffect().getAmplifier() * 0.1F + (b.getEffect().getDuration() / (600));
				}
				else
				{
					f += 0.5F + b.getEffect().getAmplifier() * 0.1F + (b.getEffect().getDuration() / (600));
				}
			}
		}
		return f;
	}
}
