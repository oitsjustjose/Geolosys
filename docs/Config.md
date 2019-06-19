# The New Config

As of Geolosys v3.0.0, the `json` file has been moved from `geolosys_ores.json` to `geolosys.json`. Not only is this more consistent with what it contains, but it also ensures that updating to v3.0.0 does not face any issues based off of an old `geolosys_ores.json` layout. This is because the functionality of this file has completely changed!

With the v3.0.0 `json` config file, you can now remove or add entries that will be dynamically parsed for you at run-time! In fact, anything you add or remove will **automatically be added or removed from the field manual!** The results of the parsing will be shown in the logs if you are uncertain as to whether or not it will work, so let's get started!

## JSON format

If you have not learned how a JSON file is formatted yet, here is a quick run-down (though really, it's quite simple. Learn more [here](https://www.digitalocean.com/community/tutorials/an-introduction-to-json) if this tutorial isn't good enough):

The `json` file *starts* with a matching pair of curly braces:

```json
{

}
```

Within any pair of curly braces, you can place a key-value pair inside! Essentially, the key is what I use in my code to look up, and the value is what you want that key to correspond to. So if your key is `yMin`, then you want the minimum Y value to be your value. For example:

```json
{
    "yMin": 30
}
```

This snippet means that the value for `yMin` shall be 30. The order of where you put this key doesn't matter, such as if you had `yMax` as a key as well and it came before or after `yMin`; that does not matter, it'll still find it.

So the next point to cover is the array. In JSON, the array is a pair of brackets:

```json
[

]
```

Arrays represent a list of items, so if you wanted to list off the ResourceLocations of some Minecraft blocks in an array, you'd do so like this:

```json
[
    "minecraft:stone:0",
    "minecraft:stone:1",
    "minecraft:stone:3",
    "minecraft:stone:5"
]
```

Why does this matter? Because your *value* to a *key* **can be an array**, and the new Geolosys config depends on this.

So now that we have this handled, let's get to how I require the `geolosys.json` file to be formatted.

## `geolosys.json` Format

The format of the `geolosys.json` file goes like so:

```json
{
    "ores": [

    ],
    "stones": [

    ]
}
```

Notice, I have two keys, separated by commas, and their values each are an array. This array is considered a type of that key. So for example, everything in the `ores` array is an ore, and everything in the `stones` array is a stone. 

## The Ores Section

The format of an ore is:

```json
{
    "blocks": [
        "minecraft:diamond_ore",
        100
    ],
    "samples": [
        "minecraft:diamond_block",
        100
    ],
    "yMin": 0,
    "yMax": 0,
    "chance": 0,
    "size": 0,
    "dimBlacklist": [-1, 1],
    "density": 0.95,
    "blockStateMatchers": [
        "minecraft:stone"
    ],
    "biomes": [
        "minecraft:plains"
    ],
    "isWhitelist": true
}
```

Let's break down each one of those items and describe what's optional and what's not:

`blocks`: An array consisting of **at least two items**: it shall be a pattern of [`a`, `b`, `a`, `b`, ...], where `a` is a stringified version of `<modid:block>` or `<modid:block:meta>`, and `b` is the chance for that ore block to occur. **Yes**, this means you can have **multiple blocks in a deposit**, but you must ensure that the sums of all of your chances is equal to 100; if not, **the game will crash**.

`samples`: An array consisting of **at least two items**: it shall be a pattern of [`a`, `b`, `a`, `b`, ...], where `a` is a stringified version of `<modid:block>` or `<modid:block:meta>`, and `b` is the chance for that sample block to occur. **Yes**, this means you can have **multiple blocks in a deposit**, but you must ensure that the sums of all of your chances is equal to 100; if not, **the game will crash**.

`yMin`: An integer value representing the minimum Y level this deposit can generate

`yMax`: An integer value representing the maximum Y level this deposit can generate

`chance`: An integer value representing the chance (relative to all other chances) for this ore to generate

`size`: An integer value representing the approximate size of the deposit

`dimBlacklist`: An array of integers representing dimension IDs not to generate this deposit in

`density`: Ranges from `0.0` to `1.0`, where a density of `1.0` represents a pluton whose spheroid generation is completely solid with the block(s) defined in `blocks`. A density of `0.5` would represent a pluton of the same shape and size as `1.0`, but consisting of half as many ore blocks, which would still remain the original blocks.

`blockStateMatchers`: **Optional**: An override for the default blocks in your `geolosys.cfg` file for which blocks this deposit can replace when generating

`biomes`: **Optional**: A list of biome ResourceLocations

`isWhitelist`: **Required if `biomes` is defined**: A boolean value defining whether or not the list `biomes` is a blacklist or whitelist for where this deposit can generate.


## The Stones Section

The format of a stone entry is:

```json
{
    "block": "minecraft:stone:1",
    "size": 40,
    "chance": 10,
    "yMin": 2,
    "yMax": 70,
    "dimBlacklist": [-1, 1]
}
```

Let's break down each one of those keys and what it represents:

`block`: A stringified version of `<modid:block>` or `<modid:block:meta>`, the block you wish to generate as your stone

`size`: An integer value representing the approximate size of the stone deposit

`chance`: An integer value representing the chance (relative to all other chances) for this stone to generate

`yMin`: An integer value representing the minimum Y level this stone deposit can generate

`yMax`: An integer value representing the maximum Y level this stone deposit can generate

`dimBlacklist`: An array of integers representing dimension IDs not to generate this stone deposit in
