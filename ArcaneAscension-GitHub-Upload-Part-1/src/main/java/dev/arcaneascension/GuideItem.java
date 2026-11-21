package dev.arcaneascension;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GuideItem extends Item {
    public GuideItem(Properties props) { super(props); }
    @Override public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide) ClientScreens.openGuide();
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), world.isClientSide);
    }
}
