package clashsoft.mods.morepotions.block;

import java.util.Random;

import clashsoft.mods.morepotions.MorePotionsMod;
import clashsoft.mods.morepotions.tileentity.TileEntityMixer;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockMixer extends BlockContainer
{
    private Random rand = new Random();
    public Icon top;
    public Icon side;
    public Icon bottom;
    
    public BlockMixer(int par1)
    {
        super(par1, Material.rock);
        this.setBlockBounds(0.25F, 0F, 0.25F, 0.75F, 0.8125F, 0.75F);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
	public int getRenderType()
    {
        return 0;
    }
    
    @Override
    public boolean canRenderInPass(int pass)
    {
    	return true;
    }
    
    @Override
    public Icon getIcon(int par1, int par2)
    {
    	return par1 == 0 ? top : par1 == 1 ? top : side;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.top = par1IconRegister.registerIcon("mixxer_top");
        this.bottom = par1IconRegister.registerIcon("mixxer_bottom");
        this.side = par1IconRegister.registerIcon("mixxer_side");
    }
    
    @Override
    public int getRenderBlockPass()
    {
    	return 0;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        par1World.setBlockTileEntity(par2, par3, par4, this.createNewTileEntity(par1World));
    }
    
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityMixer();
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityMixer var10 = (TileEntityMixer)par1World.getBlockTileEntity(par2, par3, par4);

            if (var10 != null)
            {
                FMLNetworkHandler.openGui(par5EntityPlayer, MorePotionsMod.INSTANCE, MorePotionsMod.Mixer_TEID, par1World, par2, par3, par4);
            }

            return true;
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 instanceof TileEntityMixer)
        {
            TileEntityMixer var8 = (TileEntityMixer)var7;

            for (int var9 = 0; var9 < var8.getSizeInventory(); ++var9)
            {
                ItemStack var10 = var8.getStackInSlot(var9);

                if (var10 != null)
                {
                    float var11 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var13 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (var10.stackSize > 0)
                    {
                        int var14 = this.rand.nextInt(21) + 10;

                        if (var14 > var10.stackSize)
                        {
                            var14 = var10.stackSize;
                        }

                        var10.stackSize -= var14;
                        EntityItem var15 = new EntityItem(par1World, par2 + var11, par3 + var12, par4 + var13, new ItemStack(var10.itemID, var14, var10.getItemDamage()));
                        float var16 = 0.05F;
                        var15.motionX = (float)this.rand.nextGaussian() * var16;
                        var15.motionY = (float)this.rand.nextGaussian() * var16 + 0.2F;
                        var15.motionZ = (float)this.rand.nextGaussian() * var16;
                        par1World.spawnEntityInWorld(var15);
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
	
	@Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory((IInventory)par1World.getBlockTileEntity(par2, par3, par4));
    }
}
