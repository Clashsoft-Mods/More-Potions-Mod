package clashsoft.mods.morepotions;

import clashsoft.brewingapi.brewing.Brewing;
import clashsoft.brewingapi.brewing.BrewingBase;
import clashsoft.brewingapi.brewing.BrewingList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class MorePotionsModBrewingList
{
	public static void initializeBaseBrewings_MorePotionsMod()
	{
		BrewingList.awkward = new BrewingBase("awkward", new ItemStack(Item.netherStalkSeeds));
		BrewingList.mundane = new BrewingBase("mundane", new ItemStack(Block.mushroomBrown));
		BrewingList.uninteresting = new BrewingBase("uninteresting", new ItemStack(Item.paper));
		BrewingList.bland = new BrewingBase("bland", new ItemStack(Item.melon));
		BrewingList.clear = new BrewingBase("clear", clashsoft.mods.morepotions.MorePotionsMod.dustClay);
		BrewingList.milky = new BrewingBase("milky", new ItemStack(Block.sapling));
		BrewingList.diffuse = new BrewingBase("diffuse", new ItemStack(Item.wheat));
		BrewingList.artless = new BrewingBase("artless", new ItemStack(Item.reed));
		BrewingList.thin = new BrewingBase("thin", new ItemStack(Item.redstone));
		BrewingList.flat = new BrewingBase("flat", new ItemStack(Block.waterlily));
		BrewingList.bulky = new BrewingBase("bulky", new ItemStack(Block.sand));
		BrewingList.bungling = new BrewingBase("bungling", new ItemStack(Block.mushroomRed));
		BrewingList.buttered = new BrewingBase("buttered", new ItemStack(Item.dyePowder, 1, 11));
		BrewingList.smooth = new BrewingBase("smooth", new ItemStack(Item.seeds));
		BrewingList.suave = new BrewingBase("suave", new ItemStack(Item.dyePowder, 1, 3));
		BrewingList.debonair = new BrewingBase("debonair", new ItemStack(Item.dyePowder, 1, 2));
		BrewingList.thick = new BrewingBase("thick", new ItemStack(Item.glowstone));
		BrewingList.elegant = new BrewingBase("elegant", new ItemStack(Item.enderPearl));
		BrewingList.fancy = new BrewingBase("fancy", new ItemStack(Item.flint));
		BrewingList.charming = new BrewingBase("charming", new ItemStack(Block.plantRed));
		BrewingList.dashing = new BrewingBase("dashing", new ItemStack(Item.silk));
		BrewingList.refined = new BrewingBase("refined", new ItemStack(Item.slimeBall));
		BrewingList.cordial = new BrewingBase("cordial", new ItemStack(Item.dyePowder, 1, 15));
		BrewingList.sparkling = new BrewingBase("sparkling", new ItemStack(Item.goldNugget));
		BrewingList.potent = new BrewingBase("potent", new ItemStack(Item.blazePowder));
		BrewingList.foul = new BrewingBase("foul", new ItemStack(Item.rottenFlesh));
		BrewingList.odorless = new BrewingBase("odorless", new ItemStack(Item.bread));
		BrewingList.rank = new BrewingBase("rank", new ItemStack(Item.egg));
		BrewingList.harsh = new BrewingBase("harsh", new ItemStack(Block.slowSand));
		BrewingList.acrid = new BrewingBase("acrid", new ItemStack(Item.fermentedSpiderEye));
		BrewingList.gross = new BrewingBase("gross", new ItemStack(Item.pumpkinSeeds));
		BrewingList.stinky = new BrewingBase("stinky", new ItemStack(Item.fishRaw));
	}
	
	public static void initializeBrewings_MorePotionsMod()
	{
		BrewingList.moveSlowdown = new Brewing(new PotionEffect(Potion.moveSlowdown.id, 20 * 90, 0), 4, 20 * 240, Brewing.getBaseBrewing(BrewingList.dashing));
		BrewingList.moveSpeed = new Brewing(new PotionEffect(Potion.moveSpeed.id, 20 * 180, 0), 7, 20 * 360, BrewingList.moveSlowdown, new ItemStack(Item.sugar), Brewing.getBaseBrewing(BrewingList.dashing));
		BrewingList.digSlowdown = new Brewing(new PotionEffect(Potion.digSlowdown.id, 20 * 90, 0), 4, 20 * 240, Brewing.getBaseBrewing(BrewingList.dashing));
		BrewingList.digSpeed = new Brewing(new PotionEffect(Potion.digSpeed.id, 20 * 180, 0), 7, 20 * 360, BrewingList.digSlowdown, clashsoft.mods.morepotions.MorePotionsMod.dustGold, Brewing.getBaseBrewing(BrewingList.dashing));
		BrewingList.weakness = new Brewing(new PotionEffect(Potion.weakness.id, 20 * 90, 0), 2, 20 * 240, new ItemStack(Item.fermentedSpiderEye), BrewingList.awkward);
		BrewingList.damageBoost = new Brewing(new PotionEffect(Potion.damageBoost.id, 20 * 180, 0), 4, 20 * 300, BrewingList.weakness, new ItemStack(Item.blazePowder), BrewingList.awkward);
		BrewingList.harm = new Brewing(new PotionEffect(Potion.harm.id, 1, 0), 1, 0, Brewing.getBaseBrewing(BrewingList.thick));
		BrewingList.heal = new Brewing(new PotionEffect(Potion.heal.id, 1, 0), 1, 0, BrewingList.harm, new ItemStack(Item.speckledMelon), Brewing.getBaseBrewing(BrewingList.thick));
		BrewingList.doubleLife = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.doubleLife.id, 1625000, 0), 0, 0, BrewingList.harm, clashsoft.mods.morepotions.MorePotionsMod.dustNetherstar, Brewing.getBaseBrewing(BrewingList.thick));
		BrewingList.healthBoost = new Brewing(new PotionEffect(Potion.field_76434_w.id, 45 * 20, 0), 4, 120 * 20, Brewing.getBaseBrewing(BrewingList.thick));
		BrewingList.absorption = new Brewing(new PotionEffect(Potion.field_76444_x.id, 45 * 20, 0), 4, 120 * 20, BrewingList.healthBoost, new ItemStack(Item.appleGold), Brewing.getBaseBrewing(BrewingList.thick));
		BrewingList.jump = new Brewing(new PotionEffect(Potion.jump.id, 20 * 180, 0), 4, 20 * 300, Brewing.getBaseBrewing(BrewingList.dashing));
		BrewingList.confusion = new Brewing(new PotionEffect(Potion.confusion.id, 20 * 90, 0), 2, 20 * 180, new ItemStack(Item.poisonousPotato), BrewingList.awkward);
		BrewingList.regeneration = new Brewing(new PotionEffect(Potion.regeneration.id, 20 * 45, 0), 2, 20 * 180, BrewingList.moveSlowdown, new ItemStack(Item.ghastTear), BrewingList.awkward);
		BrewingList.resistance = new Brewing(new PotionEffect(Potion.resistance.id, 20 * 180, 0), 3, 20 * 240, clashsoft.mods.morepotions.MorePotionsMod.dustDiamond, Brewing.getBaseBrewing(BrewingList.thick));
		BrewingList.ironSkin = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.ironSkin.id, 20 * 120, 0), 1, 20 * 240, clashsoft.mods.morepotions.MorePotionsMod.dustIron, Brewing.getBaseBrewing(BrewingList.thick));
		BrewingList.obsidianSkin = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.obsidianSkin.id, 20 * 120, 0), 1, 20 * 240, clashsoft.mods.morepotions.MorePotionsMod.dustObsidian, Brewing.getBaseBrewing(BrewingList.thick));
		BrewingList.fireResistance = new Brewing(new PotionEffect(Potion.fireResistance.id, 20 * 180, 0), 0, 20 * 360, BrewingList.moveSlowdown, new ItemStack(Item.magmaCream), BrewingList.awkward);
		BrewingList.waterWalking = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.waterWalking.id, 20 * 120, 0), 0, 240 * 20, BrewingList.awkward);
		BrewingList.waterBreathing = new Brewing(new PotionEffect(Potion.waterBreathing.id, 20 * 180, 0), 2, 20 * 360, BrewingList.waterWalking, clashsoft.mods.morepotions.MorePotionsMod.dustClay, BrewingList.awkward);
		BrewingList.coldness = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.coldness.id, 20 * 180, 0), 1, 20 * 360, new ItemStack(Item.snowball), BrewingList.awkward);
		BrewingList.invisibility = new Brewing(new PotionEffect(Potion.invisibility.id, 20 * 180, 0), 0, 720 * 20);
		BrewingList.blindness = new Brewing(new PotionEffect(Potion.blindness.id, 20 * 90, 0), 0, 20 * 240, new ItemStack(Item.dyePowder, 1, 0), Brewing.getBaseBrewing(BrewingList.thin));
		BrewingList.nightVision = new Brewing(new PotionEffect(Potion.nightVision.id, 20 * 180, 0), 0, 20 * 300, BrewingList.invisibility, new ItemStack(Item.goldenCarrot), Brewing.getBaseBrewing(BrewingList.thin));
		BrewingList.poison = new Brewing(new PotionEffect(Potion.poison.id, 20 * 45, 0), 2, 20 * 60, new ItemStack(Item.spiderEye), Brewing.getBaseBrewing(BrewingList.acrid));
		BrewingList.hunger = new Brewing(new PotionEffect(Potion.hunger.id, 20 * 45, 0), 3, 20 * 60, Brewing.getBaseBrewing(BrewingList.acrid));
		BrewingList.saturation = new Brewing(new PotionEffect(Potion.field_76443_y.id, 20 * 45, 0), 3, 20 * 60, BrewingList.hunger, new ItemStack(Item.bread), Brewing.getBaseBrewing(BrewingList.awkward));
		BrewingList.wither = new Brewing(new PotionEffect(Potion.wither.id, 450, 0), 1, 20 * 60, clashsoft.mods.morepotions.MorePotionsMod.dustWither, Brewing.getBaseBrewing(BrewingList.acrid));
		BrewingList.explosiveness = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.explosiveness.id, 20 * 10, 0), 4, 20 * 20, BrewingList.awkward);
		BrewingList.fire = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.fire.id, 20 * 10, 0), 0, 20 * 20, BrewingList.explosiveness, new ItemStack(Item.fireballCharge), BrewingList.awkward);
		BrewingList.random = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.random.id, clashsoft.mods.morepotions.MorePotionsMod.randomMode == 0 ? 1 : 20 * 45, 0), 0, clashsoft.mods.morepotions.MorePotionsMod.randomMode == 0 ? 1 : 20 * 90, BrewingList.awkward);
		BrewingList.effectRemove = new Brewing(new PotionEffect(clashsoft.mods.morepotions.MorePotionsMod.effectRemove.id, 20 * 45, 0), 0, 20 * 90, BrewingList.random, new ItemStack(Item.bucketMilk), BrewingList.awkward);
	}
	
	public static void registerBrewings_MorePotionsMod()
	{
		BrewingList.regeneration.register();
		BrewingList.moveSpeed.register();
		BrewingList.moveSlowdown.register();
		BrewingList.digSpeed.register();
		BrewingList.digSlowdown.register();
		BrewingList.fireResistance.register();
		BrewingList.waterBreathing.register();
		BrewingList.waterWalking.register(); //
		BrewingList.coldness.register(); //
		BrewingList.doubleLife.register(); //
		BrewingList.heal.register();
		BrewingList.harm.register();
		BrewingList.healthBoost.register();
		BrewingList.absorption.register();
		BrewingList.poison.register();
		BrewingList.fire.register();
		BrewingList.explosiveness.register();
		BrewingList.wither.register();
		BrewingList.saturation.register();
		BrewingList.hunger.register();
		BrewingList.confusion.register();
		BrewingList.nightVision.register();
		BrewingList.invisibility.register();
		BrewingList.blindness.register();
		BrewingList.damageBoost.register();
		BrewingList.weakness.register();
		BrewingList.jump.register();
		BrewingList.resistance.register();
		BrewingList.ironSkin.register(); //
		BrewingList.obsidianSkin.register(); //
		BrewingList.effectRemove.register(); //
		BrewingList.random.register();
	}
	
	public static void registerBaseBrewings_MorePotionsMod()
	{
		BrewingList.awkward.register();
		BrewingList.thick.register();
		BrewingList.thin.register();
		BrewingList.dashing.register();
		BrewingList.acrid.register();
		
		if (BrewingList.SHOW_ALL_BASEBREWINGS)
		{
			BrewingList.mundane.register();
			BrewingList.elegant.register();
			BrewingList.uninteresting.register();
			BrewingList.bland.register();
			BrewingList.clear.register();
			BrewingList.milky.register();
			BrewingList.diffuse.register();
			BrewingList.artless.register();
			BrewingList.flat.register();
			BrewingList.bulky.register();
			BrewingList.bungling.register();
			BrewingList.buttered.register();
			BrewingList.smooth.register();
			BrewingList.suave.register();
			BrewingList.debonair.register();
			BrewingList.fancy.register();
			BrewingList.charming.register();
			BrewingList.dashing.register();
			BrewingList.refined.register();
			BrewingList.cordial.register();
			BrewingList.sparkling.register();
			BrewingList.potent.register();
			BrewingList.foul.register();
			BrewingList.odorless.register();
			BrewingList.rank.register();
			BrewingList.harsh.register();
			BrewingList.gross.register();
			BrewingList.stinky.register();
		}
	}
}
