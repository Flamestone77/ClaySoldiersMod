/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.claysoldiers.network;

import de.sanandrew.core.manpack.util.javatuples.Quartet;
import de.sanandrew.core.manpack.util.javatuples.Quintet;
import de.sanandrew.core.manpack.util.javatuples.Septet;
import de.sanandrew.mods.claysoldiers.network.packet.PacketParticleFX;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

public final class ParticlePacketSender
{
    public static void sendSoldierDeathFx(double x, double y, double z, int dimension, String team) {
        PacketProcessor.sendToAllAround(PacketProcessor.PKG_PARTICLES, dimension, x, y, z, 64.0D, Quintet.with(PacketParticleFX.FX_SOLDIER_DEATH, x, y, z, team));
    }

    public static void sendBreakFx(double x, double y, double z, int dimension, Item item) {
        PacketProcessor.sendToAllAround(PacketProcessor.PKG_PARTICLES, dimension, x, y, z, 64.0D,
                                        Quintet.with(PacketParticleFX.FX_BREAK, x, y, z, Item.itemRegistry.getNameForObject(item))
        );
    }

    public static void sendDiggingFx(double x, double y, double z, int dimension, Block block) {
        PacketProcessor.sendToAllAround(PacketProcessor.PKG_PARTICLES, dimension, x, y, z, 64.0D,
                                        Quintet.with(PacketParticleFX.FX_DIGGING, x, y, z, Block.blockRegistry.getNameForObject(block))
        );
    }

    public static void sendSpellFx(double x, double y, double z, int dimension, double red, double green, double blue) {
        PacketProcessor.sendToAllAround(PacketProcessor.PKG_PARTICLES, dimension, x, y, z, 64.0D,
                                        Septet.with(PacketParticleFX.FX_SPELL, x, y, z, red, green, blue)
        );
    }

    public static void sendCritFx(double x, double y, double z, int dimension) {
        PacketProcessor.sendToAllAround(PacketProcessor.PKG_PARTICLES, dimension, x, y, z, 64.0D, Quartet.with(PacketParticleFX.FX_CRIT, x, y, z));
    }

    public static void sendHorseDeathFx(double x, double y, double z, int dimension, byte type) {
        PacketProcessor.sendToAllAround(PacketProcessor.PKG_PARTICLES, dimension, x, y, z, 64.0D, Quintet.with(PacketParticleFX.FX_HORSE_DEATH, x, y, z, type));
    }

    public static void sendShockwaveFx(double x, double y, double z, float yOff, int dimension) {
        PacketProcessor.sendToAllAround(PacketProcessor.PKG_PARTICLES, dimension, x, y, z, 64.0D,
                                        Quartet.with(PacketParticleFX.FX_SHOCKWAVE, MathHelper.floor_double(x),
                                                     MathHelper.floor_double(y - 0.20000000298023224D - yOff), MathHelper.floor_double(z)));
    }
}