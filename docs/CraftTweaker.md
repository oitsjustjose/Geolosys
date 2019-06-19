# CraftTweaker

The CraftTweaker add-on has been created to make adding new deposits to Geolosys in an even more robust way. You can follow along the following pages to see how this works.

Ores require a special import in your `.zs` script:

`import mods.geolosys.ores;`

Likewise, adding custom stones also require a special import:

`import mods.geolosys.stones;`

## Plain Deposit

A plain / standard ore deposit is one which generates in **any** biome with only a single block. This is the method that has been used by Geolosys up until Version 3.0.0, so if you'd like that standard distribution of ores, you'd probably want to use this. The syntax for this is as follows:

The order of the arguments are:

- <blockstate\> `oreBlock`
- <blockstate\> `sampleBlock`
- int `yMin`
- int `yMax`
- int `size`
- int `chance`: Higher means more likely; this is relative to how many ores you have. For example, having only a total of chance=20 (for all ores), then a chance of 4 here would result in a 4 out of 20 chance.
- float `density`: Ranges from `0.0` to `1.0`, where a density of `1.0` represents a pluton whose spheroid generation is completely solid with the block(s) defined in `blocks`. A density of `0.5` would represent a pluton of the same shape and size as `1.0`, but consisting of half as many ore blocks, which would still remain the original blocks.
- int\[ \] `dimBlacklist`: Array of dimension ID's the ore is NOT allowed in
- <blockstate\>\[ \] `blocksValidForReplacement` (**optional**): Blocks this deposit can replace while generating

So, for example - to generate Spruce Logs from Y-levels 0-70, with a chance and size of 20, blacklisted from the nether and the end, and with a sample block of Oak wood, you would do the following:

```zenscript
mods.geolosys.ores.addOre(
    <blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=oak>, 0, 70, 20, 20, 1.0 as float, [-1, 1]
);
```

Optionally, if you want to specify what blocks this deposit can replace, you can do so by specifying it as a list as the last argument:

```zenscript
mods.geolosys.ores.addOre(
    <blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=oak>, 0, 70, 20, 20, 1.0 as float, [-1, 1], [<blockstate:minecraft:stone>]
);
```


## Biome-Restricted Deposit

A biome restricted deposit is new to Geolosys v3.0.0. This kind of deposit can only generate in prescribed biomes. The CraftTweaker API, as of writing, does not yet have much by means of biome functionality, but fortunatly it does have an in-game command to help your search:

`/ct biomes`

