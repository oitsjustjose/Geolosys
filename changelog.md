# Geolosys Changelog (1.19)

## 7.0.9

Initial port to 1.19.2. This version might also work on 1.19 and 1.19.1, but I haven't tested that myself.

## Changes

### Changes to Vanilla Ore Removal

If you want to tweak which ores Geolosys disables, you can easily do that now via datapack! Check out [this nifty blog post](https://forge.gemwire.uk/wiki/Biome_Modifiers#Builtin_Biome_Modifier_Types) explaining the new Biome Modifier system. In short, this system gives modders and modpack devs a way to modify which things get added to a biome - we can now add or remove features from biomes!

As such, Geolosys now removes Vanilla's Ores in [this file](https://github.com/oitsjustjose/Geolosys/blob/1.19/src/main/resources/data/geolosys/forge/biome_modifier/remove_vanilla_ores.json). You can override it via datapack to change which ores the game will disable (don't forget to set `replace` to `true`!).

### Geolosys Datapack Changes (Again, I know)

In summary, the datapack system hasn't changed too much. The biggest change is that the following properties have been removed:

```json5
"dimensions": {
    "isBlacklist": false,
    "filter": [ "overworld" ]
},
"biomes": {
    "isBlacklist": true,
    "filter": [ "minecraft:the_void" ]
}
```

I know what you're thinking: "**What?!?** How am I supposed to control where plutons generate??". In 1.18.2, Vanilla MC added in Tags for Biomes, which are user customizable and can be used to filter out biomes. This system does not care about dimensions, but fortunately it is still pretty respective (i.e. `#minecraft:is_mountain` won't include anything from the Nether or End).

So, for pack makers I strongly suggest that you take a look at [this commit](https://github.com/oitsjustjose/Geolosys/commit/0c0c6bd1c03b839bdcbe4cbaf3231cff6519b07c) to see exactly how small the change was :)

#### What if a mod adds a new dimension that I want to use?

Dimensions that aren't in the Biome Tag `#geolosys:all_dimensions` tag will **never** get Geolosys's Plutons.

To support new dimensions, just modify the Biome Tag `#geolosys:all_dimensions` to include that dimensions' biomes. I would **strongly** recommend creating a new tag for all biomes in that dimension (i.e. `#my_modpack:is_twilight_forest`) and then adding it to the tag itself, a. la.

```json
{
  "replace": false,
  "values": ["#my_modpack:is_twilight_forest"]
}
```

## Removed

### Extra Drops from Ores

Mod Compatibility Drops for ores (such as Sulfur, AE2 Quartz, Yellorium, etc.) have been disabled. As such, Yellorium Clusters have also been removed as all of these features can be easily reproduced by a pack maker using Datapacks and KubeJS / Custom Item Mods. I'm trying to keep Geolosys as light-weight and with the times as possible, and Forge is leaning away from mods' contents being "dynamic" or "disableable" without using a datapack, which is fair.
