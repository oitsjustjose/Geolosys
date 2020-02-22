# Geolosys Changelog

## 4.0.12

### Added

- Translations for Chinese (zh_cn) - Thanks to [https://github.com/Aemande123](Aemande123)!
- Translations for Brazilian Portugese (pt_br) - Thanks to [https://github.com/StalckGx](StalckGx)!

## 4.0.11

### Fixed

- Extra spaces in US translation of prospector's pick altitude when above sea level

### Changed

- Dikes:

    - Dikes are now tall columnal platforms instead of boxy
    - This fixes kimberlite literally never showing up when you dig for it
    - Dikes are more based on their `size` attribute
    - Here's how a smaller hematite dike of `size` 24 looks:

    ![Image from https://i.imgur.com/uv1rk5o.png](https://i.imgur.com/uv1rk5o.png)

## 4.0.10

### Changed

- Calculations on how many samples are generated, since this is now different due to how new deposit types work
- Made ProPick overlay text translatable
- ProPick overlay text is now in form of `Depth: +<x>m`, `Depth: -<x>m`, or `Depth: 0m`

## 4.0.9

### Added

- Text in the field manual about each of the pluton types
- Text in each resource entry letting you know what type of pluton it forms in

### Fixed

- The `size` setting of a `DIKE` pluton having no effect

## 4.0.8

### Added

- New Pluton Types!! They come in four variants:

  1. Dense (same as the old ones)
  2. Dike (A vertical column of ore)
  3. Sparse (Scattered within the chunk boundaries)
  4. Layer (A few circular-ish layers of ore)

### Fixed

- Field Manual not respecting config option
- Field Manual being given multiple times
- Cassiterite not dropping anything
- Possibly being given the Field Manual multiple times
- Disabling Field Manual gifting not working

## 4.0.7

### Fixed

- Sample bounding boxes being centered on the block, but the actual sample being off-centered

## 4.0.6

### Added

- ProPick Extras (a config option to let you define other blocks the propick should pick up on)
- Sample Placement Blacklist config option

### Changed

- The `defaultReplacementMats` option is now a list of strings, instead of a single large string
- Samples will now be generated even smarter than before, checking if the block they're placing on is in the `samplePlacementBlacklist` (a new config option) or is the top solid (a requirement for the sample block itself too)

### Fixed

## 4.0.5

### Fixed

- Nether Quartz generating in the Nether

## 4.0.4

### Fixed

- Kimberlite samples not dropping anything
- Sample Item Models now render correctly (should look better in creative menu + TOP + HWYLA)
- ConcurrentModificationException with `serializeNBT()` in `IGeolosysCapability`

## 4.0.3

### Fixed

- Typo and format errors in the field manual
- Disabling Vanilla Ore Gen removing all sand, gravel and clay

## 4.0.2

### Added

- Field Manual is back! Everything's working! (I think)
- Field Manual is gifted to you once (can be configured not to)

### Fixed

- Server crash due to weirdness with "`ItemStackTileEntityRenderer`"

## 4.0.1

### Added

- Compatability drops (waiting on [this bug to be fixed](https://github.com/MinecraftForge/MinecraftForge/issues/5828)) - once the bug is fixed it should work without me updating
- Ability to gift players a (currently non-functioning 🤔) Field Manual the first time they join the game - there is a config for this.

### Changed

- Internal API stuff to make sure pending blocks work consistently.
- ProPick Recipe (It's now three iron aross the top row, two sticks down the remaining empty right columns)

### Fixed

- ProPick not having recipe
- Python scripts I wrote being packaged with the mod

## 4.0.0

### What Works

- All ore gen and stone gen, just like how it used to work
- JSON customization, just like it was
- Most other major components except for what's listed below:

### What Hasn't Been Implemented Yet

- Campatibility drops (waiting on [this bug to be fixed](https://github.com/MinecraftForge/MinecraftForge/issues/5828))
- Geolosys "RetroGen"
- Most user entries
- Most mod compat

### What has been Permanently Removed

- Some compatibility - not much, but some of it was modifying drops (e.g. Nuclearcraft compat). This has been permanently removed because **you can change this yourself using a datapack!**.

### Mod Changes

- Samples can break if you fall on them
- Samples can now be waterlogged (hence there isn't a "can place in water" config because they should always be able to).
- Samples now have random X/Z offsets (like flowers) for more realistism
- `dimBlacklist` is no longer a list of integers (e.g. `[-1, 1]`) - it's now a string of resource locations (e.g. `["the_end", "extrautils2:deep dark"]`, etc.)
- Propick recipe: no longer requires an iron block (that didn't make sense)

### API Changes

- The API has been moved out of the `common` package and into the main `geolosys` package.
- IOre → IDeposit
- The API is now _much_ reduced - the API now only consists of:
  1. `GEOLOSYS_WORLD_CAPABILITY`: The capability used for storing:
     1. What chunks have already had ore plutons checked for
     2. What chunks have already had stone plutons checked for
     3. What chunks have already been retrogenned (not yet used)
     4. Pending blocks per-chunk (blocks that need to be placed at a blockpos but haven't been because that would cause cascading lag)
  2. `proPickExtras`: Extra `BlockState` entries to look for when prospecting
  3. `oreConverterBlacklist`: Which ores not to convert as a part of "retrogenning" (not yet used)
  4. A `PlutonRegistry` instance, allowing access to add, get and remove all `ore` and `stone` IDeposits used by Geolosys
- New `BlockPosDim` class for dimension-defined BlockPoses (used for pending blocks)
- Move BlockPosDim/ChunkPosDim to their own classes for clarity
