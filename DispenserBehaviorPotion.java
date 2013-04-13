package clashsoft.mods.morepotions;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

final class DispenserBehaviorPotion implements IBehaviorDispenseItem
{
    private final BehaviorDefaultDispenseItem field_96458_b = new BehaviorDefaultDispenseItem();

    /**
     * Dispenses the specified ItemStack from a dispenser.
     */
    public ItemStack dispense(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        return ItemPotion2.isSplash(par2ItemStack.getItemDamage()) ? (new DispenserBehaviorPotionProjectile(this, par2ItemStack)).dispense(par1IBlockSource, par2ItemStack) : this.field_96458_b.dispense(par1IBlockSource, par2ItemStack);
    }
}
