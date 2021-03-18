# Geolosys Changelog (1.16)

## 5.1.2

### Added:

- Additional documentation to the field manual -- keep in mind modpack creators and even server maintainers can change the plutons from their default configuration - these entries are **NOT** dynamic, just bare-minimum info for those that need it.
    - PACK MAKERS: Feel free to use the script in the new `scripts/` directory to dynamically create patchouli entries for your data packs.

### Fixed:

- Pending blocks not being used properly, which revealed:
- Server hang/crash when generating pending blocks

## 5.1.1

### Fixed:

- Crash due to pretty old code calling client-only localization code
- Cleaned up some internal code.

## 5.1.0

### Added:

- Nuggets! This was all thanks to [@rsslcs](https://github.com/rsslcs) and their hard work
- New Dimension Whitelist (**for Datapacks only**)

  - Not implemented for old `geolosys.json` config -- you _need_ to update for this feature
  - If you're using datapacks, don't worry! This feature doesn't break any existing datapacks, just adds a new option
  - The syntax looks like:

  ```json
  {
    ...
    "dimensions": {
      "isBlacklist": false,
      "filter": [ "overworld" ]
    }
  }
  ```

### Fixed:

- Veinmining potentially not working with Autunite, Platinum, Assorted Quartz and/or Coal
  - This was due to a hacky method of adding extra blocks to these 4 ores
  - I'm now using the proper, Forge supported way even if it killed me inside a bit
- Prospector's Pick, Sample Placement Blacklist and Default Placement Blacklist not working with all modded blocks
- Other small internal things, really this release is more a cleanup and facelift to the code than anything else.

## 5.0.3

### Fixed:

- Field Manual not opening with `Patchouli 1.16.4-49` or greater.
- Cleaned up internal code.

## 5.0.2

### Fixed:

- `Cannot set property BooleanProperty{name=waterlogged}` crash
- Emerald and Infested Stone still generating in Extreme Hills

### Removed:

- `enableIngots` config option (due to a recent vanilla Minecraft change)

## 5.0.1

### Fixed:

- Feature crash when checking for samples' abilities to be waterlogged
- Crash when breaking Assorted Quartz when Applied Energistics is installed

## 5.0.0

### The Coals Update!!

### Added:

- NEW BLOCKS:
  - Dedicated ores & samples for LIgnite, Bituminous Coal and Anthracitic Coal
  - Peat: This looks like grass, but is a much more acidic, darker variant that generates on the surface
  - Rhododendron: This is a new flower that can only be harvested with shears. It prefers Peat and is found on top of it, and can be used to craft Purple Dye!
- New `TOP_LAYER` pluton type, which starts at the surface and works its way down (similar to clay or surface gravel).
- New Data Pack config option! Instead of using `geolosys.json` or `geolosys_ores.json`, Geolosys now uses Data Packs for configuration
  - **The old method will still be supported for the duration of 1.16.x updates for convenience**
  - Geolosys no longer downloads a json config from GitHub if you didn't have one
  - All of Geolosys's built-in ores are now DP-only, overwrite their path in your own datapack to delete them [see here for files/paths](https://github.com/oitsjustjose/Geolosys/tree/1.16/src/main/resources/data/geolosys/deposits), and [here](https://minecraft.gamepedia.com/Data_Pack) for instructions on how to overwrite/work with datapacks
  - Geolosys is now inherently [compatible with Craft Tweaker!](https://github.com/CraftTweaker/CraftTweaker-Examples/blob/master/1.14/recipetypes.zs)
    - The Ore deposit type is `geolosys:ore_deposit`
    - The Stone deposit type is `geolosys:stone_deposit`
    - Yes, these are considered "recipes" to the game, but aren't ever treated as such
    - See any of the JSON files [here](https://github.com/oitsjustjose/Geolosys/tree/1.16/src/main/resources/data/geolosys/deposits) for examples.

## 4.2.0

### Added

- Block Tags to aggro Piglins in the Nether if Poor Gold Ores are broken
- Mekanism Support (`Platinum` -> `Osmium`)
- Bigger/Extreme Reactors Support (`Autunite`-> `Yellorium`)
- AE2 Support (`Assorted Quartz` -> `[Charged] Certus Quartz`)

### Changed

- Updated Patchouli book to include words on configuring Geolosys
- Creative Tab will be better organized, has new cycling icon ^\_^
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
