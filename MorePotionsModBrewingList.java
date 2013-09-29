package clashsoft.mods.morepotions;

import clashsoft.brewingapi.brewing.Brewing;
import clashsoft.brewingapi.brewing.BrewingBase;
import clashsoft.brewingapi.brewing.BrewingList;
import static clashsoft.brewingapi.brewing.BrewingList.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class MorePotionsModBrewingList
{
	public static Brewing	thorns;
	
	public static void initializeBaseBrewings_MorePotionsMod()
	{
		awkward = new BrewingBase("awkward", new ItemStack(Item.netherStalkSeeds));
		mundane = new BrewingBase("mundane", new ItemStack(Block.mushroomBrown));
		uninteresting = new BrewingBase("uninteresting", new ItemStack(Item.paper));
		bland = new BrewingBase("bland", new ItemStack(Item.melon));
		clear = new BrewingBase("clear", clashsoft.mods.morepotions.MorePotionsMod.dustClay);
		milky = new BrewingBase("milky", new ItemStack(Block.sapling));
		diffuse = new BrewingBase("diffuse", new ItemStack(Item.wheat));
		artless = new BrewingBase("artless", new ItemStack(Item.reed));
		thin = new BrewingBase("thin", new ItemStack(Item.redstone));
		flat = new BrewingBase("flat", new ItemStack(Block.waterlily));
		bulky = new BrewingBase("bulky", new ItemStack(Block.sand));
		bungling = new BrewingBase("bungling", new ItemStack(Block.mushroomRed));
		buttered = new BrewingBase("buttered", new ItemStack(Item.dyePowder, 1, 11));
		smooth = new BrewingBase("smooth", new ItemStack(Item.seeds));
		suave = new BrewingBase("suave", new ItemStack(Item.dyePowder, 1, 3));
		debonair = new BrewingBase("debonair", new ItemStack(Item.dyePowder, 1, 2));
		thick = new BrewingBase("thick", new ItemStack(Item.glowstone));
		elegant = new BrewingBase("elegant", new ItemStack(Item.enderPearl));
		fancy = new BrewingBase("fancy", new ItemStack(Item.flint));
		charming = new BrewingBase("charming", new ItemStack(Block.plantRed));
		dashing = new BrewingBase("dashing", new ItemStack(Item.silk));
		refined = new BrewingBase("refined", new ItemStack(Item.slimeBall));
		cordial = new BrewingBase("cordial", new ItemStack(Item.dyePowder, 1, 15));
		sparkling = new BrewingBase("sparkling", new ItemStack(Item.goldNugget));
		potent = new BrewingBase("potent", new ItemStack(Item.blazePowder));
		foul = new BrewingBase("foul", new ItemStack(Item.rottenFlesh));
		odorless = new BrewingBase("odorless", new ItemStack(Item.bread));
		rank = new BrewingBase("rank", new ItemStack(Item.egg));
		harsh = new BrewingBase("harsh", new ItemStack(Block.slowSand));
		acrid = new BrewingBase("acrid", new ItemStack(Item.fermentedSpiderEye));
		gross = new BrewingBase("gross", new ItemStack(Item.pumpkinSeeds));
		stinky = new BrewingBase("stinky", new ItemStack(Item.fishRaw));
	}
	
	public static void initializeBrewings_MorePotionsMod()
	{
		moveSlowdown = new Brewing(new PotionEffect(Potion.moveSlowdown.id, 20 * 90, 0), 4, 20 * 240, Brewing.getBaseBrewing(BrewingList.dashing));
		moveSpeed = new Brewing(new PotionEffect(Potion.moveSpeed.id, 20 * 180, 0), 7, 20 * 360, BrewingList.moveSlowdown, new ItemStack(Item.sugar), Brewing.getBaseBrewing(BrewingList.dashing));
		digSlowdown = new Brewing(new PotionEffect(Potion.digSlowdown.id, 20 * 90, 0), 4, 20 * 240, Brewing.getBaseBrewing(BrewingList.dashing));
		digSpeed = new Brewing(new PotionEffect(Potion.digSpeed.id, 20 * 180, 0), 7, 20 * 360, BrewingList.digSlowdown, clashsoft.mods.morepotions.MorePotionsMod.dustGold, Brewing.getBaseBrewing(BrewingList.dashing));
		weakness = new Brewing(new PotionEffect(Potion.weakness.id, 20 * 90, 0), 2, 20 * 240, new ItemStack(Item.fermentedSpiderEye), BrewingList.awkward);
		damageBoost = new Brewing(new PotionEffect(Potion.damageBoost.id, 20 * 180, 0), 4, 20 * 300, BrewingList.weakness, new ItemStack(Item.blazePowder), BrewingList.awkward);
		harm = new Brewing(new PotionEffect(Potion.harm.id, 1, 0), 1, 0, Brewing.getBaseBrewing(BrewingList.thick));
		heal = new Brewing(new PotionEffect(Potion.heal.id, 1, 0), 1, 0, BrewingList.harm, new ItemStack(Item.speckledMelon), Brewing.getBaseBrewing(BrewingList.thick));
		doubleLife = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.doubleLife.id, 1625000, 0), 0, 0, BrewingList.harm, clashsoft.mods.morepotions.MorePotionsMod.dustNetherstar, Brewing.getBaseBrewing(BrewingList.thick));
		healthBoost = new Brewing(new PotionEffect(Potion.field_76434_w.id, 45 * 20, 0), 4, 120 * 20, Brewing.getBaseBrewing(BrewingList.thick));
		absorption = new Brewing(new PotionEffect(Potion.field_76444_x.id, 45 * 20, 0), 4, 120 * 20, BrewingList.healthBoost, new ItemStack(Item.appleGold), Brewing.getBaseBrewing(BrewingList.thick));
		jump = new Brewing(new PotionEffect(Potion.jump.id, 20 * 180, 0), 4, 20 * 300, Brewing.getBaseBrewing(BrewingList.dashing));
		confusion = new Brewing(new PotionEffect(Potion.confusion.id, 20 * 90, 0), 2, 20 * 180, new ItemStack(Item.poisonousPotato), BrewingList.awkward);
		regeneration = new Brewing(new PotionEffect(Potion.regeneration.id, 20 * 45, 0), 2, 20 * 180, BrewingList.moveSlowdown, new ItemStack(Item.ghastTear), BrewingList.awkward);
		resistance = new Brewing(new PotionEffect(Potion.resistance.id, 20 * 180, 0), 3, 20 * 240, clashsoft.mods.morepotions.MorePotionsMod.dustDiamond, Brewing.getBaseBrewing(BrewingList.thick));
		ironSkin = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.ironSkin.id, 20 * 120, 0), 1, 20 * 240, clashsoft.mods.morepotions.MorePotionsMod.dustIron, Brewing.getBaseBrewing(BrewingList.thick));
		obsidianSkin = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.obsidianSkin.id, 20 * 120, 0), 1, 20 * 240, clashsoft.mods.morepotions.MorePotionsMod.dustObsidian, Brewing.getBaseBrewing(BrewingList.thick));
		fireResistance = new Brewing(new PotionEffect(Potion.fireResistance.id, 20 * 180, 0), 0, 20 * 360, BrewingList.moveSlowdown, new ItemStack(Item.magmaCream), BrewingList.awkward);
		waterWalking = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.waterWalking.id, 20 * 120, 0), 0, 240 * 20, BrewingList.awkward);
		waterBreathing = new Brewing(new PotionEffect(Potion.waterBreathing.id, 20 * 180, 0), 2, 20 * 360, BrewingList.waterWalking, clashsoft.mods.morepotions.MorePotionsMod.dustClay, BrewingList.awkward);
		coldness = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.coldness.id, 20 * 180, 0), 1, 20 * 360, new ItemStack(Item.snowball), BrewingList.awkward);
		invisibility = new Brewing(new PotionEffect(Potion.invisibility.id, 20 * 180, 0), 0, 720 * 20);
		blindness = new Brewing(new PotionEffect(Potion.blindness.id, 20 * 90, 0), 0, 20 * 240, new ItemStack(Item.dyePowder, 1, 0), Brewing.getBaseBrewing(BrewingList.thin));
		nightVision = new Brewing(new PotionEffect(Potion.nightVision.id, 20 * 180, 0), 0, 20 * 300, BrewingList.invisibility, new ItemStack(Item.goldenCarrot), Brewing.getBaseBrewing(BrewingList.thin));
		poison = new Brewing(new PotionEffect(Potion.poison.id, 20 * 45, 0), 2, 20 * 60, new ItemStack(Item.spiderEye), Brewing.getBaseBrewing(BrewingList.acrid));
		hunger = new Brewing(new PotionEffect(Potion.hunger.id, 20 * 45, 0), 3, 20 * 60, Brewing.getBaseBrewing(BrewingList.acrid));
		saturation = new Brewing(new PotionEffect(Potion.field_76443_y.id, 20 * 45, 0), 3, 20 * 60, BrewingList.hunger, new ItemStack(Item.bread), Brewing.getBaseBrewing(BrewingList.awkward));
		wither = new Brewing(new PotionEffect(Potion.wither.id, 450, 0), 1, 20 * 60, clashsoft.mods.morepotions.MorePotionsMod.dustWither, Brewing.getBaseBrewing(BrewingList.acrid));
		explosiveness = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.explosiveness.id, 20 * 10, 0), 4, 20 * 20, BrewingList.awkward);
		fire = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.fire.id, 20 * 10, 0), 0, 20 * 20, BrewingList.explosiveness, new ItemStack(Item.fireballCharge), BrewingList.awkward);
		random = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.random.id, clashsoft.mods.morepotions.MorePotionsMod.randomMode == 0 ? 1 : 20 * 45, 0), 0, clashsoft.mods.morepotions.MorePotionsMod.randomMode == 0 ? 1 : 20 * 90, BrewingList.awkward);
		effectRemove = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.effectRemove.id, 20 * 45, 0), 0, 20 * 90, BrewingList.random, new ItemStack(Item.bucketMilk), BrewingList.awkward);
		
		thorns = new Brewing(new PotionEffect(MorePotionsMod.thorns.id, 20 * 60, 0), 3, 20 * 120, null, new ItemStack(Block.cactus), BrewingList.awkward);
	}
	
	public static void registerBrewings_MorePotionsMod()
	{
		regeneration.register();
		moveSpeed.register();
		moveSlowdown.register();
		digSpeed.register();
		digSlowdown.register();
		fireResistance.register();
		waterBreathing.register();
		waterWalking.register(); //
		coldness.register(); //
		doubleLife.register(); //
		heal.register();
		harm.register();
		healthBoost.register();
		absorption.register();
		poison.register();
		fire.register();
		explosiveness.register();
		wither.register();
		saturation.register();
		hunger.register();
		confusion.register();
		nightVision.register();
		invisibility.register();
		blindness.register();
		damageBoost.register();
		weakness.register();
		jump.register();
		resistance.register();
		thorns.register();
		ironSkin.register(); //
		obsidianSkin.register(); //
		effectRemove.register(); //
		random.register();
	}
	
	public static void registerBaseBrewings_MorePotionsMod()
	{
		awkward.register();
		thick.register();
		thin.register();
		dashing.register();
		acrid.register();
		
		if (SHOW_ALL_BASEBREWINGS)
		{
			mundane.register();
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
}
