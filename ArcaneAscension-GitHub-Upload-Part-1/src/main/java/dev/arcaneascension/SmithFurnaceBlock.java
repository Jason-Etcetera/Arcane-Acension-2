package dev.arcaneascension;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/** World-interaction alloy prototype. Ingredients remain as blockstate until the next step. */
public class SmithFurnaceBlock extends Block {
    public static final BooleanProperty IRON = BooleanProperty.create("iron");
    public static final BooleanProperty COPPER = BooleanProperty.create("copper");
    public SmithFurnaceBlock(Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(IRON,false).setValue(COPPER,false));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder) { builder.add(IRON,COPPER); }
    @Override public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(level.isClientSide) return InteractionResult.SUCCESS;
        ItemStack held=player.getItemInHand(hand);
        if(held.is(Items.IRON_INGOT) && !state.getValue(IRON)) {
            if(!player.getAbilities().instabuild) held.shrink(1);
            level.setBlock(pos,state.setValue(IRON,true),3);
            player.displayClientMessage(Component.literal("Iron loaded. Add copper."),true);
        } else if(held.is(Items.COPPER_INGOT) && !state.getValue(COPPER)) {
            if(!player.getAbilities().instabuild) held.shrink(1);
            level.setBlock(pos,state.setValue(COPPER,true),3);
            player.displayClientMessage(Component.literal("Copper loaded. Add coal to smelt."),true);
        } else if((held.is(Items.COAL) || held.is(Items.CHARCOAL)) && state.getValue(IRON) && state.getValue(COPPER)) {
            if(!player.getAbilities().instabuild) held.shrink(1);
            level.setBlock(pos,state.setValue(IRON,false).setValue(COPPER,false),3);
            Block.popResource(level,pos,new ItemStack(ExpansionItems.STEEL_INGOT.get(),2));
            player.displayClientMessage(Component.literal("Forged 2 steel ingots."),true);
        } else {
            player.displayClientMessage(Component.literal("Smith Furnace: add 1 iron + 1 copper, then coal. Shift-breaking refunds inputs."),true);
        }
        return InteractionResult.CONSUME;
    }
    @Override public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if(!world.isClientSide) {
            if(state.getValue(IRON)) popResource(world,pos,new ItemStack(Items.IRON_INGOT));
            if(state.getValue(COPPER)) popResource(world,pos,new ItemStack(Items.COPPER_INGOT));
        }
        super.playerWillDestroy(world,pos,state,player);
    }
}
