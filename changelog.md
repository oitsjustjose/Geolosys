# Geolosys Changelog (1.16)

## 4.2.0

### Added

- Block Tags to aggro Piglins in the Nether if Poor Gold Ores are broken
- Mekanism Support (`Platinum` -> `Osmium`)
- Bigger/Extreme Reactors Support (`Autunite`-> `Yellorium`)
- AE2 Support (`Assorted Quartz` -> `[Charged] Certus Quartz`)

### Changed

- Updated Patchouli book to include words on configuring Geolosys
- Creative Tab will be better organized, has new cycling icon ^_^
- Lots of internal things that make small differences to my development but 0 difference to your utilization 😊

## 4.1.0

### (READ THIS -- You may want to reset / update your Geolosys ore configuration)

### Added

- Nether Ores! Vanilla Ores in the nether are now generated/disabled by Geolosys, and can be found just like overworld ores.
    - Poor Gold Ore can be broken to obtain a poor gold cluster, which can be smelted into 3 gold nuggets **or blasted into 4 gold nuggets**.
    - Ancient Debris can be rarely found in a Dike formation. This pluton is not very dense and very uncommon, so once you do finally find it there won't be too much there to obtain!

### Changes

- The prospector's pick got a few changes made in this release:
    1. The pro pick can be used to scan locally for ores _at any Y level_ now. Before you had to be below sea-level to see "`<Ore Name> found <direction> of you`" prompt. Now it works at any Y level!
    2. Similar to above, stones can now be locally prospected for. Before you would only know that a stone deposit was in your area, but now you can specifically find said deposit much more easily!

### Fixes

- Fixed dim whitelist not matching properly
- Fixed overworld stones generating in the nether
- Fixed ability for samples to spawn on the roof of the nether 😐

## 4.0.23

### Changed

- Geolosys's ore disabler once again only disables Vanilla ores! (Thanks Vazkii!!!)
- `DISABLE_ALL_ORES` has been changed back to `DISABLE_VANILLA_ORES` in the config

## 4.0.22

### Added

- Crafting recipe for the field manual
- Added field manual to the correct Geolosys tab

### Changed

- Hid the book progress from the guidbook

## 4.0.21

Initial Port to 1.16! **This is an incomplete build and things may break**. Please be sure to read the Patchouli manual for any guidance!

### Changed

- Migrate Manual over to Patchouli
