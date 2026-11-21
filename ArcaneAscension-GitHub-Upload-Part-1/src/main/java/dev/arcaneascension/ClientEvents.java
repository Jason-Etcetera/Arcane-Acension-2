package dev.arcaneascension;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = ArcaneAscension.ID, value = Dist.CLIENT)
public final class ClientEvents {
    public static final String CATEGORY = "key.categories.arcaneascension";
    public static final KeyMapping TREE = new KeyMapping("key.arcaneascension.tree", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);
    public static final KeyMapping CAST = new KeyMapping("key.arcaneascension.cast", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY);
    public static final KeyMapping CYCLE = new KeyMapping("key.arcaneascension.cycle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY);
    @Mod.EventBusSubscriber(modid = ArcaneAscension.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Registration {
        @SubscribeEvent public static void keyMappings(RegisterKeyMappingsEvent event) {
            event.register(TREE); event.register(CAST); event.register(CYCLE);
        }
    }
    @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) return;
        while (TREE.consumeClick()) { Network.sendAction("sync", ""); minecraft.setScreen(new SkillTreeScreen()); }
        while (CAST.consumeClick()) Network.sendAction("cast", "");
        while (CYCLE.consumeClick()) Network.sendAction("cycle", "");
    }
}
