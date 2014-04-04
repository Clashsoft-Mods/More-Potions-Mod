package clashsoft.mods.morepotions.potion;

import static clashsoft.brewingapi.potion.PotionList.*;
import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.potion.IPotionList;
import clashsoft.brewingapi.potion.PotionList;
import clashsoft.brewingapi.potion.type.IPotionType;
import clashsoft.brewingapi.potion.type.PotionBase;
import clashsoft.brewingapi.potion.type.PotionType;
import clashsoft.mods.morepotions.MorePotionsMod;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class MPMPotionList implements IPotionList
{
	public static MPMPotionList	instance	= new MPMPotionList();
	
	public static IPotionType	doubleLife;
	public static IPotionType	ironSkin;
	public static IPotionType	obsidianSkin;
	public static IPotionType	waterWalking;
	public static IPotionType	explosiveness;
	public static IPotionType	effectRemove;
	public static IPotionType	thorns;
	public static IPotionType	greenThumb;
	public static IPotionType	projectile;
	public static IPotionType	doubleJump;
	public static IPotionType	random;
	
	private MPMPotionList()
	{
	}
	
	@Override
	public void initPotionTypes()
	{
		awkward = new PotionBase("awkward", new ItemStack(Items.nether_wart));
		mundane = new PotionBase("mundane", new ItemStack(Blocks.brown_mushroom_block));
		uninteresting = new PotionBase("uninteresting", new ItemStack(Items.paper));
		bland = new PotionBase("bland", new ItemStack(Items.melon));
		clear = new PotionBase("clear", MorePotionsMod.dustClay);
		milky = new PotionBase("milky", new ItemStack(Blocks.sapling));
		diffuse = new PotionBase("diffuse", new ItemStack(Items.wheat));
		artless = new PotionBase("artless", new ItemStack(Items.reeds));
		thin = new PotionBase("thin", new ItemStack(Items.redstone));
		flat = new PotionBase("flat", new ItemStack(Blocks.waterlily));
		bulky = new PotionBase("bulky", new ItemStack(Blocks.sand));
		bungling = new PotionBase("bungling", new ItemStack(Blocks.red_mushroom));
		buttered = new PotionBase("buttered", new ItemStack(Items.dye, 1, 11));
		smooth = new PotionBase("smooth", new ItemStack(Items.wheat_seeds));
		suave = new PotionBase("suave", new ItemStack(Items.dye, 1, 3));
		debonair = new PotionBase("debonair", new ItemStack(Items.dye, 1, 2));
		thick = new PotionBase("thick", new ItemStack(Items.glowstone_dust));
		elegant = new PotionBase("elegant", new ItemStack(Items.ender_pearl));
		fancy = new PotionBase("fancy", new ItemStack(Items.flint));
		charming = new PotionBase("charming", new ItemStack(Blocks.red_flower));
		dashing = new PotionBase("dashing", new ItemStack(Items.string));
		refined = new PotionBase("refined", new ItemStack(Items.slime_ball));
		cordial = new PotionBase("cordial", new ItemStack(Items.dye, 1, 15));
		sparkling = new PotionBase("sparkling", new ItemStack(Items.gold_nugget));
		potent = new PotionBase("potent", new ItemStack(Items.blaze_powder));
		foul = new PotionBase("foul", new ItemStack(Items.rotten_flesh));
		odorless = new PotionBase("odorless", new ItemStack(Items.bread));
		rank = new PotionBase("rank", new ItemStack(Items.egg));
		harsh = new PotionBase("harsh", new ItemStack(Blocks.soul_sand));
		acrid = new PotionBase("acrid", new ItemStack(Items.fermented_spider_eye));
		gross = new PotionBase("gross", new ItemStack(Items.pumpkin_seeds));
		stinky = new PotionBase("stinky", new ItemStack(Items.fish));
		
		moveSlowdown = new PotionType(new PotionEffect(Potion.moveSlowdown.id, 20 * 90, 0), 4, 20 * 240);
		moveSpeed = new PotionType(new PotionEffect(Potion.moveSpeed.id, 20 * 180, 0), 7, 20 * 360, PotionList.moveSlowdown, new ItemStack(Items.sugar), (PotionList.dashing));
		digSlowdown = new PotionType(new PotionEffect(Potion.digSlowdown.id, 20 * 90, 0), 4, 20 * 240);
		digSpeed = new PotionType(new PotionEffect(Potion.digSpeed.id, 20 * 180, 0), 7, 20 * 360, PotionList.digSlowdown, MorePotionsMod.dustGold, (PotionList.dashing));
		weakness = new PotionType(new PotionEffect(Potion.weakness.id, 20 * 90, 0), 2, 20 * 240, new ItemStack(Items.fermented_spider_eye), PotionList.awkward);
		damageBoost = new PotionType(new PotionEffect(Potion.damageBoost.id, 20 * 180, 0), 4, 20 * 300, PotionList.weakness, new ItemStack(Items.blaze_powder), PotionList.awkward);
		harm = new PotionType(new PotionEffect(Potion.harm.id, 1, 0), 1, 0);
		heal = new PotionType(new PotionEffect(Potion.heal.id, 1, 0), 1, 0, PotionList.harm, new ItemStack(Items.speckled_melon), (PotionList.thick));
		doubleLife = new PotionType(new PotionEffect(MorePotionsMod.doubleLife.id, 1625000, 0), 0, 0, PotionList.harm, MorePotionsMod.dustNetherstar, (PotionList.thick));
		healthBoost = new PotionType(new PotionEffect(Potion.field_76434_w.id, 45 * 20, 0), 4, 120 * 20);
		absorption = new PotionType(new PotionEffect(Potion.field_76444_x.id, 45 * 20, 0), 4, 120 * 20, PotionList.healthBoost, new ItemStack(Items.golden_apple), (PotionList.thick));
		doubleJump = new PotionType(new PotionEffect(MorePotionsMod.doubleJump.id, 20 * 60, 0), 2, 20 * 120, null);
		jump = new PotionType(new PotionEffect(Potion.jump.id, 20 * 180, 0), 4, 20 * 300, doubleJump);
		confusion = new PotionType(new PotionEffect(Potion.confusion.id, 20 * 90, 0), 2, 20 * 180, new ItemStack(Items.poisonous_potato), PotionList.awkward);
		regeneration = new PotionType(new PotionEffect(Potion.regeneration.id, 20 * 45, 0), 2, 20 * 180, PotionList.moveSlowdown, new ItemStack(Items.ghast_tear), PotionList.awkward);
		resistance = new PotionType(new PotionEffect(Potion.resistance.id, 20 * 180, 0), 3, 20 * 240, MorePotionsMod.dustDiamond, (PotionList.thick));
		ironSkin = new PotionType(new PotionEffect(MorePotionsMod.ironSkin.id, 20 * 120, 0), 1, 20 * 240, MorePotionsMod.dustIron, (PotionList.thick));
		obsidianSkin = new PotionType(new PotionEffect(MorePotionsMod.obsidianSkin.id, 20 * 120, 0), 1, 20 * 240, MorePotionsMod.dustObsidian, (PotionList.thick));
		fireResistance = new PotionType(new PotionEffect(Potion.fireResistance.id, 20 * 180, 0), 0, 20 * 360, PotionList.moveSlowdown, new ItemStack(Items.magma_cream), PotionList.awkward);
		waterWalking = new PotionType(new PotionEffect(MorePotionsMod.waterWalking.id, 20 * 120, 0), 0, 240 * 20);
		waterBreathing = new PotionType(new PotionEffect(Potion.waterBreathing.id, 20 * 180, 0), 2, 20 * 360, waterWalking, MorePotionsMod.dustClay, PotionList.awkward);
		coldness = new PotionType(new PotionEffect(MorePotionsMod.coldness.id, 20 * 180, 0), 1, 20 * 360, new ItemStack(Items.snowball), PotionList.awkward);
		invisibility = new PotionType(new PotionEffect(Potion.invisibility.id, 20 * 180, 0), 0, 720 * 20);
		blindness = new PotionType(new PotionEffect(Potion.blindness.id, 20 * 90, 0), 0, 20 * 240, new ItemStack(Items.dye, 1, 0), (PotionList.thin));
		nightVision = new PotionType(new PotionEffect(Potion.nightVision.id, 20 * 180, 0), 0, 20 * 300, PotionList.invisibility, new ItemStack(Items.golden_carrot), (PotionList.thin));
		poison = new PotionType(new PotionEffect(Potion.poison.id, 20 * 45, 0), 2, 20 * 60, new ItemStack(Items.spider_eye), (PotionList.acrid));
		hunger = new PotionType(new PotionEffect(Potion.hunger.id, 20 * 45, 0), 3, 20 * 60);
		saturation = new PotionType(new PotionEffect(Potion.field_76443_y.id, 20 * 45, 0), 3, 20 * 60, PotionList.hunger, new ItemStack(Items.bread), (PotionList.awkward));
		wither = new PotionType(new PotionEffect(Potion.wither.id, 450, 0), 1, 20 * 60, MorePotionsMod.dustWither, (PotionList.acrid));
		explosiveness = new PotionType(new PotionEffect(MorePotionsMod.explosiveness.id, 20 * 10, 0), 4, 20 * 20);
		random = new PotionType(new PotionEffect(MorePotionsMod.random.id, MorePotionsMod.randomMode == 0 ? 1 : 20 * 45, 0), 0, MorePotionsMod.randomMode == 0 ? 1 : 20 * 90, new ItemStack(BrewingAPI.potion2), PotionList.awkward);
		effectRemove = new PotionType(new PotionEffect(MorePotionsMod.effectRemove.id, 20 * 45, 0), 0, 20 * 90, random, new ItemStack(Items.milk_bucket), PotionList.awkward)
		{
			@Override
			public boolean isCombinable()
			{
				return false;
			}
		};
		
		thorns = new PotionType(new PotionEffect(MorePotionsMod.thorns.id, 20 * 60, 0), 3, 20 * 120, null, new ItemStack(Blocks.cactus), PotionList.awkward);
		greenThumb = new PotionType(new PotionEffect(MorePotionsMod.greenThumb.id, 20 * 60, 0), 2, 20 * 120, null, new ItemStack(Blocks.leaves), PotionList.awkward);
		projectile = new PotionType(new PotionEffect(MorePotionsMod.projectile.id, 20 * 60, 0), 3, 20 * 120, null, new ItemStack(Items.arrow), PotionList.awkward);
	}
	
	@Override
	public void loadPotionTypes()
	{
		awkward.register();
		thick.register();
		thin.register();
		dashing.register();
		acrid.register();
		
		if (SHOW_ALL_BASES)
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
		doubleJump.register(); //
		resistance.register();
		thorns.register(); //
		projectile.register();
		greenThumb.register(); //
		ironSkin.register(); //
		obsidianSkin.register(); //
		effectRemove.register(); //
		random.register(); //
	}
}
