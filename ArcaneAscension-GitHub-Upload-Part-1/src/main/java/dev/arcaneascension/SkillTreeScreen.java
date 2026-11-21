package dev.arcaneascension;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/** Scrollable passive tree with passives, notables, keystones and descriptive hover tooltips. */
public final class SkillTreeScreen extends Screen {
    private static final ResourceLocation BG = new ResourceLocation(ArcaneAscension.ID,"textures/gui/passive_background.png");
    private static final String[] BRANCHES={"Offense","Defense","Utility"};
    private double panY=0;
    private double zoom=1.0;
    private boolean dragged=false;
    public SkillTreeScreen(){super(Component.literal("Arcane Ascension Passives"));}
    private int centerX(int branch) {return width/2+(int)((branch-1)*116*zoom);}
    private int centerY(int row) {return height/2-85+(int)((row*47+panY)*zoom);}
    private int radius(Skills s){return s.keystone()?17:s.notable()?13:9;}
    private Skills hovered(int mx,int my) {
        for(int b=0;b<3;b++) {
            var list=Skills.forBranch(BRANCHES[b]);
            for(int i=0;i<list.size();i++){
                Skills s=list.get(i);int x=centerX(b),y=centerY(i),r=radius(s);
                if(Math.abs(mx-x)<=r && Math.abs(my-y)<=r && y>35 && y<height-37)return s;
            }
        }
        return null;
    }
    @Override public boolean mouseClicked(double mx,double my,int button){
        if(button==0){
            Skills s=hovered((int)mx,(int)my);
            if(s!=null) { Network.sendAction("unlock",s.name());return true; }
            dragged=true;
        }
        return super.mouseClicked(mx,my,button);
    }
    @Override public boolean mouseReleased(double mx,double my,int button){dragged=false;return super.mouseReleased(mx,my,button);}
    @Override public boolean mouseDragged(double mx,double my,int button,double dx,double dy){
        if(button==0&&dragged){panY+=dy/zoom;return true;}
        return super.mouseDragged(mx,my,button,dx,dy);
    }
    @Override public boolean mouseScrolled(double mx,double my,double amount){zoom=Math.max(.65,Math.min(1.6,zoom+amount*.08));return true;}
    @Override public void render(GuiGraphics g,int mx,int my,float dt){
        renderBackground(g);
        int bx=width/2-205,by=18;
        g.blit(BG,bx,by,0,0,410,Math.max(1,Math.min(220,height-36)),512,512);
        g.fill(0,0,width,32,0xE9100C18);
        g.fill(0,height-35,width,height,0xE9100C18);
        CompoundTag d=ClientMagicData.get();
        g.drawCenteredString(font,"ARCANE ASCENSION  |  Level "+d.getInt("level")+"  |  Unspent: "+d.getInt("points"),width/2,8,0xF7DAB0);
        for(int b=0;b<3;b++){
            int x=centerX(b);
            g.drawCenteredString(font,BRANCHES[b],x,36, b==0?0xFFB17F:b==1?0xBBD5FF:0xAAFFDC);
            var list=Skills.forBranch(BRANCHES[b]);
            for(int i=0;i<list.size();i++){
                Skills skill=list.get(i);int y=centerY(i);
                if(y<38 || y>height-39) continue;
                int r=radius(skill), outer=skill.keystone()?0xFFE7B759:skill.notable()?0xFFB087FF:0xFF7B83AC;
                if(i>0){int last=centerY(i-1);g.fill(x-2,Math.max(34,last),x+2,y,skill.parent!=null&&skill.parent.learned(d)?0xFFB5ADFF:0xFF46445A);}
                g.fill(x-r-2,y-r-2,x+r+2,y+r+2,outer);
                g.fill(x-r,y-r,x+r,y+r,skill.learned(d)?0xFF4FD7A4:skill.available(d)?0xFF41365F:0xFF22202E);
                int symbol=skill.keystone()?0xFFFFD066:skill.notable()?0xFFCFADFF:0xFFDDDEEE;
                g.drawCenteredString(font,skill.keystone()?"✦":skill.notable()?"✧":"•",x,y-4,symbol);
                if(zoom>.8) g.drawCenteredString(font,skill.title,x,y+r+5,skill.learned(d)?0xFFB6FFE3:0xFFE9E1F1);
            }
        }
        Skills hover=hovered(mx,my);
        if(hover!=null){
            String prefix=hover.learned(d)?"UNLOCKED":hover.available(d)?"Click to unlock (1 point)":"Requires "+(hover.parent==null?"a skill point":hover.parent.title);
            g.renderTooltip(font,java.util.List.of(Component.literal(hover.title).getVisualOrderText(),Component.literal(hover.description).getVisualOrderText(),Component.literal(prefix).getVisualOrderText()),mx,my);
        }
        g.drawCenteredString(font,"Drag to pan  |  Scroll to zoom  |  Hover for bonuses  |  Click to unlock  |  ESC to close",width/2,height-23,0xFFE6D4EF);
    }
    @Override public boolean isPauseScreen(){return false;}
}
