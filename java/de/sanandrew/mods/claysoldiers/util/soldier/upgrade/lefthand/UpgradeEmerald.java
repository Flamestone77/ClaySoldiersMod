/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.util.soldier.upgrade.lefthand;

import de.sanandrew.core.manpack.util.client.helpers.ItemRenderHelper;
import de.sanandrew.mods.claysoldiers.entity.EntityClayMan;
import de.sanandrew.mods.claysoldiers.entity.projectile.EntityEmeraldChunk;
import de.sanandrew.mods.claysoldiers.entity.projectile.ISoldierProjectile;
import de.sanandrew.mods.claysoldiers.tileentity.TileEntityClayNexus;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.IThrowableUpgrade;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.SoldierUpgradeInst;
import de.sanandrew.mods.claysoldiers.util.soldier.upgrade.SoldierUpgrades;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.ArrayList;

public class UpgradeEmerald
        extends AUpgradeLeftHanded
        implements IThrowableUpgrade
{
    private ItemStack p_nexusItem = new ItemStack(Blocks.emerald_block);

    @Override
    public void onConstruct(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst) {
        upgradeInst.getNbtTag().setShort(NBT_USES, (short) 5);
    }

    @Override
    public void getAttackRange(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst, Entity target, MutableFloat attackRange) {
        boolean isInRange = target.getDistanceSqToEntity(clayMan) <= (clayMan.hasUpgrade(SoldierUpgrades.UPG_SUGARCANE) ? 64.0D : 16.0D);
        if( target instanceof EntityLivingBase && !target.isDead && clayMan.canEntityBeSeen(target) && ((EntityLivingBase) target).getHealth() > 0 && isInRange ) {
            clayMan.throwSomethingAtEnemy(((EntityLivingBase) target), EntityEmeraldChunk.class, clayMan.hasUpgrade(SoldierUpgrades.UPG_SUGARCANE));
            clayMan.attackTime = 30;
            upgradeInst.getNbtTag().setShort(NBT_USES, (short) (upgradeInst.getNbtTag().getShort(NBT_USES) - 1));
        }
    }

    @Override
    public boolean onUpdate(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst) {
        return upgradeInst.getNbtTag().getShort(NBT_USES) == 0;
    }

    @Override
    public void onPickup(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst, ItemStack stack) {
        if( stack.getItem() == Item.getItemFromBlock(Blocks.emerald_block) ) {
            upgradeInst.getNbtTag().setShort(NBT_USES, (short) 45);
        }

        this.consumeItem(stack, upgradeInst);
        clayMan.playSound("random.pop", 1.0F, 1.0F);
    }

    @Override
    public void getAiMoveSpeed(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst, MutableFloat speed) {
        Entity target = clayMan.getEntityToAttack();
        if( target instanceof EntityLivingBase && !target.isDead && clayMan.canEntityBeSeen(target) && ((EntityLivingBase) target).getHealth() > 0 ) {
            float multiplier = Math.min(1.0F, Math.max(-1.0F, (float) clayMan.getDistanceSqToEntity(target) - 16.0F));
            speed.setValue(speed.getValue() * multiplier);
        }
    }

    @Override
    public Class<? extends ISoldierProjectile<? extends EntityThrowable>> getThrowableClass() {
        return EntityEmeraldChunk.class;
    }

    @Override
    public void renderNexusThrowable(TileEntityClayNexus nexus, float partTicks) {
        ItemRenderHelper.renderItemIn3D(this.p_nexusItem);
    }

    @Override
    public void onItemDrop(EntityClayMan clayMan, SoldierUpgradeInst upgradeInst, ArrayList<ItemStack> droppedItems) {
        // TODO: drop it when unused or?
        int maxUses = upgradeInst.getStoredItem().getItem() == Item.getItemFromBlock(Blocks.emerald_block) ? 45 : 5;
        if( upgradeInst.getNbtTag().getShort(NBT_USES) == maxUses ) {
            droppedItems.add(upgradeInst.getStoredItem());
        }
    }
}
