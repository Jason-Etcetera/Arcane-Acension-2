package dev.arcaneascension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=ArcaneAscension.ID)
public final class ExpansionEvents {
    @SubscribeEvent public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        if(!player.getPersistentData().getBoolean("arcaneascension_book_given")) {
            player.getInventory().add(new ItemStack(ExpansionItems.ARCANE_COMPENDIUM.get()));
            player.getPersistentData().putBoolean("arcaneascension_book_given",true);
        }
    }
}
