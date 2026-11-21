package dev.arcaneascension;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkEvent;

public final class Network {
    private Network() {}
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(ArcaneAscension.ID, "main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
    public static void register() {
        int index = 0;
        CHANNEL.messageBuilder(Action.class, index++, NetworkDirection.PLAY_TO_SERVER)
            .encoder((packet, buf) -> { buf.writeUtf(packet.action); buf.writeUtf(packet.argument); })
            .decoder(buf -> new Action(buf.readUtf(32), buf.readUtf(64)))
            .consumerMainThread((packet, context) -> {
                ServerPlayer player = context.get().getSender();
                if (player == null) return;
                switch (packet.action) {
                    case "cast" -> Spells.cast(player);
                    case "cycle" -> Spells.cycle(player);
                    case "sync" -> MagicData.sync(player);
                    case "unlock" -> {
                        try { MagicData.unlock(player, Skills.valueOf(packet.argument)); }
                        catch (IllegalArgumentException ignored) { }
                    }
                }
            }).add();
        CHANNEL.messageBuilder(State.class, index, NetworkDirection.PLAY_TO_CLIENT)
            .encoder((packet, buf) -> buf.writeNbt(packet.data))
            .decoder(buf -> new State(buf.readNbt()))
            .consumerMainThread((packet, context) -> ClientMagicData.update(packet.data)).add();
    }
    public static void sendAction(String action, String argument) { CHANNEL.sendToServer(new Action(action, argument)); }
    public static void sendState(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new State(MagicData.get(player).copy()));
    }
    public record Action(String action, String argument) { }
    public record State(CompoundTag data) { }
}
