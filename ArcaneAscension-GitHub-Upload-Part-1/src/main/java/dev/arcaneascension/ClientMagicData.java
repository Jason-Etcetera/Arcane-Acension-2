package dev.arcaneascension;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientMagicData {
    private static CompoundTag state = new CompoundTag();
    private ClientMagicData() {}
    public static CompoundTag get() { return state; }
    public static void update(CompoundTag data) { state = data == null ? new CompoundTag() : data.copy(); }
}
