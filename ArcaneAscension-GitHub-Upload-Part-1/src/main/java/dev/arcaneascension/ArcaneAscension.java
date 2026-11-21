package dev.arcaneascension;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ArcaneAscension.ID)
public class ArcaneAscension {
    public static final String ID = "arcaneascension";
    public ArcaneAscension() {
        ExpansionItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ExpansionItems.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().register(ExpansionItems.class);
        Network.register();
    }
}
