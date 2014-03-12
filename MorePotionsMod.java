package clashsoft.mods.morepotions;

import clashsoft.brewingapi.BrewingAPI;
import clashsoft.cslib.minecraft.block.CSBlocks;
import clashsoft.cslib.minecraft.crafting.CSCrafting;
import clashsoft.cslib.minecraft.item.CSItems;
import clashsoft.cslib.minecraft.item.CSStacks;
import clashsoft.cslib.minecraft.item.CustomItem;
import clashsoft.cslib.minecraft.potion.CustomPotion;
import clashsoft.cslib.minecraft.update.CSUpdate;
import clashsoft.cslib.minecraft.util.CSConfig;
import clashsoft.cslib.util.CSLog;
import clashsoft.cslib.util.CSUtil;
import clashsoft.mods.morepotions.block.BlockCauldron2;
import clashsoft.mods.morepotions.block.BlockMixer;
import clashsoft.mods.morepotions.block.BlockUnbrewingStand;
import clashsoft.mods.morepotions.brewing.MPMEffectHandler;
import clashsoft.mods.morepotions.brewing.MPMIngredientHandler;
import clashsoft.mods.morepotions.brewing.MPMPotionList;
import clashsoft.mods.morepotions.common.MPMCommonProxy;
import clashsoft.mods.morepotions.common.MPMEventHandler;
import clashsoft.mods.morepotions.item.ItemMortar;
import clashsoft.mods.morepotions.network.MPMPacketHandler;
import clashsoft.mods.morepotions.tileentity.TileEntityCauldron;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = MorePotionsMod.MODID, name = MorePotionsMod.NAME, version = MorePotionsMod.VERSION)
public class MorePotionsMod
{
	public static final String			MODID						= "morepotions";
	public static final String			NAME						= "More Potions Mod";
	public static final String			ACRONYM						= "mpm";
	public static final int				REVISION					= 0;
	public static final String			VERSION						= CSUpdate.CURRENT_VERSION + "-" + REVISION;
	
	@Instance(MODID)
	public static MorePotionsMod		INSTANCE;
	
	@SidedProxy(clientSide = "clashsoft.mods.morepotions.client.MPMClientProxy", serverSide = "clashsoft.mods.morepotions.common.MPMCommonProxy")
	public static MPMCommonProxy		proxy;
	
	public static MPMPacketHandler		netHandler					= new MPMPacketHandler();
	
	public static String				customEffects				= "morepotions:gui/potions.png";
	
	// Configurables
	public static int					randomMode					= 0;
	
	public static int					mixerTileEntityID			= 12;
	public static int					cauldronTileEntityID		= 13;
	public static int					unbrewingStandTileEntityID	= 14;
	
	public static boolean				cauldronInfo				= false;
	
	static
	{
		BrewingAPI.load();
	}
	
	public static Potion				fire						= new CustomPotion("potion.fire", true, 0xFFE500, false, customEffects, 0, 0);
	public static Potion				effectRemove				= new CustomPotion("potion.effectRemove", false, 0xFFFFFF, false, customEffects, 1, 0);
	public static Potion				waterWalking				= new CustomPotion("potion.waterWalking", false, 0x124EFE, false, customEffects, 2, 0);
	public static Potion				coldness					= new CustomPotion("potion.coldness", false, 0x00DDFF, false, customEffects, 3, 0);
	public static Potion				ironSkin					= new CustomPotion("potion.ironSkin", false, 0xD8D8D8, false, customEffects, 4, 0);
	public static Potion				obsidianSkin				= new CustomPotion("potion.obsidianSkin", false, 0x101023, false, customEffects, 5, 0);
	public static Potion				doubleLife					= new CustomPotion("potion.doubleLife", false, 0xFF2222, false, customEffects, 7, 0, CSUtil.fontColorInt(0, 0, 1, 1));
	public static Potion				explosiveness				= new CustomPotion("potion.explosiveness", true, 0xCC0000, false, customEffects, 1, 1);
	public static Potion				random						= new CustomPotion("potion.random", false, 0x000000, randomMode == 0, customEffects, 2, 1, CSUtil.fontColorInt(0, 1, 1, 1));
	public static Potion				thorns						= new CustomPotion("potion.thorns", false, 0x810081, false, customEffects, 3, 1);
	public static Potion				greenThumb					= new CustomPotion("potion.greenThumb", false, 0x008100, false, customEffects, 4, 1);
	public static Potion				projectile					= new CustomPotion("potion.projectile", false, 0x101010, false, customEffects, 5, 1);
	public static Potion				doubleJump					= new CustomPotion("potion.doubleJump", false, 0x157490, false, customEffects, 6, 1);
	
