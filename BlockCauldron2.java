package clashsoft.mods.morepotions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockCauldron2 extends BlockContainer
{
	@SideOnly(Side.CLIENT)
    public Icon field_94378_a;
    @SideOnly(Side.CLIENT)
    public Icon field_94376_b;
    @SideOnly(Side.CLIENT)
    public Icon field_94377_c;
	
    public BlockCauldron2(int par1)
    {
        super(par1, Material.iron);
        this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
    }
    
    @Override
    public int getRenderType()
    {
    	return ClientProxy.cauldronRenderType;
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 == 1 ? this.field_94376_b : (par1 == 0 ? this.field_94377_c : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.field_94378_a = par1IconRegister.registerIcon("cauldron_inner");
        this.field_94376_b = par1IconRegister.registerIcon("cauldron_top");
        this.field_94377_c = par1IconRegister.registerIcon("cauldron_bottom");
        this.blockIcon = par1IconRegister.registerIcon("cauldron_side");
    }
    
    @SideOnly(Side.CLIENT)
    public static Icon func_94375_b(String par0Str)
    {
        return par0Str == "cauldron_inner" ? ((BlockCauldron2)MorePotionsMod.cauldron2).field_94378_a : (par0Str == "cauldron_bottom" ? ((BlockCauldron2)MorePotionsMod.cauldron2).field_94377_c : null);
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        float f = 0.125F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBoundsForItemRender();
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else if (par1World.getBlockTileEntity(par2, par3, par4) != null && par1World.getBlockTileEntity(par2, par3, par4) instanceof TileEntityCauldron)
        {
            ItemStack itemstack = par5EntityPlayer.inventory.getCurrentItem();
            TileEntityCauldron te = (TileEntityCauldron)par1World.getBlockTileEntity(par2, par3, par4);
            boolean flag = false;

            if (itemstack == null)
            {
                flag = false;
            }
            else
            {
                int i1 = par1World.getBlockMetadata(par2, par3, par4);

                if (itemstack.itemID == Item.bucketWater.itemID)
                {
                    if (i1 < 3)
                    {
                        if (!par5EntityPlayer.capabilities.isCreativeMode)
                        {
                            par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, new ItemStack(Item.bucketEmpty));
                        }

                        par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
                    }
                    flag = true;
                    te.addIngredient(itemstack);
                }
                else if (itemstack.itemID == Item.glassBottle.itemID)
                {
                	if (i1 > 0)
                	{
                		ItemStack itemstack1 = te.brew();

                		if (!par5EntityPlayer.inventory.addItemStackToInventory(itemstack1))
                		{
                			par1World.spawnEntityInWorld(new EntityItem(par1World, (double)par2 + 0.5D, (double)par3 + 1.5D, (double)par4 + 0.5D, itemstack1));
                		}
                		else if (par5EntityPlayer instanceof EntityPlayerMP)
                		{
                			((EntityPlayerMP)par5EntityPlayer).sendContainerToPlayer(par5EntityPlayer.inventoryContainer);
                		}

                		--itemstack.stackSize;

                		if (itemstack.stackSize <= 0)
                		{
                			par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, (ItemStack)null);
                		}

                		par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 - 1, 2);
                		flag = true;
                	}
                	else
                	{
                		te.water = true;
                		te.brewings.clear();
                	}
                }
                else if (i1 > 0 && itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getArmorMaterial() == EnumArmorMaterial.CLOTH)
                {
                	ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                	itemarmor.removeColor(itemstack);
                	par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 - 1, 2);
                	flag = true;
                }
                else
                {
                	if (te.isItemValid(itemstack))
                	{
                		te.addIngredient(itemstack);
                		te.water = false;
                		flag = true;
                	}
                }
            }
            par1World.setBlockTileEntity(par2, par3, par4, te);
            return true;
        }
        else
        {
        	return false;
        }
    }

    /**
     * currently only used by BlockCauldron to incrament meta-data during rain
     */
    public void fillWithRain(World par1World, int par2, int par3, int par4)
    {
        if (par1World.rand.nextInt(20) == 1)
        {
            int l = par1World.getBlockMetadata(par2, par3, par4);

            if (l < 3)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, l + 1, 2);
            }
        }
    }

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityCauldron();
	}
}
