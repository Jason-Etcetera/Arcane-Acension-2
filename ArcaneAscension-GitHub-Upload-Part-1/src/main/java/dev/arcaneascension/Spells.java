package dev.arcaneascension;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class Spells {
    private Spells() {}
    public static void cycle(ServerPlayer player) {
        CompoundTag d = MagicData.get(player);
        int next = d.getInt("spell");
        for (int i = 0; i < 3; i++) {
            next = (next + 1) % 3;
            if (MagicData.availableSpell(d, next)) break;
        }
        d.putInt("spell", next); MagicData.sync(player);
    }
    public static void cast(ServerPlayer player) {
        CompoundTag d = MagicData.get(player);
        int spell = d.getInt("spell");
        if (!MagicData.availableSpell(d, spell)) { d.putInt("spell", 0); MagicData.sync(player); return; }
        int raw = spell == 0 ? 12 : spell == 1 ? 25 : 30;
        int cost = MagicData.cost(d, raw);
        if (d.getInt("mana") < cost) { player.displayClientMessage(net.minecraft.network.chat.Component.literal("Not enough mana!"), true); return; }
        if (spell == 2 && !blink(player, d)) { player.displayClientMessage(net.minecraft.network.chat.Component.literal("No safe blink destination."), true); return; }
        d.putInt("mana", d.getInt("mana") - cost);
        if (spell == 0) bolt(player, d);
        if (spell == 1) {
            player.heal(Skills.BASTION.learned(d) ? 9.0F : 6.0F);
            ((ServerLevel)player.level()).sendParticles(ParticleTypes.HEART, player.getX(), player.getY() + 1, player.getZ(), 12, .5, .75, .5, 0.06);
        }
        MagicData.awardXp(player, 5);
        MagicData.sync(player);
    }
    private static void bolt(ServerPlayer player, CompoundTag d) {
        ServerLevel world = player.serverLevel();
        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle().normalize();
        double range = Skills.OVERCHARGE.learned(d) ? 30 : 18;
        Vec3 far = start.add(direction.scale(range));
        HitResult hit = world.clip(new ClipContext(start, far, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        double end = hit.getType() == HitResult.Type.MISS ? range : start.distanceTo(hit.getLocation());
        LivingEntity target = null;
        double closest = end + 1;
        AABB search = new AABB(start, start.add(direction.scale(end))).inflate(1.2);
        for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, search, e -> e != player && e.isAlive())) {
            Vec3 center = entity.getBoundingBox().getCenter();
            double t = center.subtract(start).dot(direction);
            if (t < 0 || t > end || t >= closest) continue;
            Vec3 nearest = start.add(direction.scale(t));
            if (entity.getBoundingBox().inflate(.4).contains(nearest)) { target = entity; closest = t; }
        }
        if (target != null) {
            float damage = (float)((4 + (Skills.FOCUS.learned(d) ? 2 : 0) + (Skills.AMPLIFY.learned(d) ? 3 : 0)) * Skills.damageFactor(d));
            target.hurt(player.damageSources().playerAttack(player), damage);
            if (Skills.EMBER.learned(d)) target.setSecondsOnFire(4);
            end = Math.min(end, closest);
        }
        for (double t = 0.5; t < end; t += .4) {
            Vec3 point = start.add(direction.scale(t));
            int rgb=player.getPersistentData().getInt("arcaneascension_cast_color");
            if(rgb==0) rgb=0xB78DFF;
            net.minecraft.core.particles.DustParticleOptions dust = new net.minecraft.core.particles.DustParticleOptions(new org.joml.Vector3f(((rgb>>16)&255)/255f,((rgb>>8)&255)/255f,(rgb&255)/255f),1.0f);
            world.sendParticles(dust, point.x, point.y, point.z, 2, .08, .08, .08, 0.0);
        }
        Vec3 impact = start.add(direction.scale(end));
        world.sendParticles(ParticleTypes.END_ROD, impact.x, impact.y, impact.z, 9, .25, .25, .25, .05);
    }
    private static boolean blink(ServerPlayer player, CompoundTag d) {
        ServerLevel world = player.serverLevel();
        Vec3 eye = player.getEyePosition();
        Vec3 dir = player.getLookAngle().normalize();
        double range = Skills.DISTANCE.learned(d) ? 16 : 8;
        HitResult result = world.clip(new ClipContext(eye, eye.add(dir.scale(range)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 destination = result.getType() == HitResult.Type.MISS ? eye.add(dir.scale(range)) : result.getLocation().subtract(dir.scale(1.0));
        // Find a nearby standing-height space without allowing travel inside solid blocks.
        for (int dy = 0; dy <= 3; dy++) {
            Vec3 feet = new Vec3(destination.x, destination.y - player.getEyeHeight() + dy, destination.z);
            AABB candidate = player.getDimensions(player.getPose()).makeBoundingBox(feet);
            if (world.noCollision(player, candidate) && !world.getBlockState(net.minecraft.core.BlockPos.containing(feet).below()).isAir()) {
                world.sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 1, player.getZ(), 25, .4, .5, .4, .1);
                player.teleportTo(feet.x, feet.y, feet.z);
                player.fallDistance = 0;
                world.sendParticles(ParticleTypes.PORTAL, feet.x, feet.y + 1, feet.z, 25, .4, .5, .4, .1);
                return true;
            }
        }
        return false;
    }
}
