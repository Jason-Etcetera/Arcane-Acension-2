package dev.arcaneascension;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Tiered ritual: place candles/glowstone around altar, provide blank scroll + dust. */
public class SpellAltarBlock extends Block {
    public SpellAltarBlock(Properties properties) { super(properties); }
    private int tier(Level level, BlockPos pos) {
        int candles=0, glowstone=0;
        for(int dx=-2;dx<=2;dx++) for(int dz=-2;dz<=2;dz++) {
            if(dx==0&&dz==0) continue;
            BlockState block=level.getBlockState(pos.offset(dx,0,dz));
            if(block.getBlock() instanceof CandleBlock) candles++;
            if(block.is(Blocks.GLOWSTONE)) glowstone++;
        }
        return candles>=4 && glowstone>=2 ? 3 : candles>=4 ? 2 : 1;
    }
    @Override public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(level.isClientSide) return InteractionResult.SUCCESS;
        int tier=tier(level,pos);
        ItemStack held=player.getItemInHand(hand);
        if(held.is(ExpansionItems.BLANK_SCROLL.get())) {
            boolean dust=false;
            for(int i=0;i<player.getInventory().getContainerSize();i++) {
                ItemStack slot=player.getInventory().getItem(i);
                if(slot.is(ExpansionItems.ARCANE_DUST.get())) {
                    if(!player.getAbilities().instabuild) slot.shrink(1);
                    dust=true;break;
                }
            }
            if(!dust) { player.displayClientMessage(Component.literal("Ritual requires arcane dust in inventory."),true);return InteractionResult.CONSUME; }
            if(!player.getAbilities().instabuild) held.shrink(1);
            ItemStack scroll=new ItemStack(ExpansionItems.SPELL_SCROLL.get());
            CompoundTag tag=scroll.getOrCreateTag();
            tag.putInt("Tier",tier);
            tag.putString("Shape",tier==3?"Blink":tier==2?"Heal":"Bolt");
            tag.putInt("Color",tier==3?0x80D6FF:tier==2?0x78FFA1:0xC48EFF);
            Block.popResource(level,pos.above(),scroll);
            player.displayClientMessage(Component.literal("Ritual complete: Tier "+tier+" spell scroll!"),true);
        } else {
            player.displayClientMessage(Component.literal("Altar tier "+tier+". Use blank scroll + dust; 4 candles for Tier II; plus 2 glowstone for Tier III."),true);
        }
        return InteractionResult.CONSUME;
    }
}
