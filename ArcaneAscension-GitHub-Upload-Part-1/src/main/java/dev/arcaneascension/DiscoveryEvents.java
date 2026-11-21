package dev.arcaneascension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=ArcaneAscension.ID)
public final class DiscoveryEvents {
    @SubscribeEvent public static void onDrops(LivingDropsEvent event) {
        if(event.getEntity() instanceof Player || !(event.getSource().getEntity() instanceof Player)) return;
        if(event.getEntity().getRandom().nextInt(75)!=0) return;
        ItemStack scroll=new ItemStack(ExpansionItems.SPELL_SCROLL.get());
        int tier=1+event.getEntity().getRandom().nextInt(3);
        CompoundTag tag=scroll.getOrCreateTag();
        tag.putInt("Tier",tier);
        tag.putString("Shape",tier==3?"Blink":tier==2?"Heal":"Bolt");
        tag.putInt("Color",tier==3?0x72BBFF:tier==2?0x83FF99:0xCC80FF);
        var entity=event.getEntity();
        event.getDrops().add(new ItemEntity(entity.level(),entity.getX(),entity.getY(),entity.getZ(),scroll));
    }
    @SubscribeEvent public static void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result=event.getCrafting();
        if(!result.is(ExpansionItems.SPELL_SCROLL.get())) return;
        for(int i=0;i<event.getInventory().getContainerSize();i++) {
            ItemStack input=event.getInventory().getItem(i);
            if(input.getItem() instanceof DyeItem dye) {
                result.getOrCreateTag().putInt("Color",dye.getDyeColor().getTextColor());
                result.getOrCreateTag().putInt("Palette",dye.getDyeColor().getId());
                break;
            }
        }
    }
}
