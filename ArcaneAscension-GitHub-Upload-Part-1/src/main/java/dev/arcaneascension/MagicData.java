package dev.arcaneascension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class MagicData {
    private MagicData() {}
    // Never rename KEY: existing worlds use this exact player NBT compound.
    private static final String KEY = "arcaneascension_magic";
    private static final String LEGACY_BACKUP_KEY = "arcaneascension_magic_backup_v0";
    public static final int SCHEMA_VERSION = 2;

    /**
     * Upgrade a player's save in place without resetting existing stats or skill flags.
     * Version 0 is the original v0.1.0 layout (which had no schema_version).
     * Migration is idempotent: only the first upgrade writes a backup and changes schema.
     */
    private static void migrate(Player player, CompoundTag persistent, CompoundTag data) {
        int version = data.getInt("schema_version");
        if (version > SCHEMA_VERSION) {
            // A newer mod has written this data. Never downgrade or overwrite it.
            return;
        }
        if (version == 0) {
            // Preserve the original data as a separate NBT compound, once per player.
            if (!persistent.contains(LEGACY_BACKUP_KEY, 10)) {
                persistent.put(LEGACY_BACKUP_KEY, data.copy());
            }
            // Keep existing values exactly. Add only missing fields if a save is partial.
            if (!data.contains("level", 3)) data.putInt("level", 1);
            if (!data.contains("xp", 3)) data.putInt("xp", 0);
            if (!data.contains("points", 3)) data.putInt("points", 1);
            if (!data.contains("mana", 3)) data.putInt("mana", 100);
            if (!data.contains("spell", 3)) data.putInt("spell", 0);
            data.putInt("schema_version", 1);
        }
        if (version < 2) {
            // Skill unlock keys use stable enum identifiers. No player progress is reset.
            if (!data.contains("spell_color", 3)) data.putInt("spell_color", 0xB78DFF);
            data.putInt("schema_version", 2);
        }
    }
    public static CompoundTag get(Player player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(KEY, 10)) {
            CompoundTag first = new CompoundTag();
            first.putInt("mana", 100); first.putInt("points", 1);
            first.putInt("level", 1); first.putInt("xp", 0); first.putInt("spell", 0);
            first.putInt("schema_version", SCHEMA_VERSION);
            persistent.put(KEY, first);
        }
        CompoundTag data = persistent.getCompound(KEY);
        migrate(player, persistent, data);
        return data;
    }
    public static void copy(Player from, Player to) {
        to.getPersistentData().put(KEY, get(from).copy());
        if (from.getPersistentData().contains("arcaneascension_book_given"))
            to.getPersistentData().putBoolean("arcaneascension_book_given", from.getPersistentData().getBoolean("arcaneascension_book_given"));
        if (from.getPersistentData().contains("arcaneascension_cast_color"))
            to.getPersistentData().putInt("arcaneascension_cast_color",from.getPersistentData().getInt("arcaneascension_cast_color"));
        if (from.getPersistentData().contains(LEGACY_BACKUP_KEY, 10)) {
            to.getPersistentData().put(LEGACY_BACKUP_KEY,
                from.getPersistentData().getCompound(LEGACY_BACKUP_KEY).copy());
        }
    }
    public static int maxMana(CompoundTag data) { return 100 + (Skills.RESERVOIR.learned(data) ? 40 : 0) + Skills.bonusMana(data); }
    public static int cost(CompoundTag data, int raw) {
        double factor = 1.0;
        if (Skills.ATTUNEMENT.learned(data)) factor -= .15;
        if (Skills.EFFICIENCY.learned(data)) factor -= .20;
        if (Skills.PYROMANCY.learned(data)) factor += .20;
        if (Skills.ETERNAL_FLOW.learned(data)) factor += .15;
        return Math.max(1, (int)Math.ceil(raw * factor));
    }
    public static void awardXp(ServerPlayer player, int value) {
        CompoundTag d = get(player);
        int xp = d.getInt("xp") + value;
        int level = d.getInt("level");
        int points = d.getInt("points");
        while (xp >= requiredXp(level)) {
            xp -= requiredXp(level); level++;
            points++; // one point per level for the larger passive tree
        }
        d.putInt("xp", xp); d.putInt("level", level); d.putInt("points", points);
    }
    public static int requiredXp(int level) { return 25 + level * 15; }
    public static void sync(ServerPlayer player) { Network.sendState(player); }
    public static boolean unlock(ServerPlayer player, Skills skill) {
        CompoundTag d = get(player);
        if (d.getInt("points") <= 0 || !skill.available(d)) return false;
        d.putBoolean("skill_" + skill.name(), true);
        d.putInt("points", d.getInt("points") - 1);
        d.putInt("mana", Math.min(maxMana(d), d.getInt("mana") + (skill == Skills.RESERVOIR ? 40 : 0)));
        sync(player); return true;
    }
    public static String spellName(int index) {
        return switch (index) { case 1 -> "Heal"; case 2 -> "Blink"; default -> "Arcane Bolt"; };
    }
    public static boolean availableSpell(CompoundTag d, int index) {
        return index == 0 || (index == 1 && (Skills.MENDING.learned(d) || d.getBoolean("scroll_heal"))) || (index == 2 && (Skills.STEP.learned(d) || d.getBoolean("scroll_blink")));
    }
}
