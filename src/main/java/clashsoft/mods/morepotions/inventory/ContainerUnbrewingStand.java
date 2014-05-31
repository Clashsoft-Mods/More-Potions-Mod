package clashsoft.mods.morepotions.inventory;

import clashsoft.brewingapi.inventory.slot.SlotPotion;
import clashsoft.cslib.minecraft.inventory.ContainerInventory;
import clashsoft.cslib.minecraft.inventory.SlotOutput;
import clashsoft.mods.morepotions.tileentity.TileEntityUnbrewingStand;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;

public class ContainerUnbrewingStand extends ContainerInventory
{
	private TileEntityUnbrewingStand	tileEntity;
	private int							time	= 0;
	
	public ContainerUnbrewingStand(EntityPlayer player, TileEntityUnbrewingStand tileEntity)
	{
		super(player, tileEntity);
		this.tileEntity = tileEntity;
		this.addSlotToContainer(new SlotPotion(player, tileEntity, 0, 17, 8));
		this.addSlotToContainer(new SlotOutput(tileEntity, 1, 83, 54));
		this.addSlotToContainer(new SlotOutput(tileEntity, 2, 61, 54));
		this.addSlotToContainer(new SlotOutput(tileEntity, 3, 39, 54));
		this.addSlotToContainer(new SlotOutput(tileEntity, 4, 17, 54));
		
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new SlotOutput(tileEntity, 5 + j + i * 3, 107 + j * 18, 18 + i * 18));
			}
		}
		
		this.addInventorySlots();
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getUnbrewTime());
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		boolean flag = this.time != this.tileEntity.getUnbrewTime();
		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			
			if (flag)
			{
				icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getUnbrewTime());
			}
		}
		
		this.time = this.tileEntity.getUnbrewTime();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int time)
	{
		if (id == 0)
		{
			this.time = time;
			this.tileEntity.setBrewTime(time);
		}
	}
	
	@Override
	public int[] merge(EntityPlayer player, int slot, ItemStack stack)
	{
		if (SlotPotion.canHoldPotion(stack))
		{
			return new int[] { 0 };
		}
		return null;
	}
}
