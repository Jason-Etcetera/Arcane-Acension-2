package dev.arcaneascension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;

public class SpellScrollItem extends Item {
    @Override public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        CompoundTag tag=stack.getTag();
        if(tag!=null){
            list.add(Component.literal("Spell: "+tag.getString("Shape")+"  |  Ritual tier: "+tag.getInt("Tier")));
            list.add(Component.literal("Icon: "+(tag.getInt("Icon")+1)+"  Color: #"+String.format("%06X",tag.getInt("Color")&0xFFFFFF)));
        }
        super.appendHoverText(stack,world,list,flag);
    }
    public SpellScrollItem(Properties properties) { super(properties); }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            int color = tag.contains("Color") ? tag.getInt("Color") : 0xA477FF;
            player.getPersistentData().putInt("arcaneascension_cast_color", color);
            String shape=tag.getString("Shape");
            if(shape.equals("Heal")) {
                MagicData.get(player).putBoolean("scroll_heal",true);
            } else if(shape.equals("Blink")) {
                MagicData.get(player).putBoolean("scroll_blink",true);
            }
            if (player instanceof net.minecraft.server.level.ServerPlayer sp) MagicData.sync(sp);
            player.displayClientMessage(Component.literal("Spell color selected: #"+String.format("%06X",color&0xFFFFFF)), true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
