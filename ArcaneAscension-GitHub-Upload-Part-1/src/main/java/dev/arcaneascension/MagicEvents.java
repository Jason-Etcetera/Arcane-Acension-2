package dev.arcaneascension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneAscension.ID)
public final class MagicEvents {
    private static final UUID HEALTH_ID=UUID.fromString("01c2e8a1-1541-4b3d-8b77-1910a6e60baf");
    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) MagicData.sync(player);
    }
    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) MagicData.sync(player);
    }
    @SubscribeEvent
    public static void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) MagicData.sync(player);
    }
    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            MagicData.copy(event.getOriginal(), event.getEntity());
            event.getOriginal().invalidateCaps();
        }
    }
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) return;
        CompoundTag d = MagicData.get(player);
        if (player.tickCount % 40 == 0) {
            var health=player.getAttribute(Attributes.MAX_HEALTH);
            if (health!=null) {
                int bonus=Skills.bonusHealth(d);
                health.removeModifier(HEALTH_ID);
                if (bonus>0) health.addTransientModifier(new AttributeModifier(HEALTH_ID,"Arcane passive vitality",bonus,AttributeModifier.Operation.ADDITION));
            }
        }
        if (player.tickCount % (Skills.REGEN.learned(d) ? 5 : 10) == 0) {
            int mana = d.getInt("mana");
            if (mana < MagicData.maxMana(d)) d.putInt("mana", mana + 1);
        }
        if (player.tickCount % 20 == 0) MagicData.sync(player);
    }
}
