package dev.arcaneascension;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.List;

public final class GuideScreen extends Screen {
    private int page = 0;
    private static final String[][] PAGES = {
        {"ARCANE COMPENDIUM", "K: open skill tree; V: cast; C: switch spells.", "Casting grants arcane XP. Spend points on connected nodes.", "Your progress is saved with the world/player data."},
        {"SPELL SCROLLS", "Craft blank scrolls from paper and arcane dust.", "Customize a spell scroll with dye in the crafting grid.", "Right-click the scroll to select its particle color."},
        {"SPELL ALTAR", "Build an altar with obsidian and amethyst.", "Use an altar with a blank scroll in your hand.", "Surround it with candles to upgrade the ritual tier.", "Add glowstone for higher-tier rituals."},
        {"SPELL CREATION", "A Tier I altar makes Bolt scrolls.", "Tier II unlocks stronger spells; Tier III unlocks teleport.", "Spell scroll colors use dye customization.", "Existing Bolt/Heal/Blink abilities remain compatible."},
        {"METALLURGY", "Craft hard leather from nine leather.", "Use a Smith Furnace with iron, copper and fuel.", "Combine metals to make steel ingots.", "Use steel for swords and armor."},
        {"EQUIPMENT", "Greatsword: slower, heavier hits.", "Longsword: faster, balanced damage.", "Hard Leather armor: entry-level medieval set.", "Steel armor: sturdy metal equipment."}
    };
    public GuideScreen() { super(Component.literal("Arcane Compendium")); }
    @Override protected void init() {
        addRenderableWidget(Button.builder(Component.literal("<"), b -> { page=(page+PAGES.length-1)%PAGES.length; }).bounds(width/2-112,height/2+65,40,20).build());
        addRenderableWidget(Button.builder(Component.literal(">"), b -> { page=(page+1)%PAGES.length; }).bounds(width/2+72,height/2+65,40,20).build());
        addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose()).bounds(width/2-40,height/2+91,80,20).build());
    }
    @Override public void render(GuiGraphics g, int x, int y, float dt) {
        renderBackground(g);
        int l=width/2-139, t=height/2-94;
        g.fill(l,t,l+278,t+214,0xEF181326);
        g.fill(l+4,t+4,l+274,t+210,0xFF30213E);
        g.drawCenteredString(font, PAGES[page][0],width/2,t+15,0xE7C18F);
        for(int i=1;i<PAGES[page].length;i++)
            g.drawCenteredString(font,PAGES[page][i],width/2,t+30+i*22,0xF5E8D5);
        g.drawCenteredString(font,(page+1)+" / "+PAGES.length,width/2,height/2+72,0xBBA7CF);
        super.render(g,x,y,dt);
    }
    @Override public boolean isPauseScreen(){return false;}
}
