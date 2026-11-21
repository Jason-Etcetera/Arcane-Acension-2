package dev.arcaneascension;

import java.util.Arrays;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

/** Stable enum identifiers: never rename original nodes or old saves will lose their unlock mapping. */
public enum Skills {
    FOCUS("Arcane Focus", "Offense", null, "Bolt damage +2"),
    EMBER("Ember Studies", "Offense", FOCUS, "Bolt ignites targets"),
    AMPLIFY("Amplification", "Offense", EMBER, "Bolt damage +3"),
    OVERCHARGE("Overcharge", "Offense", AMPLIFY, "Bolt range +12"),
    RESERVOIR("Mana Reservoir", "Defense", null, "Max mana +40"),
    MENDING("Restoration", "Defense", RESERVOIR, "Unlock Heal spell"),
    REGEN("Flow State", "Defense", MENDING, "Faster mana regeneration"),
    BASTION("Arcane Bastion", "Defense", REGEN, "Healing increased by 3"),
    ATTUNEMENT("Attunement", "Utility", null, "Spell costs reduced 15%"),
    STEP("Aether Step", "Utility", ATTUNEMENT, "Unlock Blink spell"),
    DISTANCE("Far Step", "Utility", STEP, "Blink travels farther"),
    EFFICIENCY("Spellweaver", "Utility", DISTANCE, "Spell costs reduced 20% more"),

    SPARK_1("Spark I", "Offense", OVERCHARGE, "+2% spell damage"),
    SPARK_2("Spark II", "Offense", SPARK_1, "+2% spell damage"),
    SPARK_3("Spark III", "Offense", SPARK_2, "+2% spell damage"),
    STORM_CORE("Storm Core", "Offense", SPARK_3, "Notable: +12% spell damage"),
    SPARK_4("Spark IV", "Offense", STORM_CORE, "+2% spell damage"),
    SPARK_5("Spark V", "Offense", SPARK_4, "+2% spell damage"),
    PYROMANCY("Pyromancy", "Offense", SPARK_5, "Keystone: +25% spell damage, +20% spell costs"),
    SPARK_6("Spark VI", "Offense", PYROMANCY, "+2% spell damage"),
    SPARK_7("Spark VII", "Offense", SPARK_6, "+2% spell damage"),
    ARCANE_MASTERY("Arcane Mastery", "Offense", SPARK_7, "Notable: +16% spell damage"),

    VITALITY_1("Vitality I", "Defense", BASTION, "+1 maximum health"),
    VITALITY_2("Vitality II", "Defense", VITALITY_1, "+1 maximum health"),
    VITALITY_3("Vitality III", "Defense", VITALITY_2, "+1 maximum health"),
    IRON_WILL("Iron Will", "Defense", VITALITY_3, "Notable: +4 maximum health"),
    VITALITY_4("Vitality IV", "Defense", IRON_WILL, "+1 maximum health"),
    VITALITY_5("Vitality V", "Defense", VITALITY_4, "+1 maximum health"),
    LIFE_WARD("Life Ward", "Defense", VITALITY_5, "Keystone: +8 maximum health, -10% spell damage"),
    VITALITY_6("Vitality VI", "Defense", LIFE_WARD, "+1 maximum health"),
    VITALITY_7("Vitality VII", "Defense", VITALITY_6, "+1 maximum health"),
    SOUL_FORTRESS("Soul Fortress", "Defense", VITALITY_7, "Notable: +4 maximum health"),

    AETHER_1("Aether I", "Utility", EFFICIENCY, "+5 maximum mana"),
    AETHER_2("Aether II", "Utility", AETHER_1, "+5 maximum mana"),
    AETHER_3("Aether III", "Utility", AETHER_2, "+5 maximum mana"),
    MANA_FOUNTAIN("Mana Fountain", "Utility", AETHER_3, "Notable: +25 maximum mana"),
    AETHER_4("Aether IV", "Utility", MANA_FOUNTAIN, "+5 maximum mana"),
    AETHER_5("Aether V", "Utility", AETHER_4, "+5 maximum mana"),
    ETERNAL_FLOW("Eternal Flow", "Utility", AETHER_5, "Keystone: +50 mana, +15% spell costs"),
    AETHER_6("Aether VI", "Utility", ETERNAL_FLOW, "+5 maximum mana"),
    AETHER_7("Aether VII", "Utility", AETHER_6, "+5 maximum mana"),
    ELDER_ARCANIST("Elder Arcanist", "Utility", AETHER_7, "Notable: +30 maximum mana");

    public final String title, branch, description;
    public final Skills parent;
    Skills(String title, String branch, Skills parent, String description) {
        this.title = title; this.branch = branch; this.parent = parent; this.description = description;
    }
    public boolean learned(CompoundTag data) { return data.getBoolean("skill_" + name()); }
    public boolean available(CompoundTag data) { return !learned(data) && (parent == null || parent.learned(data)); }
    public boolean notable() { return this==STORM_CORE||this==IRON_WILL||this==MANA_FOUNTAIN||this==ARCANE_MASTERY||this==SOUL_FORTRESS||this==ELDER_ARCANIST; }
    public boolean keystone() { return this==PYROMANCY||this==LIFE_WARD||this==ETERNAL_FLOW; }
    public static List<Skills> forBranch(String branch) {
        return Arrays.stream(values()).filter(s -> s.branch.equals(branch)).toList();
    }
    public static int bonusMana(CompoundTag d) {
        int v=0;
        for(Skills s:values()) if(s.learned(d)) {
            if(s.name().startsWith("AETHER_"))v+=5;
            if(s==MANA_FOUNTAIN)v+=25;
            if(s==ETERNAL_FLOW)v+=50;
            if(s==ELDER_ARCANIST)v+=30;
        }
        return v;
    }
    public static int bonusHealth(CompoundTag d) {
        int v=0;
        for(Skills s:values()) if(s.learned(d)) {
            if(s.name().startsWith("VITALITY_"))v++;
            if(s==IRON_WILL||s==SOUL_FORTRESS)v+=4;
            if(s==LIFE_WARD)v+=8;
        }
        return v;
    }
    public static double damageFactor(CompoundTag d) {
        double v=1;
        for(Skills s:values()) if(s.learned(d)) {
            if(s.name().startsWith("SPARK_"))v+=.02;
            if(s==STORM_CORE)v+=.12;
            if(s==PYROMANCY)v+=.25;
            if(s==ARCANE_MASTERY)v+=.16;
            if(s==LIFE_WARD)v-=.10;
        }
        return Math.max(.3,v);
    }
}
