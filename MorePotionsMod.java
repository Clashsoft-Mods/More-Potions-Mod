package clashsoft.mods.morepotions;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.src.ModLoader;
import net.minecraft.block.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;

@Mod(modid = "MorePotionsMod", name = "MorePotionsMod", version = "1.5")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class MorePotionsMod
{
	@Instance("MorePotionsMod")
	public static MorePotionsMod INSTANCE;

	@SidedProxy(clientSide = "clashsoft.mods.morepotions.ClientProxy", serverSide = "clashsoft.mods.morepotions.CommonProxy")
	public static CommonProxy proxy;

	public static boolean multiPotions = false;
	public static boolean advancedPotionInfo = false;
	public static boolean animatedPotionLiquid = true;
	public static boolean showAllBaseBrewings = false;
	public static boolean defaultAwkwardBrewing = false;

	public static int BrewingStand2_ID = 11;
	public static int Mixer_ID = 12;
	public static int Cauldron2_ID = 13;
	public static int SP2_ID = EntityRegistry.findGlobalUniqueEntityId();
	public static int PT_ID = CreativeTabs.getNextID();

	public static Potion fire = new Potion2("potion.fire", true, 0xFFE500, false, 2, 2);
	public static Potion effectRemove = new Potion2("potion.effectRemove", false, 0xFFFFFF, false, 3, 2);
	public static Potion waterWalking = new Potion2("potion.waterWalking", false, 0x124EFE, false, 4, 2);
	public static Potion coldness = new Potion2("potion.coldness", false, 0x00DDFF, false, 5, 2);

	public static Block brewingStand2;
	public static Block mixxer;
	public static Block cauldron2;
	public static Item brewingStand2Item;
	public static ItemPotion2 potion2;
	public static ItemGlassBottle2 glassBottle2;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		BrewingStand2_ID = config.get("TileEntityIDs", "BrewingStand2TEID", 11).getInt();
		Mixer_ID = config.get("TileEntityIDs", "MixerTEID", 12).getInt();
		Cauldron2_ID = config.get("TileEntityIDs", "Cauldron2TEID", 13).getInt();

		multiPotions = config.get("Potions", "MultiPotions", false, "If true, potions with 2 different effects are shown in the creative inventory.").getBoolean(false);
		advancedPotionInfo = config.get("Potions", "AdvancedPotionInfo", false).getBoolean(false);
		animatedPotionLiquid = config.get("Potions", "AnimatedPotionLiquid", true).getBoolean(true);
		showAllBaseBrewings = config.get("Potions", "ShowAllBaseBrewings", false, "If true, all base potions are shown in creative inventory.").getBoolean(false);
		defaultAwkwardBrewing = config.get("Potions", "DefaultAwkwardBrewing", false, "If true, all potions can be brewed with an awkward potion.").getBoolean(false);

		config.save();
	}
	@Init
	public void load(FMLInitializationEvent event)
	{	
		Brewing.registerBrewings();
		BrewingAPI.registerIngredientHandler(new MorePotionsModIngredientHandler());

		GameRegistry.registerTileEntity(TileEntityBrewingStand2.class, "BrewingStand2");
		GameRegistry.registerTileEntity(TileEntityMixer.class, "Mixxer");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "Cauldron2");
		MinecraftForge.EVENT_BUS.register(new MorePotionsModEventHooks());
		EntityRegistry.registerGlobalEntityID(EntityPotion2.class, "SplashPotion2", MorePotionsMod.SP2_ID);
		EntityRegistry.registerModEntity(EntityPotion2.class, "SplashPotion2", MorePotionsMod.SP2_ID, this, 100, 20, true);

		Block.blocksList[Block.brewingStand.blockID] = null;
		brewingStand2 = (new BlockBrewingStand2(Block.brewingStand.blockID)).setHardness(0.5F).setLightValue(0.125F).setUnlocalizedName("brewingStand");
		Block.blocksList[Block.cauldron.blockID] = null;
		cauldron2 = (new BlockCauldron2(Block.cauldron.blockID)).setHardness(2.0F).setUnlocalizedName("cauldron");;

		mixxer = (new BlockMixer(190)).setUnlocalizedName("mixxer").setCreativeTab(CreativeTabs.tabBrewing);
		ModLoader.registerBlock(brewingStand2);
		ModLoader.registerBlock(mixxer);
		ModLoader.addRecipe(new ItemStack(mixxer), new Object[] {"gSg", "g g", "SiS", 'g', Block.thinGlass, 'S', Block.stone, 'i', Item.ingotIron});

		Item.itemsList[Item.brewingStand.itemID] = null;
		brewingStand2Item = (new ItemReed(123, brewingStand2)).setUnlocalizedName("brewingStand").setCreativeTab(CreativeTabs.tabBrewing);

		Item.itemsList[Item.cauldron.itemID] = null;
		brewingStand2Item = (new ItemReed(124, cauldron2)).setUnlocalizedName("cauldron").setCreativeTab(CreativeTabs.tabBrewing);

		Item.itemsList[Item.potion.itemID - 256] = null;
		potion2 = (ItemPotion2)(new ItemPotion2(117)).setUnlocalizedName("potion");

		Item.itemsList[Item.glassBottle.itemID - 256] = null;
		glassBottle2 = (ItemGlassBottle2) (new ItemGlassBottle2(118)).setUnlocalizedName("glassBottle");

		Item.sugar.setCreativeTab(CreativeTabs.tabBrewing);
		Item.netherStalkSeeds.setCreativeTab(CreativeTabs.tabBrewing);

		addLocalizations();

		NetworkRegistry.instance().registerGuiHandler(INSTANCE, proxy);
		proxy.registerRenderInformation();
		proxy.registerRenderers();

		ModLoader.addDispenserBehavior(potion2, new DispenserBehaviorPotion());
	}

	private void addLocalizations()
	{
		LanguageRegistry.instance().addStringLocalization("itemGroup.morepotions", "Mixed Potions");

		LanguageRegistry.instance().addStringLocalization("potion.potency.4", "V");
		LanguageRegistry.instance().addStringLocalization("potion.potency.5", "VI");
		LanguageRegistry.instance().addStringLocalization("potion.potency.6", "VII");
		LanguageRegistry.instance().addStringLocalization("potion.potency.7", "VIII");
		LanguageRegistry.instance().addStringLocalization("potion.potency.8", "IX");
		LanguageRegistry.instance().addStringLocalization("potion.potency.9", "X");

		LanguageRegistry.instance().addStringLocalization("container.mixxer", "Mixer");
		LanguageRegistry.instance().addStringLocalization("container.mixxer", "de_DE", "Mischer");
		LanguageRegistry.instance().addStringLocalization("container.mixxer", "es_ES", "Mezclador");
		LanguageRegistry.instance().addStringLocalization("tile.mixxer.name", "Mixer");
		LanguageRegistry.instance().addStringLocalization("tile.mixxer.name", "de_DE", "Mischer");
		LanguageRegistry.instance().addStringLocalization("tile.mixxer.name", "es_ES", "Mezclador");

		LanguageRegistry.instance().addStringLocalization("potion.fire.postfix", "Potion of Fire");
		LanguageRegistry.instance().addStringLocalization("potion.fire.postfix", "de_DE", "Trank des Feuers");
		LanguageRegistry.instance().addStringLocalization("potion.fire.postfix", "es_ES", "Poci\u00F3n del fuego");
		LanguageRegistry.instance().addStringLocalization("potion.fire", "Fire");
		LanguageRegistry.instance().addStringLocalization("potion.fire", "de_DE", "Feuer");
		LanguageRegistry.instance().addStringLocalization("potion.fire", "es_ES", "Fuego");

		LanguageRegistry.instance().addStringLocalization("potion.effectRemove.postfix", "Potion of Effect Removing");
		LanguageRegistry.instance().addStringLocalization("potion.effectRemove.postfix", "de_DE", "Trank der Effektentfernung");
		LanguageRegistry.instance().addStringLocalization("potion.effectRemove.postfix", "es_ES", "Poci\u00F3n de no effectos");
		LanguageRegistry.instance().addStringLocalization("potion.effectRemove", "Effect Removing");
		LanguageRegistry.instance().addStringLocalization("potion.effectRemove", "de_DE", "Effektentfernung");
		LanguageRegistry.instance().addStringLocalization("potion.effectRemove", "es_ES", "Eliminaci\u00F3n de los effectos");

		LanguageRegistry.instance().addStringLocalization("potion.waterWalking.postfix", "Potion of Water Walking");
		LanguageRegistry.instance().addStringLocalization("potion.waterWalking.postfix", "de_DE", "Trank des \u00dcberwasserlaufens");
		LanguageRegistry.instance().addStringLocalization("potion.waterWalking.postfix", "es_ES", "Poci\u00F3n de irse sobre el agua");
		LanguageRegistry.instance().addStringLocalization("potion.waterWalking", "Water Walking");
		LanguageRegistry.instance().addStringLocalization("potion.waterWalking", "de_DE", "\u00dcberwasserlaufen");
		LanguageRegistry.instance().addStringLocalization("potion.waterWalking", "es_ES", "Ir sobre el agua");

		LanguageRegistry.instance().addStringLocalization("potion.coldness.postfix", "Potion of Coldness");
		LanguageRegistry.instance().addStringLocalization("potion.coldness.postfix", "de_DE", "Trank der K\u00e4lte");
		LanguageRegistry.instance().addStringLocalization("potion.coldness.postfix", "es_ES", "Poci\u00F3n de la frialdad");
		LanguageRegistry.instance().addStringLocalization("potion.coldness", "Coldness");
		LanguageRegistry.instance().addStringLocalization("potion.coldness", "de_DE", "K\u00e4lte");
		LanguageRegistry.instance().addStringLocalization("potion.coldness", "es_ES", "Frialdad");

		LanguageRegistry.instance().addStringLocalization("potion.goodeffects", "Good Effects");
		LanguageRegistry.instance().addStringLocalization("potion.goodeffects", "de_DE", "Gute Effekte");
		LanguageRegistry.instance().addStringLocalization("potion.goodeffects", "es_ES", "Buenos Effectos");
		LanguageRegistry.instance().addStringLocalization("potion.negativeEffects", "Bad Effects");
		LanguageRegistry.instance().addStringLocalization("potion.negativeEffects", "de_DE", "Schlechte Effekte");
		LanguageRegistry.instance().addStringLocalization("potion.negativeEffects", "es_ES", "Malos Effectos");
		LanguageRegistry.instance().addStringLocalization("potion.potionof", "Potion of");
		LanguageRegistry.instance().addStringLocalization("potion.potionof", "de_DE", "Trank von");
		LanguageRegistry.instance().addStringLocalization("potion.potionof", "es_ES", "Poci\u00F3n del");
		LanguageRegistry.instance().addStringLocalization("potion.effects", "Effects");
		LanguageRegistry.instance().addStringLocalization("potion.effects", "de_DE", "Effekten");
		LanguageRegistry.instance().addStringLocalization("potion.effects", "es_ES", "Effectos");
		LanguageRegistry.instance().addStringLocalization("potion.and", "and");
		LanguageRegistry.instance().addStringLocalization("potion.and", "de_DE", "und");
		LanguageRegistry.instance().addStringLocalization("potion.and", "es_ES", "y");

		LanguageRegistry.instance().addStringLocalization("potion.highestamplifier", "Highest Amplifier");
		LanguageRegistry.instance().addStringLocalization("potion.highestamplifier", "de_DE", "Gr\u00f6\u00dftes Level");
		LanguageRegistry.instance().addStringLocalization("potion.highestamplifier", "es_ES", "Alto Nivel");
		LanguageRegistry.instance().addStringLocalization("potion.averageamplifier", "Average Amplifier");
		LanguageRegistry.instance().addStringLocalization("potion.averageamplifier", "de_DE", "Durchschnittliches Level");
		LanguageRegistry.instance().addStringLocalization("potion.averageamplifier", "es_ES", "Nivel promedio");
		LanguageRegistry.instance().addStringLocalization("potion.highestduration", "Highest Duration");
		LanguageRegistry.instance().addStringLocalization("potion.highestduration", "de_DE", "H\u00f6chste Dauer");
		LanguageRegistry.instance().addStringLocalization("potion.highestduration", "es_ES", "Alto Duraci\u00F3n");
		LanguageRegistry.instance().addStringLocalization("potion.averageduration", "Average Duration");
		LanguageRegistry.instance().addStringLocalization("potion.averageduration", "de_DE", "Durchschnittliche Dauer");
		LanguageRegistry.instance().addStringLocalization("potion.averageduration", "es_ES", "Duraci\u00f3n promedio");
		LanguageRegistry.instance().addStringLocalization("potion.value", "Value");
		LanguageRegistry.instance().addStringLocalization("potion.value", "de_DE", "Wert");
		LanguageRegistry.instance().addStringLocalization("potion.value", "es_ES", "Valor");

		LanguageRegistry.instance().addStringLocalization("potion.alleffects.postfix", "Potion of all Effects");
		LanguageRegistry.instance().addStringLocalization("potion.alleffects.postfix", "de_DE", "Trank aller Effekte");
		LanguageRegistry.instance().addStringLocalization("potion.alleffects.postfix", "es_ES", "Poci\u00F3n de todos los efectos");
	}

	public class MorePotionsModEventHooks
	{
		@ForgeSubscribe
		public void onEntityUpdate(LivingUpdateEvent event)
		{
			if (event.entityLiving.isPotionActive(MorePotionsMod.fire))
			{
				event.entityLiving.setFire(4);
			}
			if (event.entityLiving.isPotionActive(MorePotionsMod.effectRemove))
			{
				for (int i = 0; i < Potion.potionTypes.length; i++)
				{
					if (i != MorePotionsMod.effectRemove.id)
					{
						event.entityLiving.removePotionEffect(i);
					}
				}
			}
			if (event.entityLiving.isPotionActive(MorePotionsMod.waterWalking))
			{
				if (event.entityLiving.isPotionActive(MorePotionsMod.waterWalking))
				{
					int x = (int) Math.floor(event.entityLiving.posX);
					int y = (int) (event.entityLiving.posY - event.entityLiving.getYOffset());
					int z = (int) Math.floor(event.entityLiving.posZ);
					if (event.entityLiving.worldObj.getBlockId(x, y - 1, z) == 9 && event.entityLiving.worldObj.getBlockId(x, y, z) == 0)
					{
						if (event.entityLiving.motionY < 0 && event.entityLiving.boundingBox.minY < y)
						{
							event.entityLiving.motionY = 0;
							event.entityLiving.fallDistance = 0;
							event.entityLiving.onGround = true;
							if (event.entityLiving.isSneaking())
							{
								event.entityLiving.motionY -= 0.1F;
							}
						}
					}
				}
			}
			if (event.entityLiving.isPotionActive(MorePotionsMod.coldness.id))
			{
				int x = (int) Math.floor(event.entityLiving.posX);
				int y = (int) (event.entityLiving.posY - event.entityLiving.getYOffset());
				int z = (int) Math.floor(event.entityLiving.posZ);
				int id = event.entityLiving.worldObj.getBlockId(x, y - 1, z);
				if (id == Block.waterMoving.blockID || id == Block.waterStill.blockID)
				{
					event.entityLiving.worldObj.setBlock(x, y - 1, z, Block.ice.blockID);
				}
				if (event.entityLiving.getActivePotionEffect(MorePotionsMod.coldness).getAmplifier() > 0 && Block.blocksList[id] != null && Block.blocksList[id].isBlockSolidOnSide(event.entityLiving.worldObj, x, y - 1, z, ForgeDirection.UP) && event.entityLiving.worldObj.getBlockId(x, y, z) == 0)
				{
					event.entityLiving.worldObj.setBlock(x, y, z, Block.snow.blockID);
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
}