This command will output a list of biome ResourceLocations (that's the `<modid>:<internalBiomeName>` part of the output) which can be used in blacklisting or whitelisting a deposit from that biome. The order of the arguments are:

- <blockstate\> `oreBlock`
- <blockstate\> `sampleBlock`
- int `yMin`
- int `yMax`
- int `size`
- int `chance`: Higher means more likely; this is relative to how many ores you have. For example, having only a total of chance=20 (for all ores), then a chance of 4 here would result in a 4 out of 20 chance.
- float `density`: Ranges from `0.0` to `1.0`, where a density of `1.0` represents a pluton whose spheroid generation is completely solid with the block(s) defined in `blocks`. A density of `0.5` would represent a pluton of the same shape and size as `1.0`, but consisting of half as many ore blocks, which would still remain the original blocks.
- int\[ \] `dimBlacklist`: Array of dimension ID's the ore is NOT allowed in
- string\[ \] `biomes`: Array of biomes to be selected
- bool `isWhitelist`: Determines if the `biomes` array is considered a blacklist or whitelist
- <blockstate\>\[ \] `blocksValidForReplacement` (**optional**): Blocks this deposit can replace while generating

So, expanding on the above example, if we want to restrict that to only Plains and Deserts we can use the following:

```zenscript
mods.geolosys.ores.addOre(
    <blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=oak>, 0, 70, 20, 20, 1.0 as float, [-1, 1], ["minecraft:plains", "minecraft:desert"], true
);
```

Again, this can include a specific list of blocks that this deposit can replace. Here is an example; as always, it's just tacked onto the end:

```zenscript
mods.geolosys.ores.addOre(
    <blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=oak>, 0, 70, 20, 20, 1.0 as float, [-1, 1], ["minecraft:plains", "minecraft:desert"], true, [<blockstate:minecraft:stone>]
);
```

## Multi-Ore Deposit

A multi-ore deposit is one which consists of multiple different types of ores. The frequency of each ore within the deposit must be defined, so **be sure that you follow these closely**.

The argument order is as follows:

- <blockstate\>[ ] `oreBlocks`
- int[ ] `oreBlockChances`: **Should equal to 100 and be equal number of items as** `oreBlocks`
- <blockstate\>[ ] `sampleBlocks`
- int[ ] `sampleBlocksChances`: **Should equal to 100 and be equal number of items as** `sampleBlocks`
- int `yMin`
- int `yMax`
- int `size`
- int `chance`: Higher means more likely; this is relative to how many ores you have. For example, having only a total of chance=20 (for all ores), then a chance of 4 here would result in a 4 out of 20 chance.
- float `density`: Ranges from `0.0` to `1.0`, where a density of `1.0` represents a pluton whose spheroid generation is completely solid with the block(s) defined in `blocks`. A density of `0.5` would represent a pluton of the same shape and size as `1.0`, but consisting of half as many ore blocks, which would still remain the original blocks.
- int\[ \] `dimBlacklist`: Array of dimension ID's the ore is NOT allowed in
- <blockstate\>\[ \] `blocksValidForReplacement` (**optional**): Blocks this deposit can replace while generating

For example:

```zenscript
mods.geolosys.ores.addOre(
    [<blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=birch>],
    [50, 50],
    [<blockstate:minecraft:log:variant=oak>, <blockstate:minecraft:log2:variant=acacia>],
    [20, 80],
    0, 70, 20, 20, 1.0 as float, [-1, 1]
);
```

Of course, you can once again add a custom set of blocks the deposit can replace if you'd like to override the ones in the config:

```zenscript
mods.geolosys.ores.addOre(
    [<blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=birch>],
    [50, 50],
    [<blockstate:minecraft:log:variant=oak>, <blockstate:minecraft:log2:variant=acacia>],
    [20, 80],
    0, 70, 20, 20, 1.0 as float, [-1, 1],
    [<blockstate:minecraft:stone>]
);
```

## Biome-Restricted Multi-Ore Deposit

Last in the ore deposits is a biome-restricted, multi-ore deposit. This one should speak for itself, but if it doesn't it is a deposit which whose generation biome can be black/whitelisted *and* can consist of multiple blocks. As you might imagine, the arguments look like an an amalgamation of the two:

- <blockstate\>[ ] `oreBlocks`
- int[ ] `oreBlockChances`: **Should equal to 100 and be equal number of items as** `oreBlocks`
- <blockstate\>[ ] `sampleBlocks`
- int[ ] `sampleBlocksChances`: **Should equal to 100 and be equal number of items as** `sampleBlocks`
- int `yMin`
- int `yMax`
- int `size`
- int `chance`: Higher means more likely; this is relative to how many ores you have. For example, having only a total of chance=20 (for all ores), then a chance of 4 here would result in a 4 out of 20 chance.
- float `density`: Ranges from `0.0` to `1.0`, where a density of `1.0` represents a pluton whose spheroid generation is completely solid with the block(s) defined in `blocks`. A density of `0.5` would represent a pluton of the same shape and size as `1.0`, but consisting of half as many ore blocks, which would still remain the original blocks.
- string\[ \] `biomes`: Array of biomes to be selected
- bool `isWhitelist`: Determines if the `biomes` array is considered a blacklist or whitelist
- int\[ \] `dimBlacklist`: Array of dimension ID's the ore is NOT allowed in
- <blockstate\>\[ \] `blocksValidForReplacement` (**optional**): Blocks this deposit can replace while generating

So for example, to get our previous multi-ore deposit to spawn in only the biomes we specified, you can do the following:

```zenscript
mods.geolosys.ores.addOre(
    [<blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=birch>],
    [50, 50],
    [<blockstate:minecraft:log:variant=oak>, <blockstate:minecraft:log2:variant=acacia>],
    [20, 80],
    0, 70, 20, 20, 1.0 as float, [-1, 1],
    ["minecraft:plains", "minecraft:desert"],
    true,
);
```

And lastly an example of how this would look with a specified list of replacement materials:

```zenscript
mods.geolosys.ores.addOre(
    [<blockstate:minecraft:log:variant=spruce>, <blockstate:minecraft:log:variant=birch>],
    [50, 50],
    [<blockstate:minecraft:log:variant=oak>, <blockstate:minecraft:log2:variant=acacia>],
    [20, 80],
    0, 70, 20, 20, 1.0 as float, [-1, 1],
    ["minecraft:plains", "minecraft:desert"],
    true,
    [<blockstate:minecraft:stone>]
);
```

So as you can suspect, this allows spruce and birch logs to generate with an almost-even split, with 20% oak log and 80% acacia log to represent the samplse, from Y=0-79, size and chance of 20, blacklisting the nether and end, and only generating in Plains and Deserts.

**Phew**. That was a lot, but it's what you wanted \[I hope\].