	public static BlockMixer			mixer;
	public static BlockCauldron2		cauldron2;
	public static BlockUnbrewingStand	unbrewingStand;
	
	public static Item					dust;
	public static Item					mortar;
	
	public static ItemStack				dustCoal;
	public static ItemStack				dustIron;
	public static ItemStack				dustGold;
	public static ItemStack				dustObsidian;
	public static ItemStack				dustDiamond;
	public static ItemStack				dustEmerald;
	public static ItemStack				dustQuartz;
	public static ItemStack				dustWither;
	public static ItemStack				dustEnderpearl;
	public static ItemStack				dustClay;
	public static ItemStack				dustBrick;
	public static ItemStack				dustFlint;
	public static ItemStack				dustGlass;
	public static ItemStack				dustCharcoal;
	public static ItemStack				dustWoodOak;
	public static ItemStack				dustWoodBirch;
	public static ItemStack				dustWoodSpruce;
	public static ItemStack				dustWoodJungle;
	public static ItemStack				dustNetherstar;
	public static ItemStack				dustNetherbrick;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		CSConfig.loadConfig(event.getSuggestedConfigurationFile());
		
		randomMode = CSConfig.getInt("Potions", "RandomPotionMode", "Determines how the random potion works, if this is 0 the effect is instant and you get a random potion effect when you drink the potion, 1 will give you a new effect every 2 seconds.", 0);
		cauldronInfo = CSConfig.getBool("Cauldrons", "CauldronInfo", true);
		
		CSConfig.saveConfig();
		
		cauldron2 = (BlockCauldron2) new BlockCauldron2().setBlockName("cauldron").setBlockTextureName("cauldron");
		mixer = (BlockMixer) new BlockMixer().setBlockName("mixer");
		unbrewingStand = (BlockUnbrewingStand) new BlockUnbrewingStand().setBlockName("unbrewingStand");
		
		mortar = new ItemMortar().setUnlocalizedName("mortar").setTextureName("morepotions:mortar");
		dust = new CustomItem(new String[] {
				"coal_dust",
				"iron_dust",
				"gold_dust",
				"diamond_dust",
				"emerald_dust",
				"obsidian_dust",
				"quratz_dust",
				"wither_dust",
				"enderpearl_dust",
				"clay_dust",
				"brick_dust",
				"flint_dust",
				"glass_dust",
				"charcoal_dust",
				"oak_wood_dust",
				"birch_wood_dust",
				"spruce_wood_dust",
				"jungle_wood_dust",
				"nether_star_dust",
				"nether_brick_dust" }, "morepotions").setCreativeTab(CreativeTabs.tabMaterials);
		
		// new String[] {
		// "C2",
		// "Fe",
		// "Au",
		// "C128",
		// "Be3Al2Si6O18",
		// "MgFeSi2O8",
		// "SiO2",
		// "\u00a7k???",
		// "BeK4N5Cl6",
		// "Na2LiAl2Si2",
		// "Na2LiAl2Si2",
		// "SiO2",
		// "SiO4",
		// "C",
		// "",
		// "",
		// "",
		// "",
		// "",
		// "" }
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		CSBlocks.replaceBlock(Blocks.cauldron, cauldron2);
		CSBlocks.addBlock(mixer, ItemBlock.class, "mixer");
		CSBlocks.addBlock(unbrewingStand, ItemBlock.class, "unbrewing_stand");
		
		CSItems.addItem(mortar, "mortar");
		CSItems.addItem(dust, "dust");
		
		BrewingAPI.registerEffectHandler(new MPMEffectHandler());
		BrewingAPI.registerIngredientHandler(new MPMIngredientHandler());
		
