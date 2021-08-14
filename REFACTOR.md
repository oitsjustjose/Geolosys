# The July 2021 Refactor

A refactor is finally much needed after all the things that the Geolosys codebase has been through!

**Issues**

- There many `IDeposit` instances -- this isn't really ideal as we should just us `@Nullable` for things and just have one Deposit instance (such as a `getBiomes()` function which returns NULL if the deposit is biome restricted, and a function for `getOreBlocks` (**NOT** `getOreBlock`) which just returns one ore).

## `IDeposit` Improvements

- [x] Remove `IDeposit` this isn't a Fundamentals to Data Structures and Algorithms class -- use modern `@Nullable` features to make a single class dynamic, and reconsider design and var/func names to increase flexibility

- [x] Move generation logic to the class which describes the _shape_ of the pluton, not how it generates. We will need a `generate()` and `postGenerate()` function -- `generate` to put the pluton down, and `postGenerate()` to handle samples and/or custom sample logic (i.e. stones _maybe_ having samples, but maybe not, or `top_layer` as well).

- [x] ~~Create new `Interface` for implementing how a pluton generates. This means that what will now be the deposit (which stores what block, etc..) should now refer to the function that generates it, within the API ideally (i.e. `() -> generateOre(this.block)`, etc..). These should be named something like `[Dense|Dike|Sparse|Layer|TopLayer]Pluton.java`.~~

- [x] Clamp the pluton's Y-levels such that if it would generate in the air (as a result of a YMax being above the ground) shift it down.

## Datapack Improvements

- [x] Add new `types`; instead of having _just_ `geolosys:[ore|stone]`, have:

  - `geolosys:ore_dense`
  - `geolosys:ore_dike`
  - `geolosys:ore_sparse`
  - `geolosys:ore_layer`
  - `geolosys:ore_top_layer`

  This will allow you to be able to customize these variables/attributes which will be needed for the new interface for how an ore is generated, mentioned below.

  In essence I'd like to have the following process flow:

  1. Declare datapack file of type `geolosys:ore_[dense|dike|sparse|layer|top_layer]`
  2. Using this type (and its own parser in Java), create a new instance of the deposit with the correct type (i.e.)

  ```js
  const onWorldGen = (pluton) => PlutonRegistry.pick().generate();
  ```

## Update Datapacks

- [x] Update the datapacks to the newest format, because that'll take forever............

## Fix Sparse Ores

- [x] Sparse ores and the "rolls" mechanic doesn't work. Something like "Frequency" to dictate the space between blocks - essentially think more like an exploded dense pluton. Thoughts on this: generate a list of essentially what is a dense deposit. Rather than generate this, take each BlockPos and add frequency to it - dense ores further out would be further out in the "explosion". This would two params:

* Size (of the dense pluton created)
* Spread (of the blocks from within the pluton)

## ✨✨✨✨✨

- [x] Remove `density` attribute, repro this feature using `null`as a block type.
  - [ ] Explain this in the changelog as well as in the wiki 🤔
