package clashsoft.mods.morepotions.inventory;

import clashsoft.brewingapi.inventory.slot.SlotPotion;
import clashsoft.cslib.minecraft.inventory.ContainerInventory;
import clashsoft.cslib.minecraft.inventory.SlotOutput;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;

public class ContainerMixer extends ContainerInventory
{
	private TileEntityMixer	mixer;
	
	private int				brewTime	= 0;
	
	public ContainerMixer(EntityPlayer player, TileEntityMixer mixer)
	{
		super(player, mixer);
		this.mixer = mixer;
		this.addSlotToContainer(new SlotPotion(player, mixer, 0, 56, 23));
		this.addSlotToContainer(new SlotPotion(player, mixer, 1, 79, 16));
		this.addSlotToContainer(new SlotPotion(player, mixer, 2, 102, 23));
		this.addSlotToContainer(new SlotOutput(mixer, 3, 79, 52));
		
		this.addInventorySlots();
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.mixer.getMixTime());
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			
			if (this.brewTime != this.mixer.getMixTime())
			{
				icrafting.sendProgressBarUpdate(this, 0, this.mixer.getMixTime());
			}
		}
		
		this.brewTime = this.mixer.getMixTime();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int type, int value)
	{
		if (type == 0)
		{
			this.mixer.setMixTime(value);
		}
	}
	
	@Override
	public int[] merge(EntityPlayer player, int slot, ItemStack stack)
	{
		if (SlotPotion.canHoldPotion(stack))
		{
			return new int[] { 0, 3 };
		}
		return null;
	}
}
