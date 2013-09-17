package clashsoft.mods.morepotions;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.brewingapi.api.IIngredientHandler;
import clashsoft.clashsoftapi.CustomItem;
import clashsoft.clashsoftapi.CustomPotion;
import clashsoft.clashsoftapi.util.CSArrays;
import clashsoft.clashsoftapi.util.CSCrafting;
import clashsoft.clashsoftapi.util.CSLang;
import clashsoft.clashsoftapi.util.CSUtil;
import clashsoft.mods.morepotions.block.BlockCauldron2;
import clashsoft.mods.morepotions.block.BlockMixer;
import clashsoft.mods.morepotions.item.ItemMortar;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = "MorePotionsMod", name = "More Potions Mod", version = CSUtil.CURRENT_VERION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MorePotionsMod
{
	@Instance("MorePotionsMod")
	public static MorePotionsMod	INSTANCE;
	
	@SidedProxy(clientSide = "clashsoft.mods.morepotions.ClientProxy", serverSide = "clashsoft.mods.morepotions.CommonProxy")
	public static CommonProxy		proxy;
	
	public static String			customEffects	= "gui/potionIcons.png";
	
	// Configurables
	public static int				randomMode		= 0;
	
	public static int				Mixer_TEID		= 12;
	public static int				Cauldron2_TEID	= 13;
	
	public static int				Mixer_ID		= 190;
	public static int				Dust_ID			= 14000;
	public static int				Mortar_ID		= 14001;
	
	static
	{
		BrewingAPI.expandPotionList();
	}
	
	public static Potion			fire			= new CustomPotion("potion.fire", true, 0xFFE500, false, customEffects, 0, 0);
	public static Potion			effectRemove	= new CustomPotion("potion.effectRemove", false, 0xFFFFFF, false, customEffects, 1, 0);
	public static Potion			waterWalking	= new CustomPotion("potion.waterWalking", false, 0x124EFE, false, customEffects, 2, 0);
	public static Potion			coldness		= new CustomPotion("potion.coldness", false, 0x00DDFF, false, customEffects, 3, 0);
	public static Potion			ironSkin		= new CustomPotion("potion.ironSkin", false, 0xD8D8D8, false, customEffects, 4, 0);
	public static Potion			obsidianSkin	= new CustomPotion("potion.obsidianSkin", false, 0x101023, false, customEffects, 5, 0);
	public static Potion			doubleLife		= new CustomPotion("potion.doubleLife", false, 0xFF2222, false, customEffects, 7, 0, CSUtil.fontColorInt(0, 0, 1, 1));
	public static Potion			explosiveness	= new CustomPotion("potion.explosiveness", true, 0xCC0000, false, customEffects, 1, 1);
	public static Potion			random			= new CustomPotion("potion.random", false, 0x000000, randomMode == 0, customEffects, 2, 1, CSUtil.fontColorInt(0, 1, 1, 1));
	
	public static BlockMixer		mixer;
	public static BlockCauldron2	cauldron2;
	public static Item				dust;
	public static ItemStack			dustCoal;
	public static ItemStack			dustIron;
	public static ItemStack			dustGold;
	public static ItemStack			dustObsidian;
	public static ItemStack			dustDiamond;
	public static ItemStack			dustEmerald;
	public static ItemStack			dustQuartz;
	public static ItemStack			dustWither;
	public static ItemStack			dustEnderpearl;
	public static ItemStack			dustClay;
	public static ItemStack			dustBrick;
	public static ItemStack			dustFlint;
	public static ItemStack			dustGlass;
	public static ItemStack			dustCharcoal;
	public static ItemStack			dustWoodOak;
	public static ItemStack			dustWoodBirch;
	public static ItemStack			dustWoodSpruce;
	public static ItemStack			dustWoodJungle;
	public static ItemStack			dustNetherstar;
	public static ItemStack			dustNetherbrick;
	public static ItemMortar		mortar;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		Mixer_TEID = config.get("TileEntityIDs", "MixerTEID", 12).getInt();
		Cauldron2_TEID = config.get("TileEntityIDs", "Cauldron2TEID", 13).getInt();
		
		Mixer_ID = config.getBlock("MixerID", 190).getInt();
		Dust_ID = config.getItem("DustID", 14000).getInt();
		Mortar_ID = config.getItem("MortarID", 14001).getInt();
		
		randomMode = config.get("Potions", "RandomPotionMode", 0, "Determines how the random potion works, if this is 0 the effect is instant and you get a random potion effect when you drink the potion, 1 will give you a new effect every 2 seconds.").getInt();
		
		config.save();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		BrewingAPI.registerEffectHandler(new MorePotionsModEffectHandler());
		BrewingAPI.registerIngredientHandler(new MorePotionsModIngredientHandler());
		
		GameRegistry.registerTileEntity(TileEntityMixer.class, "Mixxer");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "Cauldron2");
		
		Block.blocksList[Block.cauldron.blockID] = null;
		cauldron2 = (BlockCauldron2) (new BlockCauldron2(Block.cauldron.blockID)).setHardness(2.0F).setUnlocalizedName("cauldron");
		
		mixer = (BlockMixer) (new BlockMixer(Mixer_ID)).setUnlocalizedName("mixer").setCreativeTab(CreativeTabs.tabBrewing);
		
		ModLoader.registerBlock(mixer);
		ModLoader.registerBlock(cauldron2);
		mortar = (ItemMortar) new ItemMortar(Mortar_ID).setCreativeTab(CreativeTabs.tabTools).setMaxDamage(32).setNoRepair().setMaxStackSize(1).setUnlocalizedName("mortar");
		dust = new CustomItem(Dust_ID, CSArrays.addToAll(new String[] { "dustCoal", "dustIron", "dustGold", "dustDiamond", "dustEmerald", "dustObsidian", "dustQuartz", "dustWither", "dustEnderpearl", "dustClay", "dustBrick", "dustFlint", "dustGlass", "dustCharcoal", "dustWoodOak", "dustWoodBirch", "dustWoodSpruce", "dustWoodJungle", "dustNetherstar", "dustNetherbrick" }, "item.", ".name"), new String[] { "dustCoal", "dustIron", "dustGold", "dustDiamond", "dustEmerald", "dustObsidian", "dustQuartz", "dustWither", "dustEnderpearl", "dustClay", "dustBrick", "dustFlint", "dustGlass", "dustCoal", "dustWoodOak", "dustWoodBirch", "dustWoodSpruce", "dustWoodJungle", "dustNetherstar", "dustNetherbrick" }, new String[] { "C2", "Fe", "Au", "C128", "Be3Al2Si6O18", "MgFeSi2O8", "SiO2", "\u00a7k???", "BeK4N5Cl6", "Na2LiAl2Si2", "Na2LiAl2Si2", "SiO2", "SiO4", "C", "", "", "", "", "", "" }).setCreativeTab(CreativeTabs.tabMaterials);
		addDusts();
		
		BrewingAPI.load();
		
		Item.sugar.setCreativeTab(CreativeTabs.tabBrewing);
		Item.netherStalkSeeds.setCreativeTab(CreativeTabs.tabBrewing);
		
		ModLoader.addRecipe(new ItemStack(mortar), new Object[] { "SfS", " S ", 'S', Block.stone, 'f', Item.flint });
		ModLoader.addRecipe(new ItemStack(mixer), new Object[] { "gSg", "g g", "SiS", 'g', Block.thinGlass, 'S', Block.stone, 'i', Item.ingotIron });
		
		addLocalizations();
		
		NetworkRegistry.instance().registerGuiHandler(INSTANCE, proxy);
		proxy.registerRenderInformation();
		proxy.registerRenderers();
		MinecraftForge.EVENT_BUS.register(new MorePotionsModEventHandler());
		GameRegistry.registerCraftingHandler(new MorePotionsModCraftingHandler());
		
		System.out.println("Initializing MorePotionsMod Brewings");
		MorePotionsModBrewingList.initializeBaseBrewings_MorePotionsMod();
		MorePotionsModBrewingList.initializeBrewings_MorePotionsMod();
		
		System.out.println("Registering MorePotionsMod Brewings");
		MorePotionsModBrewingList.registerBaseBrewings_MorePotionsMod();
		MorePotionsModBrewingList.registerBrewings_MorePotionsMod();
	}
	
	private void addLocalizations()
	{
		CSLang.addLocalizationUS("itemGroup.morepotions", "Mixed Potions");
		
		CSLang.addLocalizationUS("tile.mixer.name", "Mixer");
		CSLang.addLocalization("tile.mixer.name", "de_DE", "Mischer");
		CSLang.addLocalization("tile.mixer.name", "es_ES", "Mezclador");
		CSLang.addLocalizationUS("tile.unbrewingstand.name", "Unbrewing Stand");
		CSLang.addLocalization("tile.unbrewingstand.name", "de_DE", "Entbrau-Maschine");
		
		CSLang.addLocalizationUS("item.dustCoal.name", "Coal Dust");
		CSLang.addLocalizationDE("item.dustCoal.name", "Kohlestaub");
		CSLang.addLocalizationUS("item.dustIron.name", "Iron Dust");
		CSLang.addLocalizationDE("item.dustIron.name", "Eisenstaub");
		CSLang.addLocalizationUS("item.dustGold.name", "Gold Dust");
		CSLang.addLocalizationDE("item.dustGold.name", "Goldstaub");
		CSLang.addLocalizationUS("item.dustDiamond.name", "Diamond Dust");
		CSLang.addLocalizationDE("item.dustDiamond.name", "Diamantstaub");
		CSLang.addLocalizationUS("item.dustEmerald.name", "Emerald Dust");
		CSLang.addLocalizationDE("item.dustEmerald.name", "Smaragdstaub");
		CSLang.addLocalizationUS("item.dustObsidian.name", "Obsidian Dust");
		CSLang.addLocalizationDE("item.dustObsidian.name", "Obsidianstaub");
		CSLang.addLocalizationUS("item.dustQuartz.name", "Nether Quartz Dust");
		CSLang.addLocalizationDE("item.dustQuartz.name", "Netherquarzstaub");
		CSLang.addLocalizationUS("item.dustWither.name", "Wither Dust");
		CSLang.addLocalizationDE("item.dustWither.name", "Witherstaub");
		CSLang.addLocalizationUS("item.dustEnderpearl.name", "Enderpearl Dust");
		CSLang.addLocalizationDE("item.dustEnderpearl.name", "Enderperlenstaub");
		CSLang.addLocalizationUS("item.dustClay.name", "Clay Dust");
		CSLang.addLocalizationDE("item.dustClay.name", "Lehmstaub");
		CSLang.addLocalizationUS("item.dustBrick.name", "Brick Dust");
		CSLang.addLocalizationDE("item.dustBrick.name", "Ziegelstaub");
		CSLang.addLocalizationUS("item.dustFlint.name", "Flint Dust");
		CSLang.addLocalizationDE("item.dustFlint.name", "Feuersteinstaub");
		CSLang.addLocalizationUS("item.dustGlass.name", "Glass Dust");
		CSLang.addLocalizationDE("item.dustGlass.name", "Glasstaub");
		CSLang.addLocalizationUS("item.dustCharcoal.name", "Charcoal Dust");
		CSLang.addLocalizationDE("item.dustCharcoal.name", "Holzkohlestaub");
		CSLang.addLocalizationUS("item.dustWoodOak.name", "Oak Wood Dust");
		CSLang.addLocalizationDE("item.dustWoodOak.name", "Eichenholzstaub");
		CSLang.addLocalizationUS("item.dustWoodBirch.name", "Birch Wood Dust");
		CSLang.addLocalizationDE("item.dustWoodBirch.name", "Birkenholzstaub");
		CSLang.addLocalizationUS("item.dustWoodSpruce.name", "Spruce Wood Dust");
		CSLang.addLocalizationDE("item.dustWoodSpruce.name", "Fichtenholzstaub");
		CSLang.addLocalizationUS("item.dustWoodJungle.name", "Jungle Wood Dust");
		CSLang.addLocalizationDE("item.dustWoodJungle.name", "Tropenholzstaub");
		CSLang.addLocalizationUS("item.dustNetherstar.name", "Star Dust");
		CSLang.addLocalizationDE("item.dustNetherstar.name", "Sternenstaub");
		CSLang.addLocalizationUS("item.dustNetherbrick.name", "Nether Brick Dust");
		CSLang.addLocalizationDE("item.dustNetherbrick.name", "Netherziegelstaub");
		
		CSLang.addLocalizationUS("item.mortar.name", "Mortar");
		CSLang.addLocalizationDE("item.mortar.name", "M\u00f6rser");
		
		CSLang.addLocalizationUS("potion.fire.postfix", "Potion of Fire");
		CSLang.addLocalizationDE("potion.fire.postfix", "Trank des Feuers");
		CSLang.addLocalizationUS("potion.fire", "Fire");
		CSLang.addLocalizationDE("potion.fire", "Feuer");
		CSLang.addLocalizationUS("potion.fire.description", "Makes you burn.");
		CSLang.addLocalizationDE("potion.fire.description", "L\u00e4sst dich brennen.");
		
		CSLang.addLocalizationUS("potion.effectRemove.postfix", "Potion of Effect Removing");
		CSLang.addLocalizationDE("potion.effectRemove.postfix", "Trank der Effektentfernung");
		CSLang.addLocalizationUS("potion.effectRemove", "Effect Removing");
		CSLang.addLocalizationDE("potion.effectRemove", "Effektentfernung");
		CSLang.addLocalizationUS("potion.effectRemove.description", "Prevents any effects.");
		CSLang.addLocalizationDE("potion.effectRemove.description", "Verhindert alle Effekte.");
		
		CSLang.addLocalizationUS("potion.waterWalking.postfix", "Potion of Water Walking");
		CSLang.addLocalizationDE("potion.waterWalking.postfix", "Trank des \u00dcberwasserlaufens");
		CSLang.addLocalizationUS("potion.waterWalking", "Water Walking");
		CSLang.addLocalizationDE("potion.waterWalking", "\u00dcberwasserlaufen");
		CSLang.addLocalizationUS("potion.waterWalking.description", "Lets you walk over water.");
		CSLang.addLocalizationDE("potion.waterWalking.description", "L\u00e4sst dich \u00fcber Wasser laufen.");
		
		CSLang.addLocalizationUS("potion.coldness.postfix", "Potion of Coldness");
		CSLang.addLocalizationDE("potion.coldness.postfix", "Trank der K\u00e4lte");
		CSLang.addLocalizationUS("potion.coldness", "Coldness");
		CSLang.addLocalizationDE("potion.coldness", "K\u00e4lte");
		CSLang.addLocalizationUS("potion.coldness.description", "Makes you really cold, freezing water and generating snow around you.");
		CSLang.addLocalizationDE("potion.coldness.description", "Macht dich sehr kalt, gefriert Wasser und generiert Schnee in deiner N\u00e4he.");
		
		CSLang.addLocalizationUS("potion.ironSkin.postfix", "Potion of Iron Skin");
		CSLang.addLocalizationDE("potion.ironSkin.postfix", "Trank der Eisenhaut");
		CSLang.addLocalizationUS("potion.ironSkin", "Iron Skin");
		CSLang.addLocalizationDE("potion.ironSkin", "Eisenhaut");
		CSLang.addLocalizationUS("potion.ironSkin.description", "Gives you resistance against fire and other damage sources.");
		CSLang.addLocalizationDE("potion.ironSkin.description", "Bietet Resistenz gegen Feuer und andere Schadensquellen.");
		
		CSLang.addLocalizationUS("potion.obsidianSkin.postfix", "Potion of Obsidian Skin");
		CSLang.addLocalizationDE("potion.obsidianSkin.postfix", "Trank der Obsidianhaut");
		CSLang.addLocalizationUS("potion.obsidianSkin", "Obsidian Skin");
		CSLang.addLocalizationDE("potion.obsidianSkin", "Obsidianhaut");
		CSLang.addLocalizationUS("potion.obsidianSkin.description", "Gives you resistance against fire, lava and other damage sources.");
		CSLang.addLocalizationDE("potion.obsidianSkin.description", "Bietet Resistenz gegen Feuer, Lava und andere Schadesquellen.");
		
		CSLang.addLocalizationUS("potion.doubleLife.postfix", "Potion of Double Life");
		CSLang.addLocalizationDE("potion.doubleLife.postfix", "Trank des Doppellebens");
		CSLang.addLocalizationUS("potion.doubleLife", "Double Life");
		CSLang.addLocalizationDE("potion.doubleLife", "Doppelleben");
		CSLang.addLocalizationUS("potion.doubleLife.description", "Lasts forever, resurrects you once.");
		CSLang.addLocalizationDE("potion.doubleLife.description", "H\u00e4lt f\u00fcr immer, wiederbelebt dich beim Tod ein Mal.");
		
		CSLang.addLocalizationUS("potion.explosiveness.postfix", "Potion of Explosion");
		CSLang.addLocalizationDE("potion.explosiveness.postfix", "Trank der Explosion");
		CSLang.addLocalizationUS("potion.explosiveness", "Explosion");
		CSLang.addLocalizationDE("potion.explosiveness", "Explosion");
		CSLang.addLocalizationUS("potion.explosiveness.description", "Makes you explode every 2 seconds.");
		CSLang.addLocalizationDE("potion.explosiveness.description", "L\u00e4sst dich alle 2 Sekunden explodieren.");
		
		CSLang.addLocalizationUS("potion.random.postfix", "Random Potion");
		CSLang.addLocalizationDE("potion.random.postfix", "Zufallstrank");
		CSLang.addLocalizationUS("potion.random", "Random Effect");
		CSLang.addLocalizationDE("potion.random", "Zuf\u00e4lliger Effekt");
		CSLang.addLocalizationUS("potion.random.description", randomMode == 0 ? "Gives you a random effect." : "Gives you a new random effect every 2 seconds.");
		CSLang.addLocalizationDE("potion.random.description", randomMode == 0 ? "Gibt dir einen zuf\u00e4lligen Trankeffekt." : "Gibt dir einen neuen zuf\u00e4lligen Trankeffekt alle 2 Sekunden.");
		
		CSLang.addLocalizationUS("potion.regeneration.description", "Regenerates life.");
		CSLang.addLocalizationDE("potion.regeneration.description", "Regeneriert Leben.");
		CSLang.addLocalizationUS("potion.moveSpeed.description", "Allows you to move Faster.");
		CSLang.addLocalizationDE("potion.moveSpeed.description", "Erlaubt es, sich schneller zu bewegen.");
		CSLang.addLocalizationUS("potion.moveSlowdown.description", "Slows you down.");
		CSLang.addLocalizationDE("potion.moveSlowdown.description", "Verlangsamt.");
		CSLang.addLocalizationUS("potion.digSpeed.description", "Allows you to move your arms faster and dig faster.");
		CSLang.addLocalizationDE("potion.digSpeed.description", "Erlaubt es, die Arme schneller zu bewegen und schneller zu graben.");
		CSLang.addLocalizationUS("potion.digSlowDown.description", "Slows down your arms and your digging.");
		CSLang.addLocalizationDE("potion.digSlowDown.description", "Verlangsamt Arme und Graben.");
		CSLang.addLocalizationUS("potion.fireResistance.description", "Prevents you from getting fire and lava damage.");
		CSLang.addLocalizationDE("potion.fireResistance.description", "Verhindert Feuer- und Lavaschaden.");
		CSLang.addLocalizationUS("potion.waterBreathing.description", "Lets you breathe underwater.");
		CSLang.addLocalizationDE("potion.waterBreathing.description", "L\u00e4sst dich unter Wasser atmen.");
		CSLang.addLocalizationUS("potion.heal.description", "Gives you some hearts.");
		CSLang.addLocalizationDE("potion.heal.description", "Gibt dir ein paar Leben.");
		CSLang.addLocalizationUS("potion.harm.description", "Deals some damage.");
		CSLang.addLocalizationDE("potion.harm.description", "F\u00fcgt dir Schaden zu.");
		CSLang.addLocalizationUS("potion.healthBoost.description", "Gives you some extra hearts.");
		CSLang.addLocalizationDE("potion.healthBoost.description", "Gibt dir ein paar Extraherzen.");
		CSLang.addLocalizationUS("potion.absorption.description", "Gives you some extra hearts.");
		CSLang.addLocalizationDE("potion.absorption.description", "Gibt dir ein paar Extraherzen.");
		CSLang.addLocalizationUS("potion.poison.description", "Poisons you.");
		CSLang.addLocalizationDE("potion.poison.description", "Vergiftet dich.");
		CSLang.addLocalizationUS("potion.hunger.description", "Makes your hunger bar go down faster.");
		CSLang.addLocalizationDE("potion.hunger.description", "L\u00e4sst deine Hungeranzeige schneller sinken.");
		CSLang.addLocalizationUS("potion.wither.description", "Withers you as you would be hit by a wither skeleton.");
		CSLang.addLocalizationDE("potion.wither.description", "Withert dich als w\u00fcrdest du von einem Witherskelett geschlagen werden.");
		CSLang.addLocalizationUS("potion.confusion.description", "Makes you dizzy.");
		CSLang.addLocalizationDE("potion.confusion.description", "Macht dich schwindelig.");
		CSLang.addLocalizationUS("potion.nightVision.description", "Lets you see everything in the dark.");
		CSLang.addLocalizationDE("potion.nightVision.description", "L\u00e4sst dich im Dunkeln alles sehen.");
		CSLang.addLocalizationUS("potion.invisibility.description", "Makes you invisible.");
		CSLang.addLocalizationDE("potion.invisibility.description", "Macht dich unsichtbar f\u00fcr Spieler und Mobs.");
		CSLang.addLocalizationUS("potion.blindness.description", "Makes you blind.");
		CSLang.addLocalizationDE("potion.blindness.description", "Macht dicht blind.");
		CSLang.addLocalizationUS("potion.damageBoost.description", "Allows you to deal more damage.");
		CSLang.addLocalizationDE("potion.damageBoost.description", "L\u00e4sst dich mehr Schaden austeilen.");
		CSLang.addLocalizationUS("potion.weakness.description", "Decreases your delt damage.");
		CSLang.addLocalizationDE("potion.weakness.description", "Verringert den Schaden, den du machst.");
		CSLang.addLocalizationUS("potion.jump.description", "Lets you jump higher.");
		CSLang.addLocalizationDE("potion.jump.description", "L\u00e4sst dich h\u00f6her springen.");
		CSLang.addLocalizationUS("potion.resistance.description", "Makes you get less damage when getting hit.");
		CSLang.addLocalizationDE("potion.resistance.description", "Verringert den Schaden, den du bekommst.");
		CSLang.addLocalizationUS("potion.saturation.description", "Fills up your hunger bar.");
		CSLang.addLocalizationDE("potion.saturation.description", "F\u00fcllt deine Hungerleiste auf.");
		
		CSLang.addLocalizationUS("potion.goodeffects", "Good Effects");
		CSLang.addLocalization("potion.goodeffects", "de_DE", "Gute Effekte");
		CSLang.addLocalization("potion.goodeffects", "es_ES", "Buenos Effectos");
		CSLang.addLocalizationUS("potion.negativeEffects", "Bad Effects");
		CSLang.addLocalization("potion.negativeEffects", "de_DE", "Schlechte Effekte");
		CSLang.addLocalization("potion.negativeEffects", "es_ES", "Malos Effectos");
		CSLang.addLocalizationUS("potion.potionof", "Potion of");
		CSLang.addLocalization("potion.potionof", "de_DE", "Trank von");
		CSLang.addLocalization("potion.potionof", "es_ES", "Poci\u00F3n del");
		CSLang.addLocalizationUS("potion.effects", "Effects");
		CSLang.addLocalization("potion.effects", "de_DE", "Effekten");
		CSLang.addLocalization("potion.effects", "es_ES", "Effectos");
		CSLang.addLocalizationUS("potion.and", "and");
		CSLang.addLocalization("potion.and", "de_DE", "und");
		CSLang.addLocalization("potion.and", "es_ES", "y");
		CSLang.addLocalizationUS("potion.useto", "Used to make");
		CSLang.addLocalization("potion.useto", "de_DE", "Benutzt f\u00fcr");
		CSLang.addLocalizationUS("potion.infinite", "Infinite");
		CSLang.addLocalizationDE("potion.infinite", "Unendlich");
		CSLang.addLocalizationUS("potion.description.missing", "Description not available");
		CSLang.addLocalizationDE("potion.description.missing", "Keine Beschreibung gefunden");
		
		CSLang.addLocalizationUS("potion.highestamplifier", "Highest Amplifier");
		CSLang.addLocalization("potion.highestamplifier", "de_DE", "Gr\u00f6\u00dftes Level");
		CSLang.addLocalization("potion.highestamplifier", "es_ES", "Alto Nivel");
		CSLang.addLocalizationUS("potion.averageamplifier", "Average Amplifier");
		CSLang.addLocalization("potion.averageamplifier", "de_DE", "Durchschnittliches Level");
		CSLang.addLocalization("potion.averageamplifier", "es_ES", "Nivel promedio");
		CSLang.addLocalizationUS("potion.highestduration", "Highest Duration");
		CSLang.addLocalization("potion.highestduration", "de_DE", "H\u00f6chste Dauer");
		CSLang.addLocalization("potion.highestduration", "es_ES", "Alto Duraci\u00F3n");
		CSLang.addLocalizationUS("potion.averageduration", "Average Duration");
		CSLang.addLocalization("potion.averageduration", "de_DE", "Durchschnittliche Dauer");
		CSLang.addLocalization("potion.averageduration", "es_ES", "Duraci\u00f3n promedio");
		CSLang.addLocalizationUS("potion.value", "Value");
		CSLang.addLocalization("potion.value", "de_DE", "Wert");
		CSLang.addLocalization("potion.value", "es_ES", "Valor");
		
		CSLang.addLocalizationUS("potion.alleffects.postfix", "Potion of all Effects");
		CSLang.addLocalization("potion.alleffects.postfix", "de_DE", "Trank aller Effekte");
		CSLang.addLocalization("potion.alleffects.postfix", "es_ES", "Poci\u00F3n de todos los efectos");
	}
	
	private void addDusts()
	{
		ItemStack mortarStack = new ItemStack(mortar, 1, OreDictionary.WILDCARD_VALUE);
		
		dustCoal = CSCrafting.registerOre("dustCoal", new ItemStack(dust, 1, 0));
		dustIron = CSCrafting.registerOre("dustIron", new ItemStack(dust, 1, 1));
		dustGold = CSCrafting.registerOre("dustGold", new ItemStack(dust, 1, 2));
		dustDiamond = CSCrafting.registerOre("dustDiamond", new ItemStack(dust, 1, 3));
		dustEmerald = CSCrafting.registerOre("dustEmerald", new ItemStack(dust, 1, 4));
		dustObsidian = CSCrafting.registerOre("dustObsidian", new ItemStack(dust, 1, 5));
		dustQuartz = CSCrafting.registerOre("dustQuartz", new ItemStack(dust, 1, 6));
		dustWither = CSCrafting.registerOre("dustWither", new ItemStack(dust, 1, 7));
		dustEnderpearl = CSCrafting.registerOre("dustEnderpearl", new ItemStack(dust, 1, 8));
		dustClay = CSCrafting.registerOre("dustClay", new ItemStack(dust, 1, 9));
		dustBrick = CSCrafting.registerOre("dustBrick", new ItemStack(dust, 1, 10));
		dustFlint = CSCrafting.registerOre("dustFlint", new ItemStack(dust, 1, 11));
		dustGlass = CSCrafting.registerOre("dustGlass", new ItemStack(dust, 1, 12));
		dustCharcoal = CSCrafting.registerOre("dustCharcoal", new ItemStack(dust, 1, 13));
		dustWoodOak = CSCrafting.registerOre("dustWoodOak", CSCrafting.registerOre("dustWood", new ItemStack(dust, 1, 14)));
		dustWoodBirch = CSCrafting.registerOre("dustWoodBirch", CSCrafting.registerOre("dustWood", new ItemStack(dust, 1, 15)));
		dustWoodSpruce = CSCrafting.registerOre("dustWoodSpruce", CSCrafting.registerOre("dustWood", new ItemStack(dust, 1, 16)));
		dustWoodJungle = CSCrafting.registerOre("dustWoodJungle", CSCrafting.registerOre("dustWood", new ItemStack(dust, 1, 17)));
		dustNetherstar = CSCrafting.registerOre("dustNetherstar", new ItemStack(dust, 1, 18));
		dustNetherbrick = CSCrafting.registerOre("dustNetherbrick", new ItemStack(dust, 1, 19));
		
		GameRegistry.addShapelessRecipe(dustCoal, new Object[] { Item.coal, mortarStack });
		GameRegistry.addShapelessRecipe(dustIron, new Object[] { Item.ingotIron, mortarStack });
		GameRegistry.addShapelessRecipe(dustGold, new Object[] { Item.ingotGold, mortarStack });
		GameRegistry.addShapelessRecipe(dustDiamond, new Object[] { Item.diamond, mortarStack });
		GameRegistry.addShapelessRecipe(dustEmerald, new Object[] { Item.emerald, mortarStack });
		GameRegistry.addShapelessRecipe(dustObsidian, new Object[] { Block.obsidian, mortarStack });
		GameRegistry.addShapelessRecipe(dustQuartz, new Object[] { Item.netherQuartz, mortarStack });
		GameRegistry.addShapelessRecipe(dustWither, new Object[] { new ItemStack(Item.skull, 1, 1), dustCoal, dustQuartz });
		GameRegistry.addShapelessRecipe(dustEnderpearl, new Object[] { Item.enderPearl, mortarStack });
		GameRegistry.addShapelessRecipe(dustClay, new Object[] { Item.clay, mortarStack });
		GameRegistry.addShapelessRecipe(dustBrick, new Object[] { Item.brick, mortarStack });
		GameRegistry.addShapelessRecipe(dustFlint, new Object[] { Item.flint, mortarStack });
		GameRegistry.addShapelessRecipe(dustGlass, new Object[] { Block.glass, mortarStack });
		GameRegistry.addShapelessRecipe(dustCharcoal, new Object[] { new ItemStack(Item.coal, 1, 1), mortarStack });
		GameRegistry.addShapelessRecipe(dustWoodOak, new Object[] { new ItemStack(Block.wood, 1, 0), mortarStack });
		GameRegistry.addShapelessRecipe(dustWoodBirch, new Object[] { new ItemStack(Block.wood, 1, 2), mortarStack });
		GameRegistry.addShapelessRecipe(dustWoodSpruce, new Object[] { new ItemStack(Block.wood, 1, 1), mortarStack });
		GameRegistry.addShapelessRecipe(dustWoodJungle, new Object[] { new ItemStack(Block.wood, 1, 3), mortarStack });
		GameRegistry.addShapelessRecipe(dustNetherstar, new Object[] { Item.netherStar, mortarStack });
		GameRegistry.addShapelessRecipe(dustNetherbrick, new Object[] { Item.netherrackBrick, mortarStack });
		
		CSCrafting.addSmelting(dustIron, new ItemStack(Item.ingotIron), 0F);
		CSCrafting.addSmelting(dustGold, new ItemStack(Item.ingotGold), 0F);
		CSCrafting.addSmelting(dustQuartz, new ItemStack(Item.netherQuartz), 0F);
		CSCrafting.addSmelting(dustClay, new ItemStack(Item.brick), 0.1F);
		CSCrafting.addSmelting(dustBrick, new ItemStack(Item.brick), 0F);
		CSCrafting.addSmelting(dustGlass, new ItemStack(Block.glass), 0F);
	}
	
	public class MorePotionsModIngredientHandler implements IIngredientHandler
	{
		@Override
		public boolean canHandleIngredient(ItemStack ingredient)
		{
			return ingredient.itemID == Block.pistonBase.blockID;
		}
		
		@Override
		public boolean canApplyIngredient(ItemStack ingredient, ItemStack potion)
		{
			if (ingredient != null && ingredient.itemID == Block.pistonBase.blockID && potion.getItemDamage() != 12)
			{
				return true;
			}
			return false;
		}
		
		@Override
		public ItemStack applyIngredient(ItemStack ingredient, ItemStack potion)
		{
			if (ingredient.itemID == Block.pistonBase.blockID)
			{
				potion.setItemDamage(12);
				potion.addEnchantment(Enchantment.infinity, 27);
			}
			return potion;
		}
	}
	
	public class MorePotionsModCraftingHandler implements ICraftingHandler
	{
		@Override
		public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
		{
			for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
			{
				if (craftMatrix.getStackInSlot(i) != null)
				{
					ItemStack j = craftMatrix.getStackInSlot(i);
					if (j.getItem() != null && j.getItem() == MorePotionsMod.mortar)
					{
						ItemStack k = new ItemStack(MorePotionsMod.mortar, 2, (j.getItemDamage() + 1));
						if (k.getItemDamage() >= k.getMaxDamage())
						{
							k.stackSize--;
						}
						craftMatrix.setInventorySlotContents(i, k);
					}
				}
			}
		}
		
		@Override
		public void onSmelting(EntityPlayer player, ItemStack item)
		{
		}
	}
}