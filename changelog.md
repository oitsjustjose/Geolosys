# Geolosys Changelog (1.18)

## 7.0.2

Relegating to status of Release, because ForgeCraft has found most of the nasty bugs

### Changed

- The config item `samplePlacementBlacklist` has been removed in favor of using a new block tag that ats as a _white_-list: `geolosys:supports_sample`

### Fixed

- Persistent data not being persistent
- Crashes / errors with Patreon fetching
- When holding a block/item in your offhand and a ProPick in your mainhand, both hands' items would be used
- Geolosys features showing up as `null` rather than `geolosys:deposits`, etc.. (i.e. properly register features so the game no get angy)

## 7.0.1-beta-4

### Fixed

- Patron chapter in the Field Manual causing significant lag on reload/join
- Patron chapter in the Field Manual not rendering Patrons

## 7.0.1-beta-3

### Fixed

- Wrong tags being used for Clusters. Should now work better with your machinery
- Deepslate ores not being registered as #forge:ore
- Coals not being usable for Torch recipe, not being registered as #minecraft:coal

## 7.0.1-beta-2

### Fixed

- Crash when no plutons are defined for a given condition, dimension, etc.

## 7.0.1-beta-1

### Added

- New Config Option `proPickDetectionBlacklist`
  - Allows you to blacklist certain blocks from being picked up by the Pro Pick regardless of if it's part of a deposit or not.

## 7.0.0-alpha-2

### Added

- Patchouli Compatibility
  - Patchouli is once again required for Geolosys to run.
  - You'll need to get the latest build [from the Patchouli Maven](https://maven.blamejared.com/vazkii/patchouli/Patchouli/1.18-58-SNAPSHOT/) - choose `Patchouli-1.18-58-20211211.180957-1.jar`
  - Apologies that it's not yet on CurseForge, but it's not in my control and Geolosys _is_ currently in alpha, so...
- Re-introduced Osmium, Certus Quartz, Charged Certus Quartz, Yellorium & Sulfur drops for their respective ores.

### Fixed

- Copper and Raw Copper Still Generating
- **[Potentially Fixed]** Deposits being cut off at chunk borders.

### Changed

- Malachite & Azurite are now separate.
  - Malachite is more commonly found and is found usually more higher up, though it _can_ be found in deepslate
  - Azurite is less frequently found, but comes in larger deposits. Usually found in deepslate levels
- `chunkSkipChance` is now a `double` (ranges from `0.0` to `1.0`) rather than a somewhat-arbitrary integer
  - it is used in a more logical way as well - rather than being based on the `generationWeight` for a randomly picked pluton
- Stored data for Geolosys deposit placement has been removed. This may come back if someone finds a critical issue with this
- Stored data for Manual Gifting has been moved to player-level to make it easier to maintain for server owners as well as myself
- `[CODE]` Reworked serialization/deserialization of pending blocks in Capability

## 7.0.0-alpha

Pretty much just a port rather than the full rewrite I wanted from before. Some things to note:

- Cave biomes can be used for Geolosys now!
- You can now control how many deposits try to generate in a single chunk
- New deepslate variants made
- Changes to the datapacks: [https://twitter.com/oitsjustjose/status/1467995430059487239](https://twitter.com/oitsjustjose/status/1467995430059487239)
- I know about the `Detected setBlock in a far chunk` bug. I can't fix it. I don't care.
- Patchouli isn't out yet so no field manual.. yet.