		GameRegistry.registerTileEntityWithAlternatives(TileEntityMixer.class, "Mixer", "Mixxer");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "Cauldron2");
		GameRegistry.registerTileEntity(TileEntityUnbrewingStand.class, "UnbrewingStand");
		
		Items.sugar.setCreativeTab(CreativeTabs.tabBrewing);
		Items.nether_wart.setCreativeTab(CreativeTabs.tabBrewing);
		
		this.addRecipes();
		this.registerPotionTypes();
		
		MinecraftForge.EVENT_BUS.register(new MPMEventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, proxy);
		proxy.registerRenderInformation();
		proxy.registerRenderers();
	}
	
	private void registerPotionTypes()
	{
		CSLog.info("Initializing MorePotionsMod PotionTypes");
		MPMPotionList.initializePotionBases();
		MPMPotionList.initializePotionTypes();
		
		CSLog.info("Registering MorePotionsMod PotionTypes");
		MPMPotionList.registerPotionBases();
		MPMPotionList.registerPotionTypes();
	}
	
	private void addRecipes()
	{
		CSCrafting.addCrafting(new ItemStack(mortar), new Object[] {
				"SfS",
				" S ",
				'S',
				Blocks.stone,
				'f',
				Items.flint });
		CSCrafting.addCrafting(new ItemStack(mixer), new Object[] {
				"gSg",
				"g g",
				"SiS",
				'g',
				Blocks.glass_pane,
				'S',
				Blocks.stone,
				'i',
				Items.iron_ingot });
		CSCrafting.addCrafting(new ItemStack(unbrewingStand), new Object[] {
				"bib",
				"bpb",
				"bbb",
				'b',
				Items.brick,
				'i',
				Items.iron_ingot,
				'p',
				Items.glass_bottle });
		
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
		
		CSCrafting.addShapelessCrafting(dustCoal, CSStacks.coal, mortarStack);
		CSCrafting.addShapelessCrafting(dustIron, CSStacks.iron_ingot, mortarStack);
		CSCrafting.addShapelessCrafting(dustGold, CSStacks.gold_ingot, mortarStack);
		CSCrafting.addShapelessCrafting(dustDiamond, CSStacks.diamond, mortarStack);
		CSCrafting.addShapelessCrafting(dustEmerald, CSStacks.emerald, mortarStack);
		CSCrafting.addShapelessCrafting(dustObsidian, CSStacks.obsidian, mortarStack);
		CSCrafting.addShapelessCrafting(dustQuartz, CSStacks.quartz, mortarStack);
		CSCrafting.addShapelessCrafting(dustWither, CSStacks.wither_skull, dustCoal, dustQuartz);
		CSCrafting.addShapelessCrafting(dustEnderpearl, CSStacks.ender_pearl, mortarStack);
		CSCrafting.addShapelessCrafting(dustClay, CSStacks.clay, mortarStack);
		CSCrafting.addShapelessCrafting(dustBrick, CSStacks.brick, mortarStack);
		CSCrafting.addShapelessCrafting(dustFlint, CSStacks.flint, mortarStack);
		CSCrafting.addShapelessCrafting(dustGlass, CSStacks.glass_block, mortarStack);
		CSCrafting.addShapelessCrafting(dustCharcoal, CSStacks.char_coal, mortarStack);
		CSCrafting.addShapelessCrafting(dustWoodOak, CSStacks.oak_planks, mortarStack);
		CSCrafting.addShapelessCrafting(dustWoodBirch, CSStacks.birch_planks, mortarStack);
		CSCrafting.addShapelessCrafting(dustWoodSpruce, CSStacks.spruce_planks, mortarStack);
		CSCrafting.addShapelessCrafting(dustWoodJungle, CSStacks.jungle_planks, mortarStack);
		CSCrafting.addShapelessCrafting(dustNetherstar, CSStacks.nether_star, mortarStack);
		CSCrafting.addShapelessCrafting(dustNetherbrick, CSStacks.nether_brick, mortarStack);
		
		CSCrafting.addSmelting(dustIron, CSStacks.iron_ingot, 0F);
		CSCrafting.addSmelting(dustGold, CSStacks.gold_ingot, 0F);
		CSCrafting.addSmelting(dustQuartz, CSStacks.quartz, 0F);
		CSCrafting.addSmelting(dustClay, CSStacks.brick, 0.1F);
		CSCrafting.addSmelting(dustBrick, CSStacks.brick, 0F);
		CSCrafting.addSmelting(dustGlass, CSStacks.glass_block, 0F);
	}
}