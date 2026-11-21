package dev.arcaneascension;

import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ExpansionItems {
    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void populateCreative(net.minecraftforge.event.BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == net.minecraft.world.item.CreativeModeTabs.INGREDIENTS) {
            for(var item : ITEMS.getEntries()) event.accept(item.get());
        }
    }
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArcaneAscension.ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArcaneAscension.ID);
    public static final RegistryObject<Item> HARD_LEATHER = ITEMS.register("hard_leather", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPELL_SCROLL = ITEMS.register("spell_scroll", () -> new SpellScrollItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BLANK_SCROLL = ITEMS.register("blank_scroll", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ARCANE_DUST = ITEMS.register("arcane_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ARCANE_COMPENDIUM = ITEMS.register("arcane_compendium", () -> new GuideItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RITUAL_FOCUS = ITEMS.register("ritual_focus", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Block> ALTAR = BLOCKS.register("spell_altar", () -> new SpellAltarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(3).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SPELL_WORKBENCH = BLOCKS.register("spell_workbench", () -> new SpellWorkbenchBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.5f)));
    public static final RegistryObject<Block> SMITH_FURNACE = BLOCKS.register("smith_furnace", () -> new SmithFurnaceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(4).requiresCorrectToolForDrops()));
    public static final RegistryObject<Item> ALTAR_ITEM = ITEMS.register("spell_altar", () -> new BlockItem(ALTAR.get(), new Item.Properties()));
    public static final RegistryObject<Item> WORKBENCH_ITEM = ITEMS.register("spell_workbench", () -> new BlockItem(SPELL_WORKBENCH.get(), new Item.Properties()));
    public static final RegistryObject<Item> SMITH_ITEM = ITEMS.register("smith_furnace", () -> new BlockItem(SMITH_FURNACE.get(), new Item.Properties()));
    public static final RegistryObject<Item> STEEL_GREATSWORD = ITEMS.register("steel_greatsword", () -> new SwordItem(Tiers.IRON, 8, -3.2f, new Item.Properties().durability(900)));
    public static final RegistryObject<Item> STEEL_LONGSWORD = ITEMS.register("steel_longsword", () -> new SwordItem(Tiers.IRON, 5, -2.6f, new Item.Properties().durability(850)));
    public static final RegistryObject<Item> HARD_LEATHER_HELMET = armor("hard_leather_helmet", ArmorItem.Type.HELMET, ArmorMaterials.LEATHER);
    public static final RegistryObject<Item> HARD_LEATHER_CHESTPLATE = armor("hard_leather_chestplate", ArmorItem.Type.CHESTPLATE, ArmorMaterials.LEATHER);
    public static final RegistryObject<Item> HARD_LEATHER_LEGGINGS = armor("hard_leather_leggings", ArmorItem.Type.LEGGINGS, ArmorMaterials.LEATHER);
    public static final RegistryObject<Item> HARD_LEATHER_BOOTS = armor("hard_leather_boots", ArmorItem.Type.BOOTS, ArmorMaterials.LEATHER);
    public static final RegistryObject<Item> STEEL_HELMET = armor("steel_helmet", ArmorItem.Type.HELMET, ArmorMaterials.IRON);
    public static final RegistryObject<Item> STEEL_CHESTPLATE = armor("steel_chestplate", ArmorItem.Type.CHESTPLATE, ArmorMaterials.IRON);
    public static final RegistryObject<Item> STEEL_LEGGINGS = armor("steel_leggings", ArmorItem.Type.LEGGINGS, ArmorMaterials.IRON);
    public static final RegistryObject<Item> STEEL_BOOTS = armor("steel_boots", ArmorItem.Type.BOOTS, ArmorMaterials.IRON);
    private static RegistryObject<Item> armor(String id, ArmorItem.Type type, ArmorMaterial material) {
        return ITEMS.register(id, () -> new ArmorItem(material, type, new Item.Properties()));
    }
    private ExpansionItems() {}
}
