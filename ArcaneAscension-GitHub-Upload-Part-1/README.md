# Arcane Ascension v0.2.0 — Expansion Prototype (Forge 1.20.1)

**Source project, NOT an installable mod JAR. This has not been compiled or playtested.**

The project retains original mod id `arcaneascension`, player progress keys, and all 12 original skill IDs. A migration increments schema 1 -> 2 and preserves skill/XP/mana/player progression. Back up the entire world and modpack before testing upgrades; compatibility is not guaranteed without testing.

## Implemented in source

- 42 passive nodes: 12 original plus 30 additional passive/notable/keystone nodes, with mana, health, spell damage and cost effects.
- Scrollable/pannable passive tree, tooltip descriptions, server-authoritative unlocks, dark generated GUI texture.
- Arcane Compendium (right-click): six chapter screens documenting the prototype.
- Spell Altar: right-click with blank scroll plus arcane dust in inventory. Four nearby candles -> tier 2, add two glowstone -> tier 3. Produces different scrolls.
- Spell Workbench: right-click with scroll to cycle its particle palette; sneak-click to cycle six custom item icons.
- Found scrolls: low chance to drop from monsters killed by players. Right-click scroll to select the appearance and learn Heal/Blink where applicable.
- Scroll recolor recipe uses scroll + Forge dye tag.
- Smith Furnace interaction prototype: right-click iron ingot, then copper ingot, then coal/charcoal; yields two steel ingots. Stores the two ingredient slots as blockstate; this is NOT a full furnace UI or animated smelting process.
- Hard Leather from a 3x3 of leather. Steel greatsword, steel longsword, hard leather armor, steel armor, crafting recipes, custom pixel-art item icons and simplified block textures.
- Full Java source, assets, loot tables for blocks, and GitHub Actions build configuration.

## Not yet implemented to the requested production quality

A truly massive interconnected Path of Exile graph, branching non-linear node connections, artist-drawn icons, animated 3D item meshes, wearable custom 3D armor, multistep spell crafting sequences with full GUIs, full ingredient storage/smelting GUI, extensive material alloy system, randomly distributed treasure-chest spell loot, large new spell component library, and complete guidebook research/discovery integration. This is a functional design prototype **pending compilation**, not a finished recreation of Ars Magica 2.

## Building through GitHub Actions

1. Extract this archive. Upload **the contents of this folder at the GitHub repository root** (so `build.gradle`, `.github`, and `src` are visible side by side).
2. Open repository Actions > `Build CurseForge-ready mod JAR` > Run workflow (or push to `main`).
3. If the build reports green success, download its artifact ZIP, extract the JAR under `build/libs`, and put that JAR into your CurseForge profile's `mods` folder.
4. **Before trying the new JAR, make a complete backup** of the world / server folder and `mods`, and **remove the previous version JAR**. Do not run two copies of Arcane Ascension.
5. Test on a COPY of your world first. If GitHub compilation fails, inspect the error logs and fix the source; do not install the ZIP as if it were a mod.

## Controls

K = passive tree; V = cast; C = cycle spells; right-click Arcane Compendium = open guide.

## Notes

The project intentionally uses original assets/code and generic fantasy designs, not copied original Ars Magica or Berserk assets. It currently uses generated pixel textures rather than professionally modeled 3D armor/weapons.
