/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.util.SAPUtils;
import de.sanandrew.core.manpack.util.javatuples.*;
import de.sanandrew.mods.claysoldiers.entity.mount.EnumHorseType;
import de.sanandrew.mods.claysoldiers.network.packet.PacketParticleFX;
import de.sanandrew.mods.claysoldiers.util.soldier.ClaymanTeam;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public final class ParticleHelper
{
    @SuppressWarnings("unchecked")
    public static void spawnParticles(byte particleId, Tuple particleData) {
        Minecraft mc = Minecraft.getMinecraft();

        switch( particleId ) {                                                      // TODO: use lambdas (Java 8) instead of this when Java 7 gets its EOL!
            case PacketParticleFX.FX_BREAK:
                spawnBreakFx((Quartet) particleData, mc);
                break;
            case PacketParticleFX.FX_CRIT:
                spawnCritFx((Triplet) particleData, mc);
                break;
            case PacketParticleFX.FX_SOLDIER_DEATH:
                spawnSoldierDeathFx((Quartet) particleData, mc);
                break;
            case PacketParticleFX.FX_HORSE_DEATH:
                spawnHorseDeathFx((Quartet) particleData, mc);
                break;
            case PacketParticleFX.FX_DIGGING:
                spawnDiggingFx((Quartet) particleData, mc);
                break;
            case PacketParticleFX.FX_SPELL:
                spawnSpellFx((Sextet) particleData, mc);
                break;
            case PacketParticleFX.FX_NEXUS:
                spawnNexusFx((Sextet) particleData, mc);
                break;
            case PacketParticleFX.FX_SHOCKWAVE:
                spawnShockwaveFx((Triplet) particleData, mc);
                break;
        }
    }

    public static void spawnShockwaveFx(Triplet particleData, Minecraft mc) {
        int x = (int) particleData.getValue0();
        int y = (int) particleData.getValue1();
        int z = (int) particleData.getValue2();

        Block block = mc.theWorld.getBlock(x, y, z);

        if (block.getMaterial() != Material.air) {
            double radius = (double)Math.min(0.2F + 4.0F / 15.0F, 10.0F);

            if( radius > 2.5D ) {
                radius = 2.5D;
            }

            int l1 = (int)(150.0D * radius);

            for( int i2 = 0; i2 < l1; ++i2 ) {
                float rad = MathHelper.randomFloatClamp(SAPUtils.RNG, 0.0F, ((float) Math.PI * 2F));
                double multi = (double)MathHelper.randomFloatClamp(SAPUtils.RNG, 0.75F, 1.0F);
                double partY = 0.20000000298023224D + radius / 100.0D;
                double partX = (double)(MathHelper.cos(rad) * 0.2F) * multi * multi * (radius + 0.2D);
                double partZ = (double)(MathHelper.sin(rad) * 0.2F) * multi * multi * (radius + 0.2D);

                mc.theWorld.spawnParticle("blockdust_" + Block.getIdFromBlock(block) + "_" + mc.theWorld.getBlockMetadata(x, y, z),
                                          (double)((float)x + 0.5F), (double)((float)y + 1.0F), (double)((float)z + 0.5F), partX, partY, partZ);
            }
        }
    }

    public static void spawnBreakFx(Quartet<Double, Double, Double, String> data, Minecraft mc) {
        Item item = (Item) Item.itemRegistry.getObject(data.getValue3());

        for (int i = 0; i < 5; i++) {
            EntityBreakingFX fx = new EntityBreakingFX(mc.theWorld, data.getValue0(), data.getValue1(), data.getValue2(), item);
            mc.effectRenderer.addEffect(fx);
        }
    }

    public static void spawnDiggingFx(Quartet<Double, Double, Double, String> data, Minecraft mc) {
        Block block = (Block) Block.blockRegistry.getObject(data.getValue3());

        for (int i = 0; i < 8; i++) {
            EntityDiggingFX fx = new EntityDiggingFX(mc.theWorld, data.getValue0(), data.getValue1(), data.getValue2(), SAPUtils.RNG.nextGaussian() * 0.15D,
                                                     SAPUtils.RNG.nextDouble() * 0.2D, SAPUtils.RNG.nextGaussian() * 0.15D, block, 0);
            mc.effectRenderer.addEffect(fx);
        }
    }

    public static void spawnCritFx(Triplet<Double, Double, Double> data, Minecraft mc) {
        for (int i = 0; i < 10; i++) {
            double motX = SAPUtils.RNG.nextDouble() - 0.5D;
            double motY = SAPUtils.RNG.nextDouble() * 0.5D;
            double motZ = SAPUtils.RNG.nextDouble() - 0.5D;
            EntityCritFX fx = new EntityCritFX(mc.theWorld, data.getValue0(), data.getValue1(), data.getValue2(), motX, motY, motZ);
            mc.effectRenderer.addEffect(fx);
        }
    }

    public static void spawnSoldierDeathFx(Quartet<Double, Double, Double, String> data, Minecraft mc) {
        ClaymanTeam team = ClaymanTeam.getTeamFromName(data.getValue3());

        for (int i = 0; i < 10; i++) {
            EntitySoldierDeathFX fx = new EntitySoldierDeathFX(mc.theWorld, data.getValue0(), data.getValue1(), data.getValue2(), team);
            mc.effectRenderer.addEffect(fx);
        }
    }

    public static void spawnHorseDeathFx(Quartet<Double, Double, Double, Byte> data, Minecraft mc) {
        EnumHorseType type = EnumHorseType.values[data.getValue3()];

        for (int i = 0; i < 5; i++) {
            EntityHorseDeathFX fx = new EntityHorseDeathFX(mc.theWorld, data.getValue0(), data.getValue1(), data.getValue2(), type);
            mc.effectRenderer.addEffect(fx);
        }
    }

    public static void spawnSpellFx(Sextet<Double, Double, Double, Double, Double, Double> data, Minecraft mc) {
        for (int i = 0; i < 4; i++) {
            EntitySpellParticleFX fx = new EntitySpellParticleFX(mc.theWorld, data.getValue0(), data.getValue1() - SAPUtils.RNG.nextDouble() * 0.2D,
                                                                 data.getValue2(), data.getValue3(), data.getValue4(), data.getValue5());
            mc.effectRenderer.addEffect(fx);
        }
    }

    public static void spawnNexusFx(Sextet<Double, Double, Double, Float, Float, Float> data, Minecraft mc) {
        EntityNexusFX fx = new EntityNexusFX(mc.theWorld,
                                             data.getValue0() + 0.2F + SAPUtils.RNG.nextDouble() * 0.6F,
                                             data.getValue1(),
                                             data.getValue2() + 0.2F + SAPUtils.RNG.nextDouble() * 0.6F,
                                             0.1F + SAPUtils.RNG.nextFloat() * 0.2F,
                                             data.getValue3(),
                                             data.getValue4(),
                                             data.getValue5(),
                                             true, 1.0F);
        fx.motionY = 0.02F;
        mc.effectRenderer.addEffect(fx);
    }
}