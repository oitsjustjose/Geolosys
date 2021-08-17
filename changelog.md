# Geolosys Changelog (1.16)

## 6.0.0 - The Big Refactor!

### Changed:

#### Datapack Format

**There are now four possible types, and a few properties in each type are the same across the board.** I know that the changes here are a lot and it'll be primarily manual-work to port to this new format, but it has been a HUGE deal to me to be able to make this more extensible. In the future I can easily add new deposit types with just 2 files: 1 changed and 1 that describes the whole deposit type. 

#### Datapack Properties

**The following properties are the same across the board:**

```json
{
    "blocks": [{
        "block": null or "modid:blockname",
        "chance": (0.0,1.0)
    }],
	"samples": [{
        "block": null or "modid:blockname",
        "chance": (0.0,1.0)
    }],
    "dimensions": {
        "isBlacklist": true or false,
        "filter": ["the_nether", "the_end", "twilight_forest", "etc.."]
    },
	"biomes": {
        "isBlacklist": true or false,
        "filter": ["SANDY", "minecraft:extreme_hills", "etc.."]
    },
	"blockStateMatchers": [
      "minecraft:lava"
    ],
	"generationWeight": 1
}
```

Let me explain some details here:

- In blocks or samples, a `null` block will not place anything i.e. if you give `iron_ore` a chance of `0.5` and you give `null`a chance of `0.5`, then there is a 50% chance when placing this deposit that the block `iron_ore` *will* be placed, and a 50% chance that it won't be placed and the block that was there beforehand will stay the same
- All chances in `blocks` should add up to `1.0`. Same for samples.
- `generationWeight` is just `chance`, but with a new name for clarity.
- `blockStateMatchers` is optional, and describes a custom list of blocks this pluton can overwrite. There is a fallback list in `geolosys-common.toml` if you don't describe anything here.

There are also some new, distinct values for each type of pluton. Here are a few examples:

* A Dense deposit:

  ```json
  {
    "type": "geolosys:deposit_dense",
    "config": {
      "yMin": 48,
      "yMax": 65,
      "size": 25
    }
  }
  ```

  `yMin` and `yMax` should be pretty clear. `size` describes the max radius from the center of the pluton - this wasn't renamed just to keep that one thing the same across this update.

* A Dike Deposit:

  ```json
  {
    "type": "geolosys:deposit_dike",
    "config": {
      "yMin": 8,
      "yMax": 22,
      "baseRadius": 3
    }
  }
  ```

  `yMin` and `yMax` should be pretty clear. `baseRadius` describes the base radius of the dike which will go up and down to essentially form an obelisk.

* A Sparse Deposit:

  ```json
  {
    "type": "geolosys:deposit_sparse",
    "config": {
      "yMin": 8,
      "yMax": 33,
      "spread": 6,
      "size": 24
    }
  }
  ```

  `yMin` and `yMax` should be pretty clear. `size` is the same as `size` for a dense deposit, but every block is randomly placed at `originalX + random(spread), originalY, originalZ + random(spread)`. A larger spread will spread across multiple chunks and **this is ok**  - Geolosys handles spreading gracefully and won't cause world gen lag. Additionally, samples will spread across all the possible chunks.

* A Layer Deposit:

  ```json
  {
    "type": "geolosys:deposit_layer",
    "config": {
      "yMin": 2,
      "yMax": 10,
      "radius": 3,
      "depth": 4
    }
  }
  ```

  `yMin` and `yMax` should be pretty clear. `radius` is the size of the layer, but with *some* noise to make these seem more natural. `depth` is how deep this layer should go (in blocks, with some noise as well).

* A Top-Layer Deposit:

  ```json
  {
    "type": "geolosys:deposit_top_layer",
    "config": {
      "radius": 4,
      "depth": 4,
      "chanceForSample": 0.25
    }
  }
  ```

  **There is no `yMin` or `yMax` for this one**. It just generates on the top layer of the world (i.e. grass, stone, etc.). `radius` describes the radius, with noise, of the deposit. `depth` is how deep this layer should go (in blocks, with some noise as well). `chanceForSample` is a way to control how many samples appear directly above this top  layer. You can set this anywhere between `0.0` and `1.0`. This type of deposit is the only type that uses this custom logic for slapping a sample directly on top of the deposit (like Peat generates with Rhododendron above it).

#### Dropped Support for `geolosys-ores.json`

The `geolosys.json` (or `geolosys-ores.json`) file in the `config` folder is no longer supported. In 5.x, support was deprecated by myself as Datapacks are so significantly better, but in the 6.x refactor I removed all code regarding this entirely. No, there isn't a tool to help you port from it. Sorry 🙁

#### Textures for Vanilla

I have created new Emerald Block and Item textures to match Beryl, as well as a new Diamond Block texture to match Kimberlite better. If you don't like these changes, you can just go to `Options -> Resource Packs` and set Minecraft Resources to be above Mod Resources.

#### Ore Drops

By popular demand, all Geolosys ores (not samples) can now be affected by Fortune to keep parity with the new 1.17 update. Additionally, all Geolosys ores can be slik-touched. Vanilla-like ores will give you the vanilla counterpart (i.e. Hematite would drop Iron Ore), and non-Vanilla-like will just drop themselves. If you don't care for this, feel free to overwrite the loot tables in a Data pack!

#### Block Highlighting

#### Reworked Prospecting

**Prospecting**

When prospecting anywhere (above or below ground), instead of receiving a message for the whole pluton that can be found in your region, you'll get a tooltip of any deposit blocks found in your region. This may be hard to visualize - try out the Propick on your own to see what I mean by this 🙂

**Block Highlighting**

When prospecting, if you would normally see the message `Found <DEPOSIT> <North/South/East/West/Above/Below> you`, it will also highlight these blocks with a cool effect that [you can preview here](https://twitter.com/oitsjustjose/status/1427484103008563200). If a server owner has this turned off, you - the client - will never see this affect. Likewise if you don't like it but the server owner has it on, you can disable it for yourself as well as adjust the duration of the glow on the client-side.

## 5.1.4

### Fixed:

- Logs dropping from Geolosys Coal if `dropSulfur` is enabled (definitely didn't forget to change that back after debugging 😉)

## 5.1.3

### Changed:

- Geolosys ore **blocks** are now added to Forge Tags (specifically `forge:ores`)

## 5.1.2

### Added:

- Additional documentation to the field manual -- keep in mind modpack creators and even server maintainers can change the plutons from their default configuration - these entries are **NOT** dynamic, just bare-minimum info for those that need it.
  - PACK MAKERS: Feel free to use the script in the new `scripts/` directory to dynamically create patchouli entries for your data packs.

### Fixed:

- Pending blocks not being used properly, which revealed:
  - Server hang/crash when generating pending blocks. This fixes issues with Terraforged as well.

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
