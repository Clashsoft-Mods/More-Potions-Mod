package clashsoft.mods.morepotions;

import java.awt.event.KeyEvent;
import java.util.EnumSet;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import clashsoft.clashsoftapi.CSCrafting;
import clashsoft.clashsoftapi.CSItems;
import clashsoft.clashsoftapi.CSLang;
import clashsoft.clashsoftapi.CSUtil;
import clashsoft.clashsoftapi.CustomItem;
import clashsoft.clashsoftapi.CustomPotion;
import clashsoft.clashsoftapi.EnumFontColor;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.CommandServerBan;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

@Mod(modid = "MorePotionsMod", name = "More Potions Mod", version = CSUtil.CURRENT_VERION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MorePotionsMod
{
	@Instance("MorePotionsMod")
	public static MorePotionsMod INSTANCE;

	@SidedProxy(clientSide = "clashsoft.mods.morepotions.ClientProxy", serverSide = "clashsoft.mods.morepotions.CommonProxy")
	public static CommonProxy proxy;

	//Configurables
	public static boolean multiPotions = false;
	public static boolean advancedPotionInfo = false;
	public static boolean animatedPotionLiquid = true;
	public static boolean showAllBaseBrewings = false;
	public static boolean defaultAwkwardBrewing = false;
	public static int potionStackSize = 1;
	public static int randomMode = 0;

	public static int BrewingStand2_TEID = 11;
	public static int Mixer_TEID = 12;
	public static int Cauldron2_TEID = 13;
	public static int UnbrewingStand_TEID = 14;

	public static int Mixer_ID = 190;
	public static int UnbrewingStand_ID = 191;
	public static int Dust_ID = 14000;
	public static int Mortar_ID = 14001;

	public static int SplashPotion2_ID = EntityRegistry.findGlobalUniqueEntityId();

	public static int PotionsTab_ID = CreativeTabs.getNextID();

	public static CreativeTabs potions = new CreativeTabs(PotionsTab_ID, "morepotions");

	public static Potion fire = new CustomPotion("potion.fire", true, 0xFFE500, false, 0, 0);
	public static Potion effectRemove = new CustomPotion("potion.effectRemove", false, 0xFFFFFF, false, 1, 0);
	public static Potion waterWalking = new CustomPotion("potion.waterWalking", false, 0x124EFE, false, 2, 0);
	public static Potion coldness = new CustomPotion("potion.coldness", false, 0x00DDFF, false, 3, 0);
	public static Potion ironSkin = new CustomPotion("potion.ironSkin", false, 0xD8D8D8, false, 4, 0);
	public static Potion obsidianSkin = new CustomPotion("potion.obsidianSkin", false, 0x101023, false, 5, 0);
	public static Potion doubleJump = new CustomPotion("potion.doubleJump", false, 0x123456, false, 6, 0);
	public static Potion doubleLife = new CustomPotion("potion.doubleLife", false, 0xFF2222, false, 7, 0, CSUtil.fontColorInt(0, 0, 1, 1));
	public static Potion antiHunger = new CustomPotion("potion.antiHunger", false, 0xE3E3E3, false, 0, 1);
	public static Potion explosiveness = new CustomPotion("potion.explosiveness", true, 0xCC0000, false, 1, 1);
	public static Potion random = new CustomPotion("potion.random", false, 0x000000, randomMode == 0, 2, 1, CSUtil.fontColorInt(0, 1, 1, 1));

	public static Block brewingStand2;
	public static Block mixxer;
	public static Block cauldron2;
	public static Block unbrewingStand;
	public static Item brewingStand2Item;
	public static ItemPotion2 potion2;
	public static ItemGlassBottle2 glassBottle2;
	public static Item dust;
	public static ItemStack dustCoal;
	public static ItemStack dustIron;
	public static ItemStack dustGold;
	public static ItemStack dustObsidian;
	public static ItemStack dustDiamond;
	public static ItemStack dustEmerald;
	public static ItemStack dustQuartz;
	public static ItemStack dustWither;
	public static ItemStack dustEnderpearl;
	public static ItemStack dustClay;
	public static ItemStack dustBrick;
	public static ItemStack dustFlint;
	public static ItemStack dustGlass;
	public static ItemStack dustCharcoal;
	public static ItemStack dustWoodOak;
	public static ItemStack dustWoodBirch;
	public static ItemStack dustWoodSpruce;
	public static ItemStack dustWoodJungle;
	public static ItemStack dustNetherstar;
	public static ItemStack dustNetherbrick;
	public static Item mortar;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		BrewingStand2_TEID = config.get("TileEntityIDs", "BrewingStand2TEID", 11).getInt();
		Mixer_TEID = config.get("TileEntityIDs", "MixerTEID", 12).getInt();
		Cauldron2_TEID = config.get("TileEntityIDs", "Cauldron2TEID", 13).getInt();
		UnbrewingStand_TEID = config.get("TileEntityIDs", "UnbrewingStandTEID", 14).getInt();

		Mixer_ID = config.getBlock("MixerID", 190).getInt();
		UnbrewingStand_ID = config.getBlock("UnbrewingStandID", 191).getInt();
		Dust_ID = config.getItem("DustID", 14000).getInt();
		Mortar_ID = config.getItem("MortarID", 14001).getInt();

		multiPotions = config.get("Potions", "MultiPotions", false, "If true, potions with 2 different effects are shown in the creative inventory.").getBoolean(false);
		advancedPotionInfo = config.get("Potions", "AdvancedPotionInfo", true).getBoolean(true);
		animatedPotionLiquid = config.get("Potions", "AnimatedPotionLiquid", true).getBoolean(true);
		showAllBaseBrewings = config.get("Potions", "ShowAllBaseBrewings", false, "If true, all base potions are shown in creative inventory.").getBoolean(false);
		defaultAwkwardBrewing = config.get("Potions", "DefaultAwkwardBrewing", false, "If true, all potions can be brewed with an awkward potion.").getBoolean(false);
		randomMode = config.get("Potions", "RandomPotionMode", 0, "Determines how the random potion works, if this is 0 the effect is instant and you get a random potion effect when you drink the potion, 1 will give you a new effect every 2 seconds.").getInt();
		potionStackSize = config.get("Potions", "PotionStackSize", 1).getInt();

		config.save();
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		GameRegistry.registerTileEntity(TileEntityBrewingStand2.class, "BrewingStand2");
		GameRegistry.registerTileEntity(TileEntityMixer.class, "Mixxer");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "Cauldron2");
		GameRegistry.registerTileEntity(TileEntityUnbrewingStand.class, "UnbrewingStand");
		EntityRegistry.registerGlobalEntityID(EntityPotion2.class, "SplashPotion2", MorePotionsMod.SplashPotion2_ID);
		EntityRegistry.registerModEntity(EntityPotion2.class, "SplashPotion2", MorePotionsMod.SplashPotion2_ID, this, 100, 20, true);

		Block.blocksList[Block.brewingStand.blockID] = null;
		brewingStand2 = (new BlockBrewingStand2(Block.brewingStand.blockID)).setHardness(0.5F).setLightValue(0.125F).setUnlocalizedName("brewingStand");

		Block.blocksList[Block.cauldron.blockID] = null;
		cauldron2 = (new BlockCauldron2(Block.cauldron.blockID)).setHardness(2.0F).setUnlocalizedName("cauldron");;

		mixxer = (new BlockMixer(Mixer_ID)).setUnlocalizedName("mixer").setCreativeTab(CreativeTabs.tabBrewing);

		//Block.blocksList[Block.cauldron.blockID] = null;
		//cauldron2 = (new BlockCauldron2(Block.cauldron.blockID)).setHardness(2.0F).setUnlocalizedName("cauldron");;

		unbrewingStand = (new BlockUnbrewingStand(UnbrewingStand_ID)).setUnlocalizedName("unbrewingstand").setCreativeTab(null);

		ModLoader.registerBlock(brewingStand2);
		ModLoader.registerBlock(mixxer);
		//ModLoader.registerBlock(cauldron2);
		ModLoader.registerBlock(unbrewingStand);
		ModLoader.addRecipe(new ItemStack(mixxer), new Object[] {"gSg", "g g", "SiS", 'g', Block.thinGlass, 'S', Block.stone, 'i', Item.ingotIron});

		Item.itemsList[Item.brewingStand.itemID] = null;
		brewingStand2Item = (new ItemReed(123, brewingStand2)).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing);

		//Item.itemsList[Item.cauldron.itemID] = null;
		//brewingStand2Item = (new ItemReed(124, cauldron2)).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.tabBrewing);


		Item.itemsList[Item.potion.itemID - 256] = null;
		potion2 = (ItemPotion2)(new ItemPotion2(117)).setUnlocalizedName("potion");

		Item.itemsList[Item.glassBottle.itemID - 256] = null;
		glassBottle2 = (ItemGlassBottle2) (new ItemGlassBottle2(118)).setUnlocalizedName("glassBottle");

		mortar = new Item(Mortar_ID).setCreativeTab(CreativeTabs.tabTools).setMaxDamage(32).setNoRepair().setMaxStackSize(1).setUnlocalizedName("mortar");
		ModLoader.addRecipe(new ItemStack(mortar), new Object[]{"SfS", " S ", 'S', Block.stone, 'f', Item.flint});

		dust = new CustomItem(Dust_ID,
				new String[]{"dustCoal", "dustIron", "dustGold", "dustDiamond", "dustEmerald", "dustObsidian", "dustQuartz", "dustWither", "dustEnderpearl", "dustClay", "dustBrick", "dustFlint", "dustGlass", "dustCharcoal", "dustWoodOak", "dustWoodBirch", "dustWoodSpruce", "dustWoodJungle", "dustNetherstar", "dustNetherbrick"},
				new String[]{"dustCoal", "dustIron", "dustGold", "dustDiamond", "dustEmerald", "dustObsidian", "dustQuartz", "dustWither", "dustEnderpearl", "dustClay", "dustBrick", "dustFlint", "dustGlass", "dustCoal", "dustWoodOak", "dustWoodBirch", "dustWoodSpruce", "dustWoodJungle", "dustNetherstar", "dustNetherbrick"},
				new String[]{"C2",		 "Fe", 		 "Au", 		 "C128", 		"Be3Al2Si6O18", "MgFeSi2O8",   "SiO2",		 "\u00a7k???", "BeK4N5Cl6", 	 "Na2LiAl2Si2", "Na2LiAl2Si2", "SiO2", "SiO4", "C", "", "", "", "", "", ""}).setCreativeTab(CreativeTabs.tabMaterials);
		addDusts();

		Item.sugar.setCreativeTab(CreativeTabs.tabBrewing);
		Item.netherStalkSeeds.setCreativeTab(CreativeTabs.tabBrewing);

		addLocalizations();

		NetworkRegistry.instance().registerGuiHandler(INSTANCE, proxy);
		proxy.registerRenderInformation();
		proxy.registerRenderers();
		MinecraftForge.EVENT_BUS.register(new BrewingAPI());
		MinecraftForge.EVENT_BUS.register(new MorePotionsModEventHandler());
		BrewingAPI.registerEffectHandler(new MorePotionsModEffectHandler());
		BrewingAPI.registerIngredientHandler(new MorePotionsModIngredientHandler());
		Brewing.registerBrewings();
		GameRegistry.registerCraftingHandler(new MorePotionsModCraftingHandler());
		ModLoader.addDispenserBehavior(potion2, new DispenserBehaviorPotion());
	}

	@ServerStarting
	public void serverStart(FMLServerStartingEvent event)
	{
		System.out.println("Registering Commands");
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		ICommandManager command = server.getCommandManager();
		if (command instanceof CommandHandler)
		{
			System.out.println(((CommandHandler)command).registerCommand(new CommandPotion()));
		}
	}

	private void addLocalizations()
	{
		CSLang.addLocalizationUS("itemGroup.morepotions", "Mixed Potions");

		CSLang.addLocalizationUS("potion.potency.4", "V");
		CSLang.addLocalizationUS("potion.potency.5", "VI");
		CSLang.addLocalizationUS("potion.potency.6", "VII");
		CSLang.addLocalizationUS("potion.potency.7", "VIII");
		CSLang.addLocalizationUS("potion.potency.8", "IX");
		CSLang.addLocalizationUS("potion.potency.9", "X");

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
		CSLang.addLocalizationDE("potion.waterWalking.description", "L\u00e4sst dich über Wasser laufen.");

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

		CSLang.addLocalizationUS("potion.doubleJump", "Double Jump");
		CSLang.addLocalizationDE("potion.doubleJump", "Doppelsprung");
		CSLang.addLocalizationUS("potion.doubleJump.postfix", "Potion of Double Jump");
		CSLang.addLocalizationDE("potion.doubleJump.postfix", "Trank des Doppelsprungs");
		CSLang.addLocalizationUS("potion.doubleJump.description", "Allows you to jump in mid-air.");
		CSLang.addLocalizationDE("potion.doubleJump.description", "Erlaubt es, in der Luft ein zweites Mal zu springen.");

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
		
		CSLang.addLocalizationUS("potion.antiHunger.postfix", "Potion of Anti Hunger");
		CSLang.addLocalizationDE("potion.antiHunger.postfix", "Trank des Antihungers");
		CSLang.addLocalizationUS("potion.antiHunger", "Anti Hunger");
		CSLang.addLocalizationDE("potion.antiHunger", "Antihunger");
		CSLang.addLocalizationUS("potion.antiHunger.description", "Makes your hunger bar regenerate.");
		CSLang.addLocalizationDE("potion.antiHunger.description", "L\u00e4sst deine Hungerleiste regenerieren.");
		
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
		CSLang.addLocalizationDE("potion.invisibility.description", "Macht dich unsichtbar für Spieler und Mobs.");
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

		ModLoader.addShapelessRecipe(dustCoal, new Object[]{Item.coal, mortarStack});
		ModLoader.addShapelessRecipe(dustIron, new Object[]{Item.ingotIron, mortarStack});
		ModLoader.addShapelessRecipe(dustGold, new Object[]{Item.ingotGold, mortarStack});
		ModLoader.addShapelessRecipe(dustDiamond, new Object[]{Item.diamond, mortarStack});
		ModLoader.addShapelessRecipe(dustEmerald, new Object[]{Item.emerald, mortarStack});
		ModLoader.addShapelessRecipe(dustObsidian, new Object[]{Block.obsidian, mortarStack});
		ModLoader.addShapelessRecipe(dustQuartz, new Object[]{Item.netherQuartz, mortarStack});
		ModLoader.addShapelessRecipe(dustWither, new Object[]{new ItemStack(Item.skull, 1, 1), dustCoal, dustQuartz});
		ModLoader.addShapelessRecipe(dustEnderpearl, new Object[]{Item.enderPearl, mortarStack});
		ModLoader.addShapelessRecipe(dustClay, new Object[]{Item.clay, mortarStack});
		ModLoader.addShapelessRecipe(dustBrick, new Object[]{Item.brick, mortarStack});
		ModLoader.addShapelessRecipe(dustFlint, new Object[]{Item.flint, mortarStack});
		ModLoader.addShapelessRecipe(dustGlass, new Object[]{Block.glass, mortarStack});
		ModLoader.addShapelessRecipe(dustCharcoal, new Object[]{new ItemStack(Item.coal, 1, 1), mortarStack});
		ModLoader.addShapelessRecipe(dustWoodOak, new Object[]{new ItemStack(Block.wood, 1, 0), mortarStack});
		ModLoader.addShapelessRecipe(dustWoodBirch, new Object[]{new ItemStack(Block.wood, 1, 2), mortarStack});
		ModLoader.addShapelessRecipe(dustWoodSpruce, new Object[]{new ItemStack(Block.wood, 1, 1), mortarStack});
		ModLoader.addShapelessRecipe(dustWoodJungle, new Object[]{new ItemStack(Block.wood, 1, 3), mortarStack});
		ModLoader.addShapelessRecipe(dustNetherstar, new Object[]{Item.netherStar, mortarStack});
		ModLoader.addShapelessRecipe(dustNetherbrick, new Object[]{Item.netherrackBrick, mortarStack});

		CSCrafting.addSmelting(dustIron, new ItemStack(Item.ingotIron), 0F);
		CSCrafting.addSmelting(dustGold, new ItemStack(Item.ingotGold), 0F);
		CSCrafting.addSmelting(dustQuartz, new ItemStack(Item.netherQuartz), 0F);
		CSCrafting.addSmelting(dustClay, new ItemStack(Item.brick), 0.1F);
		CSCrafting.addSmelting(dustBrick, new ItemStack(Item.brick), 0F);
		CSCrafting.addSmelting(dustGlass, new ItemStack(Block.glass), 0F);
	}

	public static class MorePotionsModEffectHandler implements IPotionEffectHandler
	{
		private static boolean hasJumped = false;
		private float tick = 0;

		public void onPotionUpdate(EntityLiving living, PotionEffect effect)
		{
			if (effect.getPotionID() == MorePotionsMod.fire.id)
			{
				int x = (int) Math.floor(living.posX);
				int y = (int) (living.posY - living.getYOffset());
				int z = (int) Math.floor(living.posZ);
				int id = living.worldObj.getBlockId(x, y - 1, z);
				if (Block.blocksList[id] != null && Block.blocksList[id].isBlockSolidOnSide(living.worldObj, x, y - 1, z, ForgeDirection.UP) && living.worldObj.getBlockId(x, y, z) == 0)
				{
					living.worldObj.setBlock(x, y, z, Block.fire.blockID);
				}
				
				living.setFire(1);
			}
			if (effect.getPotionID() == (MorePotionsMod.effectRemove.id))
			{
				for (int i = 0; i < Potion.potionTypes.length; i++)
				{
					if (i != MorePotionsMod.effectRemove.id)
					{
						living.removePotionEffect(i);
					}
				}
			}
			else if (effect.getPotionID() == (MorePotionsMod.waterWalking.id))
			{
				int x = (int) Math.floor(living.posX);
				int y = (int) (living.posY - living.getYOffset());
				int z = (int) Math.floor(living.posZ);
				if (living.worldObj.getBlockId(x, y - 1, z) == 9 && living.worldObj.getBlockId(x, y, z) == 0)
				{
					if (living.motionY < 0 && living.boundingBox.minY < y)
					{
						living.motionY = 0;
						living.fallDistance = 0;
						living.onGround = true;
						if (living.isSneaking())
						{
							living.motionY -= 0.1F;
						}
					}
				}
			}
			else if (effect.getPotionID() == (MorePotionsMod.coldness.id))
			{
				int x = (int) Math.floor(living.posX);
				int y = (int) (living.posY - living.getYOffset());
				int z = (int) Math.floor(living.posZ);
				int id = living.worldObj.getBlockId(x, y - 1, z);
				if (id == Block.waterMoving.blockID || id == Block.waterStill.blockID)
				{
					living.worldObj.setBlock(x, y - 1, z, Block.ice.blockID);
				}
				if (living.getActivePotionEffect(MorePotionsMod.coldness).getAmplifier() > 0 && Block.blocksList[id] != null && Block.blocksList[id].isBlockSolidOnSide(living.worldObj, x, y - 1, z, ForgeDirection.UP) && living.worldObj.getBlockId(x, y, z) == 0)
				{
					living.worldObj.setBlock(x, y, z, Block.snow.blockID);
				}
			}
			else if (effect.getPotionID() == MorePotionsMod.doubleJump.id)
			{
				if (living instanceof EntityPlayer)
				{
					boolean canJump = !living.isPlayerSleeping() && living.isJumping;
					if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && living.motionY < 0.07 && !hasJumped && canJump)  //Waaaaaay more checks than necessary
					{
						double motionY = 0.41999998688697815D;

						if (living.isPotionActive(Potion.jump))
						{
							motionY += (double)((float)(living.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
						}
						//checks for armour/abilities...
						living.addVelocity(0, motionY, 0);
						living.setAir(0);
						hasJumped = true;
					}
					if(living.onGround)
					{
						hasJumped = false;
					}
				}
			}
			else if (effect.getPotionID() == MorePotionsMod.antiHunger.id)
			{
				if (living instanceof EntityPlayer && ((int)tick) % 40 == 0) //True every 2 seconds
				{
					((EntityPlayer)living).getFoodStats().addStats(1, 0.1F);
				}
			}
			else if (effect.getPotionID() == MorePotionsMod.explosiveness.id)
			{
				if (((int)tick) % 40 == 0)
				{
					living.worldObj.createExplosion(living, living.posX, living.posY, living.posZ, (effect.getAmplifier() + 1) * 2, true);
				}
			}
			else if (effect.getPotionID() == MorePotionsMod.random.id)
			{
				if (randomMode == 0)
				{
					this.addEffectQueue.clear();
					this.addEffectQueue.add(Brewing.effectBrewings.get(living.getRNG().nextInt(Brewing.effectBrewings.size() - 1)).getEffect());
					this.removeEffectQueue.add(MorePotionsMod.random.id);
				}
				else
				{
					if (((int)tick) % 40 == 0)
					{
						PotionEffect pe = Brewing.combinableEffects.get(living.getRNG().nextInt(Brewing.effectBrewings.size() - 1)).getEffect();
						if (pe.getDuration() >= 2)
						{
							pe = new PotionEffect(pe.getPotionID(), 2*20, pe.getAmplifier());
						}
						this.addEffectQueue.add(pe);
					}
				}
			}
			tick += 1F / ((float)living.getActivePotionEffects().size());
		}

		public boolean canHandle(PotionEffect effect)
		{
			return true;
		}
	}

	public class MorePotionsModEventHandler
	{		
		@ForgeSubscribe(priority = EventPriority.LOW)
		public void onEntityDamaged(LivingAttackEvent event) 
		{
			if (event.entityLiving.isPotionActive(MorePotionsMod.doubleLife))
			{
				if (event.entityLiving.getHealth() - event.ammount <= 0)
				{
					event.setCanceled(true);
					event.entityLiving.setEntityHealth(event.entityLiving.getMaxHealth());
					event.entityLiving.removePotionEffect(MorePotionsMod.doubleLife.id);
					if (event.entityLiving instanceof EntityPlayer)
					{
						((EntityPlayer)event.entityLiving).addChatMessage("<\u00a7kCLASHSOFT\u00a7r>: \u00a7bYour life has just been saved by a magical power. Be careful next time, it wont help you again.");
					}
				}
			}
			if (event.entityLiving.isPotionActive(MorePotionsMod.ironSkin))
			{
				event.entityLiving.addPotionEffect(new PotionEffect(Potion.resistance.id, event.entityLiving.getActivePotionEffect(MorePotionsMod.ironSkin).getDuration(), 2));
				if (event.source == DamageSource.inFire || event.source == DamageSource.onFire)
				{
					event.entityLiving.extinguish();
					event.setCanceled(true);
				}
			}
			if (event.entityLiving.isPotionActive(MorePotionsMod.obsidianSkin))
			{
				event.entityLiving.addPotionEffect(new PotionEffect(Potion.resistance.id, event.entityLiving.getActivePotionEffect(MorePotionsMod.obsidianSkin).getDuration(), 2));
				if (event.source == DamageSource.lava || event.source == DamageSource.inFire || event.source == DamageSource.onFire)
				{
					event.entityLiving.extinguish();
					event.setCanceled(true);
				}
			}
		}
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
			for(int i=0; i < craftMatrix.getSizeInventory(); i++)
			{               
				if(craftMatrix.getStackInSlot(i) != null)
				{
					ItemStack j = craftMatrix.getStackInSlot(i);
					if(j.getItem() != null && j.getItem() == MorePotionsMod.mortar)
					{
						ItemStack k = new ItemStack(MorePotionsMod.mortar, 2, (j.getItemDamage() + 1));
						if(k.getItemDamage() >= k.getMaxDamage())
						{
							k.stackSize--;
						}
						craftMatrix.setInventorySlotContents(i, k);
					}
				}
			}
		}

		@Override
		public void onSmelting(EntityPlayer player, ItemStack item) {}	
	}
}