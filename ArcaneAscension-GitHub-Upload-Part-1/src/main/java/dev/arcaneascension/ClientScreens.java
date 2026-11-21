package dev.arcaneascension;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientScreens {
    private ClientScreens() {}
    public static void openGuide() { Minecraft.getInstance().setScreen(new GuideScreen()); }
}
