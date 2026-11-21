package dev.arcaneascension;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Configure crafted scrolls by changing icon choice and particle palette. */
public class SpellWorkbenchBlock extends Block {
    private static final int[] COLORS={0xB78DFF,0xFF614A,0x60C9FF,0x76FF9F,0xFFD467,0xFF83D5};
    public SpellWorkbenchBlock(Properties props){super(props);}
    @Override public InteractionResult use(BlockState state,Level level,BlockPos pos,Player player,InteractionHand hand,BlockHitResult hit){
        if(level.isClientSide) return InteractionResult.SUCCESS;
        ItemStack held=player.getItemInHand(hand);
        if(held.is(ExpansionItems.SPELL_SCROLL.get())){
            var tag=held.getOrCreateTag();
            if(player.isShiftKeyDown()){
                int index=(tag.getInt("Icon")+1)%6;
                tag.putInt("Icon",index);
                tag.putInt("CustomModelData",index+1);
                player.displayClientMessage(Component.literal("Scroll icon #"+(index+1)),true);
            } else {
                int old=tag.getInt("Palette");int index=(old+1)%COLORS.length;
                tag.putInt("Palette",index);tag.putInt("Color",COLORS[index]);
                player.displayClientMessage(Component.literal("Particle palette #"+(index+1)),true);
            }
        } else player.displayClientMessage(Component.literal("Hold a spell scroll; click to cycle colors, sneak-click to change icon."),true);
        return InteractionResult.CONSUME;
    }
}
