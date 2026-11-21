# Updating and preserving your world

1. Stop Minecraft and the multiplayer server completely.
2. Back up the entire instance: world/save directory, server `world`, config and mods folders. Retain a backup of the old JAR.
3. For testing, copy the world into a *separate* CurseForge profile with the same Forge 1.20.1 version and other mods.
4. In the copied profile `mods` folder, remove the older Arcane Ascension JAR, then add the new compiled v0.2.0 JAR. Do not use the source ZIP.
5. Log in and verify existing level, XP, mana, unlocked skills, inventories and crafting. Test death/respawn, dimensional travel, and server reconnects.
6. Only update the production server after those checks; clients must run compatible mod versions.

Original `arcaneascension_magic` persisted data and existing item IDs are preserved in source. Future registered IDs must not be renamed carelessly. The schema migration adds a backup of legacy data and version numbers, but never replaces making a separate world backup. The new mod version has not yet been compiled or runtime verified.
