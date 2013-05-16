package clashsoft.mods.morepotions;

import java.awt.event.KeyEvent;
import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import clashsoft.clashsoftapi.CSLang;
import clashsoft.clashsoftapi.CustomItem;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
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
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;
import net.minecraft.util.DamageSource;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

@Mod(modid = "MorePotionsMod", name = "MorePotionsMod", version = "1.5.2")
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
	public static int potionStackSize = 1;

	public static int BrewingStand2_ID = 11;
	public static int Mixer_ID = 12;
	public static int Cauldron2_ID = 13;
	public static int UnbrewingStand_ID = 14;
	public static int SplashPotion2_ID = EntityRegistry.findGlobalUniqueEntityId();
	public static int PotionsTab_ID = CreativeTabs.getNextID();
	
	public static CreativeTabs potions = new CreativeTabs(PotionsTab_ID, "morepotions");

	public static Potion fire = new Potion2("potion.fire", true, 0xFFE500, false, 2, 2);
	public static Potion effectRemove = new Potion2("potion.effectRemove", false, 0xFFFFFF, false, 3, 2);
	public static Potion waterWalking = new Potion2("potion.waterWalking", false, 0x124EFE, false, 4, 2);
	public static Potion coldness = new Potion2("potion.coldness", false, 0x00DDFF, false, 5, 2);
	public static Potion ironSkin = new Potion2("potion.ironSkin", false, 0xD8D8D8, false, 6, 2);
	public static Potion obsidianSkin = new Potion2("potion.obsidianSkin", false, 0x101023, false, 7, 2);
	public static Potion doubleJump = new Potion2("potion.doubleJump", false, 0x123456, false, 8, 2);

	public static Block brewingStand2;
	public static Block mixxer;
	public static Block cauldron2;
	public static Block unbrewingStand;
	public static Item brewingStand2Item;
	public static ItemPotion2 potion2;
	public static ItemGlassBottle2 glassBottle2;
	public static Item dust;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		BrewingStand2_ID = config.get("TileEntityIDs", "BrewingStand2TEID", 11).getInt();
		Mixer_ID = config.get("TileEntityIDs", "MixerTEID", 12).getInt();
		Cauldron2_ID = config.get("TileEntityIDs", "Cauldron2TEID", 13).getInt();
		UnbrewingStand_ID =config.get("TileEntityIDs", "UnbrewingStandTEID", 14).getInt();

		multiPotions = config.get("Potions", "MultiPotions", false, "If true, potions with 2 different effects are shown in the creative inventory.").getBoolean(false);
		advancedPotionInfo = config.get("Potions", "AdvancedPotionInfo", true).getBoolean(true);
		animatedPotionLiquid = config.get("Potions", "AnimatedPotionLiquid", true).getBoolean(true);
		showAllBaseBrewings = config.get("Potions", "ShowAllBaseBrewings", false, "If true, all base potions are shown in creative inventory.").getBoolean(false);
		defaultAwkwardBrewing = config.get("Potions", "DefaultAwkwardBrewing", false, "If true, all potions can be brewed with an awkward potion.").getBoolean(false);
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

		mixxer = (new BlockMixer(190)).setUnlocalizedName("mixxer").setCreativeTab(CreativeTabs.tabBrewing);

		//Block.blocksList[Block.cauldron.blockID] = null;
		//cauldron2 = (new BlockCauldron2(Block.cauldron.blockID)).setHardness(2.0F).setUnlocalizedName("cauldron");;

		unbrewingStand = (new BlockUnbrewingStand(191)).setUnlocalizedName("unbrewingstand").setCreativeTab(null);

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
		
		dust = new CustomItem(17500, new String[]{"dustIron", "dustObsidian"}, new String[]{"dustIron", "dustObsidian"}).setCreativeTab(CreativeTabs.tabMaterials);
		OreDictionary.registerOre("dustIron", new ItemStack(dust, 1, 0));
		OreDictionary.registerOre("dustObsidian", new ItemStack(dust, 1, 1));

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
		ModLoader.addDispenserBehavior(potion2, new DispenserBehaviorPotion());
	}

	private void addLocalizations()
	{
		CSLang.addTranslation("itemGroup.morepotions", "Mixed Potions");

		CSLang.addTranslation("potion.potency.4", "V");
		CSLang.addTranslation("potion.potency.5", "VI");
		CSLang.addTranslation("potion.potency.6", "VII");
		CSLang.addTranslation("potion.potency.7", "VIII");
		CSLang.addTranslation("potion.potency.8", "IX");
		CSLang.addTranslation("potion.potency.9", "X");

		CSLang.addTranslation("tile.mixer.name", "Mixer");
		CSLang.addTranslation("tile.mixer.name", "de_DE", "Mischer");
		CSLang.addTranslation("tile.mixer.name", "es_ES", "Mezclador");
		CSLang.addTranslation("tile.unbrewingstand.name", "Unbrewing Stand");
		CSLang.addTranslation("tile.unbrewingstand.name", "de_DE", "Entbrau-Maschine");
		CSLang.addTranslation("item.obsidianDust.name", "Obsidian Dust");
		CSLang.addGermanTranslation("item.obsidianDust.name", "Obsidianstaub");
		CSLang.addTranslation("item.ironDust.name", "Iron Dust");
		CSLang.addGermanTranslation("item.ironDust.name", "Eisenstaub");

		CSLang.addTranslation("potion.fire.postfix", "Potion of Fire");
		CSLang.addTranslation("potion.fire.postfix", "de_DE", "Trank des Feuers");
		CSLang.addTranslation("potion.fire.postfix", "es_ES", "Poci\u00F3n del fuego");
		CSLang.addTranslation("potion.fire", "Fire");
		CSLang.addTranslation("potion.fire", "de_DE", "Feuer");
		CSLang.addTranslation("potion.fire", "es_ES", "Fuego");

		CSLang.addTranslation("potion.effectRemove.postfix", "Potion of Effect Removing");
		CSLang.addTranslation("potion.effectRemove.postfix", "de_DE", "Trank der Effektentfernung");
		CSLang.addTranslation("potion.effectRemove.postfix", "es_ES", "Poci\u00F3n de no effectos");
		CSLang.addTranslation("potion.effectRemove", "Effect Removing");
		CSLang.addTranslation("potion.effectRemove", "de_DE", "Effektentfernung");
		CSLang.addTranslation("potion.effectRemove", "es_ES", "Eliminaci\u00F3n de los effectos");

		CSLang.addTranslation("potion.waterWalking.postfix", "Potion of Water Walking");
		CSLang.addTranslation("potion.waterWalking.postfix", "de_DE", "Trank des \u00dcberwasserlaufens");
		CSLang.addTranslation("potion.waterWalking.postfix", "es_ES", "Poci\u00F3n de irse sobre el agua");
		CSLang.addTranslation("potion.waterWalking", "Water Walking");
		CSLang.addTranslation("potion.waterWalking", "de_DE", "\u00dcberwasserlaufen");
		CSLang.addTranslation("potion.waterWalking", "es_ES", "Ir sobre el agua");

		CSLang.addTranslation("potion.coldness.postfix", "Potion of Coldness");
		CSLang.addTranslation("potion.coldness.postfix", "de_DE", "Trank der K\u00e4lte");
		CSLang.addTranslation("potion.coldness.postfix", "es_ES", "Poci\u00F3n de la frialdad");
		CSLang.addTranslation("potion.coldness", "Coldness");
		CSLang.addTranslation("potion.coldness", "de_DE", "K\u00e4lte");
		CSLang.addTranslation("potion.coldness", "es_ES", "Frialdad");
		
		CSLang.addTranslation("potion.ironSkin.postfix", "Potion of Iron Skin");
		CSLang.addGermanTranslation("potion.ironSkin.postfix", "Trank der Eisenhaut");
		CSLang.addTranslation("potion.ironSkin", "Iron Skin");
		CSLang.addGermanTranslation("potion.ironSkin", "Eisenhaut");
		
		CSLang.addTranslation("potion.doubleJump", "Double Jump");
		CSLang.addGermanTranslation("potion.doubleJump", "Doppelsprung");
		CSLang.addTranslation("potion.doubleJump.postfix", "Potion of Double Jump");
		CSLang.addGermanTranslation("potion.doubleJump.postfix", "Trank des Doppelsprungs");
		
		CSLang.addTranslation("potion.obsidianSkin.postfix", "Potion of Obsidian Skin");
		CSLang.addGermanTranslation("potion.obsidianSkin.postfix", "Trank der Obsidianhaut");
		CSLang.addTranslation("potion.obsidianSkin", "Obsidian Skin");
		CSLang.addGermanTranslation("potion.obsidianSkin", "Obsidianhaut");

		CSLang.addTranslation("potion.goodeffects", "Good Effects");
		CSLang.addTranslation("potion.goodeffects", "de_DE", "Gute Effekte");
		CSLang.addTranslation("potion.goodeffects", "es_ES", "Buenos Effectos");
		CSLang.addTranslation("potion.negativeEffects", "Bad Effects");
		CSLang.addTranslation("potion.negativeEffects", "de_DE", "Schlechte Effekte");
		CSLang.addTranslation("potion.negativeEffects", "es_ES", "Malos Effectos");
		CSLang.addTranslation("potion.potionof", "Potion of");
		CSLang.addTranslation("potion.potionof", "de_DE", "Trank von");
		CSLang.addTranslation("potion.potionof", "es_ES", "Poci\u00F3n del");
		CSLang.addTranslation("potion.effects", "Effects");
		CSLang.addTranslation("potion.effects", "de_DE", "Effekten");
		CSLang.addTranslation("potion.effects", "es_ES", "Effectos");
		CSLang.addTranslation("potion.and", "and");
		CSLang.addTranslation("potion.and", "de_DE", "und");
		CSLang.addTranslation("potion.and", "es_ES", "y");
		CSLang.addTranslation("potion.useto", "Used to make");
		CSLang.addTranslation("potion.useto", "de_DE", "Benutzt für");

		CSLang.addTranslation("potion.highestamplifier", "Highest Amplifier");
		CSLang.addTranslation("potion.highestamplifier", "de_DE", "Gr\u00f6\u00dftes Level");
		CSLang.addTranslation("potion.highestamplifier", "es_ES", "Alto Nivel");
		CSLang.addTranslation("potion.averageamplifier", "Average Amplifier");
		CSLang.addTranslation("potion.averageamplifier", "de_DE", "Durchschnittliches Level");
		CSLang.addTranslation("potion.averageamplifier", "es_ES", "Nivel promedio");
		CSLang.addTranslation("potion.highestduration", "Highest Duration");
		CSLang.addTranslation("potion.highestduration", "de_DE", "H\u00f6chste Dauer");
		CSLang.addTranslation("potion.highestduration", "es_ES", "Alto Duraci\u00F3n");
		CSLang.addTranslation("potion.averageduration", "Average Duration");
		CSLang.addTranslation("potion.averageduration", "de_DE", "Durchschnittliche Dauer");
		CSLang.addTranslation("potion.averageduration", "es_ES", "Duraci\u00f3n promedio");
		CSLang.addTranslation("potion.value", "Value");
		CSLang.addTranslation("potion.value", "de_DE", "Wert");
		CSLang.addTranslation("potion.value", "es_ES", "Valor");

		CSLang.addTranslation("potion.alleffects.postfix", "Potion of all Effects");
		CSLang.addTranslation("potion.alleffects.postfix", "de_DE", "Trank aller Effekte");
		CSLang.addTranslation("potion.alleffects.postfix", "es_ES", "Poci\u00F3n de todos los efectos");
	}

	public static class MorePotionsModEffectHandler implements IPotionEffectHandler
	{
		private static boolean hasJumped = false;
		
		@ForgeSubscribe
		public void onPotionUpdate(EntityLiving living, PotionEffect effect)
		{
			if (effect.getPotionID() == (MorePotionsMod.fire.id))
			{
				living.setFire(4);
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
			if (effect.getPotionID() == (MorePotionsMod.waterWalking.id))
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
			if (effect.getPotionID() == (MorePotionsMod.coldness.id))
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
			if (effect.getPotionID() == MorePotionsMod.doubleJump.id)
			{
				if (living instanceof EntityPlayer)
				{
					if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && (living.isJumping || living.isAirBorne) && living.motionY < 0.07 && !hasJumped)  //Waaaaaay more checks than necessary
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
			if (event.entityLiving.isPotionActive(MorePotionsMod.ironSkin) || event.entityLiving.isPotionActive(MorePotionsMod.obsidianSkin));
			{
				if (event.source == DamageSource.inFire || event.source == DamageSource.onFire)
				{
					event.entityLiving.extinguish();
					event.setCanceled(true);
				}
			}
			if (event.entityLiving.isPotionActive(MorePotionsMod.obsidianSkin))
			{
				if (event.source == DamageSource.lava)
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


